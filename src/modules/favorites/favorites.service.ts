// src/modules/favorites/favorites.service.ts
import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Favorite } from './entities/favorite.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { PostsService } from '../posts/posts.service';

@Injectable()
export class FavoritesService {
  constructor(
    @InjectRepository(Favorite) private favoritesRepository: Repository<Favorite>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private postsService: PostsService,
  ) {}

  async addToFavorites(userId: number): Promise<any> {
    // Assuming that the postId is passed in CreatePostDto
    // Adjust according to your CreatePostDto structure
    // Here, since the controller doesn't pass postId, it's unclear
    // Adjust accordingly
    throw new BadRequestException('Thiếu postId.');
  }

  async addToFavoritesWithPostId(userId: number, postId: number): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { id: userId }, relations: ['favorites'] });
    const post = await this.postsService.getPostById(postId);

    if (user.favorites.some((fav) => fav.post.id === postId)) {
      throw new BadRequestException('Bài viết đã được thêm vào danh sách yêu thích.');
    }

    const favorite = this.favoritesRepository.create({ user, post });
    await this.favoritesRepository.save(favorite);

    // Gửi thông báo đến tác giả bài viết
    await this.postsService.sendFavoriteNotification(post.author.email, post.title);

    return { message: 'Đã thêm bài viết vào danh sách yêu thích.', favoritesCount: user.favorites.length + 1 };
  }

  async deleteFromFavorites(postId: number, userId: number): Promise<any> {
    const favorite = await this.favoritesRepository.findOne({
      where: { user: { id: userId }, post: { id: postId } },
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
      relations: ['post'],
    });
    return { favorites };
  }
}
