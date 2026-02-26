import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { federation } from '@module-federation/vite'

// https://vite.dev/config/
export default defineConfig({
  // base: '/',
  build: {
    target: 'esnext',
  },
  plugins: [
    vue(),

    // vueDevTools(),
    federation({
      name: 'layoutApp',
      remotes: {
        situacaoOcupanteApp: {
          name: 'situacaoOcupanteApp',
          entry: `${process.env.VITE_SITUACAO_OCUPANTE_URL}/remoteEntry.js`,
          type: 'module',
        },

        situacaoJuridicaApp: {
          name: 'situacaoJuridicaApp',
          entry: `${process.env.VITE_SITUACAO_JURIDICA_URL}/remoteEntry.js`,
          type: 'module',
        },
        orgaoApp: {
          name: 'orgaoApp',
          entry: `${process.env.VITE_ORGAO_URL}/remoteEntry.js`,
          type: 'module',
        },
        interessadoApp: {
          name: 'interessadoApp',
          entry: `${process.env.VITE_INTERESSADO_URL}/remoteEntry.js`,
          type: 'module',
        },
        atualizacaoApp: {
          name: 'atualizacaoApp',
          entry: `${process.env.VITE_ATUALIZACAO_URL}/remoteEntry.js`,
          type: 'module',
        },
        statusApp: {
          name: 'statusApp',
          entry: `${process.env.VITE_STATUS_URL}/remoteEntry.js`,
          type: 'module',
        },
        tipoComodoApp: {
          name: 'tipoComodoApp',
          entry: `${process.env.VITE_TIPO_COMODO_URL}/remoteEntry.js`,
          type: 'module',
        },
        tipoOrgaoApp: {
          name: 'tipoOrgaoApp',
          entry: `${process.env.VITE_TIPO_ORGAO_URL}/remoteEntry.js`,
          type: 'module',
        },
        estadoApp: {
          name: 'estadoApp',
          entry: `${process.env.VITE_ESTADO_URL}/remoteEntry.js`,
          type: 'module',
        },
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
    port: 5173,
    strictPort: true,
    allowedHosts: ['cerurb-vue.foxinline.com'],
    headers: {
      'Content-Security-Policy': "frame-ancestors *",
    },
    proxy: {
      '/api/situacao-ocupante': {
        target: 'http://api-situacao-ocupante:8081',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/situacao-ocupante/, ''),
      },

      '/api/situacao-juridica': {
        target: 'http://api-situacao-juridica:8082',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/situacao-juridica/, ''),
      },

      '/api/orgao-empresa': {
        target: 'http://api-orgao-empresa:8083',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/orgao-empresa/, ''),
      },
      '/api/interessado': {
        target: 'http://api-interessado:8084',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/interessado/, ''),
      },
      '/api/atualizacao': {
        target: 'http://api-atualizacao:8085',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/atualizacao/, ''),
      },
      '/api/status': {
        target: 'http://api-status:8086',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/status/, ''),
      },
      '/api/tipo-comodo': {
        target: 'http://api-tipo-comodo:8087',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/tipo-comodo/, ''),
      },
      '/api/tipo-orgao': {
        target: 'http://api-tipo-orgao:8088',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/tipo-orgao/, ''),
      },
     '/api/estado': {
        target: 'http://api-estado:8090',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/estado/, ''),
      },

    },
  },
})
