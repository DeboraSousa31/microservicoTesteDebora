<template>
  <VeeForm
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    class="review-form"
    @submit="confirmarEnvio"
  >
    <div class="row">
      <div class="mb-2 col-md-4 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField
            name="nome"
            v-model="formData.nome"
            v-slot="{ field, errors }"
          >
            <InputText
              class="w-100"
              id="nome"
              type="text"
              v-model="formData.nome"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="nome">Nome *</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="nome" class="p-error" />
      </div>

      <div class="mb-2 col-md-3 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField
            name="legenda"
            v-model="formData.legenda"
            v-slot="{ field, errors }"
          >
            <InputText
              class="w-100"
              id="legenda"
              type="text"
              v-model="formData.legenda"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="legenda">Legenda *</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="legenda" class="p-error" />
      </div>

      <div class="mb-2 col-md-3 pb-4">
        <VeeField
          name="categoriaStatus"
          v-model="formData.categoriaStatus"
          v-slot="{ field, errors, value }"
        >
          <GenericDropdown
            v-model="formData.categoriaStatus"
            :fetch-url="`${API_BASE_URL}/status/categorias`"
            input-id="categoriaId"
            label="Categoria *"
            optionLabel="nome"
            :showClear="true"
            v-bind="field"
            :disabled="isViewing"
            :invalid="errors.length > 0"
          />
        </VeeField>
        <ErrorMessage name="categoriaStatus" class="p-error" />
      </div>
      <div class="mb-2 col-md-2 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField
            name="posicao"
            v-model="formData.posicao"
            v-slot="{ field, errors }"
          >
            <InputText
              class="w-100"
              id="posicao"
              type="text"
              v-model="formData.posicao"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="posicao">Posição *</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="posicao" class="p-error" />
      </div>

      <div class="mb-12 col-md-12 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField
            name="descricao"
            v-model="formData.descricao"
            v-slot="{ field, errors }"
          >
            <Textarea
              id="descricao"
              :disabled="isViewing"
              class="w-100"
              v-model="formData.descricao"
              :autoResize="false"
              rows="4"
            />
            <label for="descricao" class="form-label">Descrição</label>
        
          </VeeField>
        </FloatLabel>
      </div>
      <div class="mb-12 col-md-12 pb-4">
        <VeeField
          name="credenciadaStatus"
          v-model="formData.credenciadaStatus"
          v-slot="{ field, errors, value }"
        >
          <GenericDropdown
            v-model="formData.credenciadaStatus"
            :fetch-url="`${API_BASE_URL}/status/credenciadas`"
            input-id="credenciadaId"
            label="Credenciada "
            optionLabel="nome"
            :showClear="true"
            v-bind="field"
            :disabled="isViewing"
            :invalid="errors.length > 0"
          />
        </VeeField>
        <ErrorMessage name="credenciadaStatus" class="p-error" />
      </div>


      <div class="mb-2 col-md-6 pb-4">
        <VeeField name="corStatus"   v-slot="{ field, errors }">
          
          <div class="card p-fluid text-center ">
            <h5>Cor do Status</h5>
            <div class="custom-colorpicker">
              <label for="cp-hex">Selecione a Cor</label>
              <ColorPicker
              id="corStatus"
              class="w-100"
              input-id="cp-hex"
             v-model="formData.corStatus"
              v-bind="field"
              :disabled="isViewing"
              :invalid="errors.length > 0"
              format="hex"
            />
            </div>
          </div>
        </VeeField>
        <ErrorMessage name="corStatus" class="p-error" />
      </div>

      <div class="mb-2 col-md-6 pb-4">
        <VeeField name="corMapa"  v-slot="{ field, errors }">
          
          <div class="card p-fluid text-center ">
            <h5>Cor do Mapa</h5>
            <div class="custom-colorpicker">
              <label for="cp-hex">Selecione a Cor</label>
              <ColorPicker
              id="corStatus"
              class="w-100"
              input-id="cp-hex"
             v-model="formData.corMapa" 
              v-bind="field"
              :disabled="isViewing"
              :invalid="errors.length > 0"
              format="hex"
            />
            </div>
          </div>
        </VeeField>
        <ErrorMessage name="corMapa" class="p-error" />
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
import { computed, ref, watch } from "vue";
import InputText from "primevue/inputtext";
import Textarea from "primevue/textarea";
import ColorPicker from "primevue/colorpicker";
import Button from "primevue/button";
import GenericDropdown from "./shared/GenericDropdown.vue";
import FloatLabel from "primevue/floatlabel";
import InputNumber from "primevue/inputnumber";

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
const API_BASE_URL = `/api/status`;

