// src/modules/users/dtos/update-user.dto.ts
import { IsOptional, IsString, IsPhoneNumber, IsUrl, MaxLength } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateUserDto {
  @IsOptional()
  @IsString()
  @MaxLength(30)
  @ApiPropertyOptional({ description: 'Tên người dùng mới' })
  username?: string;

  @IsOptional()
  @IsString()
  @MaxLength(160)
  @ApiPropertyOptional({ description: 'Bio cá nhân' })
  bio?: string;

  @IsOptional()
  @IsPhoneNumber(null)
  @ApiPropertyOptional({ description: 'Số điện thoại' })
  phone?: string;

  @IsOptional()
  @IsUrl()
  @ApiPropertyOptional({ description: 'URL avatar mới' })
  avatar?: string;
}
