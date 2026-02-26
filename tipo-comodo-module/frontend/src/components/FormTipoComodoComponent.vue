<template>
  <VeeForm
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    class="review-form"
    @submit="confirmarEnvio"
  >
    <div class="row">
      <div class="mb-2 col-md-4">
        <FloatLabel class="flex flex-col">
          <VeeField name="nomeSingular" v-model="formData.nomeSingular" v-slot="{ field, errors }">
            <InputText
              class="w-100"
              id="nomeSingular"
              type="text"
              v-model="formData.nomeSingular"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="nomeSingular">Nome Singular*</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="nome" class="p-error" />
      </div>

      <div class="mb-2 col-md-4">
        <FloatLabel class="flex flex-col">
          <VeeField name="nomePlural" v-model="formData.nomePlural" v-slot="{ field, errors }">
            <InputText
              class="w-100"
              id="nomePlural"
              type="text"
              v-model="formData.nomePlural"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="nomePlural">Nome Plural *</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="nomePlural" class="p-error" />
      </div>

      <div class="mb-2 col-md-4">
        <VeeField
          name="credenciadaTipoComodo"
          v-model="formData.credenciadaTipoComodo"
          v-slot="{ field, errors, value }"
        >
          <GenericDropdown
            v-model="formData.credenciadaTipoComodo"
            :fetch-url="`${API_BASE_URL}/tipo-comodo/credenciadas`"
            input-id="credFormUnificado"
            label="Credenciada "
            optionLabel="nome"
            :showClear="true"
            v-bind="field"
            :disabled="isViewing"
            :invalid="errors.length > 0"
          />
        </VeeField>
        <ErrorMessage name="credenciadaTipoComodo" class="p-error" />
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
import 'bootstrap/dist/css/bootstrap.min.css'
import '@/assets/base.css'
import '@/assets/main.css'
import { computed, ref, watch } from 'vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import GenericDropdown from './shared/GenericDropdown.vue'
import FloatLabel from 'primevue/floatlabel'
import axios from 'axios'
import { Form as VeeForm, Field as VeeField, ErrorMessage, useForm } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'


const props = defineProps<{
  mode: 'cadastrar' | 'editar' | 'visualizar'
  situacaoId?: number | null
}>()

const emit = defineEmits(['form-submitted', 'form-cancelled'])


// const API_BASE_URL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL
const API_BASE_URL = `/api`;


interface Credenciada {
  id: number
  nome: string
}

type FormData = {
  id?: number | null
  nomeSingular: string
  nomePlural: string
  credenciadaTipoComodo: Credenciada | null
  idUsuario: number
}

const formData = ref<FormData>({
  id: null,
  nomeSingular: '',
  nomePlural: '',
  idUsuario: 454,
  credenciadaTipoComodo: null,
})

const tipoComodoZodSchema = z.object({
  nomeSingular: z
    .string({ required_error: 'O Nome Singular é obrigatório.' })
    .min(1, 'O Nome Singular é obrigatório.')
    .min(3, 'O Nome Singular deve ter pelo menos 3 caracteres.'),
  nomePlural: z
    .string({ required_error: 'O Nome Plural é obrigatório.' })
    .min(1, 'AO Nome Plural é obrigatório.'),
  
})

type FormValidationValues = z.infer<typeof tipoComodoZodSchema>

const veeValidateSchema = toTypedSchema(tipoComodoZodSchema)

const {
  handleSubmit,
  resetForm,
  setValues,
  errors: formErrors,
  meta: formMeta,
} = useForm<FormValidationValues>({
  validationSchema: veeValidateSchema,
})

const loading = ref<boolean>(false)

const isViewing = computed(() => props.mode === 'visualizar')
const isEditing = computed(() => props.mode === 'editar')
const isCreating = computed(() => props.mode === 'cadastrar')

const computedButtonLabel = computed(() => {
  return isEditing.value ? 'Atualizar' : 'Salvar'
})

