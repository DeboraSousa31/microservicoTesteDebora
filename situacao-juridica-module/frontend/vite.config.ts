import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { federation } from '@module-federation/vite'

// https://vite.dev/config/
export default defineConfig({
  base: '/situacao-juridica',
  build: {
    target: 'esnext',
  },
  plugins: [
    vue(),
    // vueDevTools(),
    federation({
      name: 'situacaoJuridicaApp',
      filename: 'remoteEntry.js',
      library: { type: 'var', name: 'situacaoJuridicaApp' },
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
    port: 4175,
    strictPort: true,
    allowedHosts: ['cerurb-vue.foxinline.com'],
    proxy: {
      '/api': {
        target: 'http://api-situacao-juridica:8082',
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
