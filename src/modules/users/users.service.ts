// src/modules/users/users.service.ts
import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { UserProfile } from './entities/user-profile.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { UpdateUserDto } from './dtos/update-user.dto';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(UserProfile) private profilesRepository: Repository<UserProfile>,
    @InjectRepository(User) private usersRepository: Repository<User>,
  ) {}

  async getUserById(userId: number): Promise<any> {
    const user = await this.usersRepository.findOne({
      where: { id: userId },
      relations: ['recipes', 'followers', 'following'],
    });
    if (!user) {
      throw new NotFoundException('Người dùng không tồn tại.');
    }
    const { password, verificationToken, resetPasswordToken, resetPasswordExpires, ...result } = user;
    return result;
  }

  async updateUser(userId: number, updateUserDto: UpdateUserDto, currentUser: any): Promise<any> {
    if (currentUser.id !== userId && !currentUser.roles.includes('admin')) {
      throw new ForbiddenException('Bạn không có quyền chỉnh sửa hồ sơ này.');
    }

    const user = await this.usersRepository.findOne({ where: { id: userId } });
    if (!user) {
      throw new NotFoundException('Người dùng không tồn tại.');
    }

    // Cập nhật thông tin người dùng
    Object.assign(user, updateUserDto);
    await this.usersRepository.save(user);

    const { password, verificationToken, resetPasswordToken, resetPasswordExpires, ...result } = user;
    return { message: 'Cập nhật hồ sơ thành công.', user: result };
  }
}
