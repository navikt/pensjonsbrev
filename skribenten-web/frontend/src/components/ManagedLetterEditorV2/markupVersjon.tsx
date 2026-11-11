import { type QueryClient, useQuery } from "@tanstack/react-query";
import { type ReactNode } from "react";

import { getBrevInfo } from "~/api/brev-queries";
import { brevV2Keys } from "~/api/brevV2-queries";
import { hentAlleBrevInfoForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { CenteredLoader } from "~/components/CenteredLoader";
import { type BrevInfo } from "~/types/brev";

export function isCachedAsV2(queryClient: QueryClient, saksId: string, brevId: number): boolean {
  if (queryClient.getQueryData(brevV2Keys.id(brevId)) || queryClient.getQueryData(brevV2Keys.attestering(brevId))) {
    return true;
  }
  const info = queryClient.getQueryData<BrevInfo>(getBrevInfo(brevId).queryKey);
  if (info) return info.markupVersjon === "V2";
  const sakBrev = queryClient.getQueryData<BrevInfo[]>(hentAlleBrevInfoForSak.queryKey(saksId));
  return sakBrev?.find((brev) => brev.id === brevId)?.markupVersjon === "V2";
}

export const MarkupVersjonConflict = (props: { brevId: number; v2: () => ReactNode; otherwise: () => ReactNode }) => {
  const infoQuery = useQuery({ ...getBrevInfo(props.brevId), retry: false });
  if (infoQuery.isPending) return <CenteredLoader label="Henter brev..." />;
  if (infoQuery.isError) return <ApiError error={infoQuery.error} title="En feil skjedde ved henting av brev" />;
  return infoQuery.data.markupVersjon === "V2" ? props.v2() : props.otherwise();
};
