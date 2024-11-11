// src/modules/auth/dtos/login.dto.ts
import { IsString, MinLength, MaxLength } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class LoginDto {
  @IsString()
  @MinLength(3)
  @MaxLength(30)
  @ApiProperty({ description: 'Tên đăng nhập' })
  username: string;

  @IsString()
  @MinLength(6)
  @MaxLength(20)
  @ApiProperty({ description: 'Mật khẩu' })
  password: string;
}
