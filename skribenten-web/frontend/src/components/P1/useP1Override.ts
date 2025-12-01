import { useQuery } from "@tanstack/react-query";

import { getOrCreateP1OverrideQuery } from "~/api/brev-queries";

export const useP1Override = (saksId: string, brevId: number, enabled: boolean) =>
  useQuery({
    ...getOrCreateP1OverrideQuery(saksId, brevId),
    enabled,
  });
