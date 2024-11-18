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
  @Controller('')
  export class PostsController {
    constructor(private readonly postsService: PostsService) {}

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post('posts')
    @ApiOperation({ summary: 'Tạo bài viết mới' })
    @ApiResponse({ status: 201, description: 'Tạo bài viết thành công' })
    @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ' })
    createPost(@Body() createPostDto: CreatePostDto, @Request() req) {
      return this.postsService.createPost(createPostDto, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Put('posts/:postId')
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
    @Delete('posts/:postId')
    @ApiOperation({ summary: 'Xóa bài viết' })
    @ApiResponse({ status: 200, description: 'Xóa bài viết thành công' })
    @ApiResponse({ status: 403, description: 'Không có quyền xóa' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    deletePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.deletePost(postId, req.user.id);
    }
  
    @Get('posts/:postId')
    @ApiOperation({ summary: 'Xem bài viết chi tiết' })
    @ApiResponse({ status: 200, description: 'Thông tin chi tiết của bài viết' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    getPostById(@Param('postId') postId: number) {
      return this.postsService.getPostById(postId);
    }
    @Get('likes/:postId/:page')
    @ApiOperation({ summary: 'Xem danh sách thích bài viết theo trang (mỗi trang 10, bắt đầu từ trang 1), more true là có trang tiếp theo' })
    @ApiResponse({ status: 200, description: 'Danh sách người thích bài viết' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    getLikeByPostId(@Param('postId') postId: number, @Param('page') page: number) {
      return this.postsService.getLikeByPostId(postId, page);
    }
    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Get('newfeeds/:limit')
    @ApiOperation({ summary: 'Lấy newfeeds theo limit' })
    @ApiResponse({ status: 200, description: 'Newfeed' })
    @ApiResponse({ status: 404, description: 'Error' })
    getNewfeeds(@Request() req, @Param('limit') limit: number) {
      return this.postsService.getNewfeeds(req.user.id, limit);
    }



    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post('posts/:postId/like')
    @ApiOperation({ summary: 'Thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã thích bài viết' })
    @ApiResponse({ status: 400, description: 'Đã thích bài viết này trước đó' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    likePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.likePost(postId, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Delete('posts/:postId/like')
    @ApiOperation({ summary: 'Bỏ thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã bỏ thích bài viết' })
    @ApiResponse({ status: 400, description: 'Bạn chưa thích bài viết này' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    unlikePost(@Param('postId') postId: number, @Request() req) {
      return this.postsService.unlikePost(postId, req.user.id);
    }

  }
  