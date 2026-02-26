<template>
  <div class="view-container d-flex flex-column">
    <div class="header cerurb-header">
      <h3 class="header-title">
        <i class="pi pi-search"></i> Pesquisar Interessado
      </h3>
    </div>
    <Card class="content-card">
      <template #title>
        <router-link
          :to="{ name: 'CadastrarInteressado' }"
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
      </template>

      <template #content>
        <GenericDataTable
          ref="genericTableRef"
          title="Interessados"
          :initial-filters="filters"
          :fetch-data-function="fetchInteressado"
          :key="filters.tipoPessoa"
        >
          <template #search-fields="{ filters: filtrosDaTabela }">
            <div class="col-12 mb-4">
              <h6 class="mb-2">Tipo de Pessoa</h6>
              <div class="d-flex align-items-center">
                <RadioButton
                  inputId="tipoFisica"
                  name="documentType"
                  value="CPF"
                  v-model="filters.tipoPessoa"
                />
                <label for="tipoFisica" class="ml-2 mr-4">Física</label>
                <RadioButton
                  inputId="tipoJuridica"
                  name="documentType"
                  value="CNPJ"
                  v-model="filters.tipoPessoa"
                />
                <label for="tipoJuridica" class="ml-2">Jurídica</label>
              </div>
            </div>

            <template v-if="filters.tipoPessoa === 'CPF'">
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
                  <InputMask
                    id="cpf"
                    class="w-100"
                    mask="999.999.999-99"
                    v-model="filters.cpf"
                  />
                  <label for="cpf">CPF *</label>
                </FloatLabel>
              </div>

              <div class="mb-4 col-md-4">
                <InputGroup>
                  <FloatLabel>
                    <InputMask
                      id="dataNascimento"
                      v-model="filters.dataNascimento"
                      mask="99/99/9999"
                      class="w-100"
                    />
                    <label for="dataNascimento">Data Nascimento *</label>
                  </FloatLabel>
                  <Button
                    icon="pi pi-calendar"
                    aria-label="Abrir Calendário"
                    @click="toggleCalendar"
                  />
                </InputGroup>
                <OverlayPanel ref="op">
                  <Calendar
                    v-model="dateValueForCalendar"
                    :inline="true"
                    dateFormat="dd/mm/yy"
                    showWeek
                  />
                </OverlayPanel>
              </div>

              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="emailFisica"
                    type="email"
                    v-model="filters.email"
                  />
                  <label for="emailFisica">Email *</label>
                </FloatLabel>
              </div>

              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="telefone"
                    type="text"
                    v-model="filters.telefone"
                  />
                  <label for="telefone">Telefone</label>
                </FloatLabel>
              </div>

              <div class="mb-4 col-md-2">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="orgaoEmissor"
                    type="text"
                    v-model="filters.orgaoEmissor"
                  />
                  <label for="orgaoEmissor">Órgão Emissor</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-2">
                <GenericDropdown
                  v-model="filters.credenciadaSelecionada"
                  :fetch-url="`${API_BASE_URL}/interessado/credenciadas`"
                  input-id="credenciadaPesquisa"
                  label="Credenciada"
                  :showClear="true"
                />
              </div>
            </template>

            <template v-if="filters.tipoPessoa === 'CNPJ'">
              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="razaoSocial"
                    type="text"
                    v-model="filters.razaoSocial"
                  />
                  <label for="razaoSocial">Razão Social</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="nomeFantasia"
                    type="text"
                    v-model="filters.nomeFantasia"
                  />
                  <label for="nomeFantasia">nome Fantasia</label>
                </FloatLabel>
              </div>

              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputMask
                    id="cnpj"
                    class="w-100"
                    mask="99.999.999/9999-99"
                    v-model="filters.cnpj"
                  />
                  <label for="cnpj">CNPJ *</label>
                </FloatLabel>
              </div>

              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="profissao"
                    type="text"
                    v-model="filters.profissao"
                  />
                  <label for="profissao">Profissão</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-4">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="emailFisica"
                    type="email"
                    v-model="filters.email"
                  />
                  <label for="emailFisica">Email *</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-2">
                <FloatLabel>
                  <InputText
                    class="w-100"
                    id="telefone"
                    type="text"
                    v-model="filters.telefone"
                  />
                  <label for="telefone">Telefone</label>
                </FloatLabel>
              </div>
              <div class="mb-4 col-md-2">
                <GenericDropdown
                  inputId="credenciadaFisica"
                  label="Credenciada *"
                  :showClear="true"
                  :fetch-url="`${API_BASE_URL}/interessado/credenciadas`"
                  v-model="filters.idCredenciadaInteressado"
                  option-label="nome"
                />
              </div>
            </template>
          </template>

          <template #columns>
            <Column
              header="Ações"
              style="width: 8rem; text-align: center; word-wrap: normal"
            >
              <template #body="slotProps">
                <div class="div-acoes">
                  <router-link
                    :to="{
                      name: 'VisualizarInteressado',
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

            <Column
              v-for="coluna in colunasDaTabela"
              :key="coluna.field"
              :field="coluna.field"
              :header="coluna.header"
              sortable
            >
            </Column>
          </template>
        </GenericDataTable>
      </template>
    </Card>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, nextTick } from "vue";
