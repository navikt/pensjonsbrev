import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useState } from "react";

import { getBrev } from "~/api/brev-queries";
import { endreMottaker, fjernOverstyrtMottaker, hentAlleBrevInfoForSak, hentPdfForBrev } from "~/api/sak-api-endpoints";
import type { BrevInfo, Mottaker } from "~/types/brev";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";

export function useEndreMottaker(saksId: string, brevId: number) {
  const queryClient = useQueryClient();
  const [modalÅpen, setModalÅpen] = useState(false);

  const mottakerMutation = useMutation<BrevInfo, AxiosError, Mottaker>({
    mutationFn: (mottaker) => endreMottaker(saksId, brevId, { mottaker }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) => (brevInfo.id === response.id ? response : brevInfo)),
      );
      queryClient.setQueryData(getBrev.queryKey(brevId), response);
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
      setModalÅpen(false);
    },
  });

  const fjernMottakerMutation = useMutation<void, AxiosError>({
    mutationFn: () => fjernOverstyrtMottaker({ saksId, brevId }),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevInfoForSak.queryKey(saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brevInfo) => (brevInfo.id === brevId ? { ...brevInfo, mottaker: null } : brevInfo)),
      );
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
    },
  });

  return {
    modalÅpen,
    åpneModal: () => setModalÅpen(true),
    lukkModal: () => setModalÅpen(false),
    endreMottaker: (mottaker: string | import("~/types/apiTypes").Adresse) => {
      mottakerMutation.mutate(mapEndreMottakerValueTilMottaker(mottaker));
    },
    resetEndreMottaker: () => mottakerMutation.reset(),
    endreMottakerError: mottakerMutation.error,
    endreMottakerIsPending: mottakerMutation.isPending,
    fjernMottaker: () => fjernMottakerMutation.mutate(),
    fjernMottakerIsPending: fjernMottakerMutation.isPending,
    fjernMottakerIsError: fjernMottakerMutation.isError,
  };
}
