// src/modules/comments/controllers/comments.controller.ts
import { Controller, Post, Delete, Get, Body, Param, UseGuards, Request } from '@nestjs/common';
import { CommentsService } from '../comments.service';
import { CreateCommentDto } from '../dtos/create-comment.dto';
import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('comments')
@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@Controller('comments')
export class CommentsController {
  constructor(private readonly commentsService: CommentsService) {}

  @Post(':postId')
  @ApiOperation({ summary: 'Thêm bình luận' })
  @ApiResponse({ status: 201, description: 'Thêm bình luận thành công' })
  @ApiResponse({ status: 400, description: 'Nội dung bình luận không hợp lệ' })
  @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
  createComment(
    @Param('postId') postId: number,
    @Body() createCommentDto: CreateCommentDto,
    @Request() req,
  ) {
    return this.commentsService.createComment(postId, createCommentDto, req.user.id);
  }

  @Delete(':commentId')
  @ApiOperation({ summary: 'Xóa bình luận của mình' })
  @ApiResponse({ status: 200, description: 'Xóa bình luận thành công' })
  @ApiResponse({ status: 403, description: 'Không có quyền xóa' })
  @ApiResponse({ status: 404, description: 'Bình luận không tồn tại' })
  deleteComment(@Param('commentId') commentId: number, @Request() req) {
    return this.commentsService.deleteComment(commentId, req.user.id);
  }

  @Get(':postId')
  @ApiOperation({ summary: 'Xem bình luận' })
  @ApiResponse({ status: 200, description: 'Danh sách bình luận' })
  @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
  getComments(@Param('postId') postId: number) {
    return this.commentsService.getComments(postId);
  }
}
