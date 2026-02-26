<template>
  <Teleport to="body">
    <Transition name="cerurb-loading-fade">
      <div
        v-show="modelValue"
        class="cerurb-loading-overlay"
        role="status"
        aria-live="polite"
        aria-label="Carregando"
      >
        <div class="cerurb-loading-backdrop" @click.prevent></div>
        <div class="cerurb-loading-card status-dialog">
          <div class="loader-container">
            <div class="loading-animation" :style="{ backgroundImage: `url(${spinnerGif})` }"></div>
            <div class="loading-text">
              {{ message }}<span class="cerurb-loading-dots"></span>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import spinnerGif from '@/assets/spinner.gif'

defineProps<{
  modelValue: boolean
  message?: string
}>()
</script>

<style scoped>
.cerurb-loading-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.cerurb-loading-backdrop {
  position: absolute;
  inset: 0;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(6px);
}

.cerurb-loading-card {
  position: relative;
  background: #fff;
  border-radius: 8%;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  padding: 1.5rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 200px;
  overflow: hidden;
}

.cerurb-loading-card.status-dialog {
  border-radius: 8%;
}

/* Container igual ao Cerurb (layout.xhtml) */
.loader-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 140px;
  pointer-events: none;
}

/* GIF do globo com pins (spinner) – mesmo estilo do Cerurb; URL injetada via :style */
.loading-animation {
  width: 130px;
  height: 100px;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: contain;
  transform: scale(1.2);
  pointer-events: none;
}

/* Texto "Carregando" + pontos animados (estilo layout.xhtml) */
.loading-text {
  font-weight: 700;
  font-size: 1rem;
  color: rgb(59, 125, 221);
  text-align: center;
  margin-top: 0.1rem;
  position: relative;
}

.cerurb-loading-text {
  font-weight: 700;
  font-size: 1rem;
  color: rgb(59, 125, 221);
  text-align: center;
  position: relative;
}

.cerurb-loading-dots::after {
  content: "";
  animation: cerurb-dots 1.2s steps(4, end) infinite;
}

@keyframes cerurb-dots {
  0%,
  25% {
    content: "";
  }
  50% {
    content: ".";
  }
  75% {
    content: "..";
  }
  100% {
    content: "...";
  }
}

/* Transição */
.cerurb-loading-fade-enter-active,
.cerurb-loading-fade-leave-active {
  transition: opacity 0.2s ease;
}

.cerurb-loading-fade-enter-from,
.cerurb-loading-fade-leave-to {
  opacity: 0;
}

.cerurb-loading-fade-enter-active .cerurb-loading-card,
.cerurb-loading-fade-leave-active .cerurb-loading-card {
  transition: transform 0.2s ease;
}

.cerurb-loading-fade-enter-from .cerurb-loading-card,
.cerurb-loading-fade-leave-to .cerurb-loading-card {
  transform: scale(0.96);
}
</style>
