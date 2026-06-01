// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.

import axios from "axios";

import { type TemplateDescription, type TemplateDocumentation } from "~/api/brevbakerTypes";

const BREVBAKER_API_BASE_PATH = "/brevbaker";

/**
 * Anbefalt lesing for react-query key factory pattern: https://tkdodo.eu/blog/effective-react-query-keys
 */

export const templateDescriptionKeys = {
  all: ["TEMPLATE_DESCRIPTION"] as const,
  id: (malType: MalType, templateId: string) => [...templateDescriptionKeys.all, malType, templateId] as const,
};

export type MalType = "redigerbar" | "autobrev";

export const templateDocumentationKeys = {
  all: ["TEMPLATE_DOCUMENTATION"] as const,
  idWithLanguage: (malType: MalType, templateId: string, language: string) =>
    [...templateDocumentationKeys.all, malType, templateId, language] as const,
};

export const brevkoderKeys = {
  all: ["BREVKODER"] as const,
  malType: (malType: MalType) => [...brevkoderKeys.all, malType] as const,
};

export const getTemplateDescription = {
  queryKey: templateDescriptionKeys.id,
  queryFn: async (type: MalType, templateId: string) =>
    (await axios.get<TemplateDescription>(`${BREVBAKER_API_BASE_PATH}/templates/${type}/${templateId}`)).data,
};

export const getTemplateDocumentation = {
  queryKey: templateDocumentationKeys.idWithLanguage,
  queryFn: async (type: MalType, templateId: string, language: string) =>
    (
      await axios.get<TemplateDocumentation>(
        `${BREVBAKER_API_BASE_PATH}/templates/${type}/${templateId}/doc/${language}`,
      )
    ).data,
};

/** One template's documentation for a single language, as returned by the batch
 * documentation endpoint. The batch omits the (large) `templateModelSpecification`
 * to keep the single response small; it is only used to build the full-text index. */
export type TemplateDocumentationBatchEntry = {
  brevkode: string;
  language: string;
  documentation: TemplateDocumentation;
};

export const getAllTemplateDocumentation = {
  queryKey: (malType: MalType) => [...templateDocumentationKeys.all, malType, "BATCH"] as const,
  queryFn: async (malType: MalType) =>
    (await axios.get<TemplateDocumentationBatchEntry[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}/doc`)).data,
};

export const getBrevkoder = {
  queryKey: brevkoderKeys.malType,
  queryFn: async (malType: MalType) =>
    (await axios.get<string[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}`)).data,
};

export const getBrevkoderMedMetadata = {
  queryKey: (malType: MalType) => [...brevkoderKeys.malType(malType), "METADATA"] as const,
  queryFn: async (malType: MalType) =>
    (await axios.get<TemplateDescription[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}?includeMetadata=true`))
      .data,
};
