// src/modules/auth/controllers/auth.controller.ts
import { Controller, Post, Body, Get, Query, Req } from '@nestjs/common';
import { AuthService } from '../auth.service';
import { RegisterDto } from '../dtos/register.dto';
import { LoginDto } from '../dtos/login.dto';
import { ResetPasswordDto } from '../dtos/reset-password.dto';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { ForgotDto } from '../dtos/forgot.dto';
import {Request} from 'express';

@ApiTags('auth')
@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('register')
  @ApiOperation({ summary: 'Đăng ký tài khoản mới' })
  @ApiResponse({ status: 201, description: 'Đăng ký thành công' })
  @ApiResponse({ status: 400, description: 'Dữ liệu không hợp lệ hoặc đã tồn tại' })
  register(@Body() registerDto: RegisterDto, @Req() req: Request) {
    const baseUrl = `${req.protocol}://${req.get('Host')}`;
    return this.authService.register(registerDto, baseUrl);
  }

  @Post('login')
  @ApiOperation({ summary: 'Đăng nhập' })
  @ApiResponse({ status: 200, description: 'Đăng nhập thành công' })
  @ApiResponse({ status: 401, description: 'Sai tên đăng nhập hoặc mật khẩu' })
  login(@Body() loginDto: LoginDto) {
    return this.authService.login(loginDto);
  }

  @Get('verify-email')
  @ApiOperation({ summary: 'Xác thực Email' })
  @ApiResponse({ status: 200, description: 'Xác thực thành công' })
  @ApiResponse({ status: 400, description: 'Token không hợp lệ hoặc đã hết hạn' })
  verifyEmail(@Query('token') token: string) {
    return this.authService.verifyEmail(token);
  }

  @Post('forgot-password')
  @ApiOperation({ summary: 'Quên mật khẩu' })
  @ApiResponse({ status: 200, description: 'Liên kết đặt lại mật khẩu đã được gửi nếu email tồn tại' })
  @ApiResponse({ status: 400, description: 'Email không hợp lệ' })
  forgotPassword(@Body() forgotDto: ForgotDto, @Req() req: Request) {
    
    return this.authService.forgotPassword(forgotDto );
  }

  @Post('reset-password')
  @ApiOperation({ summary: 'Đặt lại mật khẩu' })
  @ApiResponse({ status: 200, description: 'Đặt lại mật khẩu thành công' })
  @ApiResponse({ status: 400, description: 'Token không hợp lệ hoặc đã hết hạn' })
  resetPassword(@Body() resetPasswordDto: ResetPasswordDto) {
    return this.authService.resetPassword(resetPasswordDto);
  }
  
}
