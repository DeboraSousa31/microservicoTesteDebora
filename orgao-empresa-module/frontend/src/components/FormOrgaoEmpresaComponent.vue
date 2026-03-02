<template>
  <VeeForm
    :key="props.formType"
    :validation-schema="veeValidateSchema"
    ref="meuFormularioRef"
    :initialValues="formData"
    class="review-form"
    v-slot="{ values, errors }"
    @submit="enviarDadosParaAPI"
  >
    <div
      v-if="loading"
      class="d-flex justify-content-center align-items-center p-5"
    >
      <ProgressSpinner />
    </div>
    <div v-else>
      <div v-if="props.formType === 'CPF'" class="row">
        <div class="mb-4 col-md-4">
          <VeeField
            name="tipoOrgao"
            v-model="formData.tipoOrgao"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              inputId="tipoOrgao"
              label="Tipo de Órgão*"
              :showClear="true"
              v-model="formData.tipoOrgao"
              v-bind="field"
              :disabled="isViewing"
              :class="{ 'p-invalid': errors.length > 0 }"
              :fetch-url="`${API_BASE_URL}/orgao/tipoOrgao`"
            />
          </VeeField>

          <ErrorMessage name="tipoOrgao" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="nome"
              v-model="formData.nome"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeFisica"
                type="text"
                :disabled="isViewing"
                v-model="formData.nome"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFisica">Nome *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="nome" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="email"
              v-model="formData.email"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="emailFisica"
                type="email"
                :disabled="isViewing"
                v-model="formData.email"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="emailFisica">Email *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="email" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="cpf"
              v-model="formData.cpf"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="cpf"
                class="w-100"
                mask="999.999.999-99"
                :disabled="isViewing"
                slotChar="___.___.___-__"
                v-model="formData.cpf"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="cpf">CPF *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="cpf" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="telefone"
              v-model="formData.telefone"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="telefone"
                class="w-100"
                mask="(99) 99999-9999"
                :disabled="isViewing"
                slotChar="(__) _____-____"
                v-model="formData.telefone"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="telefone">Telefone *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="telefone" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <InputGroup>
            <VeeField
              name="dataNascimento"
              v-model="formData.dataNascimento"
              v-slot="{ field, errors }"
            >
              <FloatLabel>
                <InputMask
                  id="dataNascimento"
                  v-model="formData.dataNascimento"
                  mask="99/99/9999"
                  class="w-100"
                  v-bind="field"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />

                <label for="dataNascimento">Data Nascimento *</label>
              </FloatLabel>

              <Button
                icon="pi pi-calendar"
                aria-label="Abrir Calendário"
                @click="toggleCalendar"
              />
            </VeeField>
          </InputGroup>

          <ErrorMessage name="dataNascimento" class="p-error" />

          <OverlayPanel ref="op">
            <Calendar
              v-model="dateValueForCalendar"
              :inline="true"
              dateFormat="dd/mm/yy"
              showWeek
            />
          </OverlayPanel>
        </div>

        <div class="mb-4 col-md-12">
          <VeeField
            name="idCredenciadaOrgao"
            v-model="formData.idCredenciadaOrgao"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              inputId="credenciadaFisica"
              label="Credenciada *"
              :showClear="true"
              :fetch-url="`${API_BASE_URL}/orgao/credenciadas`"
              v-model="formData.idCredenciadaOrgao"
              v-bind="field"
              :disabled="isViewing"
              option-label="nome"
              :class="{ 'p-invalid': errors.length > 0 }"
            />
          </VeeField>

          <ErrorMessage name="idCredenciadaOrgao" class="p-error" />
        </div>
      </div>

      <!-- encerrando tipo pessoa fisica -->

      <!-- iniciando tipo pessoa juridica -->

      <div v-if="props.formType === 'CNPJ'" class="row">
        <div class="mb-4 col-md-4">
          <VeeField
            name="tipoOrgao"
            v-model="formData.tipoOrgao"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              input-id="tipoOrgao"
              label="Tipo de Órgão *"
              :showClear="true"
              :disabled="isViewing"
              v-model="formData.tipoOrgao"
              :fetch-url="`${API_BASE_URL}/orgao/tipoOrgao`"
              v-bind="field"
              :class="{ 'p-invalid': errors.length > 0 }"
            />
          </VeeField>

          <ErrorMessage name="tipoOrgao" class="p-error" />
        </div>

        <div class="mb-4 col-md-8">
          <FloatLabel>
            <VeeField
              name="nome"
              v-model="formData.nome"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeFantasia"
                v-model="formData.nome"
                :disabled="isViewing"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="nomeFantasia">Nome *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="nome" class="p-error" />
        </div>

        <div
          class="mb-4 col-md-4"
          v-if="
            (formData.tipoOrgao?.nome ?? '').trim() === 'PREFEITURA MUNICIPAL'
          "
        >
          <FloatLabel>
            <VeeField
              name="nomePrefeito"
              v-model="formData.nomePrefeito"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomePrefeito"
                v-bind="field"
                v-model="formData.nomePrefeito"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />
              <label for="nomePrefeito">Nome do Prefeito *</label>
            </VeeField>
          </FloatLabel>
          <ErrorMessage name="nomePrefeito" class="p-error" />
        </div>

        <template v-if="(formData.tipoOrgao?.nome ?? '').trim() === 'Cartório'">
          <div class="mb-4 col-md-4">
            <FloatLabel>
              <VeeField
                name="nomeTabeliao"
                v-model="formData.nomeTabeliao"
                v-slot="{ field, errors }"
              >
                <InputText
                  class="w-100"
                  id="nomeTabeliao"
                  v-bind="field"
                  v-model="formData.nomeTabeliao"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />
                <label for="nomeTabeliao">Nome do Tabelião *</label>
              </VeeField>
            </FloatLabel>
            <ErrorMessage name="nomeTabeliao" class="p-error" />
          </div>

          <div class="mb-4 col-md-4">
            <FloatLabel>
              <VeeField
                name="codigoIBGEServentia"
                v-model="formData.codigoIBGEServentia"
                v-slot="{ field, errors }"
              >
                <InputText
                  class="w-100"
                  id="codigoIBGEServentia"
                  v-bind="field"
                  v-model="formData.codigoIBGEServentia"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />
                <label for="codigoIBGEServentia">Código IBGE Serventia *</label>
              </VeeField>
            </FloatLabel>
            <ErrorMessage name="codigoIBGEServentia" class="p-error" />
          </div>

          <div class="mb-4 col-md-4">
            <FloatLabel>
              <VeeField
                name="codigoServentia"
                v-model="formData.codigoServentia"
                v-slot="{ field, errors }"
              >
                <InputText
                  class="w-100"
                  id="codigoServentia"
                  v-bind="field"
                  v-model="formData.codigoServentia"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />
                <label for="codigoServentia">Código Serventia *</label>
              </VeeField>
            </FloatLabel>
            <ErrorMessage name="codigoServentia" class="p-error" />
          </div>

          <div class="mb-4 col-md-4">
            <FloatLabel>
              <VeeField
                name="oficioServentia"
                v-model="formData.oficioServentia"
                v-slot="{ field, errors }"
              >
                <InputText
                  class="w-100"
                  id="oficioServentia"
                  v-bind="field"
                  v-model="formData.oficioServentia"
                  :disabled="isViewing"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />
                <label for="oficioServentia">Ofício Serventia *</label>
              </VeeField>
            </FloatLabel>
            <ErrorMessage name="oficioServentia" class="p-error" />
          </div>
        </template>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="email"
              v-model="formData.email"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="emailJuridico"
                type="email"
                :disabled="isViewing"
                v-model="formData.email"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="emailJuridico">Email *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="email" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <FloatLabel>
            <VeeField
              name="cnpj"
              v-model="formData.cnpj"
              v-slot="{ field, errors }"
            >
              <InputMask
                id="cnpj"
                class="w-100"
                mask="99.999.999/9999-99"
                v-model="formData.cnpj"
                v-bind="field"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="cnpj">CNPJ *</label>
            </VeeField>
          </FloatLabel>

          <ErrorMessage name="cnpj" class="p-error" />
        </div>

        <div
          v-if="formData.tipoOrgao?.nome === 'Empresa' || null || ''"
          :class="[
            formData.tipoOrgao?.nome === 'Empresa'
              ? 'col-md-2 mb-2'
              : 'col-md-4 mb-4',
          ]"
        >
          <FloatLabel>
            <VeeField
              name="razaoSocial"
              v-model="formData.razaoSocial"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="razaoSocial"
                v-bind="field"
                v-model="formData.razaoSocial"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />
              <label for="razaoSocial">Razão Social *</label>
            </VeeField>
          </FloatLabel>
          <ErrorMessage name="razaoSocial" class="p-error" />
        </div>

        <div
          class="mb-4 col-md-2"
          v-if="formData.tipoOrgao?.nome === 'Empresa'"
        >
          <FloatLabel>
            <VeeField
              name="nomeResponsavel"
              v-model="formData.nomeResponsavel"
              v-slot="{ field, errors }"
            >
              <InputText
                class="w-100"
                id="nomeResponsavel"
                v-bind="field"
                v-model="formData.nomeResponsavel"
                :disabled="isViewing"
                :class="{ 'p-invalid': errors.length > 0 }"
              />
              <label for="nomeResponsavel">Responsável *</label>
            </VeeField>
          </FloatLabel>
          <ErrorMessage name="nomeResponsavel" class="p-error" />
        </div>

        <!-- Campos extras conforme o tipo -->

        <div class="mb-4 col-md-12">
          <VeeField
            name="idCredenciadaOrgao"
            v-model="formData.idCredenciadaOrgao"
            v-slot="{ field, errors }"
          >
            <GenericDropdown
              input-id="credenciadaJuridica"
              label="Credenciada *"
              :showClear="true"
              :fetch-url="`${API_BASE_URL}/orgao/credenciadas`"
              v-model="formData.idCredenciadaOrgao"
              :disabled="isViewing"
              option-label="nome"
              v-bind="field"
              :class="{ 'p-invalid': errors.length > 0 }"
            />
          </VeeField>
          <ErrorMessage name="idCredenciadaOrgao" class="p-error" />
        </div>
      </div>

      <!-- -------------------------------------------------- -->

      <!------------------- endereco -------------------------->

      <h6 class="mt-4">Endereço</h6>

      <hr class="mb-5" />

      <div class="row ">
        <!-- <pre
          style="background-color: #eee; padding: 10px; border-radius: 5px"
          >{{ values }}</pre
        >
        <pre style="background-color: #fdd">Erros: {{ errors }}</pre> -->

        <div class="col-md-4">
          <VeeField
            name="endereco.cep"
            v-model="formData.endereco.cep"
            v-slot="{ field, errors }"
          >
            <InputGroup>
              <FloatLabel>
                <InputMask
                  id="cepImovelInputMask"
                  v-model="formData.endereco.cep"
                  mask="99999-999"
                  :disabled="isViewing"
                  class="w-100"
                  v-bind="field"
                  :class="{ 'p-invalid': errors.length > 0 }"
                />

                <label for="cepImovelInputMask">CEP</label>
              </FloatLabel>

              <Button
                icon="pi pi-search"
                title="Pesquisar Endereço"
                :disabled="isViewing"
                :loading="loading"
                @click="pesquisarCepAPI"
              />
            </InputGroup>
          </VeeField>

          <ErrorMessage name="endereco.cep" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <VeeField
            name="endereco.logradouro"
            v-model="formData.endereco.logradouro"
            v-slot="{ field, errors }"
          >
            <FloatLabel>
              <InputText
                class="w-100"
                id="logradouro"
                :disabled="isViewing"
                v-model="formData.endereco.logradouro"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="logradouro">Logradouro *</label>
            </FloatLabel>
          </VeeField>

          <ErrorMessage name="endereco.logradouro" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <VeeField
            name="endereco.numero"
            v-model="formData.endereco.numero"
            v-slot="{ field, errors }"
          >
            <FloatLabel>
              <InputText
                class="w-100"
                id="numero"
                :disabled="isViewing"
                v-model="formData.endereco.numero"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="numero">Número *</label></FloatLabel
            >
          </VeeField>

          <ErrorMessage name="endereco.numero" class="p-error" />
        </div>
        <div class="mb-4 col-md-3">
          <VeeField
            name="endereco.cidade"
            v-slot="{ errors, value, handleChange }"
          >
            <FloatLabel>
              <AutoComplete
                id="cidade"
                class="w-100"
                :disabled="isViewing"
                :suggestions="cidadesSugeridas"
                @complete="buscarCidades"
                @item-select="onCidadeSelect"
                :class="{ 'p-invalid': errors.length > 0 }"
                optionLabel="nome"
                dropdown
                v-model="formData.endereco.cidade"
                @update:modelValue="
                  (event) =>
                    handleChange(
                      event && event.value !== undefined ? event.value : event
                    )
                "
              >
              </AutoComplete>
              <label for="cidade">Cidade *</label>
            </FloatLabel>
          </VeeField>
          <ErrorMessage name="endereco.cidade" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <VeeField
            name="endereco.bairro"
            v-slot="{ errors, value, handleChange }"
          >
            <FloatLabel>
              <AutoComplete
                id="bairro"
                class="w-100"
                :disabled="isViewing"
                :suggestions="bairrosSugeridos"
                @complete="buscarBairros"
                @item-select="onBairroSelect"
                :class="{ 'p-invalid': errors.length > 0 }"
                option-label="nome"
                dropdown
                v-model="formData.endereco.bairro"
                @update:modelValue="
                  (event) =>
                    handleChange(
                      event && event.value !== undefined ? event.value : event
                    )
                "
              />
              <label for="bairro">Bairro *</label>
            </FloatLabel>
          </VeeField>
          <ErrorMessage name="endereco.bairro" class="p-error" />
        </div>

        <div class="mb-4 col-md-1">
          <VeeField
            name="endereco.ufEstado"
            v-model="formData.endereco.ufEstado"
            v-slot="{ field, errors }"
          >
            <FloatLabel>
              <InputText
                id="estado"
                class="w-100"
                v-bind="field"
                :disabled="isViewing"
                v-model="formData.endereco.ufEstado"
                :class="{ 'p-invalid': errors.length > 0 }"
              />
              <label for="estado">Estado *</label>
            </FloatLabel>
          </VeeField>
          <ErrorMessage name="endereco.uf" class="p-error" />
        </div>

        <div class="mb-4 col-md-4">
          <VeeField
            name="endereco.complemento"
            v-model="formData.endereco.complemento"
            v-slot="{ field, errors }"
          >
            <FloatLabel>
              <InputText
                class="w-100"
                id="complemento"
                :disabled="isViewing"
                v-model="formData.endereco.complemento"
                v-bind="field"
                :class="{ 'p-invalid': errors.length > 0 }"
              />

              <label for="complemento">Complemento</label></FloatLabel
            >
          </VeeField>
          <ErrorMessage name="endereco.complemento" class="p-error" />
        </div>

        <div class="col-md-12 mt-5 text-center action-buttons-form">
          <Button
            v-if="!isViewing"
            class="px-3 mx-2"
            type="submit"
            icon="pi pi-save"
            severity="info"
            :label="computedButtonLabel"
            :loading="loading"
            raised
          />
        </div>
      </div>
    </div>
  </VeeForm>
