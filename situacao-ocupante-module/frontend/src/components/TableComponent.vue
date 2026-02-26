<template>
  <form @submit.prevent="pesquisar">
    <div class="row">
      <div class="mb-2 col-md-4 pb-4">
        <FloatLabel class="flex flex-col">
          <InputText class="w-100" id="nome" type="text" v-model="dadosPesquisa.nome" />
          <label for="nome">Nome</label>
        </FloatLabel>
      </div>

      <div class="mb-2 col-md-4 pb-4">
        <FloatLabel class="flex flex-col">
          <InputText class="w-100" id="descricao" type="text" v-model="dadosPesquisa.descricao" />
          <label for="descricao">Descrição</label>
        </FloatLabel>
      </div>

      <div class="mb-2 col-md-4">
        <CredenciadaDropdown v-model="dadosPesquisa.credenciadaSelecionada" :input-id="'credenciadaPesquisa'"
          :label="'Credenciada'" :container-class="'flex flex-col'" optionLabel="nome" :showClear="true" />

      </div>

    </div>

    <div class="row d-flex justify-content-center mb-3 text-center mt-4">
      <div class="col">
        <Button icon="pi pi-search" type="submit" class="mx-1" label="Pesquisar"></Button>
        <Button icon="pi pi-eraser" type="button" class="mx-1 px-4" label="Limpar" @click="limparDados"></Button>
      </div>
    </div>

    <div class="meu-container-de-tabela card">
      <DataTable class="w-100" :paginator="true" :rows="lazyParams.rows" :value="listSituacaoOcupante" stripedRows
        :loading="loading" :rowsPerPageOptions="[5, 10, 25, 50]" removableSort tableStyle="min-width: 10rem"
        editMode="row" scrollable scrollHeight="600px"
        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
        currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros" dataKey="id" lazy
        :totalRecords="totalRecords" @page="onPage" @sort="onSort" v-model:first="lazyParams.first">
        <template #header>
          <div class="meu-cabecalho-personalizado">
            Situação do ocupante (<span>{{ totalRecords }}</span>)
          </div>
        </template>
        <Column header="Ações" style="width: 8rem;">
          <template #body="slotProps">
            <div class="div-acoes">

              <router-link :to="{ name: 'VisualizarSituacaoProprietario', params: { id: slotProps.data.id } }" custom
                v-slot="{ href }">
                <Button severity="info" icon="pi pi-eye" title="Visualizar Situação do Ocupante"
                  @click="abrirEmNovaGuia(href)"></Button>
              </router-link>

              <Button severity="danger" icon="pi pi-trash" title="Remover Situação do Ocupante"
                @click="removerItem(slotProps.data)"></Button>
            </div>
          </template>
        </Column>

        <Column field="nome" sortable header="Nome"></Column>

        <Column field="descricao" sortable header="Descrição"></Column>

        <template #empty>
          <div class="text-center p-1">
            <i class="pi pi-search" style="font-size: 1.5rem; margin-right: 8px; color: var(--text-color-light);"></i>
            <strong style="display: block; margin-top: 1rem;">Nenhum dado encontrado.</strong>
            <p>Tente alterar os filtros ou clique em Pesquisar novamente.</p>
          </div>
        </template>
      </DataTable>

    </div>
  </form>

</template>

<script setup lang="ts">
import 'bootstrap/dist/css/bootstrap.min.css';
import '@/assets/base.css';
import '@/assets/main.css';

import Button from 'primevue/button';
import DataTable, {
  type DataTablePageEvent,
  type DataTableSortEvent
} from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import FloatLabel from 'primevue/floatlabel';
// import { useAppToast } from '@/composables/useAppToast';
// import { useConfirm } from "primevue/useconfirm";
import Column from 'primevue/column';
import CredenciadaDropdown from './shared/CredenciadaDropDown.vue';
import { ref, watch, nextTick, computed } from 'vue';
import axios from 'axios';

const API_BASE_URL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL;
// const appToast = useAppToast();
// const confirm = useConfirm();

type Credenciada = {
  id: number;
  nome: string;
}

type SituacaoOcupante = {
  id: number;
  nome: string;
  descricao: string;
  idCredenciadaUsuario: number;
  idUsuario: number;
}

type PesquisaSituacaoOcupante = {
  nome: string;
  descricao: string;
  credenciadaSelecionada: Credenciada | null;
}

type LazyParams = {
  first: number;
  rows: number;
  sortField: string | null;
  sortOrder: 1 | -1 | 0 | null | undefined;
}

