<template>
  <div id="app-shell">
    <router-view />
    <Toast />
    <!-- Adicione :closableMask="true" se quiser fechar ao clicar fora -->
    <ConfirmDialog :closableMask="true" />
  </div>
</template>