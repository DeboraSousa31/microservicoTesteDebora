<template>
  <div class="page-container">
    <h1>Página Inicial</h1>
    <p>Bem-vindo ao esqueleto do projeto CERURB PRO!</p>
    <Card class="mt-4">
      <template #title>Conteúdo de Exemplo</template>
      <template #content>
        <p>Este é o conteúdo principal da página inicial.</p>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import Card from 'primevue/card';
</script>

<style scoped>
.page-container {
  padding: 1rem;
}
</style>
