import type { RouteRecordRaw } from "vue-router";

const exposedRoutes: Array<RouteRecordRaw> = [
  {
    path: "/atualizacao",
    name: "PesquisarAtualizacao",
    component: () => import("@/views/PesquisarSituacaoView.vue"),
    meta: {
      title: "Pesquisar Atualização",
    },
  },
  {
    path: "/atualizacao-form/novo",
    name: "CadastrarAtualizacao",
    component: () => import("@/views/SituacaoFormView.vue"),
    meta: {
      title: "Cadastrar Atualização",
      mode: "cadastrar",
    },
  },
  {
    path: "/atualizacao-form/editar/:id(\\d+)",
    name: "EditarAtualizacao",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Editar Atualização",
      mode: "editar",
    },
  },
  {
    path: "/atualizacao-form/visualizar/:id(\\d+)",
    name: "VisualizarAtualizacao",
    component: () => import("@/views/SituacaoFormView.vue"),
    props: true,
    meta: {
      title: "Visualizar Atualização",
      mode: "visualizar",
    },
  },
];
export default exposedRoutes;
