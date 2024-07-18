import { css } from "@emotion/react";
import { ArrowRightIcon, PlusIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Checkbox, CheckboxGroup, HStack, Label, Modal, VStack } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useEffect, useMemo, useRef, useState } from "react";

import { delvisOppdaterBrev, hentAlleBrevForSak, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevResponse, DelvisOppdaterBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevKlar } from "~/utils/brevUtils";

import BrevbehandlerMeny from "./-components/BrevbehandlerMeny";
import { BrevForhåndsvisning } from "./-components/BrevForhåndsvisning";
import { PDFViewerContextProvider, usePDFViewerContext } from "./-components/PDFViewerContext";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler")({
  component: () => (
    //Fordi høyden til routen som viser PDF'en ikke er helt fastsatt på forhånd, anser vi denne routen som direkte parenten av PDF-vieweren, som da får bestemme PDF-viewerens høyde
    <PDFViewerContextProvider>
      <Brevbehandler />
    </PDFViewerContextProvider>
  ),
  validateSearch: (search: Record<string, unknown>): { brevId?: string } => ({
    brevId: search.brevId?.toString(),
  }),
});

function Brevbehandler() {
  const { saksId } = Route.useParams();
  const { brevId } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);

  const brevPdfContainerReference = useRef<HTMLDivElement>(null);
  const pdfHeightContext = usePDFViewerContext();

  //TODO - et lite problem er dersom dem resizer vinduet etter at PDF'en er lastet inn, så vil høyden være feil
  useEffect(() => {
    pdfHeightContext.setHeight(brevPdfContainerReference?.current?.getBoundingClientRect().height ?? null);
  }, [brevPdfContainerReference, pdfHeightContext]);

  //TODO - sjekk om dette er good
  //vi henter data her istedenfor i route-loaderen fordi vi vil vise stort sett lik skjermbilde
  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey,
    queryFn: () => hentAlleBrevForSak.queryFn(saksId),
  });

  return (
    <div
      css={css`
        display: flex;
        flex: 1;
        justify-content: center;
      `}
    >
      {modalÅpen && <FerdigstillOgSendBrevModal onClose={() => setModalÅpen(false)} sakId={saksId} åpen={modalÅpen} />}
      <div
        css={css`
          display: grid;
          grid-template-columns: 33% 66%;
          grid-template-rows: 1fr auto;
          grid-template-areas:
            "meny pdf"
            "footer footer";

          background-color: white;
          width: 1200px;
        `}
      >
        <div>
          {alleBrevForSak.isPending && <Label>Henter alle brev for saken...</Label>}
          {alleBrevForSak.isError && (
            <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle brev for saken"} />
          )}
          {alleBrevForSak.isSuccess && <BrevbehandlerMeny brevInfo={alleBrevForSak.data} sakId={saksId} />}
        </div>

        {/* vi har lyst til å fortsette å vise der PDF'en skal være - derfor må vi wrappe outlet'en i ev div, så css'en treffer */}
        <div ref={brevPdfContainerReference}>{brevId && <BrevForhåndsvisning brevId={brevId} sakId={saksId} />}</div>

        <HStack
          css={css`
            padding: 8px 12px;
            grid-area: footer;
            border-top: 1px solid var(--a-gray-200);
          `}
          justify="space-between"
        >
          <Button
            onClick={() => {
              navigate({
                to: "/saksnummer/$saksId/brevvelger",
                params: { saksId: saksId },
              });
            }}
            type="button"
            variant="secondary"
          >
            <HStack>
              <PlusIcon fontSize="1.5rem" title="pluss-ikon" />
              Lag nytt brev
            </HStack>
          </Button>
          {alleBrevForSak.isSuccess && (
            <FerdigstillOgSendBrevButton
              brevInfo={alleBrevForSak.data}
              sakId={saksId}
              valgtBrev={brevId}
              åpneFerdigstillModal={() => setModalÅpen(true)}
            />
          )}
        </HStack>
      </div>
    </div>
  );
}

const FerdigstillOgSendBrevButton = (properties: {
  sakId: string;
  valgtBrev?: string;
  brevInfo: BrevInfo[];
  åpneFerdigstillModal: () => void;
}) => {
  if (!properties.valgtBrev && properties.brevInfo.some(erBrevKlar)) {
    return (
      <Button onClick={properties.åpneFerdigstillModal} type="button">
        <HStack gap="2">
          Send ferdigstilte brev
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
        type="button"
      >
        <HStack gap="2">
          {erLåst ? "Ferdigstill brev" : "Ferdigstill brev og send"}
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    </div>
  );
};

const useSendMutation = (sakId: string): UseMutationResult<BestillBrevResponse, Error, number> => {
  return useMutation({
    mutationFn: (brevId) => sendBrev(sakId, brevId),
  });
};

const FerdigstillOgSendBrevModal = (properties: { sakId: string; åpen: boolean; onClose: () => void }) => {
  const [brevSomSkalSendes, setBrevSomSkalSendes] = useState<number[]>([]);

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

  const mutation = useSendMutation(properties.sakId);

  const handleMutations = async () => {
    try {
      const results = await Promise.all(brevSomSkalSendes.map((brevId) => mutation.mutateAsync(brevId)));
      console.log("All mutations completed", results);
    } catch (error) {
      console.error("Error with some items", error);
    }
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
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="4">
          <Button onClick={properties.onClose} type="button" variant="tertiary">
            Avbryt
          </Button>
          <Button
            onClick={() => {
              console.log("Ferdigstiller og sender brev:", brevSomSkalSendes);
              handleMutations();
            }}
            type="button"
          >
            Ja, send valgte brev
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};
