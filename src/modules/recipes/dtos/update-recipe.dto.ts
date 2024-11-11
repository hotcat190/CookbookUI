// src/modules/recipes/dtos/update-recipe.dto.ts
import { PartialType } from '@nestjs/mapped-types';
import { CreateRecipeDto } from './create-recipe.dto';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateRecipeDto extends PartialType(CreateRecipeDto) {
  @ApiPropertyOptional({ description: 'Tiêu đề bài viết mới' })
  title?: string;

  @ApiPropertyOptional({ description: 'Mô tả bài viết mới' })
  description?: string;
}
