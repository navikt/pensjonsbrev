import axios from "axios";

import { type SearchLine, type TemplateDescription, type TemplateDocumentation } from "~/api/brevbakerTypes";
import { type TemplateDocumentationV2 } from "~/api/brevbakerTypesV2";

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

export const templateDocumentationV2Keys = {
  all: ["TEMPLATE_DOCUMENTATION_V2"] as const,
  idWithLanguage: (malType: MalType, templateId: string, language: string) =>
    [...templateDocumentationV2Keys.all, malType, templateId, language] as const,
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

export type SearchableContent = {
  brevkode: string;
  language: string;
  lines: SearchLine[];
};

export const getAllTemplateDocumentation = {
  queryKey: (malType: MalType) => [...templateDocumentationKeys.all, malType, "BATCH"] as const,
  queryFn: async (malType: MalType) =>
    (await axios.get<SearchableContent[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}/all`)).data,
};

export const getBrevkoderMedMetadata = {
  queryKey: (malType: MalType) => ["BREVKODER", malType, "METADATA"] as const,
  queryFn: async (malType: MalType) =>
    (await axios.get<TemplateDescription[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}?includeMetadata=true`))
      .data,
};

export const getTemplateDocumentationV2 = {
  queryKey: templateDocumentationV2Keys.idWithLanguage,
  queryFn: async (type: MalType, templateId: string, language: string) =>
    (
      await axios.get<TemplateDocumentationV2>(
        `${BREVBAKER_API_BASE_PATH}/templates/${type}/${templateId}/doc/v2/${language}`,
      )
    ).data,
};

export const getBrevkoder = {
  queryKey: brevkoderKeys.malType,
  queryFn: async (malType: MalType) =>
    (await axios.get<string[]>(`${BREVBAKER_API_BASE_PATH}/templates/${malType}`)).data,
};
