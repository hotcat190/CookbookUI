// src/modules/mailer/mailer.service.ts
import { Injectable } from '@nestjs/common';
import { MailerService as NestMailerService } from '@nestjs-modules/mailer';

@Injectable()
export class MailerService {
  constructor(private mailer: NestMailerService) {}

  async sendVerificationEmail(username: string, email: string, token: string): Promise<void> {
    const baseUrl = process.env.APP_URL;
    const url = `${baseUrl}/auth/verify-email?token=${token}`;
    
    try {
      await this.mailer.sendMail({
        to: email,
        subject: `Xác thực Email của bạn`,
        html: `
          <p>Đã có người đăng ký bằng Email của bạn. User: ${username}</p>
          <p>Xác thực Email của bạn bằng cách click vào button bên dưới:</p>
          <a href="${url}" style="background: #4CAF50; color: white; padding: 14px 20px; text-decoration: none; border-radius: 8px; display: inline-block;">
          Xác thực Email
          </a>
        `,
      });
    } catch (error) {
      console.error('Failed to send verification email:', error);
      throw error;
    }
  }

  async sendResetPasswordEmail(email: string, token: string): Promise<void> {
    const baseUrl = process.env.APP_URL || 'https://cookbook.example.com';
    const url = `${baseUrl}/reset-password?token=${token}`;
    
    try {
      await this.mailer.sendMail({
        to: email,
        subject: 'Đặt lại mật khẩu',
        text: `Để đặt lại mật khẩu của bạn, vui lòng nhấp vào liên kết sau: ${url}`,
      });
    } catch (error) {
      console.error('Failed to send reset password email:', error);
      throw error;
    }
  }

  async sendLikeNotification(email: string, recipeTitle: string): Promise<void> {
    await this.mailer.sendMail({
      to: email,
      subject: 'Bài viết của bạn đã được thích',
      template: 'like-notification', // Name of the template file (like-notification.hbs)
      context: {
        recipeTitle,
      },
    });
  }

  async sendUnlikeNotification(email: string, recipeTitle: string): Promise<void> {
    await this.mailer.sendMail({
      to: email,
      subject: 'Bài viết của bạn đã bị bỏ thích',
      template: 'unlike-notification', // Name of the template file (unlike-notification.hbs)
      context: {
        recipeTitle,
      },
    });
  }

  async sendNotificationEmail(email: string, message: string): Promise<void> {
    await this.mailer.sendMail({
      to: email,
      subject: 'Thông báo mới',
      template: 'notification', // Name of the template file (notification.hbs)
      context: {
        message,
      },
    });
  }
}
