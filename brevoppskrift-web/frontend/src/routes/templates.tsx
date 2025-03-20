import { Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";

import { getBrevkoder, MalType } from "~/api/brevbaker-api-endpoints";
import {css} from "@emotion/react";

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
  console.log(templates);
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
      <Heading level="2" size="large" css={css`
          margin-top: 28px;
          margin-bottom: 10px;
      `}>
        Automatiske brev
      </Heading>
      <TemplateList templates={templates.autobrev} malType={"autobrev"} />
      <Heading level="2" size="large" css={css`
          margin-top: 28px;
          margin-bottom: 10px;
      `}>
        Redigerbare brev
      </Heading>
      <TemplateList templates={templates.redigerbar} malType={"redigerbar"} />
    </div>
  );
}
