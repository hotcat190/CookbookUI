// src/modules/auth/entities/user.entity.ts
import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
  } from 'typeorm';
  import { Recipe } from '../../recipes/entities/recipe.entity';
  import { Follow } from '../../follows/entities/follow.entity';
  import { Favorite } from '../../favorites/entities/favorite.entity';
  import { Notification } from '../../notifications/entities/notification.entity';
  import { Exclude } from 'class-transformer';
  
  @Entity()
  export class User {
    @PrimaryGeneratedColumn()
    id: number;
  
    @Column({ unique: true })
    username: string;
  
    @Column({ unique: true })
    email: string;
  
    @Exclude()
    @Column()
    password: string;
  
    @Column({ default: false })
    isActive: boolean;
  
    @Column({ nullable: true })
    verificationToken: string;
  
    @Column({ nullable: true })
    resetPasswordToken: string;
  
    @Column({ type: 'timestamp', nullable: true })
    resetPasswordExpires: Date;
  
    @Column('json', {
      nullable: true // Remove any default value here
    })
    roles: string[];
  
    @OneToMany(() => Recipe, (recipe) => recipe.author)
    recipes: Recipe[];
  
    @OneToMany(() => Follow, (follow) => follow.follower)
    following: Follow[];
  
    @OneToMany(() => Follow, (follow) => follow.following)
    followers: Follow[];
  
    @OneToMany(() => Favorite, (favorite) => favorite.user)
    favorites: Favorite[];
  
    @OneToMany(() => Notification, (notification) => notification.user)
    notifications: Notification[];
    
    @Column({ nullable: true, length: 200 })
    bio: string;

    @Column({ default: 'Tôi dại dột', length: 30 })
    name: string;

    @Column({ nullable: true })
    avatar: string;
  }
  