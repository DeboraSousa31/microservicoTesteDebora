<template>
  <VeeForm
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    @submit="confirmarEnvio"
  >
    <div class="row">
      <div class="col-md-6 mb-3">
        <label for="descricao" class="form-label">Descrição</label>

        <Textarea
          id="descricao"
          :disabled="isViewing"
          class="w-100"
          v-model="formData.descricao"
          :autoResize="false"
          rows="4"
        />
      </div>

      <div class="col-md-6 mb-3">
        <label for="time" class="form-label">Data Atualização *</label>
        <VeeField
          name="dataAgendamento"
          v-model="formData.dataAgendamento"
          v-slot="{ field, errors }"
        >
          <Calendar
            id="time"
            v-bind="field"
            :disabled="isViewing"
            :class="{ 'p-invalid': errors.length > 0 }"
            showTime
            hourFormat="24"
            mask="99/99/9999 99:99"
            placeholder="Ex: 01/01/2025 00:00"
            dateFormat="dd/mm/yy"
            class="w-100"
            v-model="formData.dataAgendamento"
          />
        </VeeField>
        <ErrorMessage name="dataAgendamento" class="p-error" />
      </div>

      <div class="col-12">
        <h5>Novidades</h5>
        <hr class="mt-0" />
      </div>

      <div class="col-12 mb-3">
        <label for="credFormUnificado" class="form-label">Credenciada</label>

        <GenericDropdown
          :fetch-url="`${API_BASE_URL}/atualizacao/credenciadas`"
          input-id="credFormUnificado"
          optionLabel="nome"
          :showClear="true"
          :disabled="isViewing"
        />
      </div>

      <div class="col-12 mb-3">
        <label class="form-label">Descrição</label>

        <Editor
          editorStyle="height: 320px;"
          v-model="formData.textoNovidade"
          :readonly="isViewing"
          :disabled="isViewing"
        />
      </div>

      <div class="col-12 text-center mt-4">
        <Button
          v-if="!isViewing"
          type="submit"
          icon="pi pi-save"
          :label="computedButtonLabel"
          :loading="loading"
          raised
        />
      </div>
    </div>
  </VeeForm>
</template>

<script setup lang="ts">
import "bootstrap/dist/css/bootstrap.min.css";
import "@/assets/base.css";
import "@/assets/main.css";
import { computed, ref, watch } from "vue";
import InputText from "primevue/inputtext";
import Editor from "primevue/editor";
import Card from "primevue/card";
import Textarea from "primevue/textarea";
import Calendar from "primevue/calendar";
import Dropdown from "primevue/dropdown";
import { format } from "date-fns";

import Button from "primevue/button";
import GenericDropdown from "./shared/GenericDropdown.vue";
import FloatLabel from "primevue/floatlabel";
//import { useAppToast } from '@/composables/useAppToast'
import axios from "axios";
//import { useConfirm } from 'primevue/useconfirm'
import {
  Form as VeeForm,
  Field as VeeField,
  ErrorMessage,
  useForm,
} from "vee-validate";
import { z } from "zod";
import { toTypedSchema } from "@vee-validate/zod";

//import { useToast } from 'primevue/usetoast'

const props = defineProps<{
  mode: "cadastrar" | "editar" | "visualizar";
  situacaoId?: number | null;
}>();

const emit = defineEmits(["form-submitted", "form-cancelled"]);

// const appToast = useAppToast()
// const confirmService = useConfirm()
// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;
const API_BASE_URL = `/api/atualizacao`;

interface Credenciada {
  id: number;
  nome: string;
}

type FormData = {
  id?: number | null;
  textoNovidade: string;
  descricao: string;
  dataAgendamento: Date | null;
  credenciadaAtualizacao: Credenciada | null;
  idCredenciadaUsuario: number;
  idUsuario: number;
};

const formData = ref<FormData>({
  id: null,
  textoNovidade: "",
  dataAgendamento: null,
  descricao: "",
  idCredenciadaUsuario: 53291,
  idUsuario: 454,
  credenciadaAtualizacao: null,
});

const loading = ref<boolean>(false);

const isViewing = computed(() => props.mode === "visualizar");
const isEditing = computed(() => props.mode === "editar");
const isCreating = computed(() => props.mode === "cadastrar");

const computedButtonLabel = computed(() => {
  return isEditing.value ? "Atualizar" : "Salvar";
});
const atualizacaoZodSchema = z.object({
  dataAgendamento: z.date({
    required_error: "A data de agendamento é obrigatória.",
  }),
});

type FormValidationValues = z.infer<typeof atualizacaoZodSchema>;

const veeValidateSchema = toTypedSchema(atualizacaoZodSchema);
const {
  handleSubmit,
  setValues,
  setFieldValue,
  values,
  resetForm,
  validateField,
} = useForm({
  validationSchema: veeValidateSchema,
});

