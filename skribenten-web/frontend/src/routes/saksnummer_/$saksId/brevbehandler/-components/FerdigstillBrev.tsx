import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Checkbox, CheckboxGroup, HStack, Label, Modal } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";

import { delvisOppdaterBrev, hentAlleBrevForSak, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevResponse, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevKlar } from "~/utils/brevUtils";

import { useFerdigstillResultatContext } from "../../kvittering/-components/FerdigstillResultatContext";
import { Route } from "../route";

export const FerdigstillOgSendBrevButton = (properties: {
  sakId: string;
  valgtBrev?: string;
  brevInfo: BrevInfo[];
  åpneFerdigstillModal: () => void;
}) => {
  if (!properties.valgtBrev && properties.brevInfo.some(erBrevKlar)) {
    return (
      <Button onClick={properties.åpneFerdigstillModal} size="small" type="button">
        <HStack gap="2">
          <Label>Send ferdigstilte brev</Label>
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    );
  }

  if (properties.valgtBrev && !properties.brevInfo.some((brev) => brev.id.toString() === properties.valgtBrev)) {
    return <BodyShort>Fant ikke brev med id {properties.valgtBrev}</BodyShort>;
  }

  if (properties.valgtBrev) {
    return (
      <FerdigstillValgtBrevWrapper
        brevId={properties.valgtBrev}
        brevInfo={properties.brevInfo}
        sakId={properties.sakId}
        åpneFerdigstillModal={properties.åpneFerdigstillModal}
      />
    );
  }

  return null;
};

const FerdigstillValgtBrevWrapper = (properties: {
  sakId: string;
  brevId: string;
  brevInfo: BrevInfo[];
  åpneFerdigstillModal: () => void;
}) => {
  const valgtBrev = properties.brevInfo.find((brev) => brev.id.toString() === properties.brevId);
  if (!valgtBrev) {
    return <BodyShort>Fant ikke brev med id {properties.brevId}</BodyShort>;
  }

  return (
    <FerdigstillValgtBrev
      brev={valgtBrev}
      sakId={properties.sakId}
      åpneFerdigstillModal={properties.åpneFerdigstillModal}
    />
  );
};

const FerdigstillValgtBrev = (properties: { sakId: string; brev: BrevInfo; åpneFerdigstillModal: () => void }) => {
  const queryClient = useQueryClient();

  const låsForRedigeringMutation = useMutation<DelvisOppdaterBrevResponse, Error, boolean, unknown>({
    mutationFn: (låst) =>
      delvisOppdaterBrev({ sakId: properties.sakId, brevId: properties.brev.id, laastForRedigering: låst }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey, (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === properties.brev.id ? response.info : brev)),
      );
      properties.åpneFerdigstillModal();
    },
  });

  const erLåst = useMemo(() => erBrevKlar(properties.brev), [properties.brev]);

  return (
    <div>
      <Button
        loading={låsForRedigeringMutation.isPending}
        onClick={() => {
          if (erLåst) {
            properties.åpneFerdigstillModal();
          } else {
            låsForRedigeringMutation.mutate(true);
          }
        }}
        size="small"
        type="button"
      >
        <HStack gap="2">
          <Label>{erLåst ? "Ferdigstill brev" : "Ferdigstill og send brev"}</Label>
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    </div>
  );
};

export const FerdigstillOgSendBrevModal = (properties: { sakId: string; åpen: boolean; onClose: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const [valgtBrevSomSkalSendes, setValgtBrevSomSkalSendes] = useState<number[]>([]);
  const ferdigstillBrevContext = useFerdigstillResultatContext();

  const mutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey,
    queryFn: () => hentAlleBrevForSak.queryFn(properties.sakId),
  });

  const alleFerdigstilteBrev = useMemo(() => {
    if (alleBrevForSak.isSuccess) {
      return alleBrevForSak.data.filter(erBrevKlar);
    }
    return [];
  }, [alleBrevForSak.data, alleBrevForSak.isSuccess]);

  useEffect(() => {
    setValgtBrevSomSkalSendes(alleFerdigstilteBrev.map((brev) => brev.id));
  }, [alleFerdigstilteBrev]);

  const handleMutations = async () => {
    const brevSomSkalSendes = valgtBrevSomSkalSendes
      .map((brevId) => alleFerdigstilteBrev.find((brev) => brev.id === brevId))
      .filter((brev) => brev !== undefined);

    const requests = brevSomSkalSendes.map((brevInfo) =>
      mutation.mutateAsync(brevInfo.id).then(
        //vi har fortsatt behov for informasjon i brevet, så vi returnerer brevinfo sammen med responsen
        (response) => ({ status: "fulfilledWithSuccess" as const, brevInfo, response }),
        (error: AxiosError) => ({ status: "fulfilledWithError" as const, brevInfo, error }),
      ),
    );

    const resultat = await Promise.allSettled(requests).then((result) =>
      result.map((response) => {
        switch (response.status) {
          //fordi vi håndterer vanlige caser av rejected i mutation, vil resultatet 'alltid' være fulfilled
          case "fulfilled": {
            return response.value;
          }
          //en safe-guard dersom noe uventet går feil? Kan se om vi kan håndtere dette på en bedre måte
          case "rejected": {
            throw new Error(`Feil ved sending av minst 1 brev. Original error: ${response.reason}`);
          }
        }
      }),
    );

    ferdigstillBrevContext.setResultat(resultat);
    navigate({ to: "/saksnummer/$saksId/kvittering", params: { saksId: properties.sakId } });
  };

  return (
    <Modal
      css={css`
        border-radius: 0.25rem;
      `}
      header={{
        heading: "Vil du ferdigstille, og sende disse brevene?",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      portal
      width={600}
    >
      <Modal.Body>
        <div
          css={css`
            margin-bottom: 1rem;
          `}
        >
          <div>
            <BodyShort>
              Brevene du ferdigstiller og sender vil bli lagt til i brukers dokumentoversikt. Du kan ikke angre denne
              handlingen.
            </BodyShort>
            <br />
            <BodyShort>Kun brev du har valgt å ferdigstille vil bli sendt.</BodyShort>
          </div>
          <br />
          <div>
            {alleBrevForSak.isPending && <Label>Henter alle ferdigstilte brev...</Label>}
            {alleBrevForSak.isError && (
              <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle ferdigstilte for saken"} />
            )}
            {alleBrevForSak.isSuccess && (
              <div>
                <CheckboxGroup
                  hideLegend
                  legend="Velg brev som skal sendes"
                  onChange={setValgtBrevSomSkalSendes}
                  value={valgtBrevSomSkalSendes}
                >
                  {alleFerdigstilteBrev.map((brev) => (
                    <Checkbox key={brev.id} value={brev.id}>
                      {brev.brevkode}
                    </Checkbox>
                  ))}
                </CheckboxGroup>
              </div>
            )}
          </div>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={properties.onClose} type="button" variant="tertiary">
            Avbryt
          </Button>
          {/* TODO - validering på at minst 1 brev er faktisk valgt */}
          <Button loading={mutation.isPending} onClick={handleMutations} type="button">
            Ja, send valgte brev
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