</template>

<script setup lang="ts">
import "bootstrap/dist/css/bootstrap.min.css";
import "@/assets/base.css";
import "@/assets/main.css";
import GenericDropdown from "./shared/GenericDropdown.vue"; // Ajuste o caminho se necessário
import FloatLabel from "primevue/floatlabel";
import {
  Form as VeeForm,
  Field as VeeField,
  ErrorMessage,
  useForm,
} from "vee-validate";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import InputMask from "primevue/inputmask";
import { ref, watch, computed, nextTick } from "vue";
import InputGroup from "primevue/inputgroup";
import OverlayPanel from "primevue/overlaypanel";
import Calendar from "primevue/calendar";
import AutoComplete from "primevue/autocomplete";
import axios from "axios";
import { z } from "zod";
import { toTypedSchema } from "@vee-validate/zod";
import type { RefSymbol } from "@vue/reactivity";
import { veeValidateSchema } from "@/schemas/orgaoSchema";
import ProgressSpinner from "primevue/progressspinner";

const props = withDefaults(defineProps<{
  mode: "cadastrar" | "editar" | "visualizar";
  situacaoId?: number | null;
  formType: "CPF" | "CNPJ";
  optionValue?: string;
  modelValue?: number | string | object | null;
}>(), {
  optionValue: "",
  modelValue: undefined,
});

