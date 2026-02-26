import { z } from "zod";
import { toTypedSchema } from "@vee-validate/zod";

const selectedObjectSchema = z
  .object(
    {
      id: z.number().nullable(),
      nome: z.string(),
    },
    {
      required_error: "Este campo é obrigatório.",
      invalid_type_error: "Por favor, selecione uma opção válida.",
    }
  )
  .nullable()
  .refine((val) => val != null, { message: "Este campo é obrigatório." });

const interessadoSchemaMestre = z
  .object({
    tipoDocumento: z.enum(["CPF", "CNPJ"]),

    nome: z.string().optional(),
    email: z
      .string({ required_error: "O Email é obrigatório." })
      .email("O email informado é inválido."),
    telefone: z
      .string({ required_error: "O Email é obrigatório." })
      .min(3, "O Telefone deve ser preenchido completamente."),
    idCredenciadaInteressado: selectedObjectSchema,
    cpf: z.string().optional(),
    nomeFantasia: z.string().optional(),
    dataNascimento: z.string().optional(),
    cnpj: z.string().optional(),
    razaoSocial: z.string().optional(),
    orgaoEmissor: z.string().optional(),
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
      if (!data.telefone || data.telefone.length < 11) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["telefone"],
          message: "O Telefone deve ser preenchido completamente.",
        });
      }
      if (!data.nome || data.nome.length < 3) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["nome"],
          message: "O Nome deve ser preenchido completamente.",
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
      if (!data.razaoSocial || data.razaoSocial.length < 3) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["razaoSocial"],
          message: "Razão Social deve ser preenchido completamente.",
        });
      }
      if (!data.nomeFantasia || data.nomeFantasia.length < 3) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["nomeFantasia"],
          message: "O Nome Fantasia deve ser preenchido completamente.",
        });
      }
    }
  });

export const veeValidateSchema = toTypedSchema(interessadoSchemaMestre);
