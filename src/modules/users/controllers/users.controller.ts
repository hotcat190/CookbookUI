// src/modules/users/controllers/users.controller.ts
import { Controller, Get, Param, Put, Body, UseGuards, Request } from '@nestjs/common';
import { UsersService } from '../users.service';
import { UpdateUserDto } from '../dtos/update-user.dto';
import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('users')
@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Get(':userId')
  @ApiOperation({ summary: 'Xem thông tin người dùng' })
  @ApiResponse({ status: 200, description: 'Thông tin người dùng' })
  @ApiResponse({ status: 404, description: 'Người dùng không tồn tại' })
  async getUser(@Param('userId') userId: number) {
    return this.usersService.getUserById(userId);
  }

  @Put(':userId')
  @ApiOperation({ summary: 'Chỉnh sửa thông tin người dùng' })
  @ApiResponse({ status: 200, description: 'Cập nhật thành công' })
  @ApiResponse({ status: 403, description: 'Không có quyền chỉnh sửa' })
  @ApiResponse({ status: 404, description: 'Người dùng không tồn tại' })
  async updateUser(
    @Param('userId') userId: number,
    @Body() updateUserDto: UpdateUserDto,
    @Request() req,
  ) {
    return this.usersService.updateUser(userId, updateUserDto, req.user);
  }
}
