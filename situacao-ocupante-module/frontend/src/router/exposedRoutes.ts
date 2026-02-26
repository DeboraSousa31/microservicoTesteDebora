import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/situacao-ocupante",
    name: "PesquisarSituacaoOcupante",
    component: () => import("@/views/PesquisarSituacaoView.vue"),
    meta: {
      title: "Pesquisar Situação Ocupante",
    },
  },
  {
    path: "/situacao-proprietario/novo",
    name: "CadastrarSituacaoProprietario",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Situação Ocupante",
      mode: "cadastrar",
    },
  },
  {
    path: "/situacao-proprietario/editar/:id(\\d+)",
    name: "EditarSituacaoProprietario",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Situação Ocupante",
      mode: "editar",
    },
  },
  {
    path: "/situacao-proprietario/visualizar/:id(\\d+)",
    name: "VisualizarSituacaoProprietario",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Situação Ocupante",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
