// src/modules/posts/dtos/update-post.dto.ts
import { PartialType } from '@nestjs/mapped-types';
import { CreatePostDto } from './create-post.dto';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdatePostDto extends PartialType(CreatePostDto) {
  @ApiPropertyOptional({ description: 'Tiêu đề bài viết mới' })
  title?: string;

  @ApiPropertyOptional({ description: 'Mô tả bài viết mới' })
  description?: string;
}
