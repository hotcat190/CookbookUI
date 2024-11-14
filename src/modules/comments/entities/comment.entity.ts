// src/modules/comments/entities/comment.entity.ts
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, CreateDateColumn } from 'typeorm';
import { Post } from '../../posts/entities/post.entity';
import { User } from '../../auth/entities/user.entity';

@Entity()
export class Comment {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('text')
  content: string;

  @ManyToOne(() => Post, (post) => post.comments, { onDelete: 'CASCADE' })
  post: Post;

  @ManyToOne(() => User, (user) => user.id, { eager: true, onDelete: 'CASCADE' })
  user: User;

  @CreateDateColumn()
  createdAt: Date;
}
