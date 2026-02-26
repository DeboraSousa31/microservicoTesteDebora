<template>
  <VeeForm
    :key="props.formType"
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    class="review-form"
    v-slot="{ values, errors }"
    @submit="enviarDadosParaAPI"
  >
    <div
      v-if="loading"
      class="d-flex justify-content-center align-items-center p-5"
    >
      <ProgressSpinner />
    </div>
    <div v-else>
      <div v-if="props.formType === 'CPF'" class="row">
        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="nome"
              v-model="formData.nome"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeFisica"
                type="text"
                :disabled="isViewing"
                v-model="formData.nome"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFisica">Nome *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="nome" class="p-error" />
        </div>
        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="cpf"
              v-model="formData.cpf"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="cpf"
                class="w-100"
                mask="999.999.999-99"
                :disabled="isViewing"
                slotChar="___.___.___-__"
                v-model="formData.cpf"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="cpf">CPF *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="cpf" class="p-error" />
        </div>
        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="rg"
              v-model="formData.rg"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="rgFisica"
                type="text"
                :disabled="isViewing"
                v-model="formData.rg"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="rgFisica">RG </label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="rg" class="p-error" />
        </div>
        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="orgaoEmissor"
              v-model="formData.orgaoEmissor"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="orgaoEmissor"
                type="text"
                :disabled="isViewing"
                v-model="formData.orgaoEmissor"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="orgaoEmissor">Órgão Emissor </label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="orgaoEmissor" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="profissao"
              v-model="formData.profissao"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeFisica"
                type="text"
                :disabled="isViewing"
                v-model="formData.profissao"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFisica">Profissão </label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="profissao" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <InputGroup>
            <VeeField
              name="dataNascimento"
              v-model="formData.dataNascimento"
              v-slot="{ field, errors }"
            >
              <FloatLabel>
                <InputMask
                  id="dataNascimento"
                  v-model="formData.dataNascimento"
                  mask="99/99/9999"
                  class="w-100"
                  v-bind="field"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />

                <label for="dataNascimento">Data Nascimento *</label>
              </FloatLabel>

              <Button
                icon="pi pi-calendar"
                aria-label="Abrir Calendário"
                @click="toggleCalendar"
              />
            </VeeField>
          </InputGroup>

          <ErrorMessage name="dataNascimento" class="p-error" />

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
            <VeeField
              name="email"
              v-model="formData.email"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="emailFisica"
                type="email"
                :disabled="isViewing"
                v-model="formData.email"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="emailFisica">Email *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="email" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="telefone"
              v-model="formData.telefone"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="telefone"
                class="w-100"
                mask="(99) 99999-9999"
                :disabled="isViewing"
                slotChar="(__) _____-____"
                v-model="formData.telefone"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="telefone">Telefone *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="telefone" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <VeeField
            name="idCredenciadaInteressado"
            v-model="formData.idCredenciadaInteressado"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              inputId="credenciadaFisica"
              label="Credenciada *"
              :showClear="true"
              :fetch-url="`${API_BASE_URL}/interessado/credenciadas`"
              v-model="formData.idCredenciadaInteressado"
              v-bind="field"
              :disabled="isViewing"
              option-label="nome"
              :class="{ 'p-invalid': errors.length > 0 }"
            />
          </VeeField>

          <ErrorMessage name="idCredenciadaInteressado" class="p-error" />
        </div>
      </div>

      <!-- encerrando tipo pessoa fisica -->

      <!-- iniciando tipo pessoa juridica -->

      <div v-if="props.formType === 'CNPJ'" class="row">
        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="razaoSocial"
              v-model="formData.razaoSocial"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="razaoSocial"
                v-model="formData.razaoSocial"
                :disabled="isViewing"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFantasia">Razão Social *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="razaoSocial" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="nomeFantasia"
              v-model="formData.nomeFantasia"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeFantasia"
                type="email"
                :disabled="isViewing"
                v-model="formData.nomeFantasia"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFantasia">Nome Fantasia *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="nomeFantasia" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="cnpj"
              v-model="formData.cnpj"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="cnpj"
                class="w-100"
                mask="99.999.999/9999-99"
                v-model="formData.cnpj"
                v-bind="field"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="cnpj">CNPJ *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="cnpj" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="profissao"
              v-model="formData.profissao"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="razaoSocial"
                v-bind="field"
                v-model="formData.profissao"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />
              <label for="razaoSocial">Profissão </label>
            </VeeField>
          </FloatLabel>
          <ErrorMessage name="profissao" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="email"
              v-model="formData.email"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="emailFisica"
                type="email"
                :disabled="isViewing"
                v-model="formData.email"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="emailFisica">Email *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="email" class="p-error" />
        </div>

        <div class="mb-4 col-md-2">
          <FloatLabel>
            <VeeField
              name="telefone"
              v-model="formData.telefone"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="telefone"
                class="w-100"
                mask="(99) 99999-9999"
                :disabled="isViewing"
                slotChar="(__) _____-____"
                v-model="formData.telefone"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="telefone">Telefone *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="telefone" class="p-error" />
        </div>

        <div class="mb-4 col-md-2">
          <VeeField
            name="idCredenciadaInteressado"
            v-model="formData.idCredenciadaInteressado"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              input-id="credenciadaJuridica"
              label="Credenciada *"
              :showClear="true"
              :fetch-url="`${API_BASE_URL}/interessado/credenciadas`"
              v-model="formData.idCredenciadaInteressado"
              :disabled="isViewing"
              option-label="nome"
              v-bind="field"
              :class="{ 'p-invalid': errors.length > 0 }"
            />
          </VeeField>
          <ErrorMessage name="idCredenciadaInteressado" class="p-error" />
        </div>
      </div>

      <div class="col-md-12 mt-5 text-center action-buttons-form">
        <Button
          v-if="!isViewing"
          class="px-3 mx-2"
          type="submit"
          icon="pi pi-save"
          severity="info"
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
import GenericDropdown from "./shared/GenericDropdown.vue"; // Ajuste o caminho se necessário
import FloatLabel from "primevue/floatlabel";
import {
  Form as VeeForm,
  Field as VeeField,
  ErrorMessage,
  useForm,
} from "vee-validate";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import InputMask from "primevue/inputmask";
import { ref, watch, computed } from "vue";
import InputGroup from "primevue/inputgroup";
import OverlayPanel from "primevue/overlaypanel";
import Calendar from "primevue/calendar";

