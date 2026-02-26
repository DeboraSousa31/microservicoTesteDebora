import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/status",
    name: "PesquisarStatus",
    component: () => import("@/views/PesquisarSituacaoView.vue"),
    meta: {
      title: "Pesquisar Status",
    },
  },
  {
    path: "/status-form/novo",
    name: "CadastrarStatus",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Status",
      mode: "cadastrar",
    },
  },
  {
    path: "/status-form/editar/:id(\\d+)",
    name: "EditarStatus",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Status",
      mode: "editar",
    },
  },
  {
    path: "/status-form/visualizar/:id(\\d+)",
    name: "VisualizarStatus",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Status",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
