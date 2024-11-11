
// src/config/configuration.ts
export default () => ({
  port: parseInt(process.env.PORT, 10) || 3000,
  jwt: {
    secret: process.env.JWT_SECRET || 'defaultSecret',
    expiresIn: process.env.JWT_EXPIRES_IN || '3600s',
  },
  database: {
    host: process.env.DB_HOST || 'cookbook-hoapri123-95dd.a.aivencloud.com',
    port: parseInt(process.env.DB_PORT, 10) || 16906,
    username: process.env.DB_USERNAME || 'avnadmin',
    password: process.env.DB_PASSWORD || 'AVNS_F5X1dhEyAZLQHfc71si',
    database: process.env.DB_NAME || 'defaultdb',
    ssl: {
     rejectUnauthorized: true,
    },
  },
  mailer: {
    transport: {
    host: process.env.MAIL_HOST || 'smtp.example.com',
    port: 465,
    secure: true, 
    auth: {
      user: process.env.MAIL_USER || 'user@example.com',
      pass: process.env.MAIL_PASS || 'password',
    },
    },
    defaults: {
    from: '"CookBook" <no-reply@example.com>',
    }
  },
  });
  