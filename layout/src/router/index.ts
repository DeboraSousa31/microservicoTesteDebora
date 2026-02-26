import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
    meta: { title: 'Cerurb Pro' },
  },
  {
    path: '/aboutView',
    name: 'AboutView',
    component: () => import('../views/AboutView.vue'),
    meta: { title: 'AboutView' },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

const handleRemoteError = (moduleName: string, error: unknown) => {
  console.error(
    `❌ Falha ao carregar o módulo remoto '${moduleName}'. Verifique se ele está online.`,
    error,
  )
  return null
}

export async function initializeRouter() {
  console.log('Inicializando carregamento de rotas remotas...')

  const modulesToLoad: Promise<{ default: RouteRecordRaw[] } | null>[] = [
    import('situacaoOcupanteApp/exposedRoutes').catch((error) =>
      handleRemoteError('situacaoOcupanteApp', error),
    ),
    import('situacaoJuridicaApp/exposedRoutes').catch((error) =>
      handleRemoteError('situacaoJuridicaApp', error),
    ),
    import('orgaoApp/exposedRoutes').catch((error) =>
      handleRemoteError('orgaoApp', error),
    ),
    import('interessadoApp/exposedRoutes').catch((error) =>
      handleRemoteError('orgaoApp', error),
    ),
    import('atualizacaoApp/exposedRoutes').catch((error) =>
      handleRemoteError('atualizacaoApp', error),
    ),
    import('statusApp/exposedRoutes').catch((error) =>
      handleRemoteError('statusApp', error),
    ),
    import('tipoComodoApp/exposedRoutes').catch((error) =>
      handleRemoteError('tipoComodoApp', error),
    ),
    import('tipoOrgaoApp/exposedRoutes').catch((error) =>
      handleRemoteError('tipoOrgaoApp', error),
    ),
    import('estadoApp/exposedRoutes').catch((error) =>
      handleRemoteError('estadoApp', error),
    ),
  ]

  const loadedModules = await Promise.all(modulesToLoad)

  // Itera sobre os módulos que carregaram com sucesso
  loadedModules.forEach((module) => {
    if (module && module.default && Array.isArray(module.default)) {
      const remoteRoutes = module.default
      remoteRoutes.forEach((route: RouteRecordRaw) => {
        router.addRoute(route)
        console.log(`✅ Rota '${String(route.name) || String(route.path)}' adicionada.`)
      })
    }
  })
}

router.afterEach((to) => {
  document.title = (to.meta.title as string) || 'Cerurb Pro'
})

export default router
