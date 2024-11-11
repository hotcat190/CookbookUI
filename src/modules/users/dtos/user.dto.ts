// src/modules/users/dtos/user.dto.ts
import { Exclude, Expose } from 'class-transformer';
import { ApiProperty } from '@nestjs/swagger';

export class UserDto {
  @ApiProperty({ description: 'ID của người dùng' })
  id: number;

  @ApiProperty({ description: 'Tên đăng nhập của người dùng' })
  username: string;

  @ApiProperty({ description: 'Email của người dùng' })
  email: string;

  @ApiProperty({ description: 'Bio cá nhân', nullable: true })
  bio?: string;

  @ApiProperty({ description: 'Số điện thoại', nullable: true })
  phone?: string;

  @ApiProperty({ description: 'URL avatar', nullable: true })
  avatar?: string;

  @ApiProperty({ description: 'Vai trò của người dùng' })
  roles: string[];

  @ApiProperty({ description: 'Trạng thái hoạt động của người dùng' })
  isActive: boolean;

  @Expose()
  createdAt: Date;

  @Expose()
  updatedAt: Date;
}