const limparMascara = (valor: string | null | undefined): string => {
  return valor ? valor.replace(/\D/g, "") : "";
};

interface Credenciada {
  id: number;
  nome: string;
}

interface TipoDocumento {
  id: number;
  nome: string;
}

interface Estado {
  id: number;
  nome: string;
  uf: string;
}

interface Cidade {
  id: number | null;
  nome: string;
  estado: Estado | null;
}

interface Bairro {
  id: number | null;
  nome: string;
  cidade: Cidade | null;
}

interface TipoLogradouro {
  id: number;
  nome: string;
}

interface TipoOrgao {
  id: number;
  nome: string;
}

interface Endereco {
  id?: number;
  cep: string;
  logradouro: string;
  numero: string;
  complemento: string;
  bairroId: number | null;
  cidadeId: number | null;
  tipoLogradouro: TipoLogradouro | null;
  cidade?: Cidade | string | null;
  bairro?: Bairro | string | null;
  nomeCidade: string;
  bairroNome: string;
  codigoIbge: string;
  nomeEstado: string;
  ufEstado: string;
}

type FormData = {
  id?: number | null;
  nome: string;
  razaoSocial: string;
  telefone: string;
  tipoDocumento: TipoDocumento | string | null;
  endereco: Endereco;
  tipoOrgao: TipoOrgao | null;
  dataNascimento: string;
  cpf: string;
  cnpj: string;
  email: string;
  nomePrefeito: string;
  nomeResponsavel: string;
  codigoIBGEServentia: string;
  codigoServentia: string;
  oficioServentia: string;
  nomeTabeliao: string;
  idCredenciadaOrgao: Credenciada | null;
  idCredenciadaUsuario: number;
  idUsuario: number;
};

