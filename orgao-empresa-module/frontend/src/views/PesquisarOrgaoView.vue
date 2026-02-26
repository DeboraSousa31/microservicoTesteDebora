<template>
  <div class="view-container d-flex flex-column">
    <div class="header cerurb-header">
      <h3 class="header-title">
        <i class="pi pi-search"></i> Pesquisar Órgão/Empresa
      </h3>
    </div>
    <Card class="content-card">
      <template #title>
        <router-link
          :to="{ name: 'CadastrarOrgao' }"
          custom
          v-slot="{ navigate }"
        >
          <Button
            type="button"
            class="info-button"
            icon="pi pi-plus"
            severity="info"
            label="Cadastrar"
            raised
            @click="navigate"
          />
        </router-link>
        <div>Pesquisar Órgãos/Empresas</div>
        <hr class="mb-5" />
      </template>
      <template #content>
        <GenericDataTable
          ref="genericTableRef"
          title="Órgãos/Empresas"
          :initial-filters="initialFilters"
          :fetch-data-function="fetchOrgao"
        >
          <template #search-fields="{ filters }">
            <div class="mb-4 col-md-4">
              <FloatLabel>
                <InputText
                  class="w-100"
                  id="nome"
                  type="text"
                  v-model="filters.nome"
                />
                <label for="nome">Nome</label>
              </FloatLabel>
            </div>

            <div class="mb-4 col-md-4">
              <FloatLabel>
                <AutoComplete
                  id="cidade"
                  class="w-100"
                  v-model="filters.cidade"
                  :suggestions="cidadeSuggestions"
                  @complete="searchCidades"
                  field="nome"
                  optionLabel="nome"
                  forceSelection
                  dropdown
                />
                <label for="cidade">Cidade</label>
              </FloatLabel>
            </div>
            <div class="mb-4 col-md-4">
              <GenericDropdown
                v-model="filters.tipoOrgao"
                :fetch-url="`${API_BASE_URL}/orgao/tipoOrgao`"
                input-id="credenciadaPesquisa"
                label="Tipo Órgão"
                :showClear="true"
              />
            </div>

          

            <div
              class="mb-4 col-md-4"
              v-if="filters.tipoOrgao?.nome === 'Empresa'"
            >
              <FloatLabel>
                <InputText
                  id="nomeResponsavel"
                  class="w-100"
                  v-model="filters.nomeResponsavel"
                />
                <label for="nomeResponsavel">Nome do Responsável</label>
              </FloatLabel>
            </div>

            <template v-if="filters.tipoOrgao?.nome.trim() === 'Cartório'">
              <div class="mb-4 col-md-3">
                <FloatLabel>
                  <InputText
                    id="nomeTabeliao"
                    class="w-100"
                    v-model="filters.nomeTabeliao"
                  />
                  <label for="nomeTabeliao">Nome do Tabelião</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-3">
                <FloatLabel>
                  <InputText
                    id="codigoIBGEServentia"
                    class="w-100"
                    v-model="filters.codigoIBGEServentia"
                  />
                  <label for="codigoIBGEServentia">Código IBGE Serventia</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-3">
                <FloatLabel>
                  <InputText
                    id="codigoServentia"
                    class="w-100"
                    v-model="filters.codigoServentia"
                  />
                  <label for="codigoServentia">Código Serventia</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-3">
                <FloatLabel>
                  <InputText
                    id="oficioServentia"
                    class="w-100"
                    v-model="filters.oficioServentia"
                  />
                  <label for="oficioServentia">Ofício Serventia</label>
                </FloatLabel>
              </div>
            </template>

            <div class="mb-4 col-md-12">
              <GenericDropdown
                v-model="filters.credenciadaSelecionada"
                :fetch-url="`${API_BASE_URL}/orgao/credenciadas`"
                input-id="credenciadaPesquisa"
                label="Credenciada"
                :showClear="true"
              />
            </div>
          </template>

          <template #columns>
            <Column header="Ações" style="width: 8rem; text-align: center">
              <template #body="slotProps">
                <div class="div-acoes">
                  <router-link
                    :to="{
                      name: 'VisualizarOrgao',
                      params: { id: slotProps.data.id },
                    }"
                    custom
                    v-slot="{ href }"
                  >
                    <Button
                      severity="info"
                      icon="pi pi-eye"
                      title="Visualizar"
                      @click="abrirEmNovaGuia(href)"
                    ></Button>
                  </router-link>
                  <Button
                    severity="danger"
                    icon="pi pi-trash"
                    title="Remover"
                    @click="removerItem(slotProps.data)"
                  ></Button>
                </div>
              </template>
            </Column>
            <Column field="nome" sortable header="Nome"></Column>
            <Column field="endereco.bairro.nomeCidade" sortable header="Cidade"></Column>
            <Column
              field="nomeTipoOrgao"
              sortable
              header="Tipo de Órgão"
            ></Column>
          </template>
        </GenericDataTable>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";