async function carregarDados(id: number) {
  loading.value = true
  try {
    const response = await axios.get(`${API_BASE_URL}/tipo-comodo/${id}`)
    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data
      formData.value.id = dadosApi.id
      formData.value.nomeSingular = dadosApi.nomeSingular
      formData.value.nomePlural = dadosApi.nomePlural
      formData.value.idUsuario = 454


      resetForm({
        values: {
          nomeSingular: formData.value.nomeSingular,
          nomePlural: formData.value.nomePlural,
        },
      })

      if (dadosApi.idCredenciada != null && dadosApi.nomeCredenciada != null) {
        formData.value.credenciadaTipoComodo = {
          id: dadosApi.idCredenciada,
          nome: dadosApi.nomeCredenciada,
        }
      } else {
        formData.value.credenciadaTipoComodo = null
      }
    } else {

      if (window.showHostToast) {
        window.showHostToast({
          severity: 'error',
          summary: 'Erro!',
          detail: 'Falha ao tentar obter dados.',
        })
      }
      console.warn('Requisição retornou status não esperado:', response.status)
      emit('form-cancelled')
    }
  } catch (error) {

    if (window.showHostToast) {
      window.showHostToast({
        severity: 'error',
        summary: 'Erro!',
        detail: 'Falha ao tentar obter dados.',
      })
    }
    console.error('Erro ao editar item da API:', error)
    emit('form-cancelled')
  } finally {
    loading.value = false
  }
}

async function confirmarEnvio() {
  const acao = isEditing.value ? 'atualizar' : 'cadastrar'
  const acaoLabel = isEditing.value ? 'Atualizar' : 'Salvar'
  // await enviarDadosParaAPI();

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja ${acao} este Tipo Cômodo?`,
        header: `Confirmar ${acaoLabel}`,
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: `Sim, ${acaoLabel}`,
        rejectLabel: 'Cancelar',
        acceptClass: 'p-button-danger',
      });


      if (confirmed) {
        await enviarDadosParaAPI();
      } else {
        if (window.showHostToast) {
          window.showHostToast({ severity: 'info', summary: 'Info', detail: `Operação de ${acao} cancelada.` });
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

async function enviarDadosParaAPI() {
  if (isViewing.value) return

  loading.value = true

  const url: string = `${API_BASE_URL}/tipo-comodo`
  const dataToSend = {
    id: isEditing.value ? props.situacaoId : undefined,
    nomeSingular: formData.value.nomeSingular,
    nomePlural: formData.value.nomePlural,
    idUsuario: formData.value.idUsuario,
    idCredenciadaTipoComodo: formData.value.credenciadaTipoComodo
      ? formData.value.credenciadaTipoComodo.id
      : null,
  }

  if (isCreating.value) {
    delete dataToSend.id

  }

  try {
    let response
    let idRetornado: number | undefined = undefined

    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}`, dataToSend)
      idRetornado = props.situacaoId
    } else if (isCreating.value) {
      response = await axios.post(`${url}`, dataToSend)
      idRetornado = response.data.id
    }

    if (response && response.status >= 200 && response.status < 300) {
      emit('form-submitted', { id: idRetornado, mode: props.mode })
      if (isCreating.value) {
        formData.value = {
          id: null,
          nomeSingular: '',
          nomePlural: '',
          credenciadaTipoComodo: null,
          idUsuario: 454,
        }
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: 'success',
          summary: 'Sucesso',
          detail: `Tipo Cômodo ${isEditing.value ? 'atualizado' : 'cadastrado'}`,
        })
      }
    } else {
      if (window.showHostToast) {
        window.showHostToast({
          severity: 'error',
          summary: 'Erro',
          detail: `Erro ao tentar ${isEditing.value ? 'atualizar' : 'cadastrar'}`,
        })
      }
      console.error('Response recebido: ' + response?.status)
    }
  } catch (error) {
    const action = isEditing.value ? 'atualizar' : 'cadastrar'

    if (window.showHostToast) {
      window.showHostToast({
        severity: 'error',
        summary: 'Erro',
        detail: `Não foi possível ${action} o Tipo Cômodo. Tente novamente.`,
      })
    }

    console.error(`Problema ao ${action} dados:`, error)

  } finally {
    loading.value = false
  }
}

watch(
  () => props.mode,
  (newMode, oldMode) => {
    if (newMode === 'cadastrar') {
      formData.value = {
        id: null,
        nomeSingular: '',
        nomePlural: '',
        credenciadaTipoComodo: null,
        idUsuario: 454,
      }
    } else if ((newMode === 'editar' || newMode === 'visualizar') && props.situacaoId != null) {
      carregarDados(props.situacaoId)
    }
  },
  { immediate: true },
)

watch(
  () => props.situacaoId,
  (newId, oldId) => {
    if (
      newId != null &&
      (props.mode === 'editar' || props.mode === 'visualizar') &&
      newId !== oldId
    ) {
      carregarDados(newId)
    }
  },
)
</script>

<style scoped>
.review-form {
 
 
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
