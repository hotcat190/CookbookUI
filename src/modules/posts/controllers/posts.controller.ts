// src/modules/posts/controllers/posts.controller.ts
import {
    Controller,
    Post,
    Body,
    UseGuards,
    Request,
    Get,
    Param,
    Put,
    Delete,
    Query,
  } from '@nestjs/common';
  import { PostsService } from '../posts.service';
  import { CreatePostDto } from '../dtos/create-post.dto';
  import { UpdatePostDto } from '../dtos/update-post.dto';
  import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
  import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';
  
  @ApiTags('posts')
  @Controller('posts')
  export class PostsController {
    constructor(private readonly postsService: PostsService) {}

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post()
    @ApiOperation({ summary: 'Tạo bài viết mới' })
    @ApiResponse({ status: 201, description: 'Tạo bài viết thành công' })
    @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ' })
    createPost(@Body() createPostDto: CreatePostDto, @Request() req) {
      return this.postsService.createPost(createPostDto, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Put(':postId')
    @ApiOperation({ summary: 'Chỉnh sửa bài viết' })
    @ApiResponse({ status: 200, description: 'Cập nhật bài viết thành công' })
    @ApiResponse({ status: 403, description: 'Không có quyền chỉnh sửa' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    updatePost(
      @Param('postId') postId: number,
      @Body() updatePostDto: UpdatePostDto,
      @Request() req,
    ) {
      return this.postsService.updatePost(postId, updatePostDto, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Delete(':postId')
    @ApiOperation({ summary: 'Xóa bài viết' })
    @ApiResponse({ status: 200, description: 'Xóa bài viết thành công' })
    @ApiResponse({ status: 403, description: 'Không có quyền xóa' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    deletePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.deletePost(postId, req.user.id);
    }
  
    @Get(':postId')
    @UseGuards() // Optional: Add guards if necessary
    @ApiOperation({ summary: 'Xem bài viết chi tiết' })
    @ApiResponse({ status: 200, description: 'Thông tin chi tiết của bài viết' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    getPostById(@Param('postId') postId: number) {
      console.log('start', postId);
      return this.postsService.getPostById(postId);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post(':postId/like')
    @ApiOperation({ summary: 'Thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã thích bài viết' })
    @ApiResponse({ status: 400, description: 'Đã thích bài viết này trước đó' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    likePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.likePost(postId, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Delete(':postId/like')
    @ApiOperation({ summary: 'Bỏ thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã bỏ thích bài viết' })
    @ApiResponse({ status: 400, description: 'Bạn chưa thích bài viết này' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    unlikePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.unlikePost(postId, req.user.id);
    }
  }
  