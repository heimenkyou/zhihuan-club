import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path';

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    host: '0.0.0.0',
    port: 3000,
    allowedHosts: [
      '.f.luowb.cn', // 用于内网穿透服务器
    ],
    proxy: {
      '/api': {
        target: 'https://club.luowb.cn/api',
        // target: 'http://localhost:12321',
        // target:'http://localhost:3200',
        // target: ' http://127.0.0.1:4523/m1/6949200-6665662-default',

        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
});
