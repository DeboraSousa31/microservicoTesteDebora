/* eslint-disable @typescript-eslint/no-unused-vars */
import HomeView from '@/views/HomeView.vue'
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import exposedRoutes from './exposedRoutes'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
    meta: {
      title: 'Cerurb Pro',
    },
  },
  ...exposedRoutes,
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

const DEFAULT_TITLE: string = 'Orgão'

router.afterEach((to, from) => {
  document.title = (to.meta.title as string) || DEFAULT_TITLE
})

export default router

