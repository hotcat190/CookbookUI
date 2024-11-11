// src/modules/favorites/entities/favorite.entity.ts
import { Entity, PrimaryGeneratedColumn, ManyToOne, JoinColumn, CreateDateColumn } from 'typeorm';
import { User } from '../../auth/entities/user.entity';
import { Recipe } from '../../recipes/entities/recipe.entity';

@Entity()
export class Favorite {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => User, (user) => user.favorites, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn()
  user: User;

  @ManyToOne(() => Recipe, (recipe) => recipe.id, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn()
  recipe: Recipe;

  @CreateDateColumn()
  addedAt: Date;
}
