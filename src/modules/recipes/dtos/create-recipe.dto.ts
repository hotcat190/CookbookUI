// src/modules/recipes/dtos/create-recipe.dto.ts
import { IsString, IsNotEmpty, IsOptional, IsNumber, IsArray, ValidateNested, IsUrl, ArrayMinSize } from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

class IngredientDto {
  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Tên nguyên liệu' })
  name: string;

  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Số lượng nguyên liệu' })
  quantity: string;

  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Đơn vị nguyên liệu' })
  unit: string;
}

class StepDto {
  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Mô tả bước thực hiện' })
  description: string;
}

export class CreateRecipeDto {
  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Tiêu đề bài viết' })
  title: string;

  @IsString()
  @IsNotEmpty()
  @ApiProperty({ description: 'Mô tả bài viết' })
  description: string;

  @IsOptional()
  @IsString()
  @ApiPropertyOptional({ description: 'Thời gian nấu' })
  cookTime?: string;

  @IsOptional()
  @IsNumber()
  @ApiPropertyOptional({ description: 'Số khẩu phần' })
  servings?: number;

  @IsOptional()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => IngredientDto)
  @ApiPropertyOptional({ description: 'Danh sách nguyên liệu' })
  ingredients?: IngredientDto[];

  @IsOptional()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => StepDto)
  @ApiPropertyOptional({ description: 'Các bước thực hiện' })
  steps?: StepDto[];

  @IsOptional()
  @IsUrl()
  @ApiPropertyOptional({ description: 'Hình ảnh chính của món ăn' })
  mainImage?: string;
}
