// src/modules/comments/dtos/delete-comment.dto.ts
import { IsNumber } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class DeleteCommentDto {
  @IsNumber()
  @ApiProperty({ description: 'ID của bình luận cần xóa' })
  commentId: number;
}
