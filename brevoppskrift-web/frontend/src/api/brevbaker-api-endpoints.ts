// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { TemplateDocumentation } from "~/api/brevbakerTypes";

const BREVBAKER_API_BASE_PATH = "/brevbaker";

/**
 * Anbefalt lesing for react-query key factory pattern: https://tkdodo.eu/blog/effective-react-query-keys
 */

export const templateKeys = {
  all: ["TEMPLATE"] as const,
  id: (templateId: string) => [...templateKeys.all, templateId] as const,
};

export const getTemplate = {
  queryKey: templateKeys.id,
  queryFn: async (templateId: string) =>
    (await axios.get<TemplateDocumentation>(`${BREVBAKER_API_BASE_PATH}/templates/autobrev/${templateId}/doc/BOKMAL`))
      .data,
};
