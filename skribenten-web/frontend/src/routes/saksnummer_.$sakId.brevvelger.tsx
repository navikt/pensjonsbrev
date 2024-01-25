import { createFileRoute } from "@tanstack/react-router";

import { getLetterTemplate } from "~/api/skribenten-api-endpoints";
import { BrevvelgerPage, BrevvelgerTabOptions } from "~/pages/Brevvelger/BrevvelgerPage";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger")({
  validateSearch: (search: Record<string, unknown>): { enhetsId?: string; fane: BrevvelgerTabOptions } => ({
    fane:
      search.fane === BrevvelgerTabOptions.E_BLANKETTER
        ? BrevvelgerTabOptions.E_BLANKETTER
        : BrevvelgerTabOptions.BREVMALER,
    enhetsId: search.enhetsId?.toString(),
  }),
  loader: async ({ context: { queryClient, getSakQueryOptions } }) => {
    const { sakType } = await queryClient.ensureQueryData(getSakQueryOptions);

    const getLetterTemplateQuery = {
      queryKey: getLetterTemplate.queryKey(sakType),
      queryFn: () => getLetterTemplate.queryFn(sakType),
    };

    await queryClient.ensureQueryData(getLetterTemplateQuery);
  },
  component: BrevvelgerPage,
});