// --- Componentes ---
import Card from "primevue/card";
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import Column from "primevue/column";
import AutoComplete from "primevue/autocomplete";
import GenericDataTable from "../components/shared/GenericDataTable.vue";
import GenericDropdown from "../components/shared/GenericDropdown.vue";

// --- Tipos ---
interface Cidade {
  id: number;
  nome: string;
}
interface TipoOrgao {
  id: number;
  nome: string;
}
interface Credenciada {
  id: number;
  nome: string;
}
interface Orgao {
  id: number;
  nome: string;
  cidade: Cidade;
  tipoOrgao: TipoOrgao;
}

// --- Configuração ---
// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;

  const API_BASE_URL = `/api/orgao-empresa`;
const genericTableRef = ref<InstanceType<typeof GenericDataTable> | null>(null);


const initialFilters = ref({
  nome: "",
  cidade: null as Cidade | null,
  tipoOrgao: null as TipoOrgao | null,
  credenciadaSelecionada: null as Credenciada | null,
  nomeResponsavel: "",
  nomeTabeliao: "",
  codigoIBGEServentia: "",
  codigoServentia: "",
  oficioServentia: "",
});


async function fetchOrgao(lazyParams: any, filters: any) {
  try {
    const page = lazyParams.first / lazyParams.rows;
    const params: Record<string, unknown> = {
      nome: filters.nome || undefined,
      cidade: filters.cidade?.id || undefined,
      tipoOrgao: filters.tipoOrgao?.id || undefined,
      idCredenciadaOrgao: filters.credenciadaSelecionada?.id || undefined,
      nomeResponsavel: filters.nomeResponsavel || undefined,
      nomeTabeliao: filters.nomeTabeliao || undefined,
      codigoIBGEServentia: filters.codigoIBGEServentia || undefined,
      codigoServentia: filters.codigoServentia || undefined,
      oficioServentia: filters.oficioServentia || undefined,
      page: page,
      size: lazyParams.rows,
      sort: `${lazyParams.sortField || "nome"},${lazyParams.sortOrder === 1 ? "asc" : "desc"}`,
    };

    const response = await axios.get<{
      content: Orgao[];
      totalElements: number;
    }>(`${API_BASE_URL}/orgao/pesquisar`, { params });
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error("Problema ao buscar dados da API: ", error);
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: "Não foi possível realizar a pesquisa.",
      });
    }
    throw error;
  }
}

// --- Lógica para AutoComplete ---
const cidadeSuggestions = ref<Cidade[]>([]);
const searchCidades = async (event) => {
  //if (!event.query) return;
  const query = event.query || '';
  const { data } = await axios.get(
    `${API_BASE_URL}/orgao/cidades?nome=${query}`
  );
  cidadeSuggestions.value = data;
};



async function removerItem(item: SituacaoJuridica) {
  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja remover "${item.nome}"?`,
        header: "Confirmação de Exclusão",
        icon: "pi pi-exclamation-triangle",
        acceptLabel: "Sim, excluir",
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

      if (confirmed) {
        const response = await axios.delete(
          `${API_BASE_URL}/orgao?id=${item.id}&idUsuario=32&idCredenciadaUsuario=49393`
        );

        if (response.status >= 200 && response.status < 300) {
          genericTableRef.value?.removerItemDaLista(item.id);
          if (window.showHostToast) {
            window.showHostToast({
              severity: "success",
              summary: "Sucesso",
              detail: "Órgão/Empresa removido com sucesso!",
            });
          }
        } else {
          console.warn(
            "Requisição retornou com status não esperado:",
            response.status
          );
          if (window.showHostToast) {
            window.showHostToast({
              severity: "error",
              summary: "Erro",
              detail: "Erro ao tentar remover Órgão/Empresa.",
            });
          }
        }
      } else {
        console.log("Exclusão cancelada pelo usuário.");
        if (window.showHostToast) {
          window.showHostToast({
            severity: "info",
            summary: "Info",
            detail: "Exclusão cancelada pelo usuário.",
          });
        }
      }
    } catch (error) {
      console.error("Erro ao chamar o ConfirmDialog do Host:", error);
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro Crítico",
          detail: "Não foi possível exibir o diálogo de confirmação.",
        });
      }
    }
  } else {
    console.warn(
      "`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente ou a função não foi exposta."
    );
  }
}

function abrirEmNovaGuia(url: string) {
  window.open(url, "_blank");
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
  max-width: 1200px;
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
  color: white;
  margin-bottom: 1rem;
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
</style>
