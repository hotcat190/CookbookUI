// src/main.ts

import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe, Logger } from '@nestjs/common';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Global Validation Pipe
  app.useGlobalPipes(new ValidationPipe({
    whitelist: true,
    forbidNonWhitelisted: true,
    transform: true,
  }));

  // Swagger Setup
  const config = new DocumentBuilder()
    .setTitle('Recipe App API')
    .setDescription('API documentation for the Recipe App')
    .setVersion('1.0')
    .addBearerAuth()
    .build();
    
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api-docs', app, document);

  // Start server
  const PORT = process.env.PORT || 3000;
  await app.listen(PORT);
  Logger.log(`Server is running on http://localhost:${PORT}`);
  Logger.log(`Swagger is available on http://localhost:${PORT}/api-docs`);
}
bootstrap();
