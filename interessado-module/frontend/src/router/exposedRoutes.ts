import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/interessado",
    name: "PesquisarInteressado",
    component: () => import("@/views/PesquisarInteressadoView.vue"),
    meta: {
      title: "Pesquisar Interessado",
    },
  },
  {
    path: "/interessado-form/novo",
    name: "CadastrarInteressado",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Interessado",
      mode: "cadastrar",
    },
  },
  {
    path: "/interessado-form/editar/:id(\\d+)",
    name: "EditarInteressado",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Interessado",
      mode: "editar",
    },
  },
  {
    path: "/interessado-form/visualizar/:id(\\d+)",
    name: "VisualizarInteressado",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Interessado",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
