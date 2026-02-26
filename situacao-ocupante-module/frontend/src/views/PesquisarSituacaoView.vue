<template>
  <div class="view-container d-flex flex-column">
    <div class="header cerurb-header">
      <h3 class="header-title">
        <i class="pi pi-search"></i> Pesquisar Situação do Ocupante
      </h3>
    </div>
    <Card class="content-card">
      <template #title>
        <router-link :to="{ name: 'CadastrarSituacaoProprietario' }" custom v-slot="{ navigate }">
          <Button type="button" class="info-button" icon="pi pi-plus" severity="info" label="Cadastrar" raised
            @click="navigate" />
        </router-link>
        <div>Pesquisar Situações Ocupantes</div>
        <hr class="mb-5" />
      </template>
      <template #content>
        <GenericDataTable ref="genericTableRef" title="Situação do ocupante" :initial-filters="initialFilters"
          :fetch-data-function="fetchSituacaoOcupante">
          <template #search-fields="{ filters }">
            <div class="mb-2 col-md-4 pb-4">
              <FloatLabel class="flex flex-col">
                <InputText class="w-100" id="nome" type="text" v-model="filters.nome" />
                <label for="nome">Nome</label>
              </FloatLabel>
            </div>
            <div class="mb-2 col-md-4 pb-4">
              <FloatLabel class="flex flex-col">
                <InputText class="w-100" id="descricao" type="text" v-model="filters.descricao" />
                <label for="descricao">Descrição</label>
              </FloatLabel>
            </div>
            <div class="mb-2 col-md-4">
              <GenericDropdown v-model="filters.credenciadaSelecionada"
                :fetch-url="`${API_BASE_URL}/situacao-ocupante/credenciadas`" input-id="credenciadaPesquisa"
                label="Credenciada" optionLabel="nome" :showClear="true" />
            </div>
          </template>
          <template #columns>
            <Column header="Ações" style="width: 8rem;">
              <template #body="slotProps">
                <div class="div-acoes">
                  <router-link :to="{ name: 'VisualizarSituacaoProprietario', params: { id: slotProps.data.id } }"
                    custom v-slot="{ href }">
                    <Button severity="info" icon="pi pi-eye" title="Visualizar" @click="abrirEmNovaGuia(href)"></Button>
                  </router-link>
                  <Button severity="danger" icon="pi pi-trash" title="Remover"
                    @click="removerItem(slotProps.data)"></Button>
                </div>
              </template>
            </Column>
            <Column field="nome" sortable header="Nome"></Column>
            <Column field="descricao" sortable header="Descrição"></Column>
          </template>
        </GenericDataTable>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';

// --- Componentes usados nos slots --
import Card from 'primevue/card';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
import Column from 'primevue/column';
import GenericDataTable from '../components/shared/GenericDataTable.vue';
import GenericDropdown from '../components/shared/GenericDropdown.vue';

type SituacaoOcupante = { id: number; nome: string; descricao: string; };
type Credenciada = { id: number; nome: string; };

// const API_BASE_URL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL;
const API_BASE_URL = `/api/situacao-ocupante`;


const initialFilters = ref({
  nome: '',
  descricao: '',
  credenciadaSelecionada: null as Credenciada | null,
});

const genericTableRef = ref<InstanceType<typeof GenericDataTable> | null>(null);

// eslint-disable-next-line @typescript-eslint/no-explicit-any
async function fetchSituacaoOcupante(lazyParams: any, filters: any) {
  try {
    const page = lazyParams.first / lazyParams.rows;
    const params: Record<string, unknown> = {
      nome: filters.nome,
      descricao: filters.descricao,
      idCredenciadaSituacaoProprietario: filters.credenciadaSelecionada?.id,
      page: page,
      size: lazyParams.rows,
    };

    if (lazyParams.sortField && (lazyParams.sortOrder === 1 || lazyParams.sortOrder === -1)) {
      params.sort = `${lazyParams.sortField},${lazyParams.sortOrder === 1 ? 'asc' : 'desc'}`;
    }
    console.log(API_BASE_URL);
    const response = await axios.get<{ content: SituacaoOcupante[], totalElements: number }>(
      `${API_BASE_URL}/situacao-ocupante/pesquisar/`, { params }
    );
    
    if (response.status >= 200 && response.status < 300) {
      return response.data;
    } else {
      const errorMessage = 'Não foi possível realizar a pesquisa (status inesperado). Tente novamente.';
      if (window.showHostToast) {
        window.showHostToast({ severity: 'error', summary: 'Erro', detail: errorMessage });
      }
      throw new Error(errorMessage);
    }
  } catch (error) {
    const errorMessage = 'Não foi possível realizar a pesquisa. Tente novamente.';
    if (window.showHostToast) {
      window.showHostToast({ severity: 'error', summary: 'Erro', detail: errorMessage });
    }
    console.error("Problema ao buscar dados da api: " + error);
    throw error;
  }
}

async function removerItem(item: SituacaoOcupante) {
  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja remover "${item.nome}"?`,
        header: 'Confirmação de Exclusão',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sim, excluir',
        rejectLabel: 'Cancelar',
        acceptClass: 'p-button-danger',
      });

      if (confirmed) {
        const response = await axios.delete(`${API_BASE_URL}/situacao-ocupante?id=${item.id}&idUsuario=32&idCredenciadaUsuario=49393`);

        if (response.status >= 200 && response.status < 300) {
          genericTableRef.value?.removerItemDaLista(item.id);
          if (window.showHostToast) {
            window.showHostToast({ severity: 'success', summary: 'Sucesso', detail: 'Situação Ocupante removido com sucesso!' });
          }

        } else {
          console.warn('Requisição retornou com status não esperado:', response.status);
          if (window.showHostToast) {
            window.showHostToast({ severity: 'error', summary: 'Erro', detail: 'Erro ao tentar remover Situação Ocupante.' });
          }
        }

      } else {
        console.log('Exclusão cancelada pelo usuário.');
        if (window.showHostToast) {
          window.showHostToast({ severity: 'info', summary: 'Info', detail: 'Exclusão cancelada pelo usuário.' });
        }
      }
    } catch (error) {
      console.error('Erro ao chamar o ConfirmDialog do Host:', error);
      if (window.showHostToast) {
        window.showHostToast({ severity: 'error', summary: 'Erro Crítico', detail: 'Não foi possível exibir o diálogo de confirmação.' });
      }
    }
  } else {
    console.warn('`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente ou a função não foi exposta.');
  }
}

function abrirEmNovaGuia(url: string) {
  window.open(url, '_blank');
}
</script>

<style scoped>
.content-card {
  width: 100%;
}

.view-container {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  width: 100%;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  padding: 20px;
}

.p-card-title button {
  float: right;
}

.header-title i {
  font-size: 1.3rem;
}

.content-card .p-card-title {
  text-align: center;
}

.header {
  width: 100%;
  float: left;
  color: white;
}

.info-button {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.info-button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}

.div-acoes {
  display: flex;
  justify-content: space-evenly;
}

Adicionei para as ações .content-card {
  width: 100%;
}

.view-container {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
  width: 100%;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  padding: 20px;
}

.p-card-title button {
  float: right;
}

.header-title i {
  font-size: 1.3rem;
}

.content-card .p-card-title {
  text-align: center;
}

.actions-footer {
  margin-top: 1.5rem;
  text-align: center;
}

.header {
  width: 100%;
  float: left;
  color: white;
}

.info-button {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.info-button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}
</style>
