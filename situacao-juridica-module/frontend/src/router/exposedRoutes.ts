import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/situacao-juridica",
    name: "PesquisarSituacaoJuridica",
    component: () => import("@/views/PesquisarSituacaoView.vue"),
    meta: {
      title: "Pesquisar Situação Jurídica",
    },
  },
  {
    path: "/situacao-juridica-form/novo",
    name: "CadastrarSituacaoJuridica",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Situação Jurídica",
      mode: "cadastrar",
    },
  },
  {
    path: "/situacao-juridica-form/editar/:id(\\d+)",
    name: "EditarSituacaoJuridica",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Situação Jurídica",
      mode: "editar",
    },
  },
  {
    path: "/situacao-juridica-form/visualizar/:id(\\d+)",
    name: "VisualizarSituacaoJuridica",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Situação Jurídica",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
