import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/orgao",
    name: "PesquisarOrgao",
    component: () => import("@/views/PesquisarOrgaoView.vue"),
    meta: {
      title: "Pesquisar Orgão",
    },
  },
  {
    path: "/orgao-form/novo",
    name: "CadastrarOrgao",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Orgão",
      mode: "cadastrar",
    },
  },
  {
    path: "/orgao-form/editar/:id(\\d+)",
    name: "EditarOrgao",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Orgão",
      mode: "editar",
    },
  },
  {
    path: "/orgao-form/visualizar/:id(\\d+)",
    name: "VisualizarOrgao",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Orgão",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
