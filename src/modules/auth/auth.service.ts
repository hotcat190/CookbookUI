// src/modules/auth/auth.service.ts
import { Injectable, BadRequestException, UnauthorizedException, NotFoundException } from '@nestjs/common';
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
import { UpdateProfileDto } from './dtos/update-profile.dto';
import { Post } from '../posts/entities/post.entity';
import { FullReponsePostDto, LiteReponsePostDto } from '../posts/dtos/create-post.dto';
import { ChangePasswordDto } from './dtos/change-password.dto';

@Injectable()
export class AuthService {
  [x: string]: any;
  constructor(
    @InjectRepository(User) private usersRepository: Repository<User>,
    @InjectRepository(Post) private postsRepository: Repository<Post>,
    private jwtService: JwtService,
    private mailerService: MailerService,
  ) {}
  async getUserIdFromToKen(token: string): Promise<number> {
    const payload = this.jwtService.decode(token);
    return payload['sub'];
  }
  async register(registerDto: RegisterDto, baseUrl: string): Promise<any> {
    
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
      const test1 = await bcrypt.hash("Password123", 10);
      const test2 = await bcrypt.hash("Password123", 10);
      const test3 = await bcrypt.hash("Password123", 10);
      const test4 = await bcrypt.hash("Password123", 10);
      const test5 = await bcrypt.hash("Password123", 10);
      console.log(test1);
      console.log(test2);
      console.log(test3);
      console.log(test4);
      console.log(test5);

      const user = this.usersRepository.create({
        username,
        email,
        password: hashedPassword,
        isActive: false,
        verificationToken: uuidv4(),
      });

      await this.usersRepository.save(user);
      const url = `${baseUrl}/auth/verify-email?token=${user.verificationToken}`;
      await this.mailerService.sendVerificationEmail(user.username, user.email, user.verificationToken, baseUrl);

      return { message: 'Đăng ký thành công. Vui lòng kiểm tra email để xác thực.', url: url };
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

  async verifyEmail(token: string): Promise<string> {
    const user = await this.usersRepository.findOne({ where: { verificationToken: token } });
    
    if (!user) {
      return `
        <!DOCTYPE html>
        <html lang="vi">
          <head>
            <meta charset="UTF-8">
            <title>Xác Thực Email - Lỗi</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
          </head>
          <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f2f5; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh;">
            <div style="max-width: 600px; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center;">
              <div style="font-size: 50px; color: #e74c3c; margin-bottom: 20px;">❌</div>
              <h1 style="color: #333333; margin-bottom: 20px;">Xác Thực Email Thất Bại</h1>
              <p style="color: #555555; font-size: 16px; line-height: 1.6;">
                Liên kết xác thực không hợp lệ hoặc đã hết hạn. Vui lòng yêu cầu gửi lại email xác thực hoặc liên hệ hỗ trợ.
              </p>
              <footer style="margin-top: 40px; color: #888888; font-size: 14px;">&copy; 2024 CookBook. Tất cả các quyền được bảo lưu.</footer>
            </div>
          </body>
        </html>
      `;
    }
  
    user.isActive = true;
    user.verificationToken = null;
    await this.usersRepository.save(user);
  
    return `
      <!DOCTYPE html>
      <html lang="vi">
        <head>
          <meta charset="UTF-8">
          <title>Xác Thực Email Thành Công</title>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f2f5; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh;">
          <div style="max-width: 600px; background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center;">
            <div style="font-size: 50px; color: #28a745; margin-bottom: 20px;">✅</div>
            <h1 style="color: #333333; margin-bottom: 20px;">🎉 Email Xác Thực Thành Công!</h1>
            <p style="color: #555555; font-size: 16px; line-height: 1.6;">
              <strong>Username:</strong> ${user.username}<br>
              <strong>Email:</strong> ${user.email}
            </p>
            <p style="color: #555555; font-size: 16px; line-height: 1.6;">
              Cảm ơn bạn đã xác thực email của mình. Bây giờ bạn có thể truy cập đầy đủ các tính năng của CookBook và bắt đầu khám phá những công thức nấu ăn tuyệt vời!
            </p>
            <a href="https://www.cookbook.com" style="display: inline-block; padding: 12px 24px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 30px;">Về Trang Chủ</a>
            <footer style="margin-top: 40px; color: #888888; font-size: 14px;">&copy; 2024 CookBook. Tất cả các quyền được bảo lưu.</footer>
          </div>
        </body>
      </html>
    `;
  }
  async changePassword(userId: number, changePasswordDto: ChangePasswordDto): Promise<any> {
    const user = await this.usersRepository.findOne({ where: { id: userId } });
    if (!user) {
      throw new NotFoundException('User not found');
    }
    const isPasswordValid = await bcrypt.compare(changePasswordDto.oldPassword, user.password);
    if (!isPasswordValid) {
      throw new UnauthorizedException('Mật khẩu cũ không chính xác.');
    }
    user.password = await bcrypt.hash(changePasswordDto.newPassword, 10);
    await this.usersRepository.save(user);
    return { message: 'Đổi mật khẩu thành công.' };
  }
  
  async forgotPassword(forgotDto: ForgotDto): Promise<any> {
    const { email } = forgotDto;
    const user = await this.usersRepository.findOne({ where: { email } });
    if (user) {
      user.resetPasswordCode = Math.floor(100000 + Math.random() * 900000).toString();
      await this.usersRepository.save(user);

      // Gửi email reset mật khẩu
      await this.mailerService.sendResetPasswordEmail(user.name, user.email, user.resetPasswordCode);
    }

    return { message: 'Nếu email tồn tại, liên kết đặt lại mật khẩu đã được gửi.' };
  }
  

  async resetPassword(resetPasswordDto: ResetPasswordDto): Promise<any> {
    const { email, code, password } = resetPasswordDto;

    const user = await this.usersRepository.findOne({ where: { email: email } });
    if(user.resetPasswordCode === null){
      throw new BadRequestException('Mã xác nhận đã hết hạn. Vui lòng yêu cầu đặt lại mật khẩu mới.');
    }
    if(user.resetPasswordCode !== code){
      throw new BadRequestException('Mã xác nhận không đúng.');
    }

    user.password = await bcrypt.hash(password, 10);
    user.resetPasswordCode = null;
    await this.usersRepository.save(user);
    return { message: 'Đặt lại mật khẩu thành công.' };
  }
  
  async updateProfile(userId: number, updateProfileDto: UpdateProfileDto): Promise<any> {
    let profile = await this.usersRepository.findOne({ where: { id: userId } });
    if (!profile) {
      return { message: 'Không tìm thấy profile.' };
    }
    Object.assign(profile, updateProfileDto);
    await this.usersRepository.save(profile);
    return { message: 'Cập nhật hồ sơ thành công.', profile };
  }
  async getProfileByUserId(userId: number): Promise<any> {
    const profile = await this.usersRepository.findOne({
      where: { id: userId },
      relations: ['followers', 'following'],
    });
    if (!profile) {
      throw new NotFoundException('Hồ sơ không tồn tại.');
    }
    const totalFollowers = profile.followers.length;
    const totalFollowing = profile.following.length;

    const { id, bio, name, avatar } = profile;
    
    return { userId: id, bio, name, avatar, totalFollowers, totalFollowing };

  }

  async addToFavorites(postId: any, userId: number): Promise<any> {
    const user = await this.usersRepository.createQueryBuilder('user')
      .leftJoinAndSelect('user.favorites', 'favorites')
      .where('user.id = :userId', { userId })
      .select(['user.id', 'favorites.id'])
      .getOne();
    if (!user) {
      throw new NotFoundException('User not found');
    }
    
    const post = await this.postsRepository.findOne({
      where: { id: postId }
    });
    if (!post) {
      throw new NotFoundException('Bài viết không tồn tại.');
    }
    
    const favoritePostIds = user.favorites.map((fav) => fav.id);
    if (favoritePostIds.some((fav) => fav == postId)) {
      throw new BadRequestException('Bài viết đã có trong danh sách yêu thích.');
    }
    
    user.favorites.push(post);
    await this.usersRepository.save(user);
    return { message: 'Đã thêm bài viết vào danh sách yêu thích.'};
  
  }

  async deleteFromFavorites(postId: number, userId: number): Promise<any> {
    const user = await this.usersRepository.createQueryBuilder('user')
      .leftJoinAndSelect('user.favorites', 'favorites')
      .where('user.id = :userId', { userId })
      .select(['user.id', 'favorites.id'])
      .getOne();
    if (!user) {
      throw new NotFoundException('User not found');
    }
    console.log(user.favorites)
    const favoriteIndex = user.favorites.findIndex((fav) => fav.id === postId);

    if (favoriteIndex === -1) {
      throw new NotFoundException('Bài viết không nằm trong danh sách yêu thích của bạn.');
    }
    user.favorites.splice(favoriteIndex, 1);

    await this.usersRepository.save(user);
    return { message: 'Đã xóa bài viết khỏi danh sách yêu thích.' };
  }

  async getFavorites(page: number, userId: number): Promise<any> {
    const user = await this.usersRepository.findOne({
      where: { id: userId },
      relations: ['favorites'],
    });
    if (!user) {
      throw new NotFoundException('User not found');
    }
    const favorites = user.favorites;

    const itemsPerPage = 10;
    const startIndex = (page - 1) * itemsPerPage;
    if (favorites.length > itemsPerPage*page) {
      return {nextPage: "true", favorites: favorites.slice(startIndex, startIndex + itemsPerPage).map(fav => new LiteReponsePostDto(fav))};
    }
    else{
      return {nextPage: "false", favorites: favorites.slice(startIndex, startIndex + itemsPerPage).map(fav => new LiteReponsePostDto(fav))};
    }
  }
}
