import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/tipo-orgao",
    name: "PesquisarTipoOrgao",
    component: () => import("@/views/PesquisarTipoOrgaoView.vue"),
    meta: {
      title: "Pesquisar Tipo de Órgão",
    },
  },
  {
    path: "/tipo-orgao-form/novo",
    name: "CadastrarTipoOrgao",
    component: () => import("@/views/TipoOrgaoFormView.vue"),
    meta: {
      title: "Cadastrar Tipo de Órgão",
      mode: "cadastrar",
    },
  },
  {
    path: "/tipo-orgao-form/editar/:id(\\d+)",
    name: "EditarTipoOrgao",
    component: () => import("@/views/TipoOrgaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Tipo de Órgão",
      mode: "editar",
    },
  },
  {
    path: "/tipo-orgao-form/visualizar/:id(\\d+)",
    name: "VisualizarTipoOrgao",
    component: () => import("@/views/TipoOrgaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Tipo de Órgão",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
