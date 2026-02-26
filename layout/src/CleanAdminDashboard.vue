<template>
  <div class="layout-wrapper">
    <!-- Header -->
    <div class="layout-topbar">
      <nav class="navbar">
        <Button icon="pi pi-bars" class="sidebar-toggle" @click="toggleSidebar" text />

        <div class="search-container">
          <InputText v-model="searchQuery" placeholder="Pesquisar..." class="search-input" />
        </div>

        <div class="navbar-end">
          <!-- CerurbCoin Display -->
          <div class="cerurb-coin">
            <div class="coin-icon">💰</div>
            <div class="coin-info">
              <span class="coin-value">00,00</span>
              <ProgressBar :value="25" style="height: 8px; width: 80px;" />
            </div>
          </div>

          <!-- Action Buttons -->
          <Button icon="pi pi-mobile" @click="showMobileDialog = true" text rounded />

          <Button icon="pi pi-youtube" @click="showVideoSidebar = true" text rounded />

          <!-- Notifications -->
          <div class="notification-wrapper">
            <Button icon="pi pi-bell" @click="toggleNotifications" text rounded />
            <Badge :value="notificationCount" severity="danger" v-if="notificationCount > 0" />

            <OverlayPanel ref="notificationPanel" class="notification-panel">
              <div class="notification-header">
                <h6>Notificações</h6>
              </div>
              <div class="notification-list">
                <div v-if="notifications.length === 0" class="no-notifications">
                  <i class="pi pi-info-circle"></i>
                  <div>
                    <div>Nenhuma notificação</div>
                    <small>Você não tem notificações não lidas no momento.</small>
                  </div>
                </div>
                <div v-else v-for="notification in notifications" :key="notification.id" class="notification-item">
                  <i :class="notification.icon"></i>
                  <div>
                    <div>{{ notification.title }}</div>
                    <small>{{ notification.description }}</small>
                  </div>
                </div>
              </div>
            </OverlayPanel>
          </div>

          <!-- Settings Menu -->
          <Menu ref="settingsMenu" :model="settingsMenuItems" :popup="true" />
          <Button icon="pi pi-cog" @click="toggleSettingsMenu" text rounded />
        </div>
      </nav>
    </div>

    <!-- Sidebar -->
    <div :class="['layout-sidebar', { 'sidebar-hidden': sidebarHidden }]">
      <div class="sidebar-brand">
        <h3>CERURB PRO</h3>
      </div>

      <div class="sidebar-content">
        <div class="sidebar-user">
          <div class="user-avatar">👤</div>
          <div class="user-info">
            <div class="user-name">{{ currentUser.name }}</div>
            <small class="user-role">{{ currentUser.role }}</small>
          </div>
        </div>

        <div class="sidebar-nav">
          <div class="nav-section">
            <div class="nav-header">Dashboard</div>
            <div class="nav-item">
              <a href="#" class="nav-link active">
                <i class="pi pi-home"></i>
                <span>Início</span>
              </a>
              <div class="nav-subitems show">
                <a href="#" class="nav-sublink">- Mapa</a>
              </div>
            </div>
          </div>

          <div class="nav-section">
            <div class="nav-header">Cadastro</div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('nucleo')">
                <i class="pi pi-building"></i>
                <span>Núcleo</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.nucleo }"></i>
              </a>
              <div v-show="expandedItems.nucleo" class="nav-subitems">
                <a href="#" class="nav-sublink">- Pesquisar</a>
                <a href="#" class="nav-sublink">- Cadastrar</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('imovel')">
                <i class="pi pi-home"></i>
                <span>Imóvel</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.imovel }"></i>
              </a>
              <div v-show="expandedItems.imovel" class="nav-subitems">
                <a href="#" class="nav-sublink">- Pesquisar</a>
                <a href="#" class="nav-sublink">- Cadastrar</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('ocupante')">
                <i class="pi pi-user"></i>
                <span>Ocupante</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.ocupante }"></i>
              </a>
              <div v-show="expandedItems.ocupante" class="nav-subitems">
                <a href="#" class="nav-sublink">- Pesquisar</a>
                <a href="#" class="nav-sublink">- Cadastrar</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('coleta')">
                <i class="pi pi-pencil"></i>
                <span>Coleta</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.coleta }"></i>
              </a>
              <div v-show="expandedItems.coleta" class="nav-subitems">
                <a href="#" class="nav-sublink">- Pesquisar</a>
                <a href="#" class="nav-sublink">- Acompanhar</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('dashboard')">
                <i class="pi pi-chart-pie"></i>
                <span>Dashboard</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.dashboard }"></i>
              </a>
              <div v-show="expandedItems.dashboard" class="nav-subitems">
                <a href="#" class="nav-sublink">- Ranking</a>
                <a href="#" class="nav-sublink">- Painel de Processos</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('financeiro')">
                <i class="pi pi-dollar"></i>
                <span>Financeiro</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.financeiro }"></i>
              </a>
              <div v-show="expandedItems.financeiro" class="nav-subitems">
                <a href="#" class="nav-sublink">- Pesquisar</a>
                <a href="#" class="nav-sublink">- Cadastrar</a>
              </div>
            </div>
          </div>

          <div class="nav-section">
            <div class="nav-header">Administração</div>
            <div class="nav-item">
              <a href="#" class="nav-link">
                <i class="pi pi-cog"></i>
                <span>Configuração</span>
              </a>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('relatorios')">
                <i class="pi pi-file"></i>
                <span>Relatórios</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.relatorios }"></i>
              </a>
              <div v-show="expandedItems.relatorios" class="nav-subitems">
                <a href="#" class="nav-sublink">- Ocupantes</a>
                <a href="#" class="nav-sublink">- Imóveis</a>
                <a href="#" class="nav-sublink">- Acervo</a>
              </div>
            </div>

            <div class="nav-item">
              <a href="#" class="nav-link" @click="toggleNavItem('cerurbjus')">
                <i class="pi pi-book"></i>
                <span>CerurbJus</span>
                <i class="pi pi-chevron-down nav-arrow" :class="{ 'rotated': expandedItems.cerurbjus }"></i>
              </a>
              <div v-show="expandedItems.cerurbjus" class="nav-subitems">
                <a href="#" class="nav-sublink">- Envio ao CerurbJus</a>
                <a href="#" class="nav-sublink">- Histórico de Envio</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div :class="['layout-main', { 'sidebar-collapsed': sidebarHidden }]">
      <div class="main-content">
        <!-- Search Form -->
        <Card class="search-card">
          <template #title>
            <div class="search-title">
              <i class="pi pi-search"></i>
              <span>Pesquisar Núcleo</span>
            </div>
          </template>

          <template #content>
            <div class="search-form">
              <div class="form-grid">
                <div class="form-field">
                  <label for="nome">Nome</label>
                  <InputText id="nome" v-model="searchForm.nome" placeholder="Nome" />
                </div>

                <div class="form-field">
                  <label for="responsavel">Responsável</label>
                  <Dropdown id="responsavel" v-model="searchForm.responsavel" :options="responsavelOptions"
                    placeholder="Responsável" optionLabel="label" optionValue="value" />
                </div>

                <div class="form-field">
                  <label for="cidade">Cidade</label>
                  <Dropdown id="cidade" v-model="searchForm.cidade" :options="cidadeOptions"
                    placeholder="Selecione a cidade" optionLabel="label" optionValue="value" />
                </div>

                <div class="form-field">
                  <label for="credenciada">Credenciada</label>
                  <Dropdown id="credenciada" v-model="searchForm.credenciada" :options="credenciadaOptions"
                    placeholder="Credenciada" optionLabel="label" optionValue="value" />
                </div>

                <div class="form-field">
                  <label for="situacaoJuridica">Situação Jurídica</label>
                  <Dropdown id="situacaoJuridica" v-model="searchForm.situacaoJuridica"
                    :options="situacaoJuridicaOptions" placeholder="Situação Jurídica" optionLabel="label"
                    optionValue="value" />
                </div>
              </div>

              <div class="search-buttons">
                <Button label="Pesquisar" icon="pi pi-search" @click="performSearch" />
                <Button label="Limpar" icon="pi pi-refresh" @click="clearSearch" severity="secondary" />
              </div>
            </div>
          </template>
        </Card>

        <!-- Results Table -->
        <Card class="results-card">
          <template #title>
            <div class="results-header">
              <span>Núcleo ({{ searchResults.length }})</span>
            </div>
          </template>

          <template #content>
            <DataTable :value="searchResults" :paginator="true" :rows="10" :rowsPerPageOptions="[5, 10, 20, 50]"
              paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
              currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros"
              responsiveLayout="scroll" class="results-table">
              <Column field="acoes" header="Ações" style="width: 100px">
                <template #body="slotProps">
                  <div class="action-buttons">
                    <Button icon="pi pi-eye" size="small" text @click="viewItem(slotProps.data)" />
                    <Button icon="pi pi-pencil" size="small" text @click="editItem(slotProps.data)" />
                  </div>
                </template>
              </Column>
              <Column field="nome" header="Nome" sortable></Column>
              <Column field="situacao" header="Situação" sortable></Column>
              <Column field="qtdQuadras" header="Qtd de Quadras" sortable></Column>
              <Column field="qtdLotes" header="Qtd de Lotes" sortable></Column>
              <Column field="qtdImoveis" header="Qtd de Imóveis" sortable></Column>
              <Column field="status" header="Status" sortable>
                <template #body="slotProps">
                  <Tag :value="slotProps.data.status" :severity="getStatusSeverity(slotProps.data.status)" />
                </template>
              </Column>

              <template #empty>
                <div class="empty-state">
                  <i class="pi pi-info-circle"></i>
                  <p>Nenhum registro encontrado...</p>
                </div>
              </template>
            </DataTable>
          </template>
        </Card>
      </div>

      <!-- Footer -->
      <footer class="layout-footer">
        <div class="footer-content">
          <p>Copyright © {{ currentYear }} - Todos os Direitos Reservados</p>
          <a href="#" target="_blank">FoxInline Technologies</a>
        </div>
      </footer>
    </div>

    <!-- Mobile App Dialog -->
    <Dialog v-model:visible="showMobileDialog" header="Baixe o aplicativo através do QR Code" :modal="true"
      :closable="true" style="width: 400px">
      <div class="mobile-dialog-content">
        <div class="qr-placeholder">
          <i class="pi pi-qrcode" style="font-size: 4rem; color: #6c757d;"></i>
          <p>QR Code do Aplicativo</p>
        </div>
      </div>
      <template #footer>
        <Button label="Abrir Link" @click="openMobileLink" />
      </template>
    </Dialog>

    <!-- Video Sidebar -->
    <Sidebar v-model:visible="showVideoSidebar" position="right" style="width: 500px">
      <template #header>
        <h3>Tutoriais</h3>
      </template>

      <Accordion :multiple="true">
        <AccordionTab header="Geoprocessamento">
          <div class="video-list">
            <div v-for="video in videos" :key="video.id" class="video-item">
              <div class="video-thumbnail">
                <i class="pi pi-play-circle" style="font-size: 2rem; color: #3b7ddd;"></i>
              </div>
              <div class="video-info">
                <div class="video-title">{{ video.title }}</div>
                <small class="video-duration">{{ video.duration }}</small>
              </div>
            </div>
          </div>
        </AccordionTab>
        <AccordionTab header="Em breve">
          <div class="coming-soon">
            <i class="pi pi-clock" style="font-size: 3rem; color: #6c757d;"></i>
            <p>Novos tutoriais em breve!</p>
          </div>
        </AccordionTab>
      </Accordion>
    </Sidebar>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Dropdown from 'primevue/dropdown'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Card from 'primevue/card'
