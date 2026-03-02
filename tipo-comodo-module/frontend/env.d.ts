/// <reference types="vite/client" />

declare global {
  interface ImportMetaEnv {
    readonly BASE_URL: string
    readonly MODE: string
    readonly DEV: boolean
    readonly PROD: boolean
    readonly SSR: boolean
  }

  interface ImportMeta {
    readonly env: ImportMetaEnv
    readonly url: string
  }
}

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<object, object, unknown>
  export default component
}

export {}
