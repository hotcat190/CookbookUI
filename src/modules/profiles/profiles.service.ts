// src/modules/profiles/profiles.service.ts
import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { UserProfile } from './entities/profile.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { UpdateProfileDto } from './dtos/update-profile.dto';

@Injectable()
export class ProfilesService {
  constructor(
    @InjectRepository(UserProfile) private profilesRepository: Repository<UserProfile>,
    @InjectRepository(User) private usersRepository: Repository<User>,
  ) {}

  async getProfileByUserId(userId: number): Promise<any> {
    const profile = await this.profilesRepository.findOne({
      where: { user: { id: userId } },
      relations: ['user'],
    });
    if (!profile) {
      throw new NotFoundException('Hồ sơ không tồn tại.');
    }
    return profile;
  }

  async updateProfile(userId: number, updateProfileDto: UpdateProfileDto): Promise<any> {
    let profile = await this.profilesRepository.findOne({ where: { user: { id: userId } } });
    if (!profile) {
      profile = this.profilesRepository.create({ user: { id: userId } });
    }
    Object.assign(profile, updateProfileDto);
    await this.profilesRepository.save(profile);
    return { message: 'Cập nhật hồ sơ thành công.', profile };
  }
}
