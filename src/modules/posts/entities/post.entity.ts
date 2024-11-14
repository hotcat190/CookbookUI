// src/modules/posts/entities/post.entity.ts
import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    ManyToOne,
    OneToMany,
    ManyToMany,
    JoinTable,
    CreateDateColumn,
    UpdateDateColumn,
  } from 'typeorm';
  import { User } from '../../auth/entities/user.entity';
  import { Comment } from './comment.entity';
  
  @Entity()
  export class Post {
    @PrimaryGeneratedColumn()
    id: number;
  
    @Column()
    title: string;
  
    @Column('text')
    description: string;
  
    @Column({ nullable: true })
    cookTime: string;
  
    @Column('simple-json', { nullable: true })
    ingredient: { 
      name: string; 
      quantity: string; 
    }[];
  
    @Column('simple-json', { nullable: true })
    //steps: { description: string }[];
    steps: string[];
  
    @Column({ nullable: true })
    mainImage: string;
  
    @ManyToOne(() => User, (user) => user.posts, { eager: true })
    author: User;
    
    @OneToMany(() => Comment, (comment) => comment.post, { cascade: true })
    comments: Comment[];
  
    @ManyToMany(() => User, { eager: true })
    @JoinTable()
    likes: User[];
  
    @CreateDateColumn()
    createdAt: Date;
  
    @UpdateDateColumn()
    updatedAt: Date;
  }
  