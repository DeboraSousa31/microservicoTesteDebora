/* eslint-disable vue/multi-word-component-names */
import { createApp } from 'vue'
import App from './App.vue'
import router, { initializeRouter } from './router'

import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura'
import { definePreset } from '@primeuix/themes'

// --- IMPORTAÇÕES PARA O TOAST GLOBAL ---
import ToastService from 'primevue/toastservice'
import Toast from 'primevue/toast'

// --- IMPORTAÇÕES PARA O DIÁLOGO DE CONFIRMAÇÃO GLOBAL ---
import ConfirmationService from 'primevue/confirmationservice'
import ConfirmDialog from 'primevue/confirmdialog'

import 'primeicons/primeicons.css'

import 'bootstrap/dist/css/bootstrap.min.css';


const blue = {
  50: '#eff6ff',
  100: '#dbeafe',
  200: '#bfdbfe',
  300: '#93c5fd',
  400: '#60a5fa',
  500: '#3b82f6',
  600: '#2563eb',
  700: '#1d4ed8',
  800: '#1e40af',
  900: '#1e3a8a',
  950: '#172554',
}

const slate = {
  50: '#f8fafc',
  100: '#f1f5f9',
  200: '#e2e8f0',
  300: '#cbd5e1',
  400: '#94a3b8',
  500: '#64748b',
  600: '#475569',
  700: '#334155',
  800: '#1e293b',
  900: '#0f172a',
  950: '#020617',
}

const BlueAuraTheme = definePreset(Aura, {
  semantic: {
    primary: blue,
    colorScheme: {
      light: {
        surface: {
          0: '#ffffff',
          50: slate['50'],
          100: slate['100'],
          200: slate['200'],
          300: slate['300'],
          400: slate['400'],
          500: slate['500'],
          600: slate['600'],
          700: slate['700'],
          800: slate['800'],
          900: slate['900'],
          950: slate['950'],
        },
      },
      dark: {
        primary: blue,
        surface: {
          0: slate['950'],
          50: slate['900'],
          100: slate['800'],
          200: slate['700'],
          300: slate['600'],
          400: slate['500'],
          500: slate['400'],
          600: slate['300'],
          700: slate['200'],
          800: slate['100'],
          900: slate['50'],
          950: '#ffffff',
        },
      },
    },
  },
})

// Função assíncrona para inicializar e montar a aplicação Vue.
async function bootstrapApp() {
  await initializeRouter()
  const app = createApp(App)

  app.use(router)

  app.use(PrimeVue, {
    unstyled: false,
    theme: {
      preset: BlueAuraTheme,
      options: {
        darkModeSelector: 'light',
      },
    },
  })

  app.use(ConfirmationService)
  app.component('ConfirmDialog', ConfirmDialog)
  app.use(ToastService)
  app.component('Toast', Toast)

  app.mount('#app')
}

bootstrapApp()