const formData = ref<FormData>({
  id: null,
  nome: "",
  razaoSocial: "",
  telefone: "",
  tipoDocumento: null,
  endereco: {
    cep: "",
    logradouro: "",
    numero: "",
    complemento: "",
    bairroId: null,
    cidadeId: null,
    tipoLogradouro: null,
    cidade: {
      id: null,
      nome: "",
      estado: null,
    },
    bairro: {
      id: null,
      nome: "",
      cidade: null,
    },
    nomeCidade: "",
    bairroNome: "",
    codigoIbge: "",
    nomeEstado: "",
    ufEstado: "",
  },

  tipoOrgao: null,
  dataNascimento: "",
  cpf: "",
  cnpj: "",
  email: "",
  nomePrefeito: "",
  nomeResponsavel: "",
  codigoIBGEServentia: "",
  codigoServentia: "",
  oficioServentia: "",
  nomeTabeliao: "",
  idCredenciadaOrgao: null,
  idCredenciadaUsuario: 53291,
  idUsuario: 454,
});
const isViewing = computed(() => props.mode === "visualizar");
const activeValidationSchema = computed(() => {
  return isViewing.value ? undefined : toTypedSchema(veeValidateSchema as unknown as import('zod').ZodType<any>);
});

const {
  handleSubmit,
  setValues,
  setFieldValue,
  values,
  resetForm,
  validateField,
} = useForm({
  validationSchema: activeValidationSchema as unknown as Record<string, unknown>,
  //validationSchema: veeValidateSchema,
  initialValues: {
    formType: props.formType,
    tipoDocumento: props.formType,
    nome: "",
    email: "",
    cpf: "",
    cnpj: "",
    telefone: "",
    dataNascimento: "",
    tipoOrgao: null,
    idCredenciadaOrgao: null,
    razaoSocial: "",
    nomeResponsavel: "",
    nomePrefeito: "",
    nomeTabeliao: "",
    codigoIBGEServentia: "",
    codigoServentia: "",
    oficioServentia: "",
    endereco: {
      cep: "",
      logradouro: "",
      numero: "",
      complemento: "",
      cidade: null,
      bairro: null,
    },
  },
});

const emit = defineEmits(["form-submitted", "form-cancelled"]);
// const API_BASE_URL = import.meta.env.DEV
//   ? "/api"
//   : import.meta.env.VITE_API_BASE_URL;
const API_BASE_URL = `/api/orgao-empresa`;

const cidadeId = computed(() => formData.value.endereco?.cidadeId);
const isEditing = computed(() => props.mode === "editar");
const isCreating = computed(() => props.mode === "cadastrar");
const loading = ref<boolean>(false);
const computedButtonLabel = computed(() => {
  return isEditing.value ? "Atualizar" : "Salvar";
});

function parseDateString(dateStr: string): Date | null {
  if (!dateStr || !/^\d{2}\/\d{2}\/\d{4}$/.test(dateStr)) {
    return null;
  }
  const [day, month, year] = dateStr.split("/").map(Number);
  return new Date(year, month - 1, day);
}

