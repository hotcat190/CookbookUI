// src/common/middleware/logger.middleware.ts
import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';
import { HttpException, HttpStatus } from '@nestjs/common';

@Injectable()
export class LoggerMiddleware implements NestMiddleware {
  private requestMap = new Map<string, number[]>();
  private readonly WINDOW_MS = 1000;
  private readonly MAX_REQUESTS = 3;

  private getDeviceId(req: Request): string {
    // Try to get device identifier from common headers
    return (
      req.headers['x-device-id'] as string || 
      req.headers['x-forwarded-for'] as string ||
      req.headers['user-agent'] as string ||
      req.headers['authorization'] as string ||
      req.ip
    );
  }

  use(req: Request, res: Response, next: NextFunction) {
    const now = Date.now();
    console.log(req.headers);
    // Use device identifier instead of just IP
    const deviceId = this.getDeviceId(req);
    const key = `${deviceId}-${req.method}-${req.originalUrl}`;

    const requestTimestamps = this.requestMap.get(key) || [];
    const recentRequests = requestTimestamps.filter(
      timestamp => now - timestamp < this.WINDOW_MS
    );

    if (recentRequests.length >= this.MAX_REQUESTS) {
      throw new HttpException(
        'Too many requests from this device',
        HttpStatus.TOO_MANY_REQUESTS
      );
    }

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