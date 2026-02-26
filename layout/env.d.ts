/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_SITUACAO_OCUPANTE_URL: string
  readonly VITE_SITUACAO_JURIDICA_URL: string
  readonly VITE_ORGAO_URL: string
  readonly VITE_INTERESSADO_URL: string
  readonly VITE_ATUALIZACAO_URL: string
  readonly VITE_STATUS_URL: string
  readonly VITE_TIPO_COMODO_URL: string
  readonly VITE_TIPO_ORGAO_URL: string
  readonly VITE_ESTADO_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
