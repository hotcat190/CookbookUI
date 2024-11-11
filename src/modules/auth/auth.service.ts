// src/modules/auth/auth.service.ts
import { Injectable, BadRequestException, UnauthorizedException } from '@nestjs/common';
import { RegisterDto } from './dtos/register.dto';
import { LoginDto } from './dtos/login.dto';
import { ForgotDto } from './dtos/forgot.dto';
import { ResetPasswordDto } from './dtos/reset-password.dto';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './entities/user.entity';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { JwtService } from '@nestjs/jwt';
import { MailerService } from '../mailer/mailer.service';
import { v4 as uuidv4 } from 'uuid';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User) private usersRepository: Repository<User>,
    private jwtService: JwtService,
    private mailerService: MailerService,
  ) {}

  async register(registerDto: RegisterDto): Promise<any> {
    
    try {
      const { username, email, password } = registerDto;

      const existingUser = await this.usersRepository.findOne({ where: [{ email }, { username }] });

      if (existingUser?.isActive) {
        throw new BadRequestException('Email hoặc tên đăng nhập đã được sử dụng.');
      }
      if(existingUser){
        await this.usersRepository.delete(existingUser.id);
      }
      const hashedPassword = await bcrypt.hash(password, 10);
      const user = this.usersRepository.create({
        username,
        email,
        password: hashedPassword,
        isActive: false,
        verificationToken: uuidv4(),
      });

      await this.usersRepository.save(user);

      await this.mailerService.sendVerificationEmail(user.username, user.email, user.verificationToken);

      return { message: 'Đăng ký thành công. Vui lòng kiểm tra email để xác thực.' };
    } 
    catch (error) {
      if (error instanceof BadRequestException) {
      throw error;
      }
      throw new BadRequestException('Có lỗi xảy ra trong quá trình đăng ký.');
    }
  }

  async login(loginDto: LoginDto): Promise<any> {
    const { username, password } = loginDto;
    const user = await this.usersRepository.findOne({ where: { username } });

    if (!user) {
      throw new UnauthorizedException('Sai tên đăng nhập hoặc mật khẩu.');
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      throw new UnauthorizedException('Sai tên đăng nhập hoặc mật khẩu.');
    }

    if (!user.isActive) {
      throw new UnauthorizedException('Tài khoản chưa được xác thực.');
    }

    const payload = { sub: user.id, username: user.username, roles: user.roles };
    const token = this.jwtService.sign(payload);

    return { access_token: token, message: 'Đăng nhập thành công' };
  }

  async verifyEmail(token: string): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { verificationToken: token } });
    if (!user) {
      throw new BadRequestException('Phiên đăng nhập không hợp lệ hoặc đã hết hạn.');
    }

    user.isActive = true;
    user.verificationToken = null;
    await this.usersRepository.save(user);

    return { message: 'Xác thực email thành công.' };
  }

  async forgotPassword(forgotDto: ForgotDto): Promise<any> {
    const { email } = forgotDto;
    const user = await this.usersRepository.findOne({ where: { email } });
    if (user) {
      user.resetPasswordToken = uuidv4();
      user.resetPasswordExpires = new Date(Date.now() + 3600000); // 1 giờ
      await this.usersRepository.save(user);

      // Gửi email reset mật khẩu
      await this.mailerService.sendResetPasswordEmail(user.email, user.resetPasswordToken);
    }

    return { message: 'Nếu email tồn tại, liên kết đặt lại mật khẩu đã được gửi.' };
  }

  async resetPassword(resetPasswordDto: ResetPasswordDto): Promise<any> {
    const { token, password, confirmPassword } = resetPasswordDto;

    if (password !== confirmPassword) {
      throw new BadRequestException('Mật khẩu không khớp.');
    }

    const user = await this.usersRepository.findOne({ where: { resetPasswordToken: token } });
    if (!user || (user.resetPasswordExpires && user.resetPasswordExpires < new Date())) {
      throw new BadRequestException('Phiên đăng nhập không hợp lệ hoặc đã hết hạn.');
    }

    user.password = await bcrypt.hash(password, 10);
    user.resetPasswordToken = null;
    user.resetPasswordExpires = null;
    await this.usersRepository.save(user);

    return { message: 'Đặt lại mật khẩu thành công.' };
  }
}
