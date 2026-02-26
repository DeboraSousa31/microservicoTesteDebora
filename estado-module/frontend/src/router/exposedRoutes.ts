import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/estado",
    name: "PesquisarEstado",
    component: () => import("@/views/PesquisarSituacaoView.vue"),
    meta: {
      title: "Pesquisar Estado",
    },
  },
  {
    path: "/estado-form/novo",
    name: "CadastrarEstado",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Estado",
      mode: "cadastrar",
    },
  },
  {
    path: "/estado-form/editar/:id(\\d+)",
    name: "EditarEstado",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Estado",
      mode: "editar",
    },
  },
  {
    path: "/estado-form/visualizar/:id(\\d+)",
    name: "VisualizarEstado",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Estado",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
