/* eslint-disable vue/multi-word-component-names */
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

// Estilos
import './assets/tipoComodo.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'primeicons/primeicons.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './assets/base.css';
import './assets/main.css';

// JS Bootstrap
import '@popperjs/core';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

// PrimeVue
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';
import { definePreset } from '@primeuix/themes';
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';
import ConfirmationService from 'primevue/confirmationservice';
import ConfirmDialog from 'primevue/confirmdialog';

// Axios
import axios from 'axios';

// --- 🔐 Importa a criptografia AES ---
import { encrypt, decrypt } from './composables/encrypt';

// --- Captura token da URL ---
const urlParams = new URLSearchParams(window.location.search);
const tokenFromUrl = urlParams.get('token');

// Se vier da URL → criptografa e salva
if (tokenFromUrl) {
  const tokenCriptografado = encrypt(tokenFromUrl);
  localStorage.setItem('jwtToken', tokenCriptografado);
  console.log('🔒 Token criptografado salvo no localStorage.');
}

// --- Recupera token do localStorage ---
const tokenCriptografado = localStorage.getItem('jwtToken');

if (tokenCriptografado) {
  try {
    const tokenOriginal = decrypt(tokenCriptografado);
    console.log('✅ Token descriptografado com sucesso:', tokenOriginal);
  } catch (e) {
    console.error('❌ Erro ao descriptografar o token:', e);
  }
} else {
  console.warn('⚠️ Nenhum token encontrado no localStorage.');
}

// --- Axios interceptor para adicionar token descriptografado ---
axios.interceptors.request.use((config) => {
  const tokenCriptografado = localStorage.getItem('jwtToken');
  if (tokenCriptografado) {
    try {
      const tokenOriginal = decrypt(tokenCriptografado);
      config.headers.Authorization = `Bearer ${tokenOriginal}`;
      console.log('🌐 Axios Header Authorization definido com token descriptografado.');
    } catch (e) {
      console.error('❌ Erro ao descriptografar token para o Axios:', e);
    }
  }
  return config;
});

// --- Criação do app ---
const app = createApp(App);
app.use(router);

// --- Configuração do tema PrimeVue (Cerurb) ---
const cerurbBlue = {
  50: '#f0f7ff', 100: '#dceeff', 200: '#b8d9fa', 300: '#8bc2f5',
  400: '#5aa3ef', 500: '#3b7ddd', 600: '#2d6ac7', 700: '#1e4e9e',
  800: '#153a78', 900: '#0f2954', 950: '#0a1a35',
};
const slate = {
  50: '#f8fafc', 100: '#f1f5f9', 200: '#e2e8f0', 300: '#cbd5e1',
  400: '#94a3b8', 500: '#64748b', 600: '#475569', 700: '#334155',
  800: '#1e293b', 900: '#0f172a', 950: '#020617',
};
const CerurbAuraTheme = definePreset(Aura, {
  semantic: {
    primary: cerurbBlue,
    colorScheme: {
      light: { surface: { 0: '#fff', 50: slate[50], 100: slate[100], 200: slate[200], 300: slate[300], 400: slate[400], 500: slate[500], 600: slate[600], 700: slate[700], 800: slate[800], 900: slate[900], 950: slate[950] } },
      dark: { primary: cerurbBlue, surface: { 0: slate[950], 50: slate[900], 100: slate[800], 200: slate[700], 300: slate[600], 400: slate[500], 500: slate[400], 600: slate[300], 700: slate[200], 800: slate[100], 900: slate[50], 950: '#fff' } },
    },
  },
});

// --- PrimeVue ---
app.use(PrimeVue, { theme: { preset: CerurbAuraTheme, options: { darkModeSelector: 'light' } } });
app.use(ConfirmationService);
app.component('ConfirmDialog', ConfirmDialog);
app.use(ToastService);
app.component('Toast', Toast);

// --- Funções globais para composables ---
// Confirm Dialog
window.showHostConfirmDialog = (options: any) => {
  return new Promise<boolean>((resolve) => {
    app.config.globalProperties.$confirm?.require({
      ...options,
      accept: () => resolve(true),
      reject: () => resolve(false),
    });
  });
};

// Toast
window.showHostToast = (options: any) => {
  app.config.globalProperties.$toast?.add(options);
};

// --- Montagem final ---
app.mount('#app');
