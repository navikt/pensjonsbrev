import axios from "axios";

import { type SearchLine, type TemplateDescription, type TemplateDocumentation } from "~/api/brevbakerTypes";

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
