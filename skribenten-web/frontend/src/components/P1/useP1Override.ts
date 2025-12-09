import { useQuery } from "@tanstack/react-query";

import { getP1OverrideQuery } from "~/api/brev-queries";

export const useP1Override = (saksId: string, brevId: number, enabled: boolean) =>
  useQuery({
    ...getP1OverrideQuery(saksId, brevId),
    enabled,
  });
