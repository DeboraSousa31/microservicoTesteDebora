import { ref } from "vue";
import GenericDataTable from "../components/shared/GenericDataTable.vue";
import axiosInstance from "./axiosInstance";
import { decrypt, encrypt } from "./encrypt";

type TipoComodo = { id: number; nomeSingular: string; nomePlural: string };
type Credenciada = { id: number; nome: string };

const API_BASE_URL = `/api`;

/**
 * Decodifica o payload de um JWT (Base64URL), sem validação de assinatura.
 * Útil apenas para ler claims (como userId). Resiliente a Base64URL sem padding.
 */
function decodeJwtPayload<T = any>(jwt: string): T | null {
  try {
    const parts = jwt.split(".");
    if (parts.length < 2) return null;
    const base64Url = parts[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64.padEnd(base64.length + (4 - (base64.length % 4)) % 4, "=");
    const json = atob(padded);
    // decodeURIComponent para lidar com UTF-8 corretamente
    const decoded = decodeURIComponent(
      json
        .split("")
        .map((c) => `%${("00" + c.charCodeAt(0).toString(16)).slice(-2)}`)
        .join("")
    );
    return JSON.parse(decoded) as T;
  } catch (e) {
    console.error("❌ Falha ao decodificar JWT localmente:", e);
    return null;
  }
}

export function useTipoComodo() {
  const initialFilters = ref({
    nomeSingular: "",
    nomePlural: "",
    credenciadaSelecionada: null as Credenciada | null,
  });

  const genericTableRef = ref<InstanceType<typeof GenericDataTable> | null>(null);

  // 🔹 Buscar Tipos de Cômodo (paginado e filtrado)
  async function fetchTipoComodo(lazyParams: any, filters: any) {
    try {
      const page = lazyParams.first / lazyParams.rows;
      const params: Record<string, unknown> = {
        nomeSingular: filters.nomeSingular,
        nomePlural: filters.nomePlural,
        idCredenciadaTipoComodo: filters.credenciadaSelecionada?.id,
        page,
        size: lazyParams.rows,
      };

      if (lazyParams.sortField && (lazyParams.sortOrder === 1 || lazyParams.sortOrder === -1)) {
        params.sort = `${lazyParams.sortField},${lazyParams.sortOrder === 1 ? "asc" : "desc"}`;
      }

      const { data } = await axiosInstance.get<{ content: TipoComodo[]; totalElements: number }>(
        `/tipo-comodo/pesquisar/`,
        { params }
      );

      return data;
    } catch (error) {
      console.error("❌ Erro ao buscar TipoCômodo:", error);
      throw error;
    }
  }

  // 🔹 Remover item
  async function removerItem(item: TipoComodo) {
    try {
      const confirmed = await window.showHostConfirmDialog?.({
        message: `Tem certeza que deseja remover "${item.nomeSingular}"?`,
        header: "Confirmação de Exclusão",
        icon: "pi pi-exclamation-triangle",
        acceptLabel: "Sim, excluir",
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

      if (!confirmed) return;

      const tokenCriptografado = localStorage.getItem("jwtToken");
      if (!tokenCriptografado) throw new Error("Usuário não autenticado");

      // 🔓 Descriptografa o JWT armazenado (AES)
      const tokenOriginal = decrypt(tokenCriptografado);
      if (!tokenOriginal) throw new Error("Token inválido após descriptografia");

      // Decodifica payload localmente para pegar userId (sem depender de util externo)
      const decoded = decodeJwtPayload<{ userId?: number }>(tokenOriginal);
      const idUsuario = decoded?.userId;

      if (!idUsuario) {
        // Se back-end aceita, podemos enviar só o token no header e não o idUsuario.
        // Mas aqui preferimos falhar para evitar operação sem identificação.
        throw new Error("ID do usuário não encontrado no token");
      }

      const response = await axiosInstance.delete(`/tipo-comodo`, {
        params: { id: item.id, idUsuario },
      });

      if (response.status >= 200 && response.status < 300) {
        genericTableRef.value?.removerItemDaLista(item.id);
        window.showHostToast?.({
          severity: "success",
          summary: "Sucesso",
          detail: "Tipo Cômodo removido com sucesso!",
        });
      }
    } catch (error) {
      console.error("❌ Erro ao remover TipoCômodo:", error);
      window.showHostToast?.({
        severity: "error",
        summary: "Erro",
        detail: "Não foi possível remover o Tipo Cômodo.",
      });
    }
  }

  // 🔹 Abrir nova guia (envia id criptografado e token criptografado)
  function abrirEmNovaGuia(id: number, modo: "visualizar" | "editar" = "visualizar") {
    const tokenCriptografado = localStorage.getItem("jwtToken");

    if (!tokenCriptografado) {
      window.showHostToast?.({
        severity: "warn",
        summary: "Sessão expirada",
        detail: "Faça login novamente.",
      });
      return;
    }

    const idCriptografado = encrypt(id.toString());
    const MONOLITO_URL = "http://localhost:8080/CerurbPro/tipoComodo.xhtml";

    const url = `${MONOLITO_URL}?id=${encodeURIComponent(idCriptografado)}&mode=${modo}&token=${encodeURIComponent(tokenCriptografado)}`;

    console.log("🌐 Navegando para:", `${MONOLITO_URL}?id=***CRIPTO***&mode=${modo}&token=***OCULTO***`);
    window.open(url, "_blank");
  }

  return {
    API_BASE_URL,
    initialFilters,
    genericTableRef,
    fetchTipoComodo,
    removerItem,
    abrirEmNovaGuia,
  };
}
