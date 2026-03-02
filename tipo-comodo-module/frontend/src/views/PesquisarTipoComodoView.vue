<script setup lang="ts">
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import Column from "primevue/column";
import GenericDataTable from "../components/shared/GenericDataTable.vue";
import GenericDropdown from "../components/shared/GenericDropdown.vue";
import { useTipoComodo } from "../composables/tipoComodoPesquisa";

const {
  API_BASE_URL,
  initialFilters,
  genericTableRef,
  fetchTipoComodo,
  removerItem,
  abrirEmNovaGuia,
} = useTipoComodo();
</script>

<template>
  <div class="view-container d-flex flex-column ">

    <!-- Tabela principal -->
    <GenericDataTable
      ref="genericTableRef"
      title="Tipo Cômodo"
      class="content-card"
      :initial-filters="initialFilters"
      :fetch-data-function="fetchTipoComodo"
    >
      <!-- Campos de pesquisa -->
      <template #search-fields="{ filters }">
        <div class="mb-2 col-md-4 pb-4">
          <FloatLabel class="flex flex-col">
            <InputText class="w-100" type="text" v-model="filters.nomeSingular" />
            <label>Nome Singular</label>
          </FloatLabel>
        </div>
        <div class="mb-2 col-md-4 pb-4">
          <FloatLabel class="flex flex-col">
            <InputText class="w-100" type="text" v-model="filters.nomePlural" />
            <label>Nome Plural</label>
          </FloatLabel>
        </div>
        <div class="mb-2 col-md-4">
          <GenericDropdown
            v-model="filters.credenciadaSelecionada"
            :fetch-url="`${API_BASE_URL}/tipo-comodo/credenciadas`"
            label="Credenciada"
            optionLabel="nome"
            :showClear="true"
          />
        </div>
      </template>

      <!-- Colunas -->
      <template #columns>
        <Column header="Ações" style="width: 8rem">
          <template #body="slotProps">
            <div class="div-acoes">
              <Button
                severity="info"
                icon="pi pi-eye"
                title="Visualizar no Monólito"
                @click="abrirEmNovaGuia(slotProps.data.id)"
              />
              <Button
                severity="danger"
                icon="pi pi-trash"
                title="Remover"
                @click="() => removerItem(slotProps.data)"
              />
            </div>
          </template>
        </Column>
        <Column field="nomeSingular" sortable header="Nome Singular" />
        <Column field="nomePlural" sortable header="Nome Plural" />
      </template>
    </GenericDataTable>
  </div>
</template>
