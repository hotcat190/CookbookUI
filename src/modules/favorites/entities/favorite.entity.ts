// src/modules/favorites/entities/favorite.entity.ts
import { Entity, PrimaryGeneratedColumn, ManyToOne, JoinColumn, CreateDateColumn } from 'typeorm';
import { User } from '../../auth/entities/user.entity';
import { Post } from '../../posts/entities/post.entity';

@Entity()
export class Favorite {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => User, (user) => user.favorites, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn()
  user: User;

  @ManyToOne(() => Post, (post) => post.id, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn()
  post: Post;

  @CreateDateColumn()
  addedAt: Date;
}
