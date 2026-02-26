<template>
  <div class="view-container d-flex flex-column" style="position: relative; bottom: 3rem">
    <div class="header cerurb-header">
      <h3 class="header-title">
        {{ pageTitle }}
      </h3>
    </div>
    <Card class="content-card">
      <template #title>
        <div class="d-flex align-items-center justify-content-between flex-wrap gap-2">
          <span class="card-section-title">{{ cardTitleInternal }}</span>
          <div class="d-flex align-items-center gap-2">
            <router-link :to="{ name: 'PesquisarEstado' }" custom v-slot="{ navigate }">
              <Button
                type="button"
                class="info-button"
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
        </div>
      </template>
      <template #content>
        <FormComponent
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
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import FormComponent from '@/components/FormEstadoComponent.vue'
// import { useAppToast } from '@/composables/useAppToast';
import Card from 'primevue/card'
import Button from 'primevue/button'
import SplitButton from 'primevue/splitbutton'
import type { MenuItem } from 'primevue/menuitem'
import axios from 'axios'
// import { useConfirm } from 'primevue/useconfirm';

// const API_BASE_URL = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL

const API_BASE_URL = `/api/estado`;

// const appToast = useAppToast();
const route = useRoute()
const router = useRouter()
// const confirm = useConfirm();

const currentMode = computed(
  () => (route.meta.mode as 'cadastrar' | 'editar' | 'visualizar') || 'cadastrar',
)
const currentSituacaoId = computed(() => {
  return route.params.id ? Number(route.params.id) : null
})

const isViewing = computed(() => currentMode.value === 'visualizar')

const pageTitle = computed(() => {
  if (currentMode.value === 'editar') {
    return 'Editar Estado'
  } else if (currentMode.value === 'visualizar') {
    return 'Visualizar Estado'
  } else {
    return 'Cadastrar Estado'
  }
})

const cardTitleInternal = computed(() => {
  if (currentMode.value === 'cadastrar') return 'Dados Cadastrais'
  if (currentMode.value === 'editar') return 'Edição de Dados Cadastrais'
  if (currentMode.value === 'visualizar') return 'Detalhes do Estado'
  return 'Dados Cadastrais'
})

const splitButtonOptions = ref<MenuItem[]>([
  {
    label: 'Editar',
    icon: 'pi pi-pencil',
    command: triggerEditMode,
  },
  {
    label: 'Excluir',
    icon: 'pi pi-trash',
    command: handleDelete,
  },
])

function triggerEditMode() {
  if (currentSituacaoId.value) {
    router.push({ name: 'EditarEstado', params: { id: currentSituacaoId.value } })
  }
}

async function handleDelete() {
  if (!currentSituacaoId.value) return

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja remover este Estado?`,
        header: 'Confirmação de Exclusão',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sim, excluir',
        rejectLabel: 'Cancelar',
        acceptClass: 'p-button-danger',
      })

      // Verifica o resultado da interação do usuário.
      if (confirmed) {
        const response = await axios.delete(
          `${API_BASE_URL}/estado?id=${currentSituacaoId.value}&idUsuario=32&idCredenciadaUsuario=49393`,
        )
        if (response.status >= 200 && response.status < 300) {
          if (window.showHostToast) {
            window.showHostToast({
              severity: 'success',
              summary: 'Sucesso',
              detail: 'Estado removido com sucesso!',
            })
            router.push({ name: 'PesquisarEstado' })
          }
        } else {
          console.warn('Requisição retornou com status não esperado:', response.status)
          if (window.showHostToast) {
            window.showHostToast({
              severity: 'error',
              summary: 'Erro',
              detail: 'Erro ao tentar remover Estado.',
            })
          }
        }
      } else {
        if (window.showHostToast) {
          window.showHostToast({
            severity: 'info',
            summary: 'Info',
            detail: 'Exclusão cancelada pelo usuário.',
          })
        }
      }
    } catch (error) {
      console.error('Erro ao chamar o ConfirmDialog do Host:', error)
      if (window.showHostToast) {
        window.showHostToast({
          severity: 'error',
          summary: 'Erro Crítico',
          detail: 'Não foi possível exibir o diálogo de confirmação.',
        })
      }
    }
  } else {
    console.warn(
      '`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente ou a função não foi exposta.',
    )
  }
}

function handleFormSubmission(eventPayload: { id?: number; mode: 'cadastrar' | 'editar' }) {

  if (eventPayload.id) {
    router.push({ name: 'VisualizarEstado', params: { id: eventPayload.id } })
  } else {
    router.push({ name: 'PesquisarEstado' })
  }
}

function handleFormCancellation() {
  if (currentMode.value === 'editar' && currentSituacaoId.value) {
    router.push({ name: 'VisualizarEstado', params: { id: currentSituacaoId.value } })
  } else {
    router.push({ name: 'PesquisarEstado' })
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
}

.content-card {
  width: 100%;
}

.content-card .p-card-title {
  width: 100%;
}

.card-section-title {
  font-weight: 600;
  font-size: 1.1rem;
  color: var(--p-card-title-color, #2d3748);
}

.actions-footer {
  margin-top: 1.5rem;
  text-align: center;
}

.header {
  width: 100%;
  color: #2d3748;
}
.header-title {
  margin: 0;
  font-size: 1.25rem;
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
</style>