import Dialog from 'primevue/dialog'
import Sidebar from 'primevue/sidebar'
import Accordion from 'primevue/accordion'
import AccordionTab from 'primevue/accordiontab'
import OverlayPanel from 'primevue/overlaypanel'
import Menu from 'primevue/menu'
import Badge from 'primevue/badge'
import Tag from 'primevue/tag'
import ProgressBar from 'primevue/progressbar'

// Reactive data
const sidebarHidden = ref(false)
const searchQuery = ref('')
const showMobileDialog = ref(false)
const showVideoSidebar = ref(false)
const notificationPanel = ref()
const settingsMenu = ref()

// User data
const currentUser = ref({
  name: 'admin',
  role: 'administrador'
})

// Navigation state
const expandedItems = ref({
  nucleo: false,
  imovel: false,
  ocupante: false,
  coleta: false,
  dashboard: false,
  financeiro: false,
  relatorios: false,
  cerurbjus: false
})

// Search form
const searchForm = ref({
  nome: '',
  responsavel: null,
  cidade: null,
  credenciada: null,
  situacaoJuridica: null
})

// Options for dropdowns
const responsavelOptions = ref([
  { label: 'João Silva', value: 'joao' },
  { label: 'Maria Santos', value: 'maria' },
  { label: 'Pedro Costa', value: 'pedro' }
])

