// src/modules/users/entities/user-profile.entity.ts
import { Entity, PrimaryGeneratedColumn, Column, OneToOne, JoinColumn } from 'typeorm';
import { User } from '../../auth/entities/user.entity';

@Entity()
export class UserProfile {
  @PrimaryGeneratedColumn()
  id: number;

  @OneToOne(() => User, (user) => user.id, { onDelete: 'CASCADE' })
  @JoinColumn()
  user: User;

  @Column({ nullable: true, length: 160 })
  bio: string;

  @Column({ nullable: true })
  phone: string;

  @Column({ nullable: true })
  avatar: string;
}
