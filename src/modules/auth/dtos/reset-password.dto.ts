// src/modules/auth/dtos/reset-password.dto.ts
import { IsString, MinLength, MaxLength, Matches } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class ResetPasswordDto {
  @IsString()
  @ApiProperty({ description: 'Token đặt lại mật khẩu' })
  token: string;

  @IsString()
  @MinLength(6)
  @MaxLength(20)
  @Matches(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/, {
    message: 'Mật khẩu phải chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường và một chữ số',
  })
  @ApiProperty({ description: 'Mật khẩu mới' })
  password: string;

  @IsString()
  @ApiProperty({ description: 'Xác nhận mật khẩu mới' })
  confirmPassword: string;
}