const cidadeOptions = ref([
  { label: 'São Paulo', value: 'sp' },
  { label: 'Rio de Janeiro', value: 'rj' },
  { label: 'Belo Horizonte', value: 'bh' }
])

const credenciadaOptions = ref([
  { label: 'Sim', value: true },
  { label: 'Não', value: false }
])

const situacaoJuridicaOptions = ref([
  { label: 'Ativa', value: 'ativa' },
  { label: 'Inativa', value: 'inativa' },
  { label: 'Pendente', value: 'pendente' }
])

// Search results - sample data
const searchResults = ref([
  {
    id: 1,
    nome: 'Núcleo Centro',
    situacao: 'Ativa',
    qtdQuadras: 25,
    qtdLotes: 150,
    qtdImoveis: 120,
    status: 'Ativo'
  },
  {
    id: 2,
    nome: 'Núcleo Norte',
    situacao: 'Ativa',
    qtdQuadras: 18,
    qtdLotes: 95,
    qtdImoveis: 85,
    status: 'Ativo'
  }
])

// Notifications
const notifications = ref([
  {
    id: 1,
    title: 'Nova atualização',
    description: 'Sistema atualizado com sucesso',
    icon: 'pi pi-info-circle'
  }
])

const notificationCount = computed(() => notifications.value.length)

