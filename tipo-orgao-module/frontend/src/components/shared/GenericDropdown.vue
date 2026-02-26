<template>
    <FloatLabel :class="containerClass">
        <Select filter :id="inputId" :modelValue="modelValue" @update:modelValue="emitValue" :options="options"
            :optionLabel="optionLabel" :placeholder="placeholder" :disabled="disabled" :showClear="showClear"
            :class="componentClass" style="width: 100%;" :invalid="props.invalid" :loading="loading" />
        <label :for="inputId">{{ label }}</label>
    </FloatLabel>
</template>

<script setup lang="ts" generic="T extends { id: any }">
import { ref, onMounted } from 'vue';
import Select from 'primevue/select';
import FloatLabel from 'primevue/floatlabel';
import axios from 'axios';

// --- Props ---
const props = withDefaults(defineProps<{
    fetchUrl: string;
    modelValue: T | null;
    label?: string;
    inputId?: string;
    placeholder?: string;
    disabled?: boolean;
    containerClass?: string;
    componentClass?: string;
    optionLabel?: string;
    showClear?: boolean;
    invalid?: boolean;
}>(), {
    // Valores padrão
    label: 'Selecione',
    inputId: 'genericDropdownField',
    placeholder: '',
    optionLabel: 'nome',
    containerClass: 'flex flex-col',
    componentClass: '',
    showClear: true,
    invalid: false,
});

// --- Emits ---
const emit = defineEmits<{
    (e: 'update:modelValue', value: T | null): void;
}>();

// --- Estado Interno ---
const options = ref<T[]>([]);
const loading = ref<boolean>(false);

// --- Lógica para Emissão ---
const emitValue = (value: T | null) => {
    emit('update:modelValue', value);
}

// --- Lógica de Busca de Dados ---
async function carregarOpcoes() {
    if (!props.fetchUrl) {
        console.warn("GenericDropdown: A prop 'fetchUrl' não foi fornecida.");
        return;
    }
    loading.value = true;
    try {
        const response = await axios.get<T[]>(props.fetchUrl);
        options.value = response.data;
    } catch (error) {
        console.error(`Erro ao carregar opções do dropdown da URL: ${props.fetchUrl}`, error);
        options.value = [];
    } finally {
        loading.value = false;
    }
}

onMounted(() => {
    carregarOpcoes();
});
</script>