// src/app.controller.ts
import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('app')
@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get('/')
  @ApiOperation({ summary: 'Kiểm tra tình trạng ứng dụng' })
  @ApiResponse({ status: 200, description: 'Ứng dụng đang hoạt động' })
  getHealth() {
    return this.appService.getHealth();
  }
}