// Settings menu
const settingsMenuItems = ref([
  {
    label: 'Meu Perfil',
    icon: 'pi pi-user',
    command: () => console.log('Profile clicked')
  },
  {
    label: 'Minha carteira CerurbCoin',
    icon: 'pi pi-wallet',
    command: () => console.log('Wallet clicked')
  },
  {
    label: 'Configurações',
    icon: 'pi pi-cog',
    command: () => console.log('Settings clicked')
  },
  {
    separator: true
  },
  {
    label: 'Sair',
    icon: 'pi pi-sign-out',
    command: () => console.log('Logout clicked')
  }
])

// Videos
const videos = ref([
  {
    id: 1,
    title: 'Tutorial de Geoprocessamento',
    duration: '10:30'
  },
  {
    id: 2,
    title: 'Como usar o sistema',
    duration: '15:45'
  }
])

// Computed
const currentYear = computed(() => new Date().getFullYear())

// Methods
const toggleSidebar = () => {
  sidebarHidden.value = !sidebarHidden.value
}

const toggleNavItem = (item: string) => {
  expandedItems.value[item] = !expandedItems.value[item]
}

const toggleNotifications = (event: Event) => {
  notificationPanel.value.toggle(event)
}

const toggleSettingsMenu = (event: Event) => {
  settingsMenu.value.toggle(event)
}

const performSearch = () => {
  console.log('Performing search with:', searchForm.value)
  // Add your search logic here
}

const clearSearch = () => {
  searchForm.value = {
    nome: '',
    responsavel: null,
    cidade: null,
    credenciada: null,
    situacaoJuridica: null
  }
}

const viewItem = (item: any) => {
  console.log('Viewing item:', item)
}

const editItem = (item: any) => {
  console.log('Editing item:', item)
}

const getStatusSeverity = (status: string) => {
  switch (status) {
    case 'Ativo': return 'success'
    case 'Inativo': return 'danger'
    case 'Pendente': return 'warning'
    default: return 'info'
  }
}

const openMobileLink = () => {
  console.log('Opening mobile app link')
  showMobileDialog.value = false
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.layout-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: 'Poppins', sans-serif;
  font-size: 13px;
  position: relative;
}

/* Background gradient */
.layout-wrapper::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 50vh;
  background: linear-gradient(135deg, #1288ff 0%, #0066cc 100%);
  z-index: 0;
}

/* Header Styles */
.layout-topbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: transparent;
  color: white;
  height: 50px;
  box-shadow: none;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1rem;
  height: 100%;
}

.sidebar-toggle {
  color: white !important;
  border: none !important;
  background: transparent !important;
  padding: 0.25rem !important;
  font-size: 1rem !important;
}

.search-container {
  flex: 1;
  max-width: 300px;
  margin: 0 1rem;
}

.search-input {
  width: 100%;
  background: rgba(255, 255, 255, 0.15) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  color: white !important;
  font-size: 13px !important;
  padding: 0.4rem 0.8rem !important;
  height: 32px !important;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.8) !important;
}

