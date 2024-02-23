import { Heading, VStack } from "@navikt/ds-react";
import { createFileRoute, Link } from "@tanstack/react-router";

import { getAllBrevkoder } from "~/api/brevbaker-api-endpoints";

export const Route = createFileRoute("/templates")({
  loader: ({ context }) => {
    return context.queryClient.ensureQueryData({
      queryKey: getAllBrevkoder.queryKey,
      queryFn: () => getAllBrevkoder.queryFn(),
    });
  },

  component: AllTemplates,
});

function AllTemplates() {
  const templates = Route.useLoaderData();

  return (
    <div>
      <Heading level="1" size="large">
        Alle brevmaler
      </Heading>
      <VStack gap="2">
        {templates.map((templateId) => (
          <Link key={templateId} params={{ templateId }} preload="intent" to="/template/$templateId">
            {templateId}
          </Link>
        ))}
      </VStack>
    </div>
  );
}
