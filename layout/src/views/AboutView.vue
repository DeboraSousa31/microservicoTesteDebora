<template>
  <div class="configuracao-page">
    <h2 class="mb-4">
      <i class="pi pi-cog me-2"></i>
      Configuração
    </h2>
    <p class="text-muted mb-4">
      Acesse os módulos para pesquisar ou cadastrar.
    </p>

    <div class="row">
      <!-- Estado -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-map me-2"></i>
              Estado
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/estado" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/estado-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Tipo Cômodo -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-th-large me-2"></i>
              Tipo Cômodo
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/tipo-comodo" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/tipo-comodo-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Tipo Órgão -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-sitemap me-2"></i>
              Tipo Órgão
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/tipo-orgao" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/tipo-orgao-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Órgão/Empresa -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-building me-2"></i>
              Órgão/Empresa
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/orgao" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/orgao-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Interessado -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-users me-2"></i>
              Interessado
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/interessado" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/interessado-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Atualização -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-refresh me-2"></i>
              Atualização
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/atualizacao" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/atualizacao-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Status -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-info-circle me-2"></i>
              Status
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/status" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/status-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Situação Jurídica -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-briefcase me-2"></i>
              Situação Jurídica
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/situacao-juridica" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/situacao-juridica-form/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Situação Ocupante -->
      <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
        <div class="card h-100">
          <div class="card-header card-fildset bg-primary">
            <h6 class="text-white mb-0">
              <i class="pi pi-user me-2"></i>
              Situação Ocupante
            </h6>
          </div>
          <div class="card-body p-0">
            <div class="list-group list-group-flush">
              <router-link to="/situacao-ocupante" class="list-group-item list-group-item-action">
                <i class="pi pi-search me-2"></i>
                Pesquisar
              </router-link>
              <router-link to="/situacao-proprietario/novo" class="list-group-item list-group-item-action">
                <i class="pi pi-plus me-2"></i>
                Cadastrar
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.configuracao-page {
  padding: 1rem;
}

.card-header.card-fildset {
  padding: 0.75rem 1rem;
}

.card-header.card-fildset h6 {
  font-size: 1rem;
  font-weight: 600;
}

.list-group-item-action:hover {
  background-color: rgba(59, 125, 221, 0.08);
}
</style>