interface Credenciada {
  id: number;
  nome: string;
}
interface Categoria {
  id: number;
  nome: string;
}
type FormData = {
  id?: number | null;
  nome: string;
  legenda: string;
  posicao: string;
  descricao: string;
  corMapa: string;
  corStatus: string;
  credenciadaStatus: Credenciada | null;
  categoriaStatus: Categoria | null,
  idCredenciadaUsuario: number;
  idUsuario: number;
};

const formData = ref<FormData>({
  id: null,
  nome: "",
  legenda: "",
  descricao: "",
  posicao: "",
  corMapa: "",
  corStatus: "",
  idCredenciadaUsuario: 53291,
  idUsuario: 454,
  credenciadaStatus: null,
  categoriaStatus: null,
});

const statusZodSchema = z.object({
  nome: z
    .string({ required_error: "O Nome é obrigatório." })
    .min(1, "O Nome é obrigatório.")
    .min(3, "O Nome deve ter pelo menos 3 caracteres."),
  legenda: z
    .string({ required_error: "A legenda é obrigatória." })
    .min(1, "A legenda é obrigatória.")
    .min(2, "A legenda deve ter pelo menos 2 caracteres."),
  posicao: z
    .string({ required_error: "A Posição é obrigatória." }),

  //  corStatus: z
  //   .object(
  //     {
  //       value: z.string().min(1, "A Cor do Status é obrigatória."),
  //     },
  //     {
  //       required_error: "A Cor do Status é obrigatória.",
  //       invalid_type_error: "Por favor, selecione uma cor.",
  //     }
  //   )
  //   .nullable(), // Adicione nullable para permitir que o campo comece vazio

  // corMapa: z
  //   .object(
  //     {
  //       value: z.string().min(1, "A Cor do Mapa é obrigatória."),
  //     },
  //     {
  //       required_error: "A Cor do Mapa é obrigatória.",
  //       invalid_type_error: "Por favor, selecione uma cor.",
  //     }
  //   )
  //   .nullable(),

  //corMapa: z.string({ required_error: "A Cor do Mapa é obrigatória." }).min(1, "A Cor do Mapa é obrigatória."),
  //corStatus: z.string({ required_error: "A Cor do Status é obrigatória." }).min(1, "A Cor do Status é obrigatória."),
  categoriaStatus: z
    .object(
      {
        id: z.number(),
        nome: z.string(),
      },
      {
        required_error: "A Categoria é obrigatória.",
        invalid_type_error: "Selecione uma Categoria válida.",
      }
    )
    .refine((val) => val !== null, { message: "A Categoria é obrigatória." }),
});

type FormValidationValues = z.infer<typeof statusZodSchema>;

const veeValidateSchema = toTypedSchema(statusZodSchema);

const {
  handleSubmit,
  resetForm,
  setValues,
  errors: formErrors,
  meta: formMeta,
} = useForm<FormValidationValues>({
  validationSchema: veeValidateSchema,
});

const loading = ref<boolean>(false);

const isViewing = computed(() => props.mode === "visualizar");
const isEditing = computed(() => props.mode === "editar");
const isCreating = computed(() => props.mode === "cadastrar");

const computedButtonLabel = computed(() => {
  return isEditing.value ? "Atualizar" : "Salvar";
});