import axios from "axios";
import { z } from "zod";
import { toTypedSchema } from "@vee-validate/zod";
import { veeValidateSchema } from "@/schemas/orgaoSchema";
import ProgressSpinner from "primevue/progressspinner";

const props = defineProps<{
  mode: "cadastrar" | "editar" | "visualizar";
  situacaoId?: number | null;
  formType: "CPF" | "CNPJ";
  // optionValue: string;
  // modelValue: number | string | object | null;
}>();

const limparMascara = (valor: string | null | undefined): string => {
  return valor ? valor.replace(/\D/g, "") : "";
};

interface Credenciada {
  id: number;
  nome: string;
}

interface TipoDocumento {
  id: number;
  nome: string;
}

type FormData = {
  id?: number | null;
  nome: string;
  cpf: string;
  cnpj: string;
  nomeFantasia: string;
  razaoSocial: string;
  email: string;
  dataNascimento: string;
  orgaoEmissor: string;
  tipoDocumento: TipoDocumento | string | null;
  rg: string;
  telefone: string;
  profissao: string;
  idCredenciadaInteressado: Credenciada | null;
  idCredenciadaUsuario: number;
  idUsuario: number;
};

const formData = ref<FormData>({
  id: null,
  nome: "",
  cpf: "",
  cnpj: "",
  nomeFantasia: "",
  razaoSocial: "",
  email: "",
  dataNascimento: "",
  orgaoEmissor: "",
  tipoDocumento: null,
  telefone: "",
  rg: "",
  profissao: "",
  idCredenciadaInteressado: null,
  idCredenciadaUsuario: 53291,
  idUsuario: 454,
});
const isViewing = computed(() => props.mode === "visualizar");
const activeValidationSchema = computed(() => {
  return isViewing.value ? undefined : veeValidateSchema;
});

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

const emit = defineEmits(["form-submitted", "form-cancelled"]);
// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;
const API_BASE_URL = `/api/interessado`;

const isEditing = computed(() => props.mode === "editar");
const isCreating = computed(() => props.mode === "cadastrar");
const loading = ref<boolean>(false);
const computedButtonLabel = computed(() => {
  return isEditing.value ? "Atualizar" : "Salvar";
});

function parseDateString(dateStr: string): Date | null {
  if (!dateStr || !/^\d{2}\/\d{2}\/\d{4}$/.test(dateStr)) {
    return null;
  }
  const [day, month, year] = dateStr.split("/").map(Number);
  return new Date(year, month - 1, day);
}

