// src/modules/profiles/controllers/profiles.controller.ts
import { Controller, Get, Put, Body, Param, UseGuards, Request } from '@nestjs/common';
import { ProfilesService } from '../profiles.service';
import { UpdateProfileDto } from '../dtos/update-profile.dto';
import { JwtAuthGuard } from '../../../common/guards/jwt-auth.guard';
import { ApiTags, ApiBearerAuth, ApiOperation, ApiResponse } from '@nestjs/swagger';

@ApiTags('profiles')
@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@Controller('profiles')
export class ProfilesController {
  constructor(private readonly profilesService: ProfilesService) {}

  @Get('me')
  @ApiOperation({ summary: 'Xem hồ sơ cá nhân của mình' })
  @ApiResponse({ status: 200, description: 'Thông tin hồ sơ cá nhân' })
  async getMyProfile(@Request() req) {
    return this.profilesService.getProfileByUserId(req.user.id);
  }

  @Put('me')
  @ApiOperation({ summary: 'Chỉnh sửa hồ sơ cá nhân của mình' })
  @ApiResponse({ status: 200, description: 'Cập nhật hồ sơ thành công' })
  async updateMyProfile(
    @Body() updateProfileDto: UpdateProfileDto,
    @Request() req,
  ) {
    return this.profilesService.updateProfile(req.user.id, updateProfileDto);
  }

  @Get(':userId')
  @ApiOperation({ summary: 'Xem hồ sơ của người dùng khác' })
  @ApiResponse({ status: 200, description: 'Thông tin hồ sơ người dùng' })
  @ApiResponse({ status: 404, description: 'Hồ sơ không tồn tại' })
  async getUserProfile(@Param('userId') userId: number) {
    return this.profilesService.getProfileByUserId(userId);
  }
}
