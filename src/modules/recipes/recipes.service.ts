// src/modules/recipes/recipes.service.ts
import { Injectable, NotFoundException, ForbiddenException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Recipe } from './entities/recipe.entity';
import { Repository } from 'typeorm';
import { CreateRecipeDto } from './dtos/create-recipe.dto';
import { UpdateRecipeDto } from './dtos/update-recipe.dto';
import { User } from '../auth/entities/user.entity';
import { MailerService } from '../mailer/mailer.service';

@Injectable()
export class RecipesService {
  [x: string]: any;
  constructor(
    @InjectRepository(Recipe) private recipesRepository: Repository<Recipe>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private mailerService: MailerService,
  ) {}

  async createRecipe(createRecipeDto: CreateRecipeDto, userId: number): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { id: userId } });
    const recipe = this.recipesRepository.create({
      ...createRecipeDto,
      author: user,
    });
    await this.recipesRepository.save(recipe);
    return { message: 'Tạo bài viết thành công.', recipe };
  }

  async updateRecipe(recipeId: number, updateRecipeDto: UpdateRecipeDto, userId: number): Promise<any> {
    const recipe = await this.recipesRepository.findOne({ where: { id: recipeId }, relations: ['author'] });
    if (!recipe) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    if (recipe.author.id !== userId) {
      throw new ForbiddenException('Bạn không có quyền chỉnh sửa bài viết này.');
    }
    Object.assign(recipe, updateRecipeDto);
    await this.recipesRepository.save(recipe);
    return { message: 'Cập nhật bài viết thành công.', recipe };
  }

  async deleteRecipe(recipeId: number, userId: number): Promise<any> {
    const recipe = await this.recipesRepository.findOne({ where: { id: recipeId }, relations: ['author'] });
    if (!recipe) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    if (recipe.author.id !== userId) {
      throw new ForbiddenException('Bạn không có quyền xóa bài viết này.');
    }
    await this.recipesRepository.remove(recipe);
    return { message: 'Xóa bài viết thành công.' };
  }

  async getRecipeById(recipeId: number): Promise<any> {
    const recipe = await this.recipesRepository.findOne({
      where: { id: recipeId },
      relations: ['author', 'comments', 'relatedRecipes'],
    });
    if (!recipe) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    return recipe;
  }

  async likeRecipe(recipeId: number, userId: number): Promise<any> {
    const recipe = await this.recipesRepository.findOne({ where: { id: recipeId }, relations: ['likes', 'author'] });
    if (!recipe) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }

    if (recipe.likes.some((like) => like.id === userId)) {
      throw new BadRequestException('Bạn đã thích bài viết này trước đó.');
    }

    recipe.likes.push({ id: userId } as User);
    await this.recipesRepository.save(recipe);

    // Gửi thông báo đến tác giả
    await this.mailerService.sendLikeNotification(recipe.author.email, recipe.title);

    return { message: 'Đã thích bài viết.', likesCount: recipe.likes.length };
  }

  async unlikeRecipe(recipeId: number, userId: number): Promise<any> {
    const recipe = await this.recipesRepository.findOne({ where: { id: recipeId }, relations: ['likes', 'author'] });
    if (!recipe) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }

    const likeIndex = recipe.likes.findIndex((like) => like.id === userId);
    if (likeIndex === -1) {
      throw new BadRequestException('Bạn chưa thích bài viết này.');
    }

    recipe.likes.splice(likeIndex, 1);
    await this.recipesRepository.save(recipe);

    // Gửi thông báo đến tác giả
    await this.mailerService.sendUnlikeNotification(recipe.author.email, recipe.title);

    return { message: 'Đã bỏ thích bài viết.', likesCount: recipe.likes.length };
  }
}
