import { css } from "@emotion/react";
import { VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";

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
        width: 800px;
        background: var(--a-bg-subtle);
        //background: aquamarine;
        position: fixed;
        right: 0;
        height: 100%;
        border-left: 1px solid var(--a-border-divider);
        padding: var(--a-spacing-4);
        white-space: nowrap;
        overflow: scroll;

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
            {key}:{" "}
            {value.isPrimitive ? (
              <span className="primitive">{value.className}</span>
            ) : (
              <Link
                from="/template/$templateId/type/$type"
                params={(p) => ({ ...p, type: value.className })}
                to="/template/$templateId/type/$type"
              >
                {trimClassName(value.className)}
              </Link>
            )}
          </span>
        ))}
        <span>)</span>
      </VStack>
    </div>
  );
}

function trimClassName(className: string) {
  return className.replace(/(.*)[$.]/, "");
}