async function carregarDados(id: number) {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/interessado/${id}`);

    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data;
      console.log("Dados recebidos da API:", dadosApi);
      formData.value.id = dadosApi.id;
      formData.value.nome = dadosApi.nome || "";
      formData.value.razaoSocial = dadosApi.razaoSocial || "";
      formData.value.email = dadosApi.email || "";
      formData.value.telefone = dadosApi.telefone || "";
      formData.value.cnpj = dadosApi.cnpj || "";
      formData.value.cpf = dadosApi.cpf || "";
      formData.value.rg = dadosApi.rg || "";
      formData.value.profissao = dadosApi.profissao || "";
      formData.value.tipoDocumento = dadosApi.tipoDocumento;
      formData.value.nomeFantasia = dadosApi.nomeFantasia;

      setValues({
        formType: props.formType,
        ...dadosApi,
      });

      if (dadosApi.dataNascimento) {
        formData.value.dataNascimento = formatarDataParaInput(
          dadosApi.dataNascimento
        );
        dateValueForCalendar.value = parseDateString(dadosApi.dataNascimento);
      }

      if (
        dadosApi.idCredenciadaInteressado &&
        dadosApi.nomeCredenciadaInteressado
      ) {
        formData.value.idCredenciadaInteressado = {
          id: dadosApi.idCredenciadaInteressado,
          nome: dadosApi.nomeCredenciadaInteressado,
        };
      } else {
        formData.value.idCredenciadaInteressado = null;
      }
    } else {
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
        message: `Tem certeza que deseja ${acao} este Interessado?`,
        header: `Confirmar ${acaoLabel}`,
        icon: "pi pi-exclamation-triangle",
        acceptLabel: `Sim, ${acaoLabel}`,
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

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

  const url: string = `${API_BASE_URL}/interessado`;
  const dataToSend: any = {
    id: isEditing.value ? props.situacaoId : undefined,
    nome: formData.value.nome,
    email: formData.value.email,
    cpf: limparMascara(formData.value.cpf),
    cnpj: limparMascara(formData.value.cnpj),
    rg: formData.value.rg,
    orgaoEmissor: formData.value.orgaoEmissor,
    profissao: formData.value.profissao,
    dataNascimento: formatarDataParaAPI(formData.value.dataNascimento),
    telefone: limparMascara(formData.value.telefone),
    idCredenciadaInteressado:
      formData.value.idCredenciadaInteressado?.id ?? null,
    tipoDocumento: props.formType,
    idCredenciadaUsuario: 53291,
    idUsuario: 454,
  };

  if (isCreating.value) {
    delete dataToSend.id;
  }

  try {
    let response;
    let idRetornado: number | undefined = undefined;

    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}/${props.situacaoId}`, dataToSend);
      idRetornado = props.situacaoId;
    } else {
      response = await axios.post(url, dataToSend);
      idRetornado = response.data.id;
    }

    if (response && response.status >= 200 && response.status < 300) {
      emit("form-submitted", { id: idRetornado, mode: props.mode });
      if (isCreating.value) {
        formData.value = {
          id: null,
          nome: "",
          cpf: "",
          cnpj: "",
          nomeFantasia: "",
          razaoSocial: "",
          email: "",
          dataNascimento: "",
          orgaoEmissor: "",
          rg: "",
          profissao: "",
          telefone: "",
          tipoDocumento: props.formType,
          idCredenciadaInteressado: null,
          idCredenciadaUsuario: 53291,
          idUsuario: 454,
        };
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: "success",
          summary: "Sucesso",
          detail: `Interessado ${isEditing.value ? "atualizado" : "cadastrado"}`,
        });
      }
    } else {
      console.error("Erro na resposta da API:", response);
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
    console.error("Falha ao enviar dados:", error);
    const action = isEditing.value ? "atualizar" : "cadastrar";
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: `Não foi possível ${action} o Órgão. Tente novamente.`,
      });
    }

    console.error(`Problema ao ${action} dados:`, error);
  } finally {
    loading.value = false;
  }
}

watch(
  () => props.mode,

  (newMode) => {
    if (newMode === "cadastrar") {
      formData.value = {
        id: null,
        nome: "",
        razaoSocial: "",
        telefone: "",
        tipoDocumento: props.formType,
        dataNascimento: "",
        cpf: "",
        cnpj: "",
        email: "",
        rg: "",
        profissao: "",
        nomeFantasia: "",
        orgaoEmissor: "",
        idCredenciadaInteressado: null,
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

const op = ref();
const dateValueForCalendar = ref<Date | null>(null);
const formattedDateForInput = ref<string | undefined>("");
const toggleCalendar = (event: Event) => {
  op.value.toggle(event);
};

watch(dateValueForCalendar, (newDate) => {
  if (newDate) {
    formData.value.dataNascimento = newDate.toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
    op.value.hide();
  }
});

watch(
  () => props.formType,
  (newFormType) => {
    setFieldValue("tipoDocumento", newFormType);
    setFieldValue("formType", newFormType);
    formData.value.tipoDocumento = newFormType;
  },
  {
    immediate: true,
  }
);

const formatarDataParaAPI = (
  dataString: string | null | undefined
): string | null => {
  if (!dataString || dataString.length < 10) {
    return null;
  }

  const partes = dataString.split("/");

  if (partes.length !== 3) {
    return null;
  }

  const [dia, mes, ano] = partes;

  return `${ano}-${mes}-${dia}`;
};
const formatarDataParaInput = (dataIso: string | null | undefined): string => {
  if (!dataIso) {
    return "";
  }

  const dataApenas = dataIso.split("T")[0];
  const partes = dataApenas.split("-");

  if (partes.length === 3) {
    const [ano, mes, dia] = partes;
    return `${dia}/${mes}/${ano}`;
  }

  return "";
};
</script>

<style scoped>
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
</style>
