<script setup lang="ts">

import FormComponent from '@/components/FormTipoComodoComponent.vue'
import Card from 'primevue/card'
import Button from 'primevue/button'
import SplitButton from 'primevue/splitbutton'
import useTipoComodoView from '@/composables/tipoComodoView'

const {
  // pageTitle,
  // cardTitleInternal,
  currentMode,           // <- adicionei aqui
  isViewing,
  currentSituacaoId,
  splitButtonOptions,
  handleFormSubmission,
  handleFormCancellation,
} = useTipoComodoView()
</script>

<template>
  <div class="view-container d-flex flex-column" style="position: relative; bottom: 3rem; min-height: 70vh">

    <Card class="content-card">
      <template #title>
        <!-- <div class="d-flex align-items-center justify-content-end" style="float: right">
          <SplitButton
            v-if="isViewing && currentSituacaoId"
            label="Opções"
            icon="pi pi-cog"
            :model="splitButtonOptions"
            severity="warning"
            class="p-button-lg p-0"
            raised
          />
        </div> -->
        <hr class="my-5" />
      </template>

      <template #content>
        <!-- Passa o currentMode diretamente -->
        <FormComponent
          :mode="currentMode"
          :situacao-id="currentSituacaoId"
          @form-submitted="handleFormSubmission"
          @form-cancelled="handleFormCancellation"
        />

        <div class="actions-footer"></div>
      </template>
    </Card>
  </div>
</template>
 