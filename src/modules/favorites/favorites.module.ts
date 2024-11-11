// src/modules/favorites/favorites.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { FavoritesController } from './controllers/favorites.controller';
import { FavoritesService } from './favorites.service';
import { Favorite } from './entities/favorite.entity';
import { User } from '../auth/entities/user.entity';
import { RecipesModule } from '../recipes/recipes.module';

@Module({
  imports: [TypeOrmModule.forFeature([Favorite, User]), RecipesModule],
  controllers: [FavoritesController],
  providers: [FavoritesService],
  exports: [FavoritesService],
})
export class FavoritesModule {}