async function carregarDados(id: number) {
  loading.value = true;
  try {
    const response = await axios.get(`${API_BASE_URL}/orgao/${id}`);

    if (response.status >= 200 && response.status < 300) {
      const dadosApi = response.data;
      console.log("Dados recebidos da API:", dadosApi);
      formData.value.id = dadosApi.id;
      formData.value.nome = dadosApi.nome || "";
      formData.value.razaoSocial = dadosApi.razaoSocial || "";
      formData.value.email = dadosApi.email || "";
      formData.value.telefone = dadosApi.telefone || "";
      formData.value.cnpj = dadosApi.cnpj || "";
      formData.value.cpf = dadosApi.cpf || "";
      formData.value.tipoDocumento = dadosApi.tipoDocumento;
      formData.value.nomeResponsavel = dadosApi.nomeResponsavel;

      setValues({
        formType: props.formType,
        ...dadosApi,
        endereco: dadosApi.endereco || values.endereco,
      });

      if (dadosApi.dataNascimento) {
        formData.value.dataNascimento = formatarDataParaInput(
          dadosApi.dataNascimento
        );
        dateValueForCalendar.value = parseDateString(dadosApi.dataNascimento);
      }

      formData.value.tipoOrgao = dadosApi.tipoOrgao || null;
      //formData.value.idCredenciadaOrgao = dadosApi.credenciada || null;

      if (dadosApi.idCredenciadaOrgao && dadosApi.nomeCredenciadaOrgao) {
        formData.value.idCredenciadaOrgao = {
          id: dadosApi.idCredenciadaOrgao,
          nome: dadosApi.nomeCredenciadaOrgao,
        };
      } else {
        formData.value.idCredenciadaOrgao = null;
      }

      if (dadosApi.endereco) {
        formData.value.endereco.id = dadosApi.endereco.id;
        formData.value.endereco.cep = dadosApi.endereco.cep || "";
        formData.value.endereco.logradouro = dadosApi.endereco.logradouro || "";
        formData.value.endereco.numero = dadosApi.endereco.numero || "";
        formData.value.endereco.complemento =
          dadosApi.endereco.complemento || "";
          
          if (dadosApi.endereco.cidade) {
            formData.value.endereco.cidade = dadosApi.endereco.cidade;
            setFieldValue("endereco.cidade", dadosApi.cidade);
          }
          
          if (dadosApi.endereco.bairro) {
            formData.value.endereco.bairro = dadosApi.endereco.bairro;
            formData.value.endereco.ufEstado =
              dadosApi.endereco.bairro.ufEstado || "";
          setFieldValue("endereco.bairro", dadosApi.bairro || null);
        }
      } else {
        formData.value.endereco = {
          cep: "",
          logradouro: "",
          numero: "",
          complemento: "",
          bairroId: null,
          cidadeId: null,
          tipoLogradouro: null,
          cidade: null,
          bairro: null,
          nomeCidade: "",
          bairroNome: "",
          codigoIbge: "",
          nomeEstado: "",
          ufEstado: "",
        };
      }
    } else {
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro!",
          detail: "Falha ao tentar obter dados.",
        });
      }
      console.warn("Requisição retornou status não esperado:", response.status);
      emit("form-cancelled");
    }
  } catch (error) {
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro!",
        detail: "Falha ao tentar obter dados.",
      });
    }
    console.error("Erro ao editar item da API:", error);
    emit("form-cancelled");
  } finally {
    loading.value = false;
  }
}

async function confirmarEnvio() {
  const acao = isEditing.value ? "atualizar" : "cadastrar";
  const acaoLabel = isEditing.value ? "Atualizar" : "Salvar";

  if (window.showHostConfirmDialog) {
    try {
      const confirmed = await window.showHostConfirmDialog({
        message: `Tem certeza que deseja ${acao} este Órgão/Empresa?`,
        header: `Confirmar ${acaoLabel}`,
        icon: "pi pi-exclamation-triangle",
        acceptLabel: `Sim, ${acaoLabel}`,
        rejectLabel: "Cancelar",
        acceptClass: "p-button-danger",
      });

      if (confirmed) {
        await enviarDadosParaAPI();
      } else {
        if (window.showHostToast) {
          window.showHostToast({
            severity: "info",
            summary: "Info",
            detail: `Operação de ${acao} cancelada.`,
          });
        }
      }
    } catch (error) {
      console.error("Erro ao chamar o ConfirmDialog do Host:", error);
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro Crítico",
          detail: "Não foi possível exibir o diálogo de confirmação.",
        });
      }
    }
  } else {
    console.warn(
      "`window.showHostConfirmDialog` não está disponível. O host pode não ter sido carregado corretamente ou a função não foi exposta."
    );
  }
}

