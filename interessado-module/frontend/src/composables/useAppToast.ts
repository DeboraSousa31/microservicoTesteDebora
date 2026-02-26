import { useToast } from "primevue/usetoast";
import type { ToastServiceMethods } from "primevue/toastservice";


export function useAppToast() {
  const toast = useToast();

  const showSuccess = (detail: string, summary: string = 'Sucesso!') => {
    toast.add({ severity: 'success', summary, detail, life: 3500 });
  };

  const showError = (detail: string, summary: string = 'Erro!') => {
    toast.add({ severity: 'error', summary, detail, life: 5000 });
  };

  const showInfo = (detail: string, summary: string = 'Informação') => {
    toast.add({ severity: 'info', summary, detail, life: 3000 });
  };

  const showWarn = (detail: string, summary: string = 'Atenção') => {
    toast.add({ severity: 'warn', summary, detail, life: 4000 });
  };

  return {
    add: toast.add as ToastServiceMethods['add'],
    showSuccess,
    showError,
    showInfo,
    showWarn
  };
}
