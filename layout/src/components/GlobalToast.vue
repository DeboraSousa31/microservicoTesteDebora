<!-- eslint-disable vue/valid-template-root -->
<template></template>
<script setup lang="ts">
import { useToast } from 'primevue/usetoast';
const toast = useToast();

interface ToastMessageOptions {
  severity?: 'success' | 'info' | 'warn' | 'error';
  summary?: string; // Título da mensagem.
  detail?: string; // Conteúdo principal da mensagem.
  life?: number; // Tempo de vida da mensagem em milissegundos antes de desaparecer.
  group?: string; // Grupo para gerenciar múltiplos toasts.
  closable?: boolean; // Se o toast pode ser fechado pelo usuário.
}

const showToast = (options: ToastMessageOptions): void => {
  toast.add({
    severity: options.severity || 'info', // Default para 'info' se não especificado
    summary: options.summary || 'Notificação', // Default para 'Notificação'
    detail: options.detail || '',
    life: options.life || 3000,
    group: options.group,
    closable: options.closable
  });
};

const showSuccessToast = (summary: string, detail: string = '', life: number = 3000): void => {
  showToast({ severity: 'success', summary, detail, life });
};

const showInfoToast = (summary: string, detail: string = '', life: number = 3000): void => {
  showToast({ severity: 'info', summary, detail, life });
};

const showWarnToast = (summary: string, detail: string = '', life: number = 3000): void => {
  showToast({ severity: 'warn', summary, detail, life });
};

const showErrorToast = (summary: string, detail: string = '', life: number = 3000): void => {
  showToast({ severity: 'error', summary, detail, life });
};

// Usa `defineExpose` para expor a função `showToast` e suas variações.
// Isso permite que o componente pai (App.vue) obtenha uma referência a estas funções
// através do `ref` e as anexe ao objeto `window`.
defineExpose({
  showToast,
  showSuccessToast,
  showInfoToast,
  showWarnToast,
  showErrorToast
});

</script>

