// src/common/middleware/logger.middleware.ts
import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';
import { HttpException, HttpStatus } from '@nestjs/common';

@Injectable()
export class LoggerMiddleware implements NestMiddleware {
  private requestMap = new Map<string, number[]>();
  private readonly WINDOW_MS = 1000; // 1 giây
  private readonly MAX_REQUESTS = 5; // Số request tối đa trong 1 giây
  use(req: Request, res: Response, next: NextFunction) {
    const now = Date.now();
    const key = `${req.ip}-${req.method}-${req.originalUrl}`;

    // Lấy danh sách timestamps của request trước đó
    const requestTimestamps = this.requestMap.get(key) || [];
    
    // Lọc ra những timestamps trong cửa sổ thời gian
    const recentRequests = requestTimestamps.filter(
      timestamp => now - timestamp < this.WINDOW_MS
    );

    // Nếu có quá nhiều request, trả về lỗi 429
    if (recentRequests.length >= this.MAX_REQUESTS) {
      throw new HttpException(
        'Quá nhiều request. Vui lòng thử lại sau.',
        HttpStatus.TOO_MANY_REQUESTS
      );
    }

    // Thêm timestamp mới vào danh sách
    recentRequests.push(now);
    this.requestMap.set(key, recentRequests);

    // Log request
    console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl}`);
    
    // Dọn dẹp requestMap định kỳ để tránh memory leak
    if (Math.random() < 0.1) { // 10% chance to cleanup
      this.cleanup();
    }
    next();
  }

  private cleanup() {
    const now = Date.now();
    for (const [key, timestamps] of this.requestMap.entries()) {
      const recentRequests = timestamps.filter(
        timestamp => now - timestamp < this.WINDOW_MS
      );
      if (recentRequests.length === 0) {
        this.requestMap.delete(key);
      } else {
        this.requestMap.set(key, recentRequests);
      }
    }
  }
}