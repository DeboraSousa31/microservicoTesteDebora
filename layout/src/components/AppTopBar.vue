<template>
  <div class="topbar" :class="{ 'sidebar-open': open, 'sidebar-closed': !open }">
    <div class="start-items">
      <Button icon="pi pi-bars" class="menu-toggle-button" @click="toggle" text />
      <div class="search-container">
        <InputText v-model="searchQuery" placeholder="Pesquisar..." class="search-input" />
      </div>
    </div>
    <div class="end-items">
      <div class="coin-display">
        <span class="coin-icon">💰</span>
        <div class="coin-info">
          <span class="coin-value">00,00</span>
          <div class="coin-progress"></div>
        </div>
      </div>
      <Button icon="pi pi-mobile" text rounded class="header-button" />
      <Button icon="pi pi-youtube" text rounded class="header-button" />
      <Button icon="pi pi-bell" text rounded class="header-button" />
      <Button icon="pi pi-cog" text rounded class="header-button" />
    </div>
  </div>

  <!-- Mobile App Dialog -->
  <Dialog v-model:visible="showMobileDialog" header="Baixe o aplicativo através do QR Code" :modal="true"
    :closable="true" style="max-width: 600px">
    <div class="modal-body text-center">
      <div class="qr-placeholder">
        <i class="pi pi-qrcode" style="font-size: 8rem; color: #6c757d;"></i>
        <p>QR Code do Aplicativo</p>
      </div>
    </div>
    <template #footer>
      <Button label="Abrir Link" @click="openMobileLink" />
    </template>
  </Dialog>

  <!-- Video Sidebar -->
  <Sidebar v-model:visible="showVideoSidebar" position="right" style="width: 500px; background: #f8f9fa;">
    <template #header>
      <h3 style="padding: 15px; border-bottom: 1px solid #ddd;">Tutoriais</h3>
    </template>

    <Accordion :multiple="true">
      <AccordionTab header="Geoprocessamento">
        <div style="max-height: 450px; overflow-y: auto;">
          <div v-for="video in videos" :key="video.id" class="video-item mb-2">
            <a href="#" class="video-link">
              <div class="video-thumbnail-container">
                <div class="video-thumbnail">
                  <i class="pi pi-play-circle" style="font-size: 2rem; color: #3b7ddd;"></i>
                </div>
                <div class="video-duration">{{ video.duracao }}</div>
              </div>
              <span class="titulo-video">{{ video.titulo }}</span>
            </a>
          </div>
        </div>
      </AccordionTab>
      <AccordionTab header="Em breve">
        <div style="width: 100%; max-height: 340px; overflow: hidden;">
          <div class="coming-soon-placeholder">
            <i class="pi pi-clock" style="font-size: 4rem; color: #6c757d;"></i>
            <p>Em breve...</p>
          </div>
        </div>
      </AccordionTab>
    </Accordion>
  </Sidebar>

  <OverlayPanel ref="notificationPanel" class="notification-dropdown">
    <div class="dropdown-menu-header d-flex justify-content-between align-items-center px-3 py-2">
      <span>Notificações</span>
      <Button icon="pi pi-ellipsis-v" class="toggle-icons-btn" @click="toggleIconContainer" text size="small" />
    </div>

    <div class="list-group">
      <div class="notification-list">
        <div v-if="notifications.length === 0" class="list-group-item">
          <div class="row g-0 align-items-center">
            <div class="col-2">
              <i class="ms-1 text-info pi pi-info-circle"></i>
            </div>
            <div class="col-10">
              <div class="text-dark">Nenhuma notificação</div>
              <div class="text-muted small mt-1">Você não tem notificações não lidas no momento.</div>
            </div>
          </div>
        </div>

        <div v-else v-for="notification in notifications" :key="notification.id" class="list-group-item">
          <div class="row g-0 align-items-center">
            <div class="col-2">
              <i :class="['ms-1 text-danger', notification.icon]"></i>
            </div>
            <div class="col-10">
              <div class="text-dark">{{ notification.titulo }}</div>
              <div class="text-muted small">{{ notification.assunto }}</div>
              <div class="text-muted small mt-1">{{ notification.descricao }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="dropdown-menu-footer">
      <a href="#" class="text-muted">Cadastrar Despacho</a>
    </div>
    <hr class="my-1" />
    <div class="dropdown-menu-footer">
      <a href="#" class="text-muted">Pesquisar Despachos</a>
    </div>
  </OverlayPanel>

  <Menu ref="settingsMenu" :model="settingsMenuItems" :popup="true" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import ProgressBar from 'primevue/progressbar';
import Badge from 'primevue/badge';
import OverlayPanel from 'primevue/overlaypanel';
import Menu from 'primevue/menu';
import Dialog from 'primevue/dialog';
import Sidebar from 'primevue/sidebar';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';

const emit = defineEmits(['toggle-sidebar']);

const props = defineProps<{
  open: boolean;
}>();

const searchQuery = ref('');
const showMobileDialog = ref(false);
const showVideoSidebar = ref(false);
const notificationPanel = ref();
const settingsMenu = ref();

const notifications = ref([
  {
    id: 1,
    titulo: 'Nova atualização',
    assunto: 'Sistema',
    descricao: 'Sistema atualizado com sucesso',
    icon: 'pi pi-info-circle'
  }
]);

const notificationCount = ref(notifications.value.length);

const videos = ref([
  {
    id: 1,
    titulo: 'Tutorial de Geoprocessamento',
    duracao: '10:30'
  },
  {
    id: 2,
    titulo: 'Como usar o sistema',
    duracao: '15:45'
  }
]);

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
]);

function toggle() {
  emit('toggle-sidebar');
}

function toggleNotifications(event: Event) {
  notificationPanel.value.toggle(event);
}

function toggleSettingsMenu(event: Event) {
  settingsMenu.value.toggle(event);
}

function toggleIconContainer() {
  // Implementar lógica para mostrar/esconder ícones de ação das notificações
}

function openMobileLink() {
  window.open('https://drive.google.com/drive/u/0/mobile/folders/1CwT6nQLtSAzKsje73EQ--JfTAPt_RCIJ?usp=sharing', '_blank');
  showMobileDialog.value = false;
}
</script>

<style scoped>
.topbar {
  background: transparent !important;
  border: none !important;
  position: fixed;
  top: 0;
  left: 0.5rem;
  right: 0;
  z-index: 5;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
  transition: margin-left 0.3s ease;
}

.topbar.sidebar-open {
  margin-left: 244px;
}

.topbar.sidebar-closed {
  margin-left: 80px;
}

.start-items {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.end-items {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.menu-toggle-button {
  color: white !important;
  border: none !important;
  background: transparent !important;
}

.search-container {
  max-width: 400px;
  flex: 1;
}

.search-input {
  width: 100%;
  background: rgba(255, 255, 255, 0.1) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  color: white !important;
  padding: 0.4rem 0.8rem !important;
  border-radius: 4px;
  font-size: 12px !important;
  height: 32px !important;
}

.search-input::placeholder {
  color: rgba(255, 255, 255, 0.7) !important;
}

.coin-display {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-right: 1rem;
}

.coin-icon {
  font-size: 1.5rem;
  color: #ffd700;
}

.coin-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.coin-value {
  font-weight: bold;
  font-size: 0.9rem;
  color: white;
}

.coin-progress {
  width: 80px;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  position: relative;
}

.coin-progress::after {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 25%;
  height: 100%;
  background: #13cb41;
  border-radius: 4px;
}

.header-button {
  color: white !important;
  border: none !important;
  background: transparent !important;
}

.header-button:hover {
  background: rgba(255, 255, 255, 0.1) !important;
}

.notification-dropdown {
  width: 350px;
  max-height: 500px;
}

.dropdown-menu-header {
  padding: 1rem;
  border-bottom: 1px solid #dee2e6;
  font-weight: bold;
  font-size: 12px;
}

.toggle-icons-btn {
  color: #6c757d !important;
}

.notification-list {
  max-height: 450px;
  overflow-y: auto;
}

.list-group-item {
  padding: 1rem;
  border-bottom: 1px solid #f1f1f1;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 11px;
}

.list-group-item:hover {
  background-color: #f8f9fa;
}

.dropdown-menu-footer {
  padding: 0.5rem 1rem;
  border-top: 1px solid #dee2e6;
}

.dropdown-menu-footer a {
  color: #6c757d;
  text-decoration: none;
  font-size: 11px;
}

.qr-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  border: 2px dashed #dee2e6;
  border-radius: 8px;
  background: #f8f9fa;
}

.video-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  margin-bottom: 1rem;
}

.video-link {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #6c757d !important;
  text-decoration: none;
  font-family: 'Poppins' !important;
}

.video-thumbnail-container {
  position: relative;
  width: 120px;
  height: 70px;
  border-radius: 5px;
  overflow: hidden;
}

.video-thumbnail {
  width: 100%;
  height: 100%;
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.video-duration {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  font-size: 10px;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: sans-serif;
}

.titulo-video {
  flex: 1;
  font-size: 11px;
}

.coming-soon-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #6c757d;
}

/* Utility classes */
.d-flex {
  display: flex;
}

.justify-content-between {
  justify-content: space-between;
}

.align-items-center {
  align-items: center;
}

.px-3 {
  padding-left: 0.75rem;
  padding-right: 0.75rem;
}

.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.ms-1 {
  margin-left: 0.25rem;
}

.mt-1 {
  margin-top: 0.25rem;
}

.my-1 {
  margin-top: 0.25rem;
  margin-bottom: 0.25rem;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.text-center {
  text-align: center;
}

.text-info {
  color: #0dcaf0;
}

.text-danger {
  color: #dc3545;
}

.text-dark {
  color: #343a40;
}

.text-muted {
  color: #6c757d;
}

.small {
  font-size: 10px;
}

.row {
  display: flex;
  flex-wrap: wrap;
}

.g-0 {
  margin-right: 0;
  margin-left: 0;
}

.col-2 {
  flex: 0 0 auto;
  width: 16.666667%;
}

.col-10 {
  flex: 0 0 auto;
  width: 83.333333%;
}

/* Responsive */
@media (max-width: 768px) {

  .topbar.sidebar-open,
  .topbar.sidebar-closed {
    margin-left: 0;
  }

  .search-container {
    display: none;
  }

  .coin-display {
    display: none;
  }

  .end-items {
    gap: 0.25rem;
  }
}
</style>
