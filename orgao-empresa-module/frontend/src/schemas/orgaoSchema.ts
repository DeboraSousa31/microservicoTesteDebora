import { z } from "zod";
import { toTypedSchema } from "@vee-validate/zod";
import { isProxy, toRaw } from "vue";
// --- Schemas Reutilizáveis (sem alterações) ---
// const selectedObjectSchema = z.object(
//   {
//     id: z.number().nullable(),
//     nome: z.string(),
//   },
//   {
//     required_error: 'Este campo é obrigatório.',
//     invalid_type_error: 'Por favor, selecione uma opção válida.',
//   },
// ).nullable().refine(val => val != null , { message: 'Este campo é obrigatório.' });
const selectedObjectSchema = z
  .union(
    [
      z.object({
        id: z.number().nullable(),
        nome: z.string(),
      }),

      z.string().min(2, "O nome deve ter pelo menos 2 caracteres."),
    ],
    {
  
      invalid_type_error: "Valor inválido.",
      required_error: "Este campo é obrigatório.",
    }
  )
  .nullable()
  .refine(

    (val) => val != null && (typeof val !== "string" || val.trim() !== ""),
    {
      message: "Este campo é obrigatório.",
    }
  );
// const campoAutoCompleteObrigatorio = z.any() // Aceita qualquer tipo inicialmente
//   .refine(value => {
//     // 1. Rejeita valores nulos ou indefinidos
//     if (value == null) return false;

//     // 2. Se for uma string (digitada pelo usuário), verifica se não está vazia
//     if (typeof value === 'string') {
//       return value.trim().length > 0;
//     }

//     // 3. Se for um objeto (vindo da API ou seleção), verifica se tem um nome válido
//     if (typeof value === 'object') {
//       console.log(value, typeof value)
//       return value.nome && typeof value.nome === 'string' && value.nome.trim().length > 0;
//     }

//     // 4. Rejeita qualquer outro tipo de dado
//     return false;
//   }, {
//     message: "Este campo é obrigatório.",
//   });
const campoAutoCompleteObrigatorio = z.any().refine(
  (val) => {
    console.log("VAL RECEBIDO NA VALIDAÇÃO:", val);
    if (val == null) return false;
    if (isProxy(val)) {
      val = toRaw(val);
    }
    console.log("VAL RECEBIDO NA VALIDAÇÃO:", val);
    if (typeof val === "object") {
      return (
        val.nome && typeof val.nome === "string" && val.nome.trim().length > 0
      );
    }
    if (typeof val === "string") {
      return val.trim().length > 0;
    }
    return false;
  },
  {
    message: "Este campo é obrigatório.",
  }
);

const enderecoSchema = z.object({
  logradouro: z
    .string({ required_error: "O Logradouro é obrigatório." })
    .min(1, "O Logradouro é obrigatório."),
  numero: z
    .string({ required_error: "O Número é obrigatório." })
    .min(1, "O Número é obrigatório."),
  //cidade: selectedObjectSchema,
  //bairro: selectedObjectSchema,
  cep: z.string().optional(),
  complemento: z.string().optional(),
  nomeCidade: z.string().optional(),
  bairroNome: z.string().optional(),
  codigoIbge: z.string().optional(),
  nomeEstado: z.string().optional(),
  ufEstado: z
    .string({ required_error: "O Estado é obrigatório." })
    .min(1, "O Estado é obrigatório."),
});



const orgaoSchemaMestre = z
  .object({
  
    
    tipoDocumento: z.enum(["CPF", "CNPJ"]),

    
    nome: z
      .string({ required_error: "O Nome é obrigatório." })
      .min(3, "O nome deve ter no mínimo 3 caracteres."),
    email: z
      .string({ required_error: "O Email é obrigatório." })
      .email("O email informado é inválido."),
    telefone: z.string().optional(),
    tipoOrgao: selectedObjectSchema,
    idCredenciadaOrgao: selectedObjectSchema,
    endereco: enderecoSchema,

    cpf: z.string().optional(),
    dataNascimento: z.string().optional(),

    cnpj: z.string().optional(),
    razaoSocial: z.string().optional(),
    nomeResponsavel: z.string().optional(),
    nomePrefeito: z.string().optional(),
    nomeTabeliao: z.string().optional(),
    codigoIBGEServentia: z.string().optional(),
    codigoServentia: z.string().optional(),
    oficioServentia: z.string().optional(),
    formType: z.string().optional(),
  })
  .superRefine((data, ctx) => {

    if (data.tipoDocumento === "CPF") {
      if (!data.cpf || data.cpf.length < 14) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["cpf"],
          message: "O CPF deve ser preenchido completamente.",
        });
      }
      if (!data.dataNascimento || data.dataNascimento.length < 10) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["dataNascimento"],
          message: "A Data de Nascimento deve ser preenchida completamente.",
        });
      }
      if (!data.telefone || data.telefone.length < 14) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["telefone"],
          message: "O Telefone deve ser preenchido completamente.",
        });
      }
    }

    if (data.tipoDocumento === "CNPJ") {
      if (!data.cnpj || data.cnpj.length < 18) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["cnpj"],
          message: "O CNPJ deve ser preenchido completamente.",
        });
      }


      const tipoOrgaoNome = (typeof data.tipoOrgao === 'object' && data.tipoOrgao && 'nome' in data.tipoOrgao) ? (data.tipoOrgao as { nome?: string }).nome?.trim() : (typeof data.tipoOrgao === 'string' ? data.tipoOrgao.trim() : undefined);

      if (
        tipoOrgaoNome === "Empresa" ||
        tipoOrgaoNome === null ||
        tipoOrgaoNome === undefined
      ) {
        if (!data.razaoSocial || data.razaoSocial.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["razaoSocial"],
            message: "A Razão Social é obrigatória.",
          });
        if (!data.nomeResponsavel || data.nomeResponsavel.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["nomeResponsavel"],
            message: "O Responsável é obrigatório.",
          });
      }
      if (tipoOrgaoNome === "PREFEITURA MUNICIPAL") {
        if (!data.nomePrefeito || data.nomePrefeito.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["nomePrefeito"],
            message: "O Nome do Prefeito é obrigatório.",
          });
      }
      if (tipoOrgaoNome === "Cartório") {
        if (!data.nomeTabeliao || data.nomeTabeliao.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["nomeTabeliao"],
            message: "O Nome do Tabelião é obrigatório.",
          });
        if (!data.codigoIBGEServentia || data.codigoIBGEServentia.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["codigoIBGEServentia"],
            message: "O Código IBGE da Serventia é obrigatório.",
          });
        if (!data.codigoServentia || data.codigoServentia.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["codigoServentia"],
            message: "O Código da Serventia é obrigatório.",
          });
        if (!data.oficioServentia || data.oficioServentia.trim() === "")
          ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["oficioServentia"],
            message: "O Ofício da Serventia é obrigatório.",
          });
      }
    }
  });


export const veeValidateSchema = toTypedSchema(orgaoSchemaMestre);