async function carregarDados(id: number) {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/atualizacao/${id}`);
    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data;

      console.log(dadosApi);

      formData.value.id = dadosApi.id;
      formData.value.textoNovidade = dadosApi.textoNovidade;
      // formData.value.dataAgendamento = dadosApi.dataAgendamento
      //   ? new Date(dadosApi.dataAgendamento)
      //   : null;
      formData.value.descricao = dadosApi.descricao;
      formData.value.idCredenciadaUsuario = 53291;
      formData.value.idUsuario = 454;

      formData.value.dataAgendamento = new Date(
        dadosApi.dataAgendamento
      );

      formData.value = { ...formData.value };

      if (dadosApi.idCredenciada != null && dadosApi.nomeCredenciada != null) {
        formData.value.credenciadaAtualizacao = {
          id: dadosApi.idCredenciada,
          nome: dadosApi.nomeCredenciada,
        };
      } else {
        formData.value.credenciadaAtualizacao = null;
      }
    } else {
      // appToast.showError('Falha ao tentar obter dados.');
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro!",
          detail: "Falha ao tentar obter dados.",
        });
      }
      console.warn("Requisição retornou status não esperado:", response.status);
      emit("form-cancelled");
    }
  } catch (error) {
    // appToast.showError('Erro ao tentar editar.')
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro!",
        detail: "Falha ao tentar obter dados.",
      });
    }
    console.error("Erro ao editar item da API:", error);
    emit("form-cancelled");
  } finally {
    loading.value = false;
  }
}

async function confirmarEnvio() {
  const acao = isEditing.value ? "atualizar" : "cadastrar";
  const acaoLabel = isEditing.value ? "Atualizar" : "Salvar";

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja ${acao} esta Atualização?`,
        header: `Confirmar ${acaoLabel}`,
        icon: "pi pi-exclamation-triangle",
        acceptLabel: `Sim, ${acaoLabel}`,
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

      // Verifica o resultado da interação do usuário.
      if (confirmed) {
        await enviarDadosParaAPI();
      } else {
        if (window.showHostToast) {
          window.showHostToast({
            severity: "info",
            summary: "Info",
            detail: `Operação de ${acao} cancelada.`,
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

async function enviarDadosParaAPI() {
  if (isViewing.value) return;

  loading.value = true;
  const dataAgendamentoFormatada = formData.value.dataAgendamento
    ? format(formData.value.dataAgendamento, "dd-MM-yyyy HH:mm")
    : null;
  const url: string = `${API_BASE_URL}/atualizacao/`;
  const dataToSend = {
    id: isEditing.value ? props.situacaoId : undefined,
    textoNovidade: formData.value.textoNovidade,
    descricao: formData.value.descricao,
    dataAgendamento: dataAgendamentoFormatada,
    idCredenciadaUsuario: 53291,
    idUsuario: formData.value.idUsuario,
    idCredenciadaAtualizacao: formData.value.credenciadaAtualizacao
      ? formData.value.credenciadaAtualizacao.id
      : null,
  };

  if (isCreating.value) {
    delete dataToSend.id;
  }

  try {
    let response;
    let idRetornado: number | undefined = undefined;

    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}`, dataToSend);
      idRetornado = props.situacaoId;
    } else if (isCreating.value) {
      response = await axios.post(`${url}`, dataToSend);
      console.log(dataToSend);
      idRetornado = response.data.id;
    }

    if (response && response.status >= 200 && response.status < 300) {
      emit("form-submitted", { id: idRetornado, mode: props.mode });
      if (isCreating.value) {
        formData.value = {
          id: null,
          textoNovidade: "",
          descricao: "",
          dataAgendamento: null,
          credenciadaAtualizacao: null,
          idCredenciadaUsuario: 53291,
          idUsuario: 454,
        };
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: "success",
          summary: "Sucesso",
          detail: `Atualização ${isEditing.value ? "atualizado" : "cadastrado"}`,
        });
      }
    } else {
      //appToast.showError(`Erro ao tentar ${isEditing.value ? 'atualizar' : 'cadastrar'}.`)
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro",
          detail: `Erro ao tentar ${isEditing.value ? "atualizar" : "cadastrar"}`,
        });
      }
      console.error("Response recebido: " + response?.status);
    }
  } catch (error) {
    const action = isEditing.value ? "atualizar" : "cadastrar";

    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: `Não foi possível ${action} a Atualização. Tente novamente.`,
      });
    }

    console.error(`Problema ao ${action} dados:`, error);
  } finally {
    loading.value = false;
  }
}

watch(
  () => props.mode,
  (newMode, oldMode) => {
    if (newMode === "cadastrar") {
      formData.value = {
        id: null,
        textoNovidade: "",
        descricao: "",
        dataAgendamento: null,
        credenciadaAtualizacao: null,
        idCredenciadaUsuario: 53291,
        idUsuario: 454,
      };
    } else if (
      (newMode === "editar" || newMode === "visualizar") &&
      props.situacaoId != null
    ) {
      carregarDados(props.situacaoId);
    }
  },
  { immediate: true }
);

watch(
  () => props.situacaoId,
  (newId, oldId) => {
    if (
      newId != null &&
      (props.mode === "editar" || props.mode === "visualizar") &&
      newId !== oldId
    ) {
      carregarDados(newId);
    }
  }
);

// function parseDataHoraBrasileira(
//   dataString: string | null | undefined
// ): Date | null {
//   if (!dataString) {
//     return null;
//   }

//   const partes = dataString.split(/[ /:]/);

//   if (partes.length < 5) {
//     return null;
//   }

//   const dia = parseInt(partes[0], 10);
//   const mes = parseInt(partes[1], 10);
//   const ano = parseInt(partes[2], 10);
//   const hora = parseInt(partes[3], 10);
//   const minuto = parseInt(partes[4], 10);
//   console.log(new Date(ano, mes - 1, dia, hora, minuto));
//   return new Date(ano, mes - 1, dia, hora, minuto);
// }
</script>

<style scoped>
.col-md-6 label {
  display: block;
  margin-bottom: 0.4rem;
  font-weight: 500;
}

.review-form {
  min-height: 35vh;
  display: flex;
  justify-content: center;
}

.p-button {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.p-button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}
.p-inputtext,
.p-calendar,
.p-dropdown,
textarea {
  width: 100% !important;
}

.p-invalid-editor .p-editor-container .p-editor-content .ql-container {
  border: 1px solid var(--red-500) !important;
}
</style>
