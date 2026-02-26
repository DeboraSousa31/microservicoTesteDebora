import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/tipo-comodo",
    name: "PesquisarTipoComodo",
    component: () => import("@/views/PesquisarTipoComodoView.vue"),
    meta: {
      title: "Pesquisar Tipo Cômodo",
    },
  },
  {
    path: "/tipo-comodo/novo",
    name: "CadastrarTipoComodo",
    component: () => import("@/views/TipoComodoFormView.vue"),
    meta: {
      title: "Cadastrar Tipo Cômodo",
      mode: "cadastrar",
    },
  },
  {
    path: "/tipo-comodo/editar/:id(\\d+)",
    name: "EditarTipoComodo",
    component: () => import("@/views/TipoComodoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Tipo Cômodo",
      mode: "editar",
    },
  },
  {
    path: "/tipo-comodo/visualizar/:id(\\d+)",
    name: "VisualizarTipoComodo",
    component: () => import("@/views/TipoComodoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Tipo Cômodo",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
