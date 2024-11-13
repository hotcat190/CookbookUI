// src/modules/recipes/controllers/recipes.controller.ts
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
  import { RecipesService } from '../recipes.service';
  import { CreateRecipeDto } from '../dtos/create-recipe.dto';
  import { UpdateRecipeDto } from '../dtos/update-recipe.dto';
  import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
  import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';
  
  @ApiTags('recipes')
  @Controller('recipes')
  export class RecipesController {
    constructor(private readonly recipesService: RecipesService) {}

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post()
    @ApiOperation({ summary: 'Tạo bài viết mới' })
    @ApiResponse({ status: 201, description: 'Tạo bài viết thành công' })
    @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ' })
    createRecipe(@Body() createRecipeDto: CreateRecipeDto, @Request() req) {
      return this.recipesService.createRecipe(createRecipeDto, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Put(':recipeId')
    @ApiOperation({ summary: 'Chỉnh sửa bài viết' })
    @ApiResponse({ status: 200, description: 'Cập nhật bài viết thành công' })
    @ApiResponse({ status: 403, description: 'Không có quyền chỉnh sửa' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    updateRecipe(
      @Param('recipeId') recipeId: number,
      @Body() updateRecipeDto: UpdateRecipeDto,
      @Request() req,
    ) {
      return this.recipesService.updateRecipe(recipeId, updateRecipeDto, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Delete(':recipeId')
    @ApiOperation({ summary: 'Xóa bài viết' })
    @ApiResponse({ status: 200, description: 'Xóa bài viết thành công' })
    @ApiResponse({ status: 403, description: 'Không có quyền xóa' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    deleteRecipe(@Param('recipeId') recipeId: number, @Request() req) {
      return this.recipesService.deleteRecipe(recipeId, req.user.id);
    }
  
    @Get(':recipeId')
    @UseGuards() // Optional: Add guards if necessary
    @ApiOperation({ summary: 'Xem bài viết chi tiết' })
    @ApiResponse({ status: 200, description: 'Thông tin chi tiết của bài viết' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    getRecipeById(@Param('recipeId') recipeId: number) {
      console.log('start', recipeId);
      return this.recipesService.getRecipeById(recipeId);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Post(':recipeId/like')
    @ApiOperation({ summary: 'Thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã thích bài viết' })
    @ApiResponse({ status: 400, description: 'Đã thích bài viết này trước đó' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    likeRecipe(@Param('recipeId') recipeId: number, @Request() req) {
      return this.recipesService.likeRecipe(recipeId, req.user.id);
    }

    @ApiBearerAuth()
    @UseGuards(JwtAuthGuard)
    @Delete(':recipeId/like')
    @ApiOperation({ summary: 'Bỏ thích bài viết' })
    @ApiResponse({ status: 200, description: 'Đã bỏ thích bài viết' })
    @ApiResponse({ status: 400, description: 'Bạn chưa thích bài viết này' })
    @ApiResponse({ status: 404, description: 'Bài viết không tồn tại' })
    unlikeRecipe(@Param('recipeId') recipeId: number, @Request() req) {
      return this.recipesService.unlikeRecipe(recipeId, req.user.id);
    }
  }
  