// src/modules/posts/posts.service.ts
import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Post } from './entities/post.entity';
import { Repository } from 'typeorm';
import { CreatePostDto, LiteReponsePostDto, ReponseUserDto } from './dtos/create-post.dto';
import { UpdatePostDto } from './dtos/update-post.dto';
import { User } from '../auth/entities/user.entity';
import { MailerService } from '../mailer/mailer.service';

@Injectable()
export class PostsService {
  [x: string]: any;
  constructor(
    @InjectRepository(Post) private postsRepository: Repository<Post>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private mailerService: MailerService,
  ) {}

  async createPost(createPostDto: CreatePostDto, userId: number): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { id: userId } });
    const post = this.postsRepository.create({
      ...createPostDto,
      author: user,
    });
    await this.postsRepository.save(post);
    return { message: 'Tạo bài viết thành công.', post: { ...post, authorName: user.name } };
  }

  async updatePost(postId: number, updatePostDto: UpdatePostDto, userId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId }, relations: ['author'] });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    if (post.author.id !== userId) {
      throw new ForbiddenException('Bạn không có quyền chỉnh sửa bài viết này.');
    }
    Object.assign(post, updatePostDto);
    await this.postsRepository.save(post);
    return { message: 'Cập nhật bài viết thành công.', post };
  }

  async deletePost(postId: number, userId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId }, relations: ['author'] });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    if (post.author.id !== userId) {
      throw new ForbiddenException('Bạn không có quyền xóa bài viết này.');
    }
    await this.postsRepository.remove(post);
    return { message: 'Xóa bài viết thành công.' };
  }

  async getPostById(postId: number): Promise<any> {
    const post = await this.postsRepository.findOne({
      where: { id: postId }
    });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    post.totalView += 1;
    return post;
  }

  async likePost(postId: number, userId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId }, relations: ['likes', 'author'] });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }

    if (post.likes.some((like) => like.id === userId)) {
      throw new BadRequestException('Bạn đã thích bài viết này trước đó.');
    }

    post.likes.push({ id: userId } as User);
    post.totalLike = post.likes.length;

    await this.postsRepository.save(post);

    return { message: 'Đã thích bài viết.', totalLike: post.totalLike };
  }

  async unlikePost(postId: number, userId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId }, relations: ['likes', 'author'] });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }

    const likeIndex = post.likes.findIndex((like) => like.id === userId);
    if (likeIndex === -1) {
      throw new BadRequestException('Bạn chưa thích bài viết này.');
    }

    post.likes.splice(likeIndex, 1);
    post.totalLike = post.likes.length;

    await this.postsRepository.save(post);

    return { message: 'Đã bỏ thích bài viết.', totalLike: post.totalLike };
  }
  
  async getNewfeeds(userId: number): Promise<any> {
    const currentTime = new Date();

    // Lấy danh sách bài viết nổi bật
    const posts = await this.postsRepository.find();
    // Tính điểm cho mỗi bài viết
    const scoredPosts = posts.map(post => {

      //const isFollow = this.followsService.isFollowing(userId, post.author.id) ? 1 : 0;
      //const isRead = this.usersService.hasReadPost(userId, post.id) ? 1 : 0;
      const isFollow = 0;
      const isRead = 0;
      const hoursAway = (currentTime.getTime() - post.createdAt.getTime()) / (1000 * 60 * 60);

      const baseScore = (Math.sqrt(post.totalLike + post.totalComment + post.totalView / 5) *
        (1 + isFollow) *
        (1 - isRead * 0.9) +
        isFollow * 2) /
        Math.sqrt(hoursAway / 2 + 1);
      console.log(post.title, baseScore);
      return { post, score: baseScore };
    });

    // Sắp xếp bài viết theo điểm giảm dần
    scoredPosts.sort((a, b) => b.score - a.score);
    
    return scoredPosts.map(sp => new LiteReponsePostDto(sp.post));
  } 
  
}