async function enviarDadosParaAPI() {
  if (isViewing.value) return;

  loading.value = true;

  const url: string = `${API_BASE_URL}/orgao`;
  const dataToSend: any = {
    id: isEditing.value ? props.situacaoId : undefined,
    email: formData.value.email,
    telefone: limparMascara(formData.value.telefone),
    idTipoOrgao: formData.value.tipoOrgao?.id ?? null,
    idCredenciadaOrgao: formData.value.idCredenciadaOrgao?.id ?? null,
    idCredenciadaUsuario: 53291,
    idUsuario: 454,
  };

  if (formData.value.endereco) {
   
    dataToSend.endereco = {
      id: formData.value.endereco.id,
      cep: limparMascara(formData.value.endereco.cep),
      logradouro: formData.value.endereco.logradouro,
      numero: formData.value.endereco.numero,
      complemento: formData.value.endereco.complemento,
      idCidade: (typeof formData.value.endereco.cidade === 'object' && formData.value.endereco.cidade && 'id' in formData.value.endereco.cidade) ? formData.value.endereco.cidade.id ?? null : null,
      idBairro: (typeof formData.value.endereco.bairro === 'object' && formData.value.endereco.bairro && 'id' in formData.value.endereco.bairro) ? formData.value.endereco.bairro.id ?? null : null,
      bairroNome:
        typeof formData.value.endereco.bairro === "string"
          ? formData.value.endereco.bairro
          : formData.value.endereco.bairro?.nome,
      nomeCidade:
        typeof formData.value.endereco.cidade === "string"
          ? formData.value.endereco.cidade
          : formData.value.endereco.cidade?.nome,
      codigoIbge: formData.value.endereco.codigoIbge,
      nomeEstado: formData.value.endereco.nomeEstado,
      ufEstado: formData.value.endereco.ufEstado,
      //cidadeId:formData.value.endereco.cidade?.id,
      //nomeCidade: typeof formData.value.endereco.cidade === 'string' ? formData.value.endereco.cidade : (typeof formData.value.endereco.cidade === 'object' ? formData.value.endereco.cidade.nome : null),
      //bairroId:formData.value.endereco.bairro?.id ,
      //nomeBairro: typeof formData.value.endereco.bairro === 'string' ? formData.value.endereco.bairro : (typeof formData.value.endereco.bairro === 'object' ? formData.value.endereco.bairro.nome : null),

      // tipoLogradouroId: formData.value.endereco.tipoLogradouro?.id ?? null,
    };
  }

  if (props.formType === "CPF") {
    dataToSend.nome = formData.value.nome;
    dataToSend.cpf = limparMascara(formData.value.cpf);
    dataToSend.dataNascimento = formatarDataParaAPI(
      formData.value.dataNascimento
    );
    dataToSend.tipoDocumento = "CPF";
  } else if (props.formType === "CNPJ") {
    dataToSend.nome = formData.value.nome;
    dataToSend.razaoSocial = formData.value.razaoSocial;
    dataToSend.cnpj = limparMascara(formData.value.cnpj);
    dataToSend.nomePrefeito = formData.value.nomePrefeito;
    dataToSend.nomeTabeliao = formData.value.nomeTabeliao;
    dataToSend.nomeResponsavel = formData.value.nomeResponsavel;
    dataToSend.codigoIBGEServentia = formData.value.codigoIBGEServentia;
    dataToSend.codigoServentia = formData.value.codigoServentia;
    dataToSend.oficioServentia = formData.value.oficioServentia;
    dataToSend.tipoDocumento = "CNPJ";
  }

  if (isCreating.value) {
    delete dataToSend.id;
  }

  try {
    let response;
    let idRetornado: number | undefined = undefined;

    if (isEditing.value && props.situacaoId) {
      response = await axios.put(`${url}/${props.situacaoId}`, dataToSend);
      idRetornado = props.situacaoId;
    } else {
      response = await axios.post(url, dataToSend);
      idRetornado = response.data.id;
    }

    if (response && response.status >= 200 && response.status < 300) {
      console.log("Dados enviados com sucesso!");
      emit("form-submitted", { id: idRetornado, mode: props.mode });
      if (isCreating.value) {
        formData.value = {
          id: null,
          nome: "",
          razaoSocial: "",
          telefone: "",
          tipoDocumento: props.formType,
          endereco: {
            cep: "",
            logradouro: "",
            numero: "",
            complemento: "",
            bairroId: null,
            cidadeId: null,
            tipoLogradouro: null,
            cidade: {
              id: null,
              nome: "",
              estado: null,
            },
            bairro: {
              id: null,
              nome: "",
              cidade: null,
            },
            nomeCidade: "",
            bairroNome: "",
            codigoIbge: "",
            nomeEstado: "",
            ufEstado: "",
          },
          tipoOrgao: null,
          dataNascimento: "",
          cpf: "",
          cnpj: "",
          email: "",
          nomePrefeito: "",
          nomeResponsavel: "",
          codigoIBGEServentia: "",
          codigoServentia: "",
          oficioServentia: "",
          nomeTabeliao: "",
          idCredenciadaOrgao: null,
          idCredenciadaUsuario: 53291,
          idUsuario: 454,
        };
      }
      if (window.showHostToast) {
        window.showHostToast({
          severity: "success",
          summary: "Sucesso",
          detail: `Situação Jurídica ${isEditing.value ? "atualizado" : "cadastrado"}`,
        });
      }
    } else {
      console.error("Erro na resposta da API:", response);
      if (window.showHostToast) {
        window.showHostToast({
          severity: "error",
          summary: "Erro",
          detail: `Erro ao tentar ${isEditing.value ? "atualizar" : "cadastrar"}`,
        });
      }
      console.error("Response recebido: " + response?.status);
    }
  } catch (error) {
    console.error("Falha ao enviar dados:", error);
    const action = isEditing.value ? "atualizar" : "cadastrar";
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: `Não foi possível ${action} o Órgão. Tente novamente.`,
      });
    }

    console.error(`Problema ao ${action} dados:`, error);
  } finally {
    loading.value = false;
  }
}

