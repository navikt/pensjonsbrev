import { useQuery } from "@tanstack/react-query";

import { getP1Override } from "~/api/brev-queries";

export const useP1Override = (saksId: string, brevId: number, enabled: boolean) =>
  useQuery({
    queryKey: getP1Override.queryKey(brevId),
    queryFn: () => getP1Override.queryFn(saksId, brevId),
    enabled,
  });
