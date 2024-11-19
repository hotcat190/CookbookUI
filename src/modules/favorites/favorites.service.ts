// src/modules/favorites/favorites.service.ts
import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Favorite } from './entities/favorite.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { Post } from '../posts/entities/post.entity';

@Injectable()
export class FavoritesService {
  constructor(
    @InjectRepository(Favorite) private favoritesRepository: Repository<Favorite>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    @InjectRepository(Post) private postsRepository: Repository<Post>,
  ) {}

  async addToFavorites(postId: any, userId: number): Promise<any> {
    const user = await this.usersRepository
    .createQueryBuilder('user')
    .leftJoinAndSelect('user.favorites', 'favorites')
    .leftJoinAndSelect('favorites.post', 'post')
    .select(['user.id', 'favorites.id', 'post.id'])
    .where('user.id = :id', { id: userId })
    .getOne();

    if (!user) {
      throw new NotFoundException('User not found');
    }

    const favoritePostIds = user.favorites.map(favorite => favorite.post.id);
    const post = await this.postsRepository.findOne({
      where: { id: postId }
    });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    if (favoritePostIds.some((fav) => fav == postId)) {
      throw new BadRequestException('Bài viết đã có trong danh sách yêu thích.');
    }
    const favorite = this.favoritesRepository.create({ user, post });
    await this.favoritesRepository.save(favorite);

    return { message: 'Đã thêm bài viết vào danh sách yêu thích.'};
  
  }

  async deleteFromFavorites(postId: number, userId: number): Promise<any> {
    const favorite = await this.favoritesRepository.findOne({
      where: { user: { id: userId }, post: { id: postId } },
    });
    if (!favorite) {
      throw new NotFoundException('Bài viết không nằm trong danh sách yêu thích của bạn.');
    }
    await this.favoritesRepository.remove(favorite);
    return { message: 'Đã xóa bài viết khỏi danh sách yêu thích.' };
  }

  async getFavorites(userId: number): Promise<any> {
    const favorites = await this.favoritesRepository.find({
      where: { user: { id: userId } },
      relations: ['post'],
    });
    return { favorites };
  }
}
