import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { federation } from "@module-federation/vite";

// https://vite.dev/config/
export default defineConfig({
  base: "/atualizacao",
  build: {
    target: "esnext",
  },
  plugins: [
    vue(),
    // vueDevTools(),
    federation({
      name: "atualizacaoApp",
      filename: "remoteEntry.js",
      library: { type: "var", name: "atualizacaoApp" },
      exposes: {
        "./exposedRoutes": "./src/router/exposedRoutes.ts",
      },
      shared: {
        vue: {
          singleton: true,
        },
        "vue-router": {
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
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    cors:true,
    port: 4181,
    strictPort: true,
    allowedHosts: ['cerurb-vue.foxinline.com'],
    proxy: {
      "/api": {
        target: "http://api-atualizacao:8085",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