async function carregarDados(id: number) {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/status/${id}`);
    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data;
      console.log(dadosApi);
      formData.value.id = dadosApi.id;
      formData.value.nome = dadosApi.nome;
      formData.value.legenda = dadosApi.legenda;
      formData.value.posicao = dadosApi.posicao;
      formData.value.corMapa = dadosApi.corMapa ? dadosApi.corMapa.replace('#', '') : '';
      formData.value.corStatus = dadosApi.cor ? dadosApi.cor.replace('#', '') : '';
      formData.value.descricao = dadosApi.descricao;
      formData.value.idCredenciadaUsuario = 53291;
      formData.value.idUsuario = 454;

      
      if (dadosApi.idCredenciada != null && dadosApi.nomeCredenciada != null) {
        formData.value.credenciadaStatus = {
          id: dadosApi.idCredenciada,
          nome: dadosApi.nomeCredenciada,
        };
      } else {
        formData.value.credenciadaStatus = null;
      }
      if (dadosApi.idCategoria != null && dadosApi.nomeCategoria != null) {
        formData.value.categoriaStatus = {
          id: dadosApi.idCategoria,
          nome: dadosApi.nomeCategoria,
        };
      } else {
        formData.value.categoriaStatus = null;
      }
      resetForm({
        values: {
          nome: formData.value.nome,
          descricao: formData.value.descricao,
          credenciadaStatus: formData.value.credenciadaStatus === null 
          ? undefined 
          : formData.value.credenciadaStatus,
          legenda: formData.value.legenda,
          posicao: formData.value.posicao,
          corMapa: formData.value.corMapa,
          corStatus: formData.value.corStatus,
          // corMapa: formData.value.corMapa ? { value: formData.value.corMapa } : null,
          // corStatus: formData.value.corStatus ? { value: formData.value.corStatus } : null,
          categoriaStatus: formData.value.categoriaStatus === null
          ? undefined
          : formData.value.categoriaStatus
        },
      })
      
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

  await enviarDadosParaAPI();
  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja ${acao} esta Status?`,
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

  const url: string = `${API_BASE_URL}/status`;
  const dataToSend = {
    id: isEditing.value ? props.situacaoId : undefined,
    nome: formData.value.nome,
    descricao: formData.value.descricao,
    idCredenciadaUsuario: 53291,
    idUsuario: formData.value.idUsuario,
    idCredenciadaStatus: formData.value.credenciadaStatus
      ? formData.value.credenciadaStatus.id
      : null,
    idCategoria: formData.value.categoriaStatus
      ? formData.value.categoriaStatus.id
      : null,
    legenda : formData.value.legenda,
    corStatus: formData.value.corStatus ? `#${formData.value.corStatus.replace('#', '')}` : null,
    corMapa: formData.value.corMapa ? `#${formData.value.corMapa.replace('#', '')}` : null,
    posicao : formData.value.posicao

  };

  if (isCreating.value) {
    delete dataToSend.id;
  }

  try {
    let response;
    let idRetornado: number | undefined = undefined;
    console.log(dataToSend);
    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}`, dataToSend);
      idRetornado = props.situacaoId;
    } else if (isCreating.value) {
      response = await axios.post(`${url}`, dataToSend);
      idRetornado = response.data.id;
    }

    if (response && response.status >= 200 && response.status < 300) {
      emit("form-submitted", { id: idRetornado, mode: props.mode });
      if (isCreating.value) {
        formData.value = {
          id: null,
          nome: "",
          descricao: "",
          legenda: "",
          posicao: "",
          corMapa: "",
          corStatus: "",
          categoriaStatus: null,
          credenciadaStatus: null,
          idCredenciadaUsuario: 53291,
          idUsuario: 454,
        };
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: "success",
          summary: "Sucesso",
          detail: `Situação Jurídica ${isEditing.value ? "atualizado" : "cadastrado"}`,
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

    //appToast.showError(`Não foi possível ${action} a Situação Jurídica. Tente novamente.`)

    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: `Não foi possível ${action} a Situação Jurídica. Tente novamente.`,
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
          nome: "",
          descricao: "",
          legenda: "",
          posicao: "",
          corMapa: "",
          corStatus: "",
          categoriaStatus: null,
          credenciadaStatus: null,
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
.custom-colorpicker {
  display: flex;
  flex-direction: column; 
  align-items: center;   
  gap: 0.5rem;        
}
.custom-colorpicker :deep(.p-colorpicker-preview) {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  transition: transform 0.2s;
  
}

.custom-colorpicker :deep(.p-colorpicker-preview:hover) {
  transform: scale(1.1);
}

.card {
  padding: 2rem;
  width: 100%;
  display: block;
  justify-content: center;
  margin: auto;

}

label {
  font-weight: bold;
 
  margin-bottom: 0.5rem;
}
</style>
