<template>
  <div id="app-shell">
    <router-view />
    <CerurbLoadingOverlay v-model="globalLoading" message="Carregando" />
    <Toast />
    <ConfirmDialog />
  </div>
</template>

<script setup lang="ts">
import { ref, provide } from 'vue'
import CerurbLoadingOverlay from '@/components/shared/CerurbLoadingOverlay.vue'
import { getCerurbLoadingKey } from '@/composables/useCerurbLoading'

const globalLoading = ref(false)
provide(getCerurbLoadingKey(), globalLoading)
</script>