import axios from "axios";

// --- Componentes ---

import Card from "primevue/card";
import Button from "primevue/button";
import InputText from "primevue/inputtext";
import FloatLabel from "primevue/floatlabel";
import Column from "primevue/column";
import RadioButton from "primevue/radiobutton";
import GenericDataTable from "../components/shared/GenericDataTable.vue";
import GenericDropdown from "../components/shared/GenericDropdown.vue";

import "bootstrap/dist/css/bootstrap.min.css";
import "@/assets/base.css";
import "@/assets/main.css";

import InputMask from "primevue/inputmask";
import InputGroup from "primevue/inputgroup";
import OverlayPanel from "primevue/overlaypanel";
import Calendar from "primevue/calendar";

interface Credenciada {
  id: number;
  nome: string;
}

// --- Configuração ---
// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;

const API_BASE_URL = `/api/interessado`;
const genericTableRef = ref<InstanceType<typeof GenericDataTable> | null>(null);

const filters = ref({
  tipoPessoa: "CPF",
  nome: "",
  credenciadaSelecionada: null as Credenciada | null,
  cpf: "",
  dataNascimento: "",
  email: "",
  telefone: "",
  orgaoEmissor: "",
  razaoSocial: "",
  nomeFantasia: "",
  cnpj: "",
  rg: "",
  profissao: "",
});

watch(
  () => filters.value.tipoPessoa,
  async (newTipoPessoa) => {
    if (newTipoPessoa === "CPF") {
      filters.value.razaoSocial = "";
      filters.value.nomeFantasia = "";
      filters.value.cnpj = "";
      filters.value.profissao = "";
    } else if (newTipoPessoa === "CNPJ") {
      filters.value.nome = "";
      filters.value.cpf = "";
      filters.value.dataNascimento = "";
      filters.value.email = "";
      filters.value.orgaoEmissor = "";
    }
    await nextTick();
    genericTableRef.value?.pesquisar();
  }
);
const colunasDaTabela = computed(() => {
  if (filters.value.tipoPessoa === "CPF") {
    return [
      { field: "nome", header: "Nome" },
      { field: "cpf", header: "CPF" },
    ];
  }
  if (filters.value.tipoPessoa === "CNPJ") {
    return [
      { field: "razaoSocial", header: "Razão Social" },
      { field: "cnpj", header: "CNPJ" },
    ];
  }
  return [];
});
async function fetchInteressado(lazyParams: any, filters: any) {
  try {
    const page = lazyParams.first / lazyParams.rows;
    const params: Record<string, unknown> = {
      nome: filters.nome || undefined,
      cpf: filters.cpf || undefined,
      rg: filters.rg || undefined,
      cnpj: filters.cnpj || undefined,
      idCredenciadaInteressado: filters.credenciadaSelecionada?.id || undefined,
      tipoDocumento: filters.tipoPessoa || undefined,
      dataNascimento: filters.dataNascimento || undefined,
      profissao: filters.profissao || undefined,
      email: filters.email || undefined,
      telefone: filters.telefone || undefined,
      razaoSocial: filters.razaoSocial || undefined,
      nomeFantasia: filters.nomeFantasia || undefined,
      orgaoEmissor: filters.orgaoEmissor || undefined,
      page: page,
      size: lazyParams.rows,
      sort: `${lazyParams.sortField || "nome"},${lazyParams.sortOrder === 1 ? "asc" : "desc"}`,
    };

    const response = await axios.get<{
      content: Interessado[];
      totalElements: number;
    }>(`${API_BASE_URL}/interessado/pesquisar`, { params });
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
const op = ref();
const dateValueForCalendar = ref<Date | null>(null);
const formattedDateForInput = ref<string | undefined>("");
const toggleCalendar = (event: Event) => {
  op.value.toggle(event);
};

watch(dateValueForCalendar, (newDate) => {
  if (newDate) {
    filters.value.dataNascimento = newDate.toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
    op.value.hide();
  }
});

async function removerItem(item: Interessado) {
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
          `${API_BASE_URL}/interessado?id=${item.id}&idUsuario=32&idCredenciadaUsuario=49393`
        );

        if (response.status >= 200 && response.status < 300) {
          genericTableRef.value?.removerItemDaLista(item.id);
          if (window.showHostToast) {
            window.showHostToast({
              severity: "success",
              summary: "Sucesso",
              detail: "Interessado removido com sucesso!",
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
              detail: "Erro ao tentar remover Interessado.",
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
.type-document {
  display: flex;
  flex-direction: row;
  align-items: center;
  font-size: 16px;
  text-align: center;
  margin-bottom: 15px;
}
</style>