const listSituacaoOcupante = ref<SituacaoOcupante[]>([])
const loading = ref<boolean>(false);

const dadosPesquisa = ref<PesquisaSituacaoOcupante>({
  nome: '',
  descricao: '',
  credenciadaSelecionada: null,
});

let debounceTimer: number | undefined;
const DEBOUNCE_DELAY = 500;
const RastreamentoDebounceAtivo = ref(true);

const camposDeTextoParaPesquisa = computed(() => ({
  nome: dadosPesquisa.value.nome,
  descricao: dadosPesquisa.value.descricao,
}));

watch(camposDeTextoParaPesquisa, () => {
  if (!RastreamentoDebounceAtivo.value) {
    return;
  }

  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    pesquisar();
  }, DEBOUNCE_DELAY);
});

watch(() => dadosPesquisa.value.credenciadaSelecionada,
  (novaCredenciada, credenciadaAnterior) => {
    if (!RastreamentoDebounceAtivo.value) {
      return;
    }

    const novaTemId = novaCredenciada && novaCredenciada.id != null;
    const anteriorTinhaId = credenciadaAnterior && credenciadaAnterior.id != null;

    if (novaTemId) {
      if (!anteriorTinhaId || (anteriorTinhaId && credenciadaAnterior.id !== novaCredenciada.id)) {
        pesquisar();
      }
    }
    else if (!novaTemId && anteriorTinhaId) {
      pesquisar();
    }

  }, { deep: true });

const totalRecords = ref<number>(0);
const lazyParams = ref<LazyParams>({
  first: 0,
  rows: 5,
  sortField: 'nome',
  sortOrder: 1
});

// INICIO --- Funções para LazyLoading da DataTable ---
function onPage(event: DataTablePageEvent) {
  lazyParams.value.first = event.first;
  lazyParams.value.rows = event.rows;
  obterDados();
}

function onSort(event: DataTableSortEvent) {
  lazyParams.value.first = 0;
  if (event.sortField) {
    lazyParams.value.sortField = event.sortField as string;
  }
  lazyParams.value.sortOrder = event.sortOrder;
  obterDados();
}
// FIM --- Funções para LazyLoading da DataTable ---

async function obterDados() {
  loading.value = true;

  try {
    const page = lazyParams.value.first / lazyParams.value.rows

    const params: Record<string, unknown> = {
      nome: dadosPesquisa.value.nome,
      descricao: dadosPesquisa.value.descricao,
      idCredenciadaSituacaoProprietario: dadosPesquisa.value.credenciadaSelecionada?.id,
      page: page,
      size: lazyParams.value.rows,
    };

    if (lazyParams.value.sortField && (lazyParams.value.sortOrder === 1 || lazyParams.value.sortOrder === -1)) {
      params.sort = `${lazyParams.value.sortField},${lazyParams.value.sortOrder === 1 ? 'asc' : 'desc'}`;
    }

    const response = await axios.get<{ content: SituacaoOcupante[], totalElements: number }>(`${API_BASE_URL}/situacao-ocupante/pesquisar/`, { params })

    console.log('response: ' + response.status)

    console.log(`${API_BASE_URL}/situacao-ocupante/pesquisar/`)

    if (response.status >= 200 && response.status < 300) {
      listSituacaoOcupante.value = response.data.content
      totalRecords.value = response.data.totalElements;
    } else {
      // appToast.showError('Não foi possível realizar a pesquisa. Tente novamente.')
      if (window.showHostToast) {
        window.showHostToast({ severity: 'error', summary: 'Erro', detail: 'Não foi possível realizar a pesquisa. Tente novamente.' });
      }
    }

  } catch (error) {
    // appToast.showError('Não foi possível realizar a pesquisa. Tente novamente.')
    if (window.showHostToast) {
      window.showHostToast({ severity: 'error', summary: 'Erro', detail: 'Não foi possível realizar a pesquisa. Tente novamente.' });
    }
    console.error("Problema ao buscar dados da api: " + error)
  } finally {
    loading.value = false;
  }
}

// INICIO --- Funcoes de Ação ---
async function pesquisar() {
  lazyParams.value.first = 0;
  await obterDados();
}

async function limparDados() {
  RastreamentoDebounceAtivo.value = false;
  dadosPesquisa.value = {
    nome: '',
    descricao: '',
    credenciadaSelecionada: null
  }
  listSituacaoOcupante.value = [];
  totalRecords.value = 0;

  await nextTick();
  RastreamentoDebounceAtivo.value = true;
}

