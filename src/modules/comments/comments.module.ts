// src/modules/comments/comments.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { CommentsController } from './controllers/comments.controller';
import { CommentsService } from './comments.service';
import { Comment } from './entities/comment.entity';
import { Post } from '../posts/entities/post.entity';
import { User } from '../auth/entities/user.entity';
import { NotificationsModule } from '../notifications/notifications.module';

@Module({
  imports: [TypeOrmModule.forFeature([Comment, Post, User]), NotificationsModule],
  controllers: [CommentsController],
  providers: [CommentsService],
  exports: [CommentsService],
})
export class CommentsModule {}
