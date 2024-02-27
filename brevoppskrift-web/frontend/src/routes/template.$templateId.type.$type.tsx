import { css } from "@emotion/react";
import { VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";

import { getTypeDocumentation } from "~/api/brevbaker-api-endpoints";

export const Route = createFileRoute("/template/$templateId/type/$type")({
  loader: async ({ context, params }) => {
    const typeDocumentation = await context.queryClient.ensureQueryData({
      queryKey: getTypeDocumentation.queryKey(params.type),
      queryFn: () => getTypeDocumentation.queryFn(params.type),
    });

    return { typeDocumentation };
  },
  component: TypeView,
});

function TypeView() {
  const { typeDocumentation } = Route.useLoaderData();
  return (
    <div
      css={css`
        width: 600px;
        background: var(--a-bg-subtle);
        //background: aquamarine;
        position: fixed;
        right: 0;
        height: 100%;
        border-left: 1px solid var(--a-border-divider);
        padding: var(--a-spacing-4);

        .field {
          margin-left: var(--a-spacing-16);
        }
        .data-class {
          color: var(--a-red-500);
        }
        .primitive {
          color: var(--a-blue-400);
        }
        .type {
          color: var(--a-purple-400);
        }
      `}
    >
      <VStack gap="1">
        <span>
          <span className="data-class">data class</span> {typeDocumentation.className}(
        </span>
        {Object.entries(typeDocumentation.fields).map(([key, value]) => (
          <span className="field" key={key}>
            {key}: <span className={value.fields ? "type" : "primitive"}>{value.className}</span>
          </span>
        ))}
        <span>)</span>
      </VStack>
    </div>
  );
}
