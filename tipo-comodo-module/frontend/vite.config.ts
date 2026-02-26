import { fileURLToPath, URL } from 'node:url'
import { defineConfig, type ViteDevServer, type Plugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import { federation } from '@module-federation/vite'

/**
 * 🔒 Plugin de segurança: bloqueia acesso direto ao microfrontend
 * sem token AES criptografado vindo do monolito.
 */
function authGuardPlugin(): Plugin {
  return {
    name: 'auth-guard-plugin',
    configureServer(server: ViteDevServer) {
      server.middlewares.use((req, res, next) => {
        // Permite recursos internos (arquivos estáticos, API, assets, etc)
        if (
          req.url?.startsWith('/api') ||
          req.url?.startsWith('/assets') ||
          req.url?.endsWith('.js') ||
          req.url?.endsWith('.css') ||
          req.url?.endsWith('.map')
        ) {
          return next()
        }

    
        if (
          req.url === '/' ||
          req.url?.startsWith('/tipo-comodo') ||
          req.url?.startsWith('/index.html')
        ) {
          try {
            const url = new URL(req.url, `http://${req.headers.host}`)
            const tokenParam = url.searchParams.get('token')

            // Verifica se o token AES existe
            if (!tokenParam) {
              res.statusCode = 403
              res.end('Acesso negado: token ausente.')
              return
            }

            const isHex = /^[0-9a-fA-F]+$/.test(tokenParam)
            if (!isHex || tokenParam.length < 32) {
              res.statusCode = 403
              res.end('Token inválido.')
              return
            }

          } catch (e) {
            res.statusCode = 400
            res.end('Requisição inválida.')
            return
          }
        }

        next()
      })
    },
  }
}

// 🧠 Configuração do Vite com segurança + federation
export default defineConfig({
  base: '/tipo-comodo/',
  build: {
    target: 'esnext',
  },
  plugins: [
    vue(),
    federation({
      name: 'tipoComodoApp',
      filename: 'remoteEntry.js',
      library: { type: 'var', name: 'tipoComodoApp' },
      exposes: {
        './exposedRoutes': './src/router/exposedRoutes.ts',
      },
      shared: {
        vue: { singleton: true },
        'vue-router': { singleton: true },
        primevue: { singleton: true },
      },
    }),
    authGuardPlugin(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
    server: {
    cors: true,
    port: 4187,
    strictPort: true,
    allowedHosts: ['cerurb-vue.foxinline.com'],
    proxy: {
      "/api": {
        target: "http://api-tipo-orgao:8088",
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
})
