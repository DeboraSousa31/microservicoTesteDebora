<template>
  <VeeForm
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    class="review-form"
    @submit="confirmarEnvio"
  >
    <div class="row justify-content-start">
      <div class="mb-2 col-md-6 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField name="nome" v-model="formData.nome" v-slot="{ field, errors }">
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

      <div class="mb-2 col-md-6 pb-4">
        <FloatLabel class="flex flex-col">
          <VeeField name="uf" v-model="formData.uf" v-slot="{ field, errors }">
            <InputText
              class="w-100"
              id="uf"
              type="text"
              v-model="formData.uf"
              :disabled="isViewing"
              v-bind="field"
              :invalid="errors.length > 0"
            />
            <label for="uf">UF *</label>
          </VeeField>
        </FloatLabel>
        <ErrorMessage name="uf" class="p-error" />
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
import InputNumber from 'primevue/inputnumber'
import Button from 'primevue/button'
import GenericDropdown from './shared/GenericDropdown.vue'
import FloatLabel from 'primevue/floatlabel'
//import { useAppToast } from '@/composables/useAppToast'
import axios from 'axios'
//import { useConfirm } from 'primevue/useconfirm'
import { Form as VeeForm, Field as VeeField, ErrorMessage, useForm } from 'vee-validate'
import { z } from 'zod'
import { toTypedSchema } from '@vee-validate/zod'
import { useCerurbLoading } from '@/composables/useCerurbLoading'

//import { useToast } from 'primevue/usetoast'

const { showLoading: showCerurbLoading, hideLoading: hideCerurbLoading } = useCerurbLoading()

const props = defineProps<{
  mode: 'cadastrar' | 'editar' | 'visualizar'
  situacaoId?: number | null
}>()

const emit = defineEmits(['form-submitted', 'form-cancelled'])


//const API_BASE_URL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL
const API_BASE_URL = `/api/estado`;


type FormData = {
  id?: number | null
  nome: string
  uf: string
  idUsuario: number
}

const formData = ref<FormData>({
  id: null,
  nome: '',
  uf: '',
  idUsuario: 454,
})

const estadoZodSchema = z.object({
  nome: z
    .string({ required_error: 'O Nome é obrigatório.' })
    .min(1, 'O Nome é obrigatório.')
    .min(3, 'O Nome deve ter pelo menos 3 caracteres.'),
  uf: z
    .string({ required_error: 'UF do Estado é obrigatória.' })
    .min(1, 'UF do Estado é obrigatória.'),
})

type FormValidationValues = z.infer<typeof  estadoZodSchema>

const veeValidateSchema = toTypedSchema(estadoZodSchema)

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
  showCerurbLoading()
  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`)
    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data
      formData.value.id = dadosApi.id
      formData.value.nome = dadosApi.nome
      formData.value.uf = dadosApi.uf
      formData.value.idUsuario = 454


      resetForm({
        values: {
          nome: formData.value.nome,
          uf: formData.value.uf,
        },
      })

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
    hideCerurbLoading()
  }
}

async function confirmarEnvio() {
  const acao = isEditing.value ? 'atualizar' : 'cadastrar'
  const acaoLabel = isEditing.value ? 'Atualizar' : 'Salvar'

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja ${acao} este Estado?`,
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
  showCerurbLoading()

  const url: string = `${API_BASE_URL}`
  const dataToSend = {
    id: isEditing.value ? props.situacaoId : undefined,
    nome: formData.value.nome,
    uf: formData.value.uf,
    idUsuario: formData.value.idUsuario,
  }

  if (isCreating.value) {
    delete dataToSend.id

  }

  try {
    let response
    let idRetornado: number | undefined = undefined

    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}/${props.situacaoId}`, dataToSend)
      console.log(dataToSend)
      console.log(response)
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
          nome: '',
          uf: '',
          idUsuario: 454,
        }
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: 'success',
          summary: 'Sucesso',
          detail: `Estado ${isEditing.value ? 'atualizado' : 'cadastrado'}`,
        })
      }
    } else {
      //appToast.showError(`Erro ao tentar ${isEditing.value ? 'atualizar' : 'cadastrar'}.`)
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
        detail: `Não foi possível ${action} a Estado. Tente novamente.`,
      })
    }

    console.error(`Problema ao ${action} dados:`, error)

  } finally {
    loading.value = false
    hideCerurbLoading()
  }
}

watch(
  () => props.mode,
  (newMode, oldMode) => {
    if (newMode === 'cadastrar') {
      formData.value = {
        id: null,
        nome: '',
        uf: '',
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
  min-height: 35vh;
  /* display: flex; */
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