.navbar-end {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.cerurb-coin {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0 0.5rem;
  font-size: 12px;
}

.coin-icon {
  font-size: 1.2rem;
  color: #ffd700;
}

.coin-info {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
}

.coin-value {
  font-weight: 600;
  font-size: 12px;
  color: white;
}

.notification-wrapper {
  position: relative;
}

.navbar-end .p-button {
  padding: 0.3rem !important;
  font-size: 0.9rem !important;
  color: white !important;
  background: transparent !important;
  border: none !important;
}

.navbar-end .p-button:hover {
  background: rgba(255, 255, 255, 0.1) !important;
}

/* Sidebar Styles */
.layout-sidebar {
  position: fixed;
  top: 50px;
  left: 0;
  width: 200px;
  height: calc(100vh - 50px);
  background: #f8f9fa;
  border-right: 1px solid #dee2e6;
  overflow-y: auto;
  transition: transform 0.3s ease;
  z-index: 999;
}

.sidebar-hidden {
  transform: translateX(-100%);
}

.sidebar-brand {
  padding: 0.8rem;
  text-align: center;
  border-bottom: 1px solid #dee2e6;
  background: white;
}

.sidebar-brand h3 {
  margin: 0;
  color: #3b7ddd;
  font-weight: 700;
  font-size: 14px;
}

.sidebar-content {
  padding: 0.5rem 0;
}

.sidebar-user {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.8rem;
  border-bottom: 1px solid #dee2e6;
  margin-bottom: 0.5rem;
}

.user-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  margin-bottom: 0.4rem;
  color: #6c757d;
}

.user-info {
  text-align: center;
}

.user-name {
  font-weight: 600;
  margin-bottom: 0.1rem;
  color: #495057;
  font-size: 12px;
}

.user-role {
  color: #6c757d;
  font-size: 10px;
}

.nav-section {
  margin-bottom: 1rem;
}

.nav-header {
  padding: 0.3rem 0.8rem;
  font-size: 10px;
  font-weight: 600;
  color: #6c757d;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.nav-item {
  margin-bottom: 0.1rem;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.5rem 0.8rem;
  color: #495057;
  text-decoration: none;
  transition: all 0.2s;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  font-size: 12px;
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
  margin-right: 0.6rem;
  width: 14px;
  font-size: 0.8rem;
}

.nav-arrow {
  margin-left: auto;
  transition: transform 0.2s;
  font-size: 0.7rem;
}

.nav-arrow.rotated {
  transform: rotate(180deg);
}

.nav-subitems {
  background-color: #f8f9fa;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.nav-subitems.show {
  display: block;
}

.nav-sublink {
  display: block;
  padding: 0.4rem 0.8rem 0.4rem 2.2rem;
  color: #6c757d;
  text-decoration: none;
  font-size: 11px;
  transition: all 0.2s;
}

.nav-sublink:hover {
  background-color: #e9ecef;
  color: #3b7ddd;
}

/* Main Content Styles */
.layout-main {
  margin-left: 200px;
  margin-top: 50px;
  min-height: calc(100vh - 50px);
  transition: margin-left 0.3s ease;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
}

.sidebar-collapsed {
  margin-left: 0;
}

.main-content {
  flex: 1;
  padding: 1.5rem;
  background: transparent;
}

.search-card {
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: white;
  border-radius: 8px;
}

.search-card .p-card-title {
  font-size: 14px !important;
  font-weight: 600 !important;
}

.search-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #3b7ddd;
  font-weight: 600;
  font-size: 14px;
}

