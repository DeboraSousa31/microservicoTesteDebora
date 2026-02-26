/* eslint-disable vue/multi-word-component-names */
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Estilos de ícones (FontAwesome, Bootstrap Icons, PrimeIcons)
import '@fortawesome/fontawesome-free/css/all.min.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'primeicons/primeicons.css'

// css global
import 'bootstrap/dist/css/bootstrap.min.css'
import './assets/base.css'
import './assets/main.css'

// --- JAVASCRIPT DO BOOTSTRAP ---
// Importe o Popper.js e depois o Bootstrap JS
import '@popperjs/core' // Adicione esta linha
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

// primevue
import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura'
import { definePreset } from '@primeuix/themes'
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';
import 'primeicons/primeicons.css'

// --- IMPORTAÇÕES PARA O DIÁLOGO DE CONFIRMAÇÃO ---
import ConfirmationService from 'primevue/confirmationservice';
import ConfirmDialog from 'primevue/confirmdialog';

// --- Axios e token (compatível com Cerurb) ---
import axios from 'axios';
import { encrypt, decrypt } from '@/composables/encrypt';

const urlParams = new URLSearchParams(window.location.search);
const tokenFromUrl = urlParams.get('token');
if (tokenFromUrl) {
  const tokenCriptografado = encrypt(tokenFromUrl);
  localStorage.setItem('jwtToken', tokenCriptografado);
}
const tokenCriptografado = localStorage.getItem('jwtToken');
if (tokenCriptografado) {
  try {
    decrypt(tokenCriptografado);
  } catch {
    // ignora erro de descriptografia
  }
}

axios.interceptors.request.use((config) => {
  const stored = localStorage.getItem('jwtToken');
  if (stored) {
    try {
      const tokenOriginal = decrypt(stored);
      config.headers.Authorization = `Bearer ${tokenOriginal}`;
    } catch {
      // ignora
    }
  }
  return config;
});

/* Paleta Cerurb Pro - azul principal #3b7ddd */
const cerurbBlue = {
  50: '#f0f7ff',
  100: '#dceeff',
  200: '#b8d9fa',
  300: '#8bc2f5',
  400: '#5aa3ef',
  500: '#3b7ddd',
  600: '#2d6ac7',
  700: '#1e4e9e',
  800: '#153a78',
  900: '#0f2954',
  950: '#0a1a35',
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

// --- CRIAÇÃO DO TEMA CUSTOMIZADO (Cerurb) ---
const CerurbAuraTheme = definePreset(Aura, {
  semantic: {
    primary: cerurbBlue,
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
        primary: cerurbBlue,
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

const app = createApp(App)

app.use(router)

app.use(PrimeVue, {
  theme: {
    preset: CerurbAuraTheme,
    options: {
      darkModeSelector: 'light',
    },
  },
})

app.use(ConfirmationService);
app.component('ConfirmDialog', ConfirmDialog);

app.use(ToastService);
app.component('Toast', Toast);

app.mount('#app')
