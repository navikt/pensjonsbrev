import { css } from "@emotion/react";
import { createFileRoute } from "@tanstack/react-router";

import { getTemplate } from "~/api/skribenten-api-endpoints";
import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";

export const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export const Route = createFileRoute("/saksnummer/$sakId/redigering/$templateId")({
  beforeLoad: () => {
    const getTemplateQueryOptions = {
      queryKey: getTemplate.queryKey(TEST_TEMPLATE),
      queryFn: () => getTemplate.queryFn(TEST_TEMPLATE),
    };

    return { getTemplateQueryOptions };
  },
  loader: ({ context: { queryClient, getTemplateQueryOptions } }) =>
    queryClient.ensureQueryData(getTemplateQueryOptions),
  component: () => (
    <div
      css={css`
        background: var(--a-white);
        display: grid;
        grid-template-columns: minmax(380px, 400px) 1fr;
        flex: 1;
        border-left: 1px solid var(--a-gray-400);
        border-right: 1px solid var(--a-gray-400);

        > form:first-of-type {
          padding: var(--a-spacing-4);
          border-right: 1px solid var(--a-gray-400);
        }
      `}
    >
      <ModelEditor />
    </div>
  ),
});
