// src/modules/posts/posts.service.ts
import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Post } from './entities/post.entity';
import { Repository } from 'typeorm';
import { CreatePostDto } from './dtos/create-post.dto';
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
    console.log('startt');
    const post = await this.postsRepository.findOne({
      where: { id: postId }
    });
    console.log('starttt');
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
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
    await this.postsRepository.save(post);

    // Gửi thông báo đến tác giả
    await this.mailerService.sendLikeNotification(post.author.email, post.title);

    return { message: 'Đã thích bài viết.', likesCount: post.likes.length };
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
    await this.postsRepository.save(post);

    // Gửi thông báo đến tác giả
    await this.mailerService.sendUnlikeNotification(post.author.email, post.title);

    return { message: 'Đã bỏ thích bài viết.', likesCount: post.likes.length };
  }
}
