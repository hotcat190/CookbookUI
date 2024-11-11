// src/modules/favorites/favorites.service.ts
import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Favorite } from './entities/favorite.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { RecipesService } from '../recipes/recipes.service';

@Injectable()
export class FavoritesService {
  constructor(
    @InjectRepository(Favorite) private favoritesRepository: Repository<Favorite>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private recipesService: RecipesService,
  ) {}

  async addToFavorites(userId: number): Promise<any> {
    // Assuming that the recipeId is passed in CreateRecipeDto
    // Adjust according to your CreateRecipeDto structure
    // Here, since the controller doesn't pass recipeId, it's unclear
    // Adjust accordingly
    throw new BadRequestException('Thiếu recipeId.');
  }

  async addToFavoritesWithRecipeId(userId: number, recipeId: number): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { id: userId }, relations: ['favorites'] });
    const recipe = await this.recipesService.getRecipeById(recipeId);

    if (user.favorites.some((fav) => fav.recipe.id === recipeId)) {
      throw new BadRequestException('Bài viết đã được thêm vào danh sách yêu thích.');
    }

    const favorite = this.favoritesRepository.create({ user, recipe });
    await this.favoritesRepository.save(favorite);

    // Gửi thông báo đến tác giả bài viết
    await this.recipesService.sendFavoriteNotification(recipe.author.email, recipe.title);

    return { message: 'Đã thêm bài viết vào danh sách yêu thích.', favoritesCount: user.favorites.length + 1 };
  }

  async deleteFromFavorites(recipeId: number, userId: number): Promise<any> {
    const favorite = await this.favoritesRepository.findOne({
      where: { user: { id: userId }, recipe: { id: recipeId } },
    });
    if (!favorite) {
      throw new NotFoundException('Bài viết không nằm trong danh sách yêu thích của bạn.');
    }
    await this.favoritesRepository.remove(favorite);
    return { message: 'Đã xóa bài viết khỏi danh sách yêu thích.', favoritesCount: favorite.user.favorites.length - 1 };
  }

  async getFavorites(userId: number): Promise<any> {
    const favorites = await this.favoritesRepository.find({
      where: { user: { id: userId } },
      relations: ['recipe'],
    });
    return { favorites };
  }
}
