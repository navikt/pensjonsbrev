import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Checkbox, CheckboxGroup, HStack, Label, Modal } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useMemo, useState } from "react";

import { delvisOppdaterBrev, hentAlleBrevForSak, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevResponse, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevKlar } from "~/utils/brevUtils";

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
  const [brevSomSkalSendes, setBrevSomSkalSendes] = useState<number[]>([]);

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
    setBrevSomSkalSendes(alleFerdigstilteBrev.map((brev) => brev.id));
  }, [alleFerdigstilteBrev]);

  const handleMutations = async () => {
    await Promise.allSettled(brevSomSkalSendes.map((brevId) => mutation.mutateAsync(brevId)));

    /*
    TODO
      her blir ønsket at man skal bli navigert til en kvitteringsside med alle breven man har valgt å sende
      Må finne ut hvordan vi kan 'sende' over resultatet av mutationene til en ny side
      https://www.figma.com/design/oUFhsO7YJUxDJeaWYKop30/%E2%9C%89%EF%B8%8F-Skribenten---MVP%2FPROTO?node-id=4007-9483&t=7TpSxrszVit6tUDf-4
    */
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
                <CheckboxGroup hideLegend legend="Something?" onChange={setBrevSomSkalSendes} value={brevSomSkalSendes}>
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
          <Button loading={mutation.isPending} onClick={handleMutations} type="button">
            Ja, send valgte brev
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
