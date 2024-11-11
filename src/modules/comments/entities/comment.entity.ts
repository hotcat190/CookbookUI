// src/modules/comments/entities/comment.entity.ts
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, CreateDateColumn } from 'typeorm';
import { Recipe } from '../../recipes/entities/recipe.entity';
import { User } from '../../auth/entities/user.entity';

@Entity()
export class Comment {
  @PrimaryGeneratedColumn()
  id: number;

  @Column('text')
  content: string;

  @ManyToOne(() => Recipe, (recipe) => recipe.comments, { onDelete: 'CASCADE' })
  recipe: Recipe;

  @ManyToOne(() => User, (user) => user.id, { eager: true, onDelete: 'CASCADE' })
  user: User;

  @CreateDateColumn()
  createdAt: Date;
}
