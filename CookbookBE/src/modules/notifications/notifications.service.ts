// src/modules/notifications/notifications.service.ts
import { Injectable, NotFoundException, ForbiddenException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Notification } from './entities/notification.entity';
import { Repository } from 'typeorm';
import { User } from '../auth/entities/user.entity';
import { MailerService } from '../mailer/mailer.service';
import { NotiDto } from './dtos/notification.dto';
import * as admin from "firebase-admin";

@Injectable()
export class NotificationsService {
  constructor(
    @InjectRepository(Notification) private notificationsRepository: Repository<Notification>,
    @InjectRepository(User) private usersRepository: Repository<User>,
    private mailerService: MailerService,
  ) {}

  async getNotifications(userId: number): Promise<any> {
    const notifications = await this.notificationsRepository.find({
      where: { user: { id: userId } },
      order: { createdAt: 'DESC' },
    });
    return { notifications };
  }

  async markAsRead(notificationId: number, userId: number): Promise<any> {
    const notification = await this.notificationsRepository.findOne({
      where: { id: notificationId, user: { id: userId } },
    });
    if (!notification) {
      throw new NotFoundException('Thông báo không tồn tại.');
    }
    notification.isRead = true;
    await this.notificationsRepository.save(notification);
    return { message: 'Đánh dấu thông báo là đã đọc.' };
  }

  async deleteNotification(notificationId: number, userId: number): Promise<any> {
    const notification = await this.notificationsRepository.findOne({
      where: { id: notificationId, user: { id: userId } },
    });
    if (!notification) {
      throw new NotFoundException('Thông báo không tồn tại.');
    }
    await this.notificationsRepository.remove(notification);
    return { message: 'Đã xóa thông báo thành công.' };
  }

  async updateSettings(userId: number, settings: any): Promise<any> {
    // Implement logic to update notification settings
    // This may involve updating user preferences in the database
    // For simplicity, assuming it's handled elsewhere
    return { message: 'Cài đặt thông báo đã được cập nhật.' };
  }


  async sendNoti(notiDto: NotiDto): Promise<any> {
    try {
      const user = await this.usersRepository.findOne({ where: { id: notiDto.userId } });
      const token = user.tokenFCM;
      const title = notiDto.title;
      const body = notiDto.body;

      const response = await admin.messaging().send({
        token,
        notification: {
          title,
          body,
        },
        data : {
          data1 : "abc",
          data2 : "123"
        }
      });
      return response;
    } catch (error) {
      throw error;
    }
  }
  async sendNotification(type_noti: string, userId: number, title: string, body: string, data1?: string, data2?: string, data3?: string, data4?: string ) {
    try {
      const user = await this.usersRepository.findOne({ where: { id: userId } });
      const token = user.tokenFCM;

      await admin.messaging().send({
        token,
        notification: {
          title,
          body,
          
        },
        data : {
          type: type_noti || "TYPE",
          data_1 : data1 || "DATA1",
          data_2 : data2 || "DATA2",
          data_3 : data3 || "DATA3",
          data_4 : data4 || "DATA4"
        }
      });
    } catch (error) {
      throw error;
    }
  }
  async sendNotificationWithImage(type_noti: string, userId: number, title: string, body: string, imageU: string, data1?: string, data2?: string, data3?: string, data4?: string ) {
    try {
      const user = await this.usersRepository.findOne({ where: { id: userId } });
      const token = user.tokenFCM;

      await admin.messaging().send({
        token,
        android: {
          notification: {
            title,
            body,
            imageUrl: imageU || "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Unicode_0x003F.svg/1200px-Unicode_0x003F.svg.png",
            icon: "ic_launcher",
          }
        },    
        data : {
          type: type_noti || "TYPE",
          data_1 : data1 || "DATA1",
          data_2 : data2 || "DATA2",
          data_3 : data3 || "DATA3",
          data_4 : data4 || "DATA4"
        }
      });
    } catch (error) {
      console.log(error);
      throw error;
    }
  }

}
