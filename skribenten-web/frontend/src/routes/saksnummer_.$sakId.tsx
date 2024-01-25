import { createFileRoute } from "@tanstack/react-router";

import { getSak } from "~/api/skribenten-api-endpoints";
import { SakBreadcrumbsPage } from "~/pages/Brevvelger/SakBreadcrumbsPage";

export const Route = createFileRoute("/saksnummer/$sakId")({
  beforeLoad: ({ params: { sakId } }) => {
    const getSakQueryOptions = {
      queryKey: getSak.queryKey(sakId),
      queryFn: () => getSak.queryFn(sakId),
    };

    return { getSakQueryOptions };
  },
  loader: async ({ context: { queryClient, getSakQueryOptions } }) => {
    await queryClient.ensureQueryData(getSakQueryOptions);
  },
  component: SakBreadcrumbsPage,
});
