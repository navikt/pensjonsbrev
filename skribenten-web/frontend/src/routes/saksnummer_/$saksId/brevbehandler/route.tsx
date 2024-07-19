import { css } from "@emotion/react";
import { PlusIcon } from "@navikt/aksel-icons";
import { Button, HStack, Label } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useEffect, useRef, useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";

import BrevbehandlerMeny from "./-components/BrevbehandlerMeny";
import { BrevForhåndsvisning } from "./-components/BrevForhåndsvisning";
import { FerdigstillOgSendBrevButton, FerdigstillOgSendBrevModal } from "./-components/FerdigstillBrev";
import { PDFViewerContextProvider, usePDFViewerContext } from "./-components/PDFViewerContext";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler")({
  component: () => (
    //Fordi høyden til routen som viser PDF'en ikke er helt fastsatt på forhånd, anser vi denne routen som direkte parenten av PDF-vieweren, som da får bestemme PDF-viewerens høyde
    //Dette funker, men er ikke så stor fan av hvordan den er løst. Se om vi kan gjøre dette på en bedre måte når vi får tid.
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

  //vi henter data her istedenfor i route-loaderen fordi vi vil vise stort sett lik skjermbilde
  //Vi kan muligens gjøre en load i route-loader slik at brevene laster litt fortere
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
              navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId: saksId } });
            }}
            size="small"
            type="button"
            variant="secondary"
          >
            <HStack>
              <PlusIcon fontSize="1.5rem" title="pluss-ikon" />
              <Label>Lag nytt brev</Label>
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
