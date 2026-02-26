export {};

declare global {
  interface Window {
    showHostConfirmDialog: (options: {
      message: string
      header?: string
      icon?: string
      acceptLabel?: string
      rejectLabel?: string
      acceptClass?: string
      rejectClass?: string
      blockScroll?: boolean
      defaultFocus?: 'accept' | 'reject' | 'none'
      group?: string
    }) => Promise<boolean>

    showHostToast: (options: {
      severity?: 'success' | 'info' | 'warn' | 'error'
      summary?: string
      detail?: string
      life?: number
      group?: string
      closable?: boolean
    }) => void
    showHostSuccessToast: (summary: string, detail?: string, life?: number) => void
    showHostInfoToast: (summary: string, detail?: string, life?: number) => void
    showHostWarnToast: (summary: string, detail?: string, life?: number) => void
    showHostErrorToast: (summary: string, detail?: string, life?: number) => void
  }
}
