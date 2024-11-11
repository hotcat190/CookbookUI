// src/modules/favorites/controllers/favorites.controller.ts
import { Controller, Post, Delete, Get, Param, UseGuards, Request } from '@nestjs/common';
import { FavoritesService } from '../favorites.service';
import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('favorites')
@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@Controller('favorites')
export class FavoritesController {
  constructor(private readonly favoritesService: FavoritesService) {}

  @Post()
  @ApiOperation({ summary: 'Thêm vào danh sách yêu thích' })
  @ApiResponse({ status: 201, description: 'Đã thêm vào danh sách yêu thích' })
  @ApiResponse({ status: 400, description: 'Bài viết đã được thêm trước đó' })
  @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
  addToFavorites(@Request() req) {
    return this.favoritesService.addToFavorites(req.user.id);
  }

  @Delete(':recipeId')
  @ApiOperation({ summary: 'Xóa khỏi danh sách yêu thích' })
  @ApiResponse({ status: 200, description: 'Đã xóa khỏi danh sách yêu thích' })
  @ApiResponse({ status: 404, description: 'Bài viết không nằm trong danh sách yêu thích' })
  deleteFromFavorites(@Param('recipeId') recipeId: number, @Request() req) {
    return this.favoritesService.deleteFromFavorites(recipeId, req.user.id);
  }

  @Get()
  @ApiOperation({ summary: 'Xem danh sách yêu thích' })
  @ApiResponse({ status: 200, description: 'Danh sách yêu thích' })
  getFavorites(@Request() req) {
    return this.favoritesService.getFavorites(req.user.id);
  }
}