watch(
  () => props.mode,

  (newMode) => {
    if (newMode === "cadastrar") {
      formData.value = {
        id: null,
        nome: "",
        razaoSocial: "",
        telefone: "",
        tipoDocumento: props.formType,
        endereco: {
          cep: "",
          logradouro: "",
          numero: "",
          complemento: "",
          bairroId: null,
          cidadeId: null,
          tipoLogradouro: null,
          cidade: {
            id: null,
            nome: "",
            estado: null,
          },
          bairro: {
            id: null,
            nome: "",
            cidade: null,
          },
          nomeCidade: "",
          bairroNome: "",
          codigoIbge: "",
          nomeEstado: "",
          ufEstado: "",
        },
        tipoOrgao: null,
        dataNascimento: "",
        cpf: "",
        cnpj: "",
        email: "",
        nomePrefeito: "",
        nomeResponsavel: "",
        codigoIBGEServentia: "",
        codigoServentia: "",
        oficioServentia: "",
        nomeTabeliao: "",
        idCredenciadaOrgao: null,
        idCredenciadaUsuario: 53291,
        idUsuario: 454,
      };
    } else if (
      (newMode === "editar" || newMode === "visualizar") &&
      props.situacaoId != null
    ) {
      carregarDados(props.situacaoId);
    }
  },
  { immediate: true }
);

watch(
  () => props.situacaoId,
  (newId, oldId) => {
    if (
      newId != null &&
      (props.mode === "editar" || props.mode === "visualizar") &&
      newId !== oldId
    ) {
      carregarDados(newId);
    }
  }
);

const op = ref();
const dateValueForCalendar = ref<Date | null>(null);
const formattedDateForInput = ref<string | undefined>("");
const toggleCalendar = (event: Event) => {
  op.value.toggle(event);
};

watch(dateValueForCalendar, (newDate) => {
  if (newDate) {
    formData.value.dataNascimento = newDate.toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
    op.value.hide();
  }
});

const cidadesSugeridas = ref([]);
const bairrosSugeridos = ref([]);

const buscarCidades = async (event: { query?: string }) => {
  const query = event.query;
  if (!query || query.trim().length === 0) {
    cidadesSugeridas.value = [];
    return;
  }

  try {
    const response = await axios.get(`${API_BASE_URL}/orgao/cidades`, {
      params: { nome: query },
    });

    cidadesSugeridas.value = response.data || [];
  } catch (error) {
    console.error("Erro ao buscar cidades:", error);
    cidadesSugeridas.value = [];
  }
};
const buscarBairros = async (event: { query?: string }) => {

  const cidadeSelecionada = formData.value.endereco?.cidade;


  //const cidadeSelecionada = values.endereco.cidade;

  if (typeof cidadeSelecionada !== "object" || !cidadeSelecionada?.id) {
  
    bairrosSugeridos.value = [];
    return;
  }
  const cidadeId = cidadeSelecionada.id;
  const query = event.query || "";

  try {
    const response = await axios.get(
      `${API_BASE_URL}/orgao/cidades/${cidadeId}/bairros`,
      {
        params: { nome: query },
      }
    );
    bairrosSugeridos.value = response.data || [];
  } catch (error) {
    console.error("Erro ao buscar bairros:", error);
    bairrosSugeridos.value = [];
  }
};

const onCidadeSelect = (event: any) => {
  formData.value.endereco.cidade = event.value;
  formData.value.endereco.bairro = null;
  setFieldValue("endereco.bairro", null);
  bairrosSugeridos.value = [];
};
const onBairroSelect = (event: any) => {
  const bairroSelecionado = event.value;
  const cidadeSelecionada = formData.value.endereco.cidade;

  if (!bairroSelecionado) return;

  if (bairroSelecionado.idCidade && bairroSelecionado.nomeCidade) {
    const cidadeDoBairro = {
      id: bairroSelecionado.idCidade,
      nome: bairroSelecionado.nomeCidade,
      ufEstado: bairroSelecionado.ufEstado,
      nomeEstado: bairroSelecionado.nomeEstado,
    };
    formData.value.endereco = {
      ...formData.value.endereco,
      ufEstado: cidadeDoBairro.ufEstado || "",
      codigoIbge: (cidadeSelecionada && typeof cidadeSelecionada === 'object' && 'codigoIbge' in cidadeSelecionada) ? (cidadeSelecionada as { codigoIbge: string }).codigoIbge : '',
      nomeEstado: cidadeDoBairro.nomeEstado,
      nomeCidade: bairroSelecionado.nomeCidade,
      cidadeId: bairroSelecionado.idCidade,
      bairroId: bairroSelecionado.id,
      bairroNome: bairroSelecionado.nome,
    } as Endereco;
    (setFieldValue as (n: string, v: unknown) => void)("endereco.cidade", cidadeDoBairro);
  }

  if (bairroSelecionado.ufEstado) {
    (setFieldValue as (n: string, v: unknown) => void)("endereco.ufEstado", bairroSelecionado.ufEstado);
  }
};

