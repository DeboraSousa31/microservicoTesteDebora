<template>
  <aside class="sidebar" :class="{ 'sidebar-open': open, 'sidebar-closed': !open }">
    <div class="sidebar-header">
      <div v-if="open" class="brand-container">
        <h1 class="brand-title">CERURB PRO</h1>
        <p class="brand-subtitle">Central de Regularização Fundiária</p>
      </div>
      <div v-else class="brand-icon">
        <i class="pi pi-building"></i>
      </div>
    </div>

    <div class="user-section">
      <div v-if="open" class="user-info-open">
        <div class="user-avatar">👤</div>
        <div class="user-details">
          <span class="username">admin</span>
          <span class="role">administrador</span>
        </div>
      </div>
      <div v-else class="user-info-closed">
        <div class="user-avatar-small">👤</div>
      </div>
    </div>

    <nav class="navigation-menu">
      <div class="nav-section">
        <div v-if="open" class="nav-section-title">Dashboard</div>
        <ul class="nav-list">
          <li class="nav-item">
            <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">
              <i class="pi pi-home"></i>
              <span v-if="open">Início</span>
            </router-link>
            <ul v-if="open && $route.path === '/'" class="nav-sublist">
              <li><a href="#" class="nav-sublink">- Mapa</a></li>
            </ul>
          </li>
        </ul>
      </div>

      <div class="nav-section">
        <div v-if="open" class="nav-section-title">Cadastro</div>
        <ul class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('nucleo')">
              <i class="pi pi-building"></i>
              <span v-if="open">Núcleo</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.nucleo ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.nucleo" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/pesquisar-nucleo" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/nucleo/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>

          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('imovel')">
              <i class="pi pi-home"></i>
              <span v-if="open">Imóvel</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.imovel ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.imovel" class="nav-sublist">
              <li><a href="#" class="nav-sublink">- Pesquisar</a></li>
              <li><a href="#" class="nav-sublink">- Cadastrar</a></li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('ocupante')">
              <i class="pi pi-user"></i>
              <span v-if="open">Ocupante</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.ocupante ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.ocupante" class="nav-sublist">
              <li><a href="#" class="nav-sublink">- Pesquisar</a></li>
              <li><a href="#" class="nav-sublink">- Cadastrar</a></li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('coleta')">
              <i class="pi pi-pencil"></i>
              <span v-if="open">Coleta</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.coleta ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.coleta" class="nav-sublist">
              <li><a href="#" class="nav-sublink">- Pesquisar</a></li>
              <li><a href="#" class="nav-sublink">- Acompanhar</a></li>
            </ul>
          </li>
        </ul>
      </div>

      <div class="nav-section">
        <div v-if="open" class="nav-section-title">Administração</div>
        <ul class="nav-list">
          <li class="nav-item">
            <router-link
              to="/aboutView"
              class="nav-link"
              :class="{ active: $route.path === '/aboutView' }"
            >
              <i class="pi pi-cog"></i>
              <span v-if="open">Configuração</span>
            </router-link>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('relatorios')">
              <i class="pi pi-file"></i>
              <span v-if="open">Relatórios</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.relatorios ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.relatorios" class="nav-sublist">
              <li><a href="#" class="nav-sublink">- Ocupantes</a></li>
              <li><a href="#" class="nav-sublink">- Imóveis</a></li>
              <li><a href="#" class="nav-sublink">- Acervo</a></li>
            </ul>
          </li>
        </ul>
      </div>
      <div class="nav-section">
        <div v-if="open" class="nav-section-title">Remotes</div>
        <ul class="nav-list">
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('situacaoocupante')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Situação Ocupante</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.situacaoocupante ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.situacaoocupante" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/situacao-ocupante" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/situacao-proprietario/novo" class="nav-link">
                  - Cadastrar
                </router-link>
              </li>
            </ul>
          </li>

          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('situacaojuridica')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Situação Jurídica</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.situacaojuridica ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.situacaojuridica" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/situacao-juridica" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/situacao-juridica-form/novo" class="nav-link">
                  - Cadastrar
                </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('orgao')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Órgão/Empresa</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.orgao ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.orgao" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/orgao" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/orgao-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('interessado')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Interessado</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.interessado ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.interessado" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/interessado" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/interessado-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('atualizacao')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Atualização</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.atualizacao ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.atualizacao" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/atualizacao" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/atualizacao-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('status')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Status</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.atualizacao ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.status" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/status" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/status-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('tipoComodo')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Tipo Cômodo</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.atualizacao ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.tipoComodo" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/tipo-comodo" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/tipo-comodo-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('tipoOrgao')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Tipo Órgão</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.atualizacao ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.tipoOrgao" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/tipo-orgao" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/tipo-orgao-form/novo" class="nav-link"> - Cadastrar </router-link>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="#" class="nav-link" @click.prevent="toggleMenu('estado')">
              <i class="pi pi-globe"></i>
              <span v-if="open">Estado</span>
              <i
                v-if="open"
                :class="[
                  'pi',
                  expandedMenus.estado ? 'pi-chevron-up' : 'pi-chevron-down',
                  'menu-arrow',
                ]"
              ></i>
            </a>
            <ul v-if="open && expandedMenus.estado" class="nav-sublist">
              <li class="nav-sublink">
                <router-link to="/estado" class="nav-link"> - Pesquisar </router-link>
              </li>
              <li class="nav-sublink">
                <router-link to="/estado-form/novo" class="nav-link"> - Cadastrar </router-link>

              </li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps<{
  open: boolean
}>()

