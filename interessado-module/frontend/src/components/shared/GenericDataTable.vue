<template>
    <form @submit.prevent="pesquisar">
        <div class="row">
            <slot name="search-fields" :filters="dadosPesquisa" :limpar-dados="limparDados"></slot>
        </div>

        <div class="row d-flex justify-content-center mb-3 text-center mt-4">
            <div class="col-auto">
                <Button icon="pi pi-search" type="submit" size="small" class="mx-1" label="Pesquisar"></Button>
                <Button icon="pi pi-eraser" type="button" size="small" class="mx-1 px-4" label="Limpar"
                    @click="limparDados"></Button>
            </div>
        </div>

        <div class="meu-container-de-tabela card">
            <DataTable class="w-100" :paginator="true" :rows="lazyParams.rows" :value="listData" stripedRows
                :loading="loading" :rowsPerPageOptions="[5, 10, 25, 50]" removableSort tableStyle="min-width: 10rem"
                
                scrollable scrollHeight="600px"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros" dataKey="id" lazy
                :totalRecords="totalRecords" @page="onPage" @sort="onSort" v-model:first="lazyParams.first">
                <template #header>
                    <div class="meu-cabecalho-personalizado">
                        {{ title }} (<span>{{ totalRecords }}</span>)
                    </div>
                </template>

                <slot name="columns"></slot>

                <template #empty>
                    <div class="text-center p-1">
                        <i class="pi pi-search"
                            style="font-size: 1.5rem; margin-right: 8px; color: var(--text-color-light);"></i>
                        <strong style="display: block; margin-top: 1rem;">Nenhum dado encontrado.</strong>
                        <p>Tente alterar os filtros ou clique em Pesquisar novamente.</p>
                    </div>
                </template>
            </DataTable>
        </div>
    </form>
</template>

<script setup lang="ts">
import '@/assets/main.css';
import { ref } from 'vue';
import Button from 'primevue/button';
import DataTable, { type DataTablePageEvent, type DataTableSortEvent } from 'primevue/datatable';

type DataItem = { id: number;[key: string]: any };
type FetchResponse = { content: DataItem[]; totalElements: number };
type LazyParams = {
    first: number;
    rows: number;
    sortField: string | null;
    sortOrder: 1 | -1 | 0 | null | undefined;
};

// --- Props ---
const props = defineProps<{
    title: string;
    initialFilters: Record<string, any>;
    fetchDataFunction: (lazyParams: LazyParams, filters: Record<string, any>) => Promise<FetchResponse>;
}>();

// --- Estado Interno Genérico ---
const listData = ref<DataItem[]>([]);
const loading = ref<boolean>(false);
const totalRecords = ref<number>(0);
const dadosPesquisa = ref(JSON.parse(JSON.stringify(props.initialFilters))); // Cópia profunda para evitar mutação

const lazyParams = ref<LazyParams>({
    first: 0,
    rows: 5,
    sortField: 'id',
    sortOrder: 1,
});

// --- Carregamento de Dados ---
async function obterDados() {
    loading.value = true;
    try {
        const response = await props.fetchDataFunction(lazyParams.value, dadosPesquisa.value);
        listData.value = response.content;
        totalRecords.value = response.totalElements;
    } catch (error) {
        console.error("Erro ao buscar dados:", error);
        listData.value = [];
        totalRecords.value = 0;
        if (window.showHostToast) {
            window.showHostToast({ severity: 'error', summary: 'Erro', detail: 'Não foi possível realizar a pesquisa. Tente novamente.' });
        }
    } finally {
        loading.value = false;
    }
 
}

async function pesquisar() {
    lazyParams.value.first = 0;
    await obterDados();
}

function limparDados() {
    dadosPesquisa.value = JSON.parse(JSON.stringify(props.initialFilters));
    listData.value = [];
    totalRecords.value = 0;
}

function removerItemDaLista(itemId: number) {
    listData.value = listData.value.filter(item => item.id !== itemId);
    totalRecords.value--;
}

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

defineExpose ({
    removerItemDaLista,
    pesquisar
})
</script>

<style scoped>
.meu-cabecalho-personalizado {
  background-color: var(--primary-color);
  text-align: center;
  color: white;
  padding: 0.5rem;
  font-size: 1.2rem;
  font-weight: 600;
  border-radius: 8px 8px 0 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.p-datatable-header) {
  padding: 0 !important;
}

.meu-container-de-tabela {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
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
