// src/modules/search/search.module.ts
import { Module } from '@nestjs/common';
import { SearchController } from './controllers/search.controller';
import { SearchService } from './search.service';
import { RecipesModule } from '../recipes/recipes.module';

@Module({
  imports: [RecipesModule],
  controllers: [SearchController],
  providers: [SearchService],
})
export class SearchModule {}
