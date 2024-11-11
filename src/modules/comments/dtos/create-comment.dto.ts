// src/modules/comments/dtos/create-comment.dto.ts
import { IsString, IsNotEmpty, MaxLength } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class CreateCommentDto {
  @IsString()
  @IsNotEmpty()
  @MaxLength(500)
  @ApiProperty({ description: 'Nội dung bình luận' })
  content: string;
}
