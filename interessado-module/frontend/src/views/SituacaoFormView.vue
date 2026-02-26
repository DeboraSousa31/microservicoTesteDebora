<template>
  <div class="view-container d-flex flex-column" style="bottom: 3rem">
    <div class="header cerurb-header">
      <h3 class="header-title">
        {{ pageTitle }}
      </h3>
    </div>
    <Card class="content-card">
      <template #title>
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <h6 class="mb-2">Tipo de Interessado</h6>
            <div class="type-document">
              <RadioButton
                v-model="documentType"
                inputId="tipoFisica"
                name="documentType"
                value="CPF"
                :disabled="isViewing"
              />
              <label for="tipoFisica" class="ml-2 mr-4">Física</label>
              <RadioButton
                v-model="documentType"
                inputId="tipoJuridica"
                name="documentType"
                value="CNPJ"
                :disabled="isViewing"
              />
              <label for="tipoJuridica" class="ml-2 mr-4">Jurídica</label>
            </div>
          </div>
        </div>
        <div
          class="d-flex align-items-center justify-content-end"
          style="float: right"
        >
          <router-link
            :to="{ name: 'PesquisarInteressado' }"
            custom
            v-slot="{ navigate }"
          >
            <Button
              type="button"
              class="info-button mx-1"
              icon="pi pi-search"
              @click="navigate"
              severity="info"
              label="Pesquisar"
              raised
            />
          </router-link>

          <SplitButton
            v-if="isViewing && currentSituacaoId"
            label="Opções"
            icon="pi pi-cog"
            :model="splitButtonOptions"
            severity="warning"
            class="p-button-lg p-0"
            raised
          />
        </div>

        <div>{{ cardTitleInternal }}</div>

        <hr class="mb-5" />
      </template>
      <template #content>
        <FormComponent
          :form-type="documentType"
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

<script setup lang="ts">
import { computed, ref, watch, onMounted } from "vue";
//import { computed, ref } from 'vue'
import { useRoute, useRouter } from "vue-router";
import FormComponent from "@/components/FormInteressadoComponent.vue";
//import { useAppToast } from '@/composables/useAppToast';
import Card from "primevue/card";
import Button from "primevue/button";
import SplitButton from "primevue/splitbutton";
import type { MenuItem } from "primevue/menuitem";
import axios from "axios";
import RadioButton from "primevue/radiobutton";
//import { useConfirm } from 'primevue/useconfirm';

// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;

const API_BASE_URL = `/api/interessado`;
const documentType = ref("CPF");

//const appToast = useAppToast();
const route = useRoute();
const router = useRouter();
//const confirm = useConfirm();

const currentMode = computed(
  () =>
    (route.meta.mode as "cadastrar" | "editar" | "visualizar") || "cadastrar"
);
const currentSituacaoId = computed(() => {
  return route.params.id ? Number(route.params.id) : null;
});

const isViewing = computed(() => currentMode.value === "visualizar");

async function fetchDocumentType() {
  if (currentSituacaoId.value && currentMode.value !== "cadastrar") {
    try {
      const response = await axios.get(
        `${API_BASE_URL}/interessado/${currentSituacaoId.value}`
      );

      if (response.data && response.data.tipoDocumento) {
        documentType.value = response.data.tipoDocumento;
      }
    } catch (error) {
      console.error("Erro ao buscar o tipo de documento:", error);
    }
  }
}

onMounted(fetchDocumentType);
watch(currentSituacaoId, fetchDocumentType);

const pageTitle = computed(() => {
  if (currentMode.value === "editar") {
    return "Editar Interessado";
  } else if (currentMode.value === "visualizar") {
    return "Visualizar Interessado";
  } else {
    return "Cadastrar Interessado";
  }
});

const cardTitleInternal = computed(() => {
  if (currentMode.value === "cadastrar") return "Novos Dados Cadastrais";
  if (currentMode.value === "editar") return "Edição de Dados Cadastrais";
  if (currentMode.value === "visualizar") return "Detalhes do Interessado";
  return "Dados Cadastrais";
});

const splitButtonOptions = ref<MenuItem[]>([
  {
    label: "Editar",
    icon: "pi pi-pencil",
    command: triggerEditMode,
  },
  {
    label: "Excluir",
    icon: "pi pi-trash",
    command: handleDelete,
  },
]);

function triggerEditMode() {
  if (currentSituacaoId.value) {
    router.push({
      name: "EditarInteressado",
      params: { id: currentSituacaoId.value },
    });
  }
}

async function handleDelete() {
  if (!currentSituacaoId.value) return;

  // confirm.require({
  //   message: `Tem certeza que deseja remover esta Situação do Ocupante?`,
  //   header: 'Confirmação de Exclusão',
  //   icon: 'pi pi-exclamation-triangle',
  //   acceptLabel: 'Sim, excluir',
  //   rejectLabel: 'Cancelar',
  //   acceptClass: 'p-button-danger',
  //   accept: async () => {
  //     try {
  //       const response = await axios.delete(`${API_BASE_URL}/orgao?id=${currentSituacaoId.value}&idUsuario=32&idCredenciadaUsuario=49393`);
  //       if (response.status >= 200 && response.status < 300) {
  //         // appToast.showSuccess('Situação Ocupante removido com sucesso!')
  //         router.push({ name: 'PesquisarOrgao' });
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

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja remover este Interessado?`,
        header: "Confirmação de Exclusão",
        icon: "pi pi-exclamation-triangle",
        acceptLabel: "Sim, excluir",
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

      // Verifica o resultado da interação do usuário.
      if (confirmed) {
        const response = await axios.delete(
          `${API_BASE_URL}/interessado?id=${currentSituacaoId.value}&idUsuario=32&idCredenciadaUsuario=49393`
        );
        if (response.status >= 200 && response.status < 300) {
          if (window.showHostToast) {
            window.showHostToast({
              severity: "success",
              summary: "Sucesso",
              detail: "Órgão/Empresa removido com sucesso!",
            });
            router.push({ name: "PesquisarInteressado" });
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

function handleFormSubmission(eventPayload: {
  id?: number;
  mode: "cadastrar" | "editar";
}) {
  // appToast.showSuccess(`Situação ${eventPayload.mode === 'cadastrar' ? 'cadastrada' : 'atualizada'} com sucesso!`);
  if (eventPayload.id) {
    router.push({ name: "VisualizarInteressado", params: { id: eventPayload.id } });
  } else {
    router.push({ name: "PesquisarInteressado" });
  }
}

function handleFormCancellation() {
  if (currentMode.value === "editar" && currentSituacaoId.value) {
    router.push({
      name: "VisualizarInteressado",
      params: { id: currentSituacaoId.value },
    });
  } else {
    router.push({ name: "PesquisarInteressado" });
  }
}
</script>

<style scoped>
.p-card-title button {
  float: right;
}

.view-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  padding: 20px;
  width: 100%;
}

.content-card {
  width: 100%;
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
  color: white;
}


.info-button {
  max-height: 2.8rem;
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.info-button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}
.type-document {
  display: flex;
  flex-direction: row;
  align-items: center;
  font-size: 16px;
  text-align: center;
}
</style>
