// src/modules/recipes/recipes.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { RecipesController } from './controllers/recipes.controller';
import { RecipesService } from './recipes.service';
import { Recipe } from './entities/recipe.entity';
import { Comment } from './entities/comment.entity';
import { UsersModule } from '../users/users.module';
import { MailerModule } from '../mailer/mailer.module';
import { User } from '../auth/entities/user.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([Recipe, Comment, User]), // Add User here
    UsersModule,
    MailerModule,
  ],
  controllers: [RecipesController],
  providers: [RecipesService],
  exports: [RecipesService],
})
export class RecipesModule {}
