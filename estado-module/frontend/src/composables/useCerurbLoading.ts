import { inject, type Ref } from 'vue'

const CERURB_LOADING_KEY = Symbol('cerurb-loading')

export type CerurbLoadingState = Ref<boolean>

export function useCerurbLoading() {
  const loading = inject<CerurbLoadingState>(CERURB_LOADING_KEY)
  if (!loading) {
    return {
      loading: { value: false },
      showLoading: () => {},
      hideLoading: () => {},
    }
  }
  return {
    loading,
    showLoading: () => {
      loading.value = true
    },
    hideLoading: () => {
      loading.value = false
    },
  }
}

export function getCerurbLoadingKey() {
  return CERURB_LOADING_KEY
}
