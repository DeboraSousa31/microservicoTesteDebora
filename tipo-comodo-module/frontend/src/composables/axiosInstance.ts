import axios from "axios";
import { decrypt } from "./encrypt"; // 🔒 importa o decrypt AES

const axiosInstance = axios.create({
  baseURL: "/api",
});

axiosInstance.interceptors.request.use((config) => {
  const tokenCriptografado = localStorage.getItem("jwtToken");

  if (tokenCriptografado) {
    try {
      // 🔓 Descriptografa o token antes de enviar
      const tokenOriginal = decrypt(tokenCriptografado);
      config.headers.Authorization = `Bearer ${tokenOriginal}`;
    } catch (err) {
      console.error("❌ Erro ao descriptografar token antes de enviar no header:", err);
    }
  }

  return config;
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    if (status === 401) {
      window.showHostToast?.({
        severity: "warn",
        summary: "Sessão expirada",
        detail: "Faça login novamente.",
      });
    }

    if (status === 403) {
      window.showHostToast?.({
        severity: "error",
        summary: "Acesso negado",
        detail: "Você não tem permissão para acessar este recurso.",
      });
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
