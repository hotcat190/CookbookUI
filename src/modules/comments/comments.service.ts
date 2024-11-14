// src/modules/comments/comments.service.ts
import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Comment } from './entities/comment.entity';
import { Repository } from 'typeorm';
import { CreateCommentDto } from './dtos/create-comment.dto';
import { Post } from '../posts/entities/post.entity';
import { User } from '../auth/entities/user.entity';
import { NotificationsService } from '../notifications/notifications.service';

@Injectable()
export class CommentsService {
  constructor(
    @InjectRepository(Comment) private commentsRepository: Repository<Comment>,
    @InjectRepository(Post) private postsRepository: Repository<Post>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private notificationsService: NotificationsService,
  ) {}

  async createComment(postId: number, createCommentDto: CreateCommentDto, userId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId }, relations: ['author'] });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }

    const user = await this.usersRepository.findOne({ where: { id: userId } });

    const comment = this.commentsRepository.create({
      content: createCommentDto.content,
      post,
      user,
    });

    await this.commentsRepository.save(comment);

    // Gửi thông báo đến tác giả bài viết
    await this.notificationsService.sendNotification(
      post.author.id,
      `${user.username} đã bình luận về bài viết của bạn.`,
      comment.id,
      'comment',
    );

    return { message: 'Thêm bình luận thành công.', comment };
  }

  async deleteComment(commentId: number, userId: number): Promise<any> {
    const comment = await this.commentsRepository.findOne({ where: { id: commentId }, relations: ['user'] });
    if (!comment) {
      throw new NotFoundException('Bình luận không tồn tại.');
    }
    if (comment.user.id !== userId) {
      throw new ForbiddenException('Bạn không có quyền xóa bình luận này.');
    }
    await this.commentsRepository.remove(comment);
    return { message: 'Xóa bình luận thành công.' };
  }

  async getComments(postId: number): Promise<any> {
    const post = await this.postsRepository.findOne({ where: { id: postId } });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    const comments = await this.commentsRepository.find({
      where: { post: { id: postId } },
      relations: ['user'],
      order: { createdAt: 'DESC' },
    });
    return { comments };
  }
}
