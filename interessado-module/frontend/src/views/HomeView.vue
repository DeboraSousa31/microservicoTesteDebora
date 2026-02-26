<template>
  <div class="home-container">
    <div class="card h-100">
      <div class="card-header card-fildset bg-primary">
        <h6 class="text-white">
          <i class="fa-solid fa-handshake"></i>
          Interessado
        </h6>
      </div>
      <div class="card-body">
        <div class="list-group list-group-flush" role="tablist">
          <router-link :to="{ name: 'CadastrarInteressado' }" custom v-slot="{ navigate }">
            <div @click="navigate" class="list-group-item list-group-item-action" style="cursor: pointer;">
              <i class="pi pi-plus"></i> Cadastrar
            </div>
          </router-link>

          <router-link :to="{ name: 'PesquisarInteressado' }" custom v-slot="{ navigate }">
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

.card {
  color: white;
  width: 15rem;
  max-width: 450px;
  text-align: center;
  font-size: 1rem;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
}

</style>