async function removerItem(item: SituacaoOcupante) {
  // confirm.require({
  //   message: `Tem certeza que deseja remover "${item.nome}"?`,
  //   header: 'Confirmação de Exclusão',
  //   icon: 'pi pi-exclamation-triangle',
  //   acceptLabel: 'Sim, excluir',
  //   rejectLabel: 'Cancelar',
  //   acceptClass: 'p-button-danger',
  //   accept: async () => {
  //     try {
  //       const response = await axios.delete(`http://localhost:8081/situacao-ocupante?id=${item.id}&idUsuario=32&idCredenciadaUsuario=49393`);

  //       if (response.status >= 200 && response.status < 300) {
  //         listSituacaoOcupante.value = listSituacaoOcupante.value.filter(i => i.id !== item.id)
  // totalRecords.value = listSituacaoOcupante.value.length
  //         // appToast.showSuccess('Situação Ocupante removido com sucesso!')
  //       } else {
  //         // appToast.showError('Erro ao tentar remover Situação Ocupante.')
  //         console.warn('Requisição retornou com status não esperado:', response.status);
  //       }

  //     } catch (error) {
  //       // appToast.showError('Erro ao tentar remover Situação Ocupante.')

  //       console.error('Erro ao remover item da API:', error);
  //     }
  //   },
  //   reject: () => {
  //     // appToast.showInfo('Exclusão cancelada pelo usuário.')
  //   }
  // })




  // Primeiro, verifique se a função `showHostConfirmDialog` está disponível no objeto `window`.
  // Isso é importante porque o host pode não ter carregado completamente ou a função pode não ter sido exposta.
  if (window.showHostConfirmDialog) {
    try {
      // Chama a função global do host para exibir o ConfirmDialog.
      // Usamos `await` porque a função retorna uma Promise que será resolvida quando o usuário interagir com o diálogo.
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja remover "${item.nome}"?`, // Mensagem dinâmica
        header: 'Confirmação de Exclusão', // Título do diálogo
        icon: 'pi pi-exclamation-triangle', // Ícone (usando PrimeIcons)
        acceptLabel: 'Sim, excluir', // Texto do botão de aceitar
        rejectLabel: 'Cancelar', // Texto do botão de rejeitar
        acceptClass: 'p-button-danger', // Classe CSS para estilizar o botão de aceitar (deixa-o vermelho)
        // Você pode adicionar outras opções do PrimeVue ConfirmDialog aqui, como `blockScroll`, `defaultFocus`, etc.
      });

      // Verifica o resultado da interação do usuário.
      if (confirmed) {
        const response = await axios.delete(`${API_BASE_URL}/situacao-ocupante?id=${item.id}&idUsuario=32&idCredenciadaUsuario=49393`);

        console.log("RESPONSEEE :   " + response.data)

        if (response.status >= 200 && response.status < 300) {
          listSituacaoOcupante.value = listSituacaoOcupante.value.filter(i => i.id !== item.id);
          totalRecords.value = listSituacaoOcupante.value.length;
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
      // Captura qualquer erro que possa ocorrer ao tentar exibir o ConfirmDialog do host.
      console.error('Erro ao chamar o ConfirmDialog do Host:', error);
      // Aqui você pode adicionar uma lógica de fallback ou exibir uma notificação de erro.
      if (window.showHostToast) {
        window.showHostToast({ severity: 'error', summary: 'Erro Crítico', detail: 'Não foi possível exibir o diálogo de confirmação.' });
      }
    }
  } else {
    // Caso a função global não esteja disponível (ex: host não carregado ou erro na exposição).
    console.warn('`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente ou a função não foi exposta.');
    // Considere uma lógica de fallback aqui, como um alert simples ou um modal local para o remote.
  }
}


function abrirEmNovaGuia(url: string) {
  window.open(url, '_blank');
}

// FIM --- Funcoes de Ação ---

</script>

<style scoped>
.meu-cabecalho-personalizado {
  background-color: var(--primary-color);
  text-align: center;
  color: white;
  padding: 0.3rem;
  font-size: 1.2rem;
  font-weight: 600;
  border-radius: 6px 6px 0 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.p-datatable-header) {
  padding: 0px !important;
}

.meu-container-de-tabela {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.div-acoes {
  display: flex;
  justify-content: space-evenly;
}

.row button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}

:deep(p-dialog-footer) {
  display: flex !important;
  flex-direction: row-reverse !important;
  justify-content: center !important;
}
</style>
