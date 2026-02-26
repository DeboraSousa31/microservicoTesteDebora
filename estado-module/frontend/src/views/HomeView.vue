<template>
  <div class="home-container">
    <div class="card h-100">
      <div class="card-header card-fildset bg-primary">
        <h6 class="text-white">
          <i class="fa-solid fa-clipboard"></i>
          Estado
        </h6>
      </div>
      <div class="card-body">
        <div class="list-group list-group-flush" role="tablist">
          <router-link :to="{ name: 'CadastrarEstado' }" custom v-slot="{ navigate }">
            <div @click="navigate" class="list-group-item list-group-item-action" style="cursor: pointer;">
              <i class="pi pi-plus"></i> Cadastrar
            </div>
          </router-link>

          <router-link :to="{ name: 'PesquisarEstado' }" custom v-slot="{ navigate }">
            <div @click="navigate" class="list-group-item list-group-item-action" style="cursor: pointer;">
              <i class="pi pi-search"></i> Pesquisar
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>

.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  padding: 20px;
}

.home-container .card {
  width: 15rem;
  max-width: 450px;
  text-align: center;
  font-size: 1rem;
  background: #fff;
  color: #2d3748;
}
.home-container .card .card-body {
  color: #2d3748;
}
.home-container .card .list-group-item {
  color: #2d3748;
  border-color: #e2e8f0;
}
.home-container .card .list-group-item:hover {
  background-color: #f0f7ff;
  color: var(--primary-color);
}

</style>
