<!-- eslint-disable vue/valid-template-root -->
<template></template>
<script setup lang="ts">
import { useConfirm } from 'primevue/useconfirm'; // Importa o hook para a Composition API

const confirm = useConfirm();

interface ConfirmOptions {
  message: string; // A mensagem principal a ser exibida no diálogo.
  header?: string; // O título do cabeçalho do diálogo.
  icon?: string; // O ícone a ser exibido no cabeçalho (ex: 'pi pi-exclamation-triangle').
  acceptLabel?: string; // O texto do botão de aceitar (ex: 'Sim', 'Confirmar').
  rejectLabel?: string; // O texto do botão de rejeitar (ex: 'Não', 'Cancelar').
  acceptClass?: string; // Classes CSS para o botão de aceitar (ex: 'p-button-danger').
  rejectClass?: string; // Classes CSS para o botão de rejeitar.
  blockScroll?: boolean; // Se o scroll da página deve ser bloqueado quando o diálogo estiver aberto.
  defaultFocus?: 'accept' | 'reject' | 'none'; // Qual botão deve receber foco inicial.
  group?: string; // Grupo para gerenciar múltiplos ConfirmDialogs.
}


const showConfirm = (options: ConfirmOptions): Promise<boolean> => {
  return new Promise((resolve) => {
    // Chama o método `require` do serviço de confirmação do PrimeVue.
    // Ele exibe o diálogo com as opções fornecidas e aguarda a interação do usuário.
    confirm.require({
      ...options, // Espalha todas as opções passadas pelo módulo remoto
      // As funções `accept` e `reject` são manipuladas internamente para resolver a Promise
      accept: () => {
        resolve(true); // Se o usuário clicou em 'aceitar', resolve a Promise com `true`.
      },
      reject: () => {
        resolve(false); // Se o usuário clicou em 'rejeitar' ou fechou, resolve a Promise com `false`.
      }
    });
  });
};

// Usa `defineExpose` para expor a função `showConfirm`.
// Isso permite que o componente pai (App.vue) obtenha uma referência a esta função
// através do `ref` e a anexe ao objeto `window`.
defineExpose({
  showConfirm
});

</script>