.search-form {
  padding: 0.8rem 0;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.form-field label {
  font-weight: 500;
  color: #495057;
  font-size: 11px;
}

.form-field .p-inputtext,
.form-field .p-dropdown {
  font-size: 12px !important;
  padding: 0.4rem 0.6rem !important;
  height: 32px !important;
}

.search-buttons {
  display: flex;
  gap: 0.8rem;
  justify-content: flex-end;
}

.search-buttons .p-button {
  font-size: 12px !important;
  padding: 0.4rem 0.8rem !important;
  height: 32px !important;
}

.results-card {
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: white;
  border-radius: 8px;
}

.results-card .p-card-title {
  font-size: 14px !important;
  font-weight: 600 !important;
}

.results-header {
  color: #3b7ddd;
  font-weight: 600;
  font-size: 14px;
}

.results-table {
  font-size: 12px !important;
}

.results-table .p-datatable-thead>tr>th {
  font-size: 11px !important;
  font-weight: 600 !important;
  padding: 0.6rem !important;
  background: #3b7ddd !important;
  color: white !important;
}

.results-table .p-datatable-tbody>tr>td {
  font-size: 11px !important;
  padding: 0.5rem !important;
}

.action-buttons {
  display: flex;
  gap: 0.2rem;
}

.action-buttons .p-button {
  font-size: 0.8rem !important;
  padding: 0.2rem !important;
  width: 24px !important;
  height: 24px !important;
}

.empty-state {
  text-align: center;
  padding: 2rem;
  color: #6c757d;
  font-size: 12px;
}

.empty-state i {
  font-size: 1.5rem;
  margin-bottom: 0.8rem;
  display: block;
}

/* Footer Styles */
.layout-footer {
  background: white;
  border-top: 1px solid #dee2e6;
  padding: 0.8rem 1.5rem;
  margin-top: auto;
  text-align: right;
}

.footer-content {
  color: #6c757d;
  font-size: 11px;
}

.footer-content p {
  margin: 0 0 0.2rem 0;
}

.footer-content a {
  color: #3b7ddd;
  text-decoration: none;
}

.footer-content a:hover {
  text-decoration: underline;
}

/* Dialog Styles */
.mobile-dialog-content {
  text-align: center;
  padding: 1.5rem;
}

.qr-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.8rem;
  padding: 1.5rem;
  border: 2px dashed #dee2e6;
  border-radius: 8px;
  background: #f8f9fa;
}

/* Video Styles */
.video-list {
  max-height: 350px;
  overflow-y: auto;
}

.video-item {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 0.8rem;
  margin-bottom: 0.8rem;
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 6px;
  border: 1px solid #dee2e6;
}

.video-item:hover {
  background-color: #f8f9fa;
}

.video-thumbnail {
  width: 60px;
  height: 45px;
  background: #f8f9fa;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dee2e6;
}

.video-info {
  flex: 1;
}

.video-title {
  display: block;
  font-weight: 500;
  margin-bottom: 0.2rem;
  color: #495057;
  font-size: 12px;
}

.video-duration {
  color: #6c757d;
  font-size: 10px;
}

.coming-soon {
  text-align: center;
  padding: 2rem;
  color: #6c757d;
  font-size: 12px;
}

/* Notification Styles */
.notification-panel {
  width: 300px;
}

.notification-header {
  padding: 0.8rem;
  border-bottom: 1px solid #dee2e6;
  font-weight: 600;
  color: #495057;
}

.notification-header h6 {
  margin: 0;
  font-size: 12px;
}

.notification-list {
  max-height: 300px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 0.8rem;
  border-bottom: 1px solid #f1f1f1;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 11px;
}

.notification-item:hover {
  background-color: #f8f9fa;
}

.notification-item i {
  margin-right: 0.8rem;
  color: #dc3545;
  margin-top: 0.2rem;
}

.no-notifications {
  display: flex;
  align-items: center;
  padding: 0.8rem;
  color: #6c757d;
  font-size: 11px;
}

.no-notifications i {
  margin-right: 0.8rem;
  color: #17a2b8;
  font-size: 1rem;
}

/* Responsive Design */
@media (max-width: 768px) {
  .layout-sidebar {
    transform: translateX(-100%);
  }

  .layout-main {
    margin-left: 0;
  }

  .search-container {
    display: none;
  }

  .navbar-end {
    gap: 0.2rem;
  }

  .cerurb-coin {
    display: none;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .search-buttons {
    flex-direction: column;
  }

  .main-content {
    padding: 1rem;
  }
}

@media (max-width: 480px) {
  .navbar {
    padding: 0 0.5rem;
  }

  .main-content {
    padding: 0.5rem;
  }
}

/* PrimeVue component overrides */
.p-card .p-card-body {
  padding: 1rem !important;
}

.p-card .p-card-title {
  margin-bottom: 0.8rem !important;
}

.p-card .p-card-content {
  padding: 0 !important;
}

.p-datatable .p-paginator {
  font-size: 11px !important;
}

.p-dropdown-panel .p-dropdown-items .p-dropdown-item {
  font-size: 12px !important;
  padding: 0.4rem 0.6rem !important;
}

.p-progressbar {
  height: 6px !important;
  background: rgba(255, 255, 255, 0.3) !important;
}

.p-progressbar .p-progressbar-value {
  background: #ffd700 !important;
}
</style>
