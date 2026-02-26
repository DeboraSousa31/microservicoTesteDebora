// src/views/tipoComodoView.ts
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { MenuItem } from 'primevue/menuitem'
import axios from 'axios'

export default function useTipoComodoView() {
  const API_BASE_URL = `/`
  const route = useRoute()  
  const router = useRouter()

  const currentMode = computed(
    () => (route.meta.mode as 'cadastrar' | 'editar' | 'visualizar') || 'cadastrar'
  )

  const currentSituacaoId = computed(() => {
    return route.params.id ? Number(route.params.id) : null
  })

  const isViewing = computed(() => currentMode.value === 'visualizar')

  // const pageTitle = computed(() => {
  //   if (currentMode.value === 'editar') return 'Editar Tipo Cômodo'
  //   if (currentMode.value === 'visualizar') return 'Visualizar Tipo Cômodo'
  //   return 'Cadastrar Tipo Cômodo'
  // })

  //   const cardTitleInternal = computed(() => {
  //     if (currentMode.value === 'cadastrar') return 'Novos Dados Cadastrais'
  //     if (currentMode.value === 'editar') return 'Edição de Dados Cadastrais'
  //     if (currentMode.value === 'visualizar') return 'Detalhes do Tipo Cômodo'
  //     return 'Dados Cadastrais'
  //   })

  const splitButtonOptions = ref<MenuItem[]>([
    {
      label: 'Editar',
      icon: 'pi pi-pencil',
      command: triggerEditMode,
    },
    // {
    //   label: 'Excluir',
    //   icon: 'pi pi-trash',
    //   command: handleDelete,
    // },
  ])

  function triggerEditMode() {
    if (currentSituacaoId.value) {
      router.push({ name: 'EditarTipoComodo', params: { id: currentSituacaoId.value } })
    }
  }

  async function handleDelete() {
    if (!currentSituacaoId.value) return

    if (window.showHostConfirmDialog) {
      try {
        const confirmed = await window.showHostConfirmDialog({
          message: `Tem certeza que deseja remover este Tipo Cômodo?`,
          header: 'Confirmação de Exclusão',
          icon: 'pi pi-exclamation-triangle',
          acceptLabel: 'Sim, excluir',
          rejectLabel: 'Cancelar',
          acceptClass: 'p-button-danger',
        })

        if (confirmed) {
          const response = await axios.delete(
            `${API_BASE_URL}?id=${currentSituacaoId.value}&idUsuario=454`
          )

          if (response.status >= 200 && response.status < 300) {
            window.showHostToast?.({
              severity: 'success',
              summary: 'Sucesso',
              detail: 'Tipo Cômodo removido com sucesso!',
            })
            router.push({ name: 'PesquisarTipoComodo' })
          } else {
            window.showHostToast?.({
              severity: 'error',
              summary: 'Erro',
              detail: 'Erro ao tentar remover Tipo Cômodo.',
            })
          }
        } else {
          window.showHostToast?.({
            severity: 'info',
            summary: 'Info',
            detail: 'Exclusão cancelada pelo usuário.',
          })
        }
      } catch (error) {
        console.error('Erro ao chamar o ConfirmDialog do Host:', error)
        window.showHostToast?.({
          severity: 'error',
          summary: 'Erro Crítico',
          detail: 'Não foi possível exibir o diálogo de confirmação.',
        })
      }
    } else {
      console.warn(
        '`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente.'
      )
    }
  }

  function handleFormSubmission(eventPayload: { id?: number; mode: 'cadastrar' | 'editar' }) {
    if (eventPayload.id) {
      router.push({ name: 'VisualizarTipoComodo', params: { id: eventPayload.id } })
    } else {
      router.push({ name: 'PesquisarTipoComodo' })
    }
  }

  function handleFormCancellation() {
    if (currentMode.value === 'editar' && currentSituacaoId.value) {
      router.push({ name: 'VisualizarTipoComodo', params: { id: currentSituacaoId.value } })
    } else {
      router.push({ name: 'PesquisarTipoComodo' })
    }
  }

  return {
    // pageTitle,
    // cardTitleInternal,
    currentMode,
    isViewing,
    currentSituacaoId,
    splitButtonOptions,
    handleFormSubmission,
    handleFormCancellation,
  }
}