const pesquisarCepAPI = async () => {
  if (!formData.value.endereco) return;

  const cep = formData.value.endereco.cep;
  if (!cep || limparMascara(cep).length !== 8) {
    console.warn("CEP inválido ou não informado.");
    return;
  }

  loading.value = true;
  try {
    const cepLimpo = limparMascara(cep);
    const responseViaCEP = await fetch(
      `https://viacep.com.br/ws/${cepLimpo}/json/`
    );
    const dadosViaCEP = await responseViaCEP.json();

    if (dadosViaCEP.erro) {
      throw new Error("CEP não encontrado na base externa.");
    }
    setFieldValue("endereco.logradouro", dadosViaCEP.logradouro);
    setFieldValue("endereco.complemento", dadosViaCEP.complemento);
    try {
      const params = new URLSearchParams({
        codigoIbge: dadosViaCEP.ibge,
        bairroNome: dadosViaCEP.bairro,
      });
      const responseSeuBackend = await fetch(
        `${API_BASE_URL}/orgao/lookup?${params.toString()}`
      );

      if (!responseSeuBackend.ok) {
        throw new Error(
          "Backend respondeu com erro: " + responseSeuBackend.status
        );
      }

      const dadosDoSeuDB = await responseSeuBackend.json();

      if (dadosDoSeuDB && dadosDoSeuDB.cidade) {
        console.log(
          "Dados encontrados e enriquecidos pelo DB local:",
          dadosDoSeuDB
        );

        formData.value.endereco = {
          ...formData.value.endereco,
          logradouro: dadosViaCEP.logradouro ?? '',
          complemento: dadosViaCEP.complemento ?? '',
          cidade: dadosDoSeuDB.cidade,
          bairro: dadosDoSeuDB.bairro || null,
          ufEstado: dadosDoSeuDB.bairro?.ufEstado || "",
          codigoIbge: dadosDoSeuDB.cidade?.codigoIbge || dadosViaCEP.ibge || "",
          nomeEstado: dadosDoSeuDB.bairro.nomeEstado,
          nomeCidade: dadosDoSeuDB.bairro.nomeCidade,
          cidadeId: dadosDoSeuDB.bairro.idCidade ?? null,
          bairroId: dadosDoSeuDB.bairro.id ?? null,
          bairroNome: dadosDoSeuDB.bairro.nome,
        } as Endereco;
        const novoEndereco = {
          ...formData.value.endereco,
          logradouro: dadosViaCEP.logradouro,
          complemento: dadosViaCEP.complemento,
          cidade: dadosDoSeuDB.cidade,
          bairro: dadosDoSeuDB.bairro || null,
          ufEstado: dadosDoSeuDB.bairro?.ufEstado || "",
          codigoIbge: dadosViaCEP.ibge,
          nomeEstado: dadosDoSeuDB.bairro.nomeEstado,
          nomeCidade: dadosDoSeuDB.bairro.nomeCidade,
          cidadeId: dadosDoSeuDB.bairro.idCidade,
          bairroId: dadosDoSeuDB.bairro.id,
          bairroNome: dadosDoSeuDB.bairro.nome,
        };

        (setFieldValue as (n: string, v: unknown) => void)("endereco.cidade", formData.value.endereco.cidade);
        (setFieldValue as (n: string, v: unknown) => void)(
          "endereco.bairro",
          formData.value.endereco.bairro || null
        );
        (setFieldValue as (n: string, v: unknown) => void)(
          "endereco.ufEstado",
          formData.value.endereco.ufEstado || ""
        );

        (validateField as (name: string) => void)("endereco.cidade");
        (validateField as (name: string) => void)("endereco.bairro");


      } else {
        throw new Error("Formato de resposta do backend inesperado.");
      }
    } catch (dbError) {
      console.log(
        "CEP não encontrado no banco local, usando dados da API externa:",
        dbError
      );

      formData.value.endereco = {
        ...formData.value.endereco,
        logradouro: dadosViaCEP.logradouro ?? '',
        complemento: dadosViaCEP.complemento ?? '',
        ufEstado: dadosViaCEP.uf ?? '',
        codigoIbge: dadosViaCEP.ibge ?? '',
        cidade: { id: null, nome: dadosViaCEP.localidade ?? '', estado: null },
        bairro: { id: null, nome: dadosViaCEP.bairro ?? '', cidade: null },
      } as Endereco;

    }
  } catch (error) {
    console.error("Falha na pesquisa de endereço:", error);
    if (window.showHostToast) {
      window.showHostToast({
        severity: "error",
        summary: "Erro",
        detail: "Falha ao pesquisar o CEP.",
      });
    }
  } finally {
    loading.value = false;
  }
};
watch(
  () => props.formType,
  (newFormType) => {
    setFieldValue("tipoDocumento", newFormType);
    setFieldValue("formType", newFormType);
    formData.value.tipoDocumento = newFormType;
  },
  {
    immediate: true,
  }
);

const formatarDataParaAPI = (
  dataString: string | null | undefined
): string | null => {
  if (!dataString || dataString.length < 10) {
    return null;
  }

  const partes = dataString.split("/");

  if (partes.length !== 3) {
    return null;
  }

  const [dia, mes, ano] = partes;

  return `${ano}-${mes}-${dia}`;
};
const formatarDataParaInput = (dataIso: string | null | undefined): string => {
  if (!dataIso) {
    return "";
  }

  const dataApenas = dataIso.split("T")[0];
  const partes = dataApenas.split("-");

  if (partes.length === 3) {
    const [ano, mes, dia] = partes;
    return `${dia}/${mes}/${ano}`;
  }

  return "";
};
</script>

<style scoped>
.review-form {
  min-height: 35vh;
  display: flex;
  justify-content: center;
}

.p-button {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.p-button:hover {
  background-color: var(--primary-color-hover) !important;
  border-color: var(--primary-color-hover) !important;
}

:deep(.p-datepicker-trigger) {
  background: var(--p-primary-color);

  border-color: var(--p-primary-color);

  border-radius: 0 6px 6px 0;
}



:deep(.p-datepicker-trigger .p-datepicker-trigger-icon) {
  color: var(--p-primary-color-text);
}



:deep(.p-datepicker-trigger:hover) {
  background: var(--p-primary-color-hover);

  border-color: var(--p-primary-color-hover);
}
</style>