const expandedMenus = ref({
  situacaoocupante: false,
  situacaojuridica: false,
  orgao: false,
  interessado:false,
  atualizacao:false,
  status: false,
  tipoComodo:false,
  tipoOrgao: false,
  nucleo: false,
  imovel: false,
  ocupante: false,
  estado: false,
  coleta: false,
  relatorios: false,
})

const toggleMenu = (menu: keyof typeof expandedMenus.value) => {
  expandedMenus.value[menu] = !expandedMenus.value[menu]
}
</script>

<style scoped>
.sidebar {
  background-color: #f8f9fa;
  border-right: 1px solid #dee2e6;
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow-x: hidden;
  flex-shrink: 0;
  height: 100vh;
  position: sticky;
  top: 0;
  font-family: 'Poppins', sans-serif !important;
  z-index: 10;
}

.sidebar-open {
  width: 260px;
}

.sidebar-closed {
  width: 80px;
}

.sidebar-header {
  padding: 1rem;
  text-align: center;
  border-bottom: 1px solid #dee2e6;
  background: white;
  min-height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-container {
  text-align: center;
}

.brand-title {
  font-size: 1.25rem;
  margin: 0;
  color: #3b7ddd;
  font-weight: bold;
  font-family: 'Poppins', sans-serif !important;
}

.brand-subtitle {
  font-size: 0.7rem;
  margin: 0.25rem 0 0 0;
  color: #6c757d;
  font-family: 'Poppins', sans-serif !important;
}

.brand-icon {
  font-size: 2rem;
  color: #3b7ddd;
}

.user-section {
  padding: 1rem;
  border-bottom: 1px solid #dee2e6;
  background: white;
}

.user-info-open {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.user-info-closed {
  display: flex;
  justify-content: center;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  color: #6c757d;
}

.user-avatar-small {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  color: #6c757d;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: bold;
  color: #495057;
  font-size: 0.9rem;
  font-family: 'Poppins', sans-serif !important;
}

.role {
  font-size: 0.75rem;
  color: #6c757d;
  font-family: 'Poppins', sans-serif !important;
}

.navigation-menu {
  flex-grow: 1;
  overflow-y: auto;
  padding: 1rem 0;
}

.nav-section {
  margin-bottom: 1.5rem;
}

.nav-section-title {
  padding: 0.5rem 1rem;
  font-size: 0.8rem;
  font-weight: bold;
  color: #6c757d;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-family: 'Poppins', sans-serif !important;
}

.nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  margin-bottom: 0.25rem;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #495057;
  text-decoration: none;
  transition: all 0.2s;
  cursor: pointer;
  font-family: 'Poppins', sans-serif !important;
}

.nav-link:hover {
  background-color: #e9ecef;
  color: #3b7ddd;
}

.nav-link.active {
  background-color: #3b7ddd;
  color: white;
}

.nav-link i {
  margin-right: 0.75rem;
  width: 16px;
  font-size: 0.9rem;
}

.sidebar-closed .nav-link {
  justify-content: center;
}

.sidebar-closed .nav-link i {
  margin-right: 0;
}

.menu-arrow {
  margin-left: auto;
  font-size: 0.8rem;
}

.nav-sublist {
  list-style: none;
  margin: 0;
  padding: 0;
  background-color: #f1f3f5;
}

.nav-sublink {
  display: block;
  padding: 0.5rem 1rem 0.5rem 3rem;
  color: #6c757d;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.2s;
  font-family: 'Poppins', sans-serif !important;
}

.nav-sublink:hover {
  background-color: #e9ecef;
  color: #3b7ddd;
}

@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
  }

  .sidebar-closed {
    transform: translateX(-100%);
  }
}
</style>
