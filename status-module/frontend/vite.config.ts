import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { federation } from '@module-federation/vite'

// https://vite.dev/config/
export default defineConfig({
  base: '/status',
  build: {
    target: 'esnext',
  },
  plugins: [
    vue(),
    // vueDevTools(),
    federation({
      name: 'statusApp',
      filename: 'remoteEntry.js',
      library: { type: 'var', name: 'statusApp' },
      exposes: {
        './exposedRoutes': './src/router/exposedRoutes.ts',
      },
      shared: {
        vue: {
          singleton: true,
        },
        'vue-router': {
          singleton: true,
        },
        primevue: {
          singleton: true,
        },
      },
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    cors: true,
    port: 4183,
    strictPort: true,
    allowedHosts: ['cerurb-vue.foxinline.com'],
    proxy: {
      '/api': {
        target: 'http://api-status:8086',
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
