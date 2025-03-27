import { css } from "@emotion/react";
import { Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";

import type { MalType } from "~/api/brevbaker-api-endpoints";
import { getBrevkoder } from "~/api/brevbaker-api-endpoints";

export const Route = createFileRoute("/templates")({
  loader: async ({ context }) => {
    const autobrev = await context.queryClient.ensureQueryData({
      queryKey: getBrevkoder.queryKey("autobrev"),
      queryFn: () => getBrevkoder.queryFn("autobrev"),
    });
    const redigerbar = await context.queryClient.ensureQueryData({
      queryKey: getBrevkoder.queryKey("redigerbar"),
      queryFn: () => getBrevkoder.queryFn("redigerbar"),
    });
    return { autobrev, redigerbar };
  },

  component: AllTemplates,
});

function TemplateList({ templates, malType }: { templates: string[]; malType: MalType }) {
  return (
    <VStack gap="2">
      {templates.map((templateId) => (
        <Link key={templateId} params={{ malType, templateId }} preload="intent" to="/template/$malType/$templateId">
          {templateId}
        </Link>
      ))}
    </VStack>
  );
}

function AllTemplates() {
  const templates = Route.useLoaderData();

  return (
    <div>
      <Heading
        css={css`
          margin-top: 28px;
          margin-bottom: 10px;
        `}
        level="2"
        size="large"
      >
        Automatiske brev
      </Heading>
      <TemplateList malType={"autobrev"} templates={templates.autobrev} />
      <Heading
        css={css`
          margin-top: 28px;
          margin-bottom: 10px;
        `}
        level="2"
        size="large"
      >
        Redigerbare brev
      </Heading>
      <TemplateList malType={"redigerbar"} templates={templates.redigerbar} />
    </div>
  );
}
