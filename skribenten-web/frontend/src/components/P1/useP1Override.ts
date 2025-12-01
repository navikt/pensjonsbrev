import { useQuery } from "@tanstack/react-query";

import { getOrCreateP1Override, p1OverrideKeys } from "~/api/brev-queries";

export const useP1Override = (saksId: string, brevId: number, enabled: boolean) =>
  useQuery({
    queryKey: p1OverrideKeys.id(saksId, brevId),
    enabled,
    queryFn: () => getOrCreateP1Override(saksId, brevId),
  });
