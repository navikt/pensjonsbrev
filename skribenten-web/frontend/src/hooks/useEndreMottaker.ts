import { useMutation, useQueryClient } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useState } from "react";

import { getBrev } from "~/api/brev-queries";
import { endreMottaker, fjernOverstyrtMottaker, hentAlleBrevInfoForSak, hentPdfForBrev } from "~/api/sak-api-endpoints";
import { type Adresse } from "~/types/apiTypes";
import { type BrevInfo, type BrevResponse } from "~/types/brev";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";

export function useEndreMottaker(saksId: string, brevId: number) {
  const queryClient = useQueryClient();
  const [modalÅpen, setModalÅpen] = useState(false);

  const mottakerMutation = useMutation<BrevInfo, Error, string | Adresse>({
    /*
    Mappingen skjer med vilje inne i mutationFn og ikke i endreMottaker under. Kaster den utenfor
    mutasjonen, havner feilen i en promise ingen venter på, og saksbehandler ser bare at knappen
    slutter å spinne. Her fanger TanStack Query den og ruter den til endreMottakerError -> ApiError.
    */
    mutationFn: (mottaker) => endreMottaker(saksId, brevId, { mottaker: mapEndreMottakerValueTilMottaker(mottaker) }),
    onSuccess: (response) => {
      queryClient.setQueryData(
        hentAlleBrevInfoForSak.queryKey(saksId),
        (currentBrevInfo: BrevInfo[] | undefined = []) =>
          currentBrevInfo.map((brevInfo) => (brevInfo.id === response.id ? response : brevInfo)),
      );
      queryClient.setQueryData(getBrev.queryKey(brevId), (current: BrevResponse | undefined) =>
        current ? { ...current, info: response } : current,
      );
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
      setModalÅpen(false);
    },
  });

  const fjernMottakerMutation = useMutation<void, AxiosError>({
    mutationFn: () => fjernOverstyrtMottaker({ saksId, brevId }),
    onSuccess: () => {
      queryClient.setQueryData(
        hentAlleBrevInfoForSak.queryKey(saksId),
        (currentBrevInfo: BrevInfo[] | undefined = []) =>
          currentBrevInfo.map((brevInfo) => (brevInfo.id === brevId ? { ...brevInfo, mottaker: null } : brevInfo)),
      );
      queryClient.setQueryData(getBrev.queryKey(brevId), (current: BrevResponse | undefined) =>
        current ? { ...current, info: { ...current.info, mottaker: null } } : current,
      );
      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
    },
  });

  return {
    modalÅpen,
    åpneModal: () => setModalÅpen(true),
    lukkModal: () => setModalÅpen(false),
    endreMottaker: (mottaker: string | Adresse) => {
      mottakerMutation.mutate(mottaker);
    },
    resetEndreMottaker: () => mottakerMutation.reset(),
    endreMottakerError: mottakerMutation.error,
    endreMottakerIsPending: mottakerMutation.isPending,
    fjernMottaker: () => fjernMottakerMutation.mutate(),
    fjernMottakerIsPending: fjernMottakerMutation.isPending,
    fjernMottakerIsError: fjernMottakerMutation.isError,
  };
}
