import { css } from "@emotion/react";
import { ArrowLeftIcon, XMarkIcon } from "@navikt/aksel-icons";
import { Button, HStack, VStack } from "@navikt/ds-react";
import { createFileRoute, Link, useNavigate, useRouter } from "@tanstack/react-router";

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
  const navigate = useNavigate({ from: Route.fullPath });
  const { history } = useRouter();

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
          color: var(--a-purple-400);
        }
      `}
    >
      <HStack align="end" gap="4" justify="space-between">
        <Button icon={<ArrowLeftIcon />} onClick={() => history.go(-1)} size="small" variant="secondary-neutral" />
        <Button
          icon={<XMarkIcon />}
          onClick={() =>
            navigate({ replace: true, to: "/template/$templateId", params: (p) => ({ templateId: p.templateId }) })
          }
          size="small"
          variant="secondary-neutral"
        />
      </HStack>
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
                search
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
