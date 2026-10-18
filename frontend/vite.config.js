import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    // 开发期用 Vite 代理解决跨域，后端不配 CORS（生产由 Nginx 反代）
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 后端接口路径不带 /api，代理时剥掉，方便将来网关层统一加前缀
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})