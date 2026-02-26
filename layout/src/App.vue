<template>
  <div class="app-layout">
    <Toast />
    <GlobalToast ref="globalToastRef" />

    <ConfirmDialog />
    <GlobalConfirmDialog ref="globalConfirmDialogRef" />

    <div class="cor-fundo"></div>
    <AppSidebar :open="isSidebarOpen" @update:open="handleSidebarUpdate" />
    <div class="main-content-wrapper" :class="{ 'sidebar-closed': !isSidebarOpen }">
      <AppTopBar @toggle-sidebar="toggleSidebar" :open="isSidebarOpen" />
      <main class="page-content-area">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>

      <!-- Footer -->
      <footer class="content-footer footer bg-footer-theme">
        <div
          class="container-fluid d-flex flex-wrap justify-content-between py-2 flex-md-row flex-column"
        >
          <div class="footer-content">
            <span>Copyright © {{ new Date().getFullYear() }} - Todos os Direitos Reservados</span>
            <a href="http://foxinline.com.s3-website-us-west-2.amazonaws.com/">
              FoxInline Technologies
            </a>
          </div>
        </div>
      </footer>
    </div>

    <!-- Chat Support Button -->
    <a
      id="chat-support-button"
      title="Contate o suporte"
      href="#"
      target="_blank"
      class="chat-support-btn"
    >
      <i class="pi pi-comments" style="font-size: 1.5rem; color: white"></i>
    </a>
  </div>
  <!-- componentes bases para o primevue dos remotes -->
  <div style="display: none">
    <CheckboxGroup />
    <AutoComplete />
    <CascadeSelect />
    <SplitButton />
    <ColorPicker />
    <Checkbox />
    <DatePicker />
    <FloatLabel />
    <InputText />
    <IconField />
    <InputIcon />
    <InputText />
    <IftaLabel />
    <InputGroup />
    <InputGroupAddon />
    <InputNumber />
    <Drawer />
    <RadioButton />
    <ProgressSpinner />
    <InputMask />
    <Editor
          editorStyle="height: 320px;"
        />
    <Textarea
          class="w-100"
          :autoResize="false"
          rows="4"
        />
    <Avatar
                  shape="circle"
                  size="large"
                />

    <DataTable
      :paginator="true"
      stripedRows
      :rowsPerPageOptions="[5, 10, 25, 50]"
      removableSort
      tableStyle="min-width: 10rem"
      editMode="row"
      scrollable
      scrollHeight="600px"
      paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown CurrentPageReport"
      currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} registros"
      dataKey="id"
      lazy
    >
      <Column field="nome" sortable header="Nome"></Column>
    </DataTable>
    <Card />
    <FormComponent />
  </div>
  <!-- componentes bases para o primevue dos remotes -->
</template>

<script setup lang="ts">
// ---- componentes bases para o primevue dos remotes
import InputNumber from 'primevue/inputnumber'
import InputMask from 'primevue/inputmask'
import InputGroup from 'primevue/inputgroup'
import InputGroupAddon from 'primevue/inputgroupaddon'
import IftaLabel from 'primevue/iftalabel'
import IconField from 'primevue/iconfield'
import InputIcon from 'primevue/inputicon'
import DatePicker from 'primevue/datepicker'
import ColorPicker from 'primevue/colorpicker'
import Checkbox from 'primevue/checkbox'
import CheckboxGroup from 'primevue/checkboxgroup'
import AutoComplete from 'primevue/autocomplete'
import CascadeSelect from 'primevue/cascadeselect'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import InputText from 'primevue/inputtext'
import FloatLabel from 'primevue/floatlabel'
import SplitButton from 'primevue/splitbutton'
import Card from 'primevue/card'
import Drawer from 'primevue/drawer'
import RadioButton from 'primevue/radiobutton'
import ProgressSpinner from 'primevue/progressspinner'

import Avatar  from 'primevue/avatar'
import Textarea from 'primevue/textarea'
import Editor from 'primevue/editor'
// ---- componentes bases para o primevue dos remotes

import { ref, onMounted, onUnmounted } from 'vue'
import AppSidebar from './components/AppSidebar.vue'
import AppTopBar from './components/AppTopBar.vue'
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
import GlobalConfirmDialog from './components/GlobalConfirmDialog.vue'
import GlobalToast from './components/GlobalToast.vue'

const isSidebarOpen = ref(true)

const globalConfirmDialogRef = ref<InstanceType<typeof GlobalConfirmDialog> | null>(null)
const globalToastRef = ref<InstanceType<typeof GlobalToast> | null>(null)

function toggleSidebar() {
  isSidebarOpen.value = !isSidebarOpen.value
}

function handleSidebarUpdate(value: boolean) {
  isSidebarOpen.value = value
}

// Função para verificar o tamanho da tela e ajustar a sidebar
function checkScreenSize() {
  if (window.innerWidth <= 990) {
    isSidebarOpen.value = false
  } else {
    isSidebarOpen.value = true
  }
}

onMounted(() => {
  checkScreenSize()
  window.addEventListener('resize', checkScreenSize)

  if (globalConfirmDialogRef.value) {
    window.showHostConfirmDialog = globalConfirmDialogRef.value.showConfirm
  } else {
    console.error(
      'GlobalConfirmDialog não foi montado corretamente. showHostConfirmDialog não está disponível.',
    )
  }

  if (globalToastRef.value) {
    window.showHostToast = globalToastRef.value.showToast
    window.showHostSuccessToast = globalToastRef.value.showSuccessToast
    window.showHostInfoToast = globalToastRef.value.showInfoToast
    window.showHostWarnToast = globalToastRef.value.showWarnToast
    window.showHostErrorToast = globalToastRef.value.showErrorToast
  } else {
    console.error(
      'GlobalToast não foi montado corretamente. Funções de toast não estão disponíveis.',
    )
  }
})

onUnmounted(() => {
  window.removeEventListener('resize', checkScreenSize)

  if (window.showHostConfirmDialog) delete window.showHostConfirmDialog
  if (window.showHostToast) delete window.showHostToast
  if (window.showHostSuccessToast) delete window.showHostSuccessToast
  if (window.showHostInfoToast) delete window.showHostInfoToast
  if (window.showHostWarnToast) delete window.showHostWarnToast
  if (window.showHostErrorToast) delete window.showHostErrorToast
})

declare global {
  interface Window {
    showHostConfirmDialog?: (options: {
      message: string
      header?: string
      icon?: string
      acceptLabel?: string
      rejectLabel?: string
      acceptClass?: string
      rejectClass?: string
      blockScroll?: boolean
      defaultFocus?: 'accept' | 'reject' | 'none'
      group?: string
    }) => Promise<boolean>

    showHostToast?: (options: {
      severity?: 'success' | 'info' | 'warn' | 'error'
      summary?: string
      detail?: string
      life?: number
      group?: string
      closable?: boolean
    }) => void
    showHostSuccessToast?: (summary: string, detail?: string, life?: number) => void
    showHostInfoToast?: (summary: string, detail?: string, life?: number) => void
    showHostWarnToast?: (summary: string, detail?: string, life?: number) => void
    showHostErrorToast?: (summary: string, detail?: string, life?: number) => void
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@100;200;300;400;500;600;700;800;900&display=swap');

/* Garantir que os ícones do PrimeIcons sejam carregados */
@import 'primeicons/primeicons.css';

* {
  font-family: 'Poppins', sans-serif !important;
}

body {
  margin: 0;
  font-family: 'Poppins', sans-serif !important;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f8f9fa;
  color: #495057;
  font-size: 13px;
}

.app-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f8f9fa;
}

.cor-fundo {
  background: radial-gradient(farthest-side at 50% 100%, #1288ff, #06c);
  height: 264px;
  left: 0;
  position: fixed;
  top: 0;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.main-content-wrapper {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-left: 1%;
  position: relative;
  z-index: 2;
}

.main-content-wrapper.sidebar-closed {
  margin-left: 0%;
}

.page-content-area {
  flex-grow: 1;
  padding: 1.5rem;
  overflow-y: auto;
  background-color: transparent;
  margin-top: 60px;
  min-height: calc(100vh - 150px);
}

.content-footer {
  padding: 1rem;
  border-top: 1px solid #dee2e6;
  background-color: #f8f9fa;
}

.footer-content {
  width: 100%;
  text-align: right;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.footer-content span {
  margin-bottom: 0px;
  font-size: 12px;
  color: #6c757d;
}

.footer-content a {
  font-size: 12px;
  color: #3b7ddd;
  text-decoration: none;
}

.footer-content a:hover {
  text-decoration: underline;
}

.chat-support-btn {
  background-color: #3b7ddd;
  width: 60px;
  height: 60px;
  z-index: 9999;
  position: fixed;
  right: 30px;
  bottom: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 1px 1px 10px rgba(108, 117, 125, 0.8);
  text-decoration: none;
  transition: transform 0.2s;
}

.chat-support-btn:hover {
  transform: scale(1.1);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Responsive */
@media (max-width: 990px) {
  .main-content-wrapper {
    margin-left: 0;
  }

  .main-content-wrapper.sidebar-closed {
    margin-left: 0;
  }

  .footer-content {
    align-items: center;
  }
}

/* Utility classes */
.container-fluid {
  width: 100%;
  padding-right: 15px;
  padding-left: 15px;
  margin-right: auto;
  margin-left: auto;
}

.d-flex {
  display: flex;
}

.flex-wrap {
  flex-wrap: wrap;
}

.justify-content-between {
  justify-content: space-between;
}

.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.flex-md-row {
  flex-direction: row;
}

.flex-column {
  flex-direction: column;
}

/* Garantir que os ícones sejam exibidos corretamente */
.pi {
  font-family: 'primeicons' !important;
  font-style: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
