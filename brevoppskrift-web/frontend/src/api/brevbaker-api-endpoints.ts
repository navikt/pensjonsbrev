// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.

import axios from "axios";

import type { TemplateDescription, TemplateDocumentation } from "~/api/brevbakerTypes";

const BREVBAKER_API_BASE_PATH = "/brevbaker";

/**
 * Anbefalt lesing for react-query key factory pattern: https://tkdodo.eu/blog/effective-react-query-keys
 */

export const templateDescriptionKeys = {
  all: ["TEMPLATE_DESCRIPTION"] as const,
  id: (templateId: string) => [...templateDescriptionKeys.all, templateId] as const,
};

export const templateDocumentationKeys = {
  all: ["TEMPLATE_DOCUMENTATION"] as const,
  id: (templateId: string) => [...templateDocumentationKeys.all, templateId] as const,
  idWithLanguage: (templateId: string, language: string) =>
    [...templateDocumentationKeys.all, templateId, language] as const,
};

export const brevkoderKeys = {
  all: ["BREVKODER"] as const,
};

export const getTemplateDescription = {
  queryKey: templateDescriptionKeys.id,
  queryFn: async (templateId: string) =>
    (await axios.get<TemplateDescription>(`${BREVBAKER_API_BASE_PATH}/templates/autobrev/${templateId}`)).data,
};

export const getTemplateDocumentation = {
  queryKey: templateDocumentationKeys.idWithLanguage,
  queryFn: async (templateId: string, language: string) =>
    (
      await axios.get<TemplateDocumentation>(
        `${BREVBAKER_API_BASE_PATH}/templates/autobrev/${templateId}/doc/${language}`,
      )
    ).data,
};

export const getAllBrevkoder = {
  queryKey: brevkoderKeys.all,
  queryFn: async () => (await axios.get<string[]>(`${BREVBAKER_API_BASE_PATH}/templates/autobrev`)).data,
};
