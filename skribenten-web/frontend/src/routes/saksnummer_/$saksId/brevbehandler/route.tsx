import { css } from "@emotion/react";
import { PencilIcon } from "@navikt/aksel-icons";
import {
  Accordion,
  Alert,
  BodyShort,
  Button,
  Heading,
  HStack,
  Label,
  Radio,
  RadioGroup,
  Switch,
  Tag,
  VStack,
} from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Link, Outlet, useNavigate } from "@tanstack/react-router";
import { useEffect, useRef, useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BrevInfo } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { formatStringDate, formatStringDateWithTime, isDateToday } from "~/utils/dateUtils";

import { DistribusjonsMetode } from "./-BrevbehandlerUtils";
import { PDFViewerContextProvider, usePDFViewerContext } from "./$brevId/-components/PDFViewerContext";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler")({
  component: () => (
    //Fordi høyden til routen som viser PDF'en ikke er helt fastsatt på forhånd, anser vi denne routen som direkte parenten av PDF-vieweren, som da får bestemme PDF-viewerens høyde
    <PDFViewerContextProvider>
      <Brevbehandler />
    </PDFViewerContextProvider>
  ),
});

function Brevbehandler() {
  const { saksId } = Route.useParams();
  const brevPdfContainerReference = useRef<HTMLDivElement>(null);
  const pdfHeightContext = usePDFViewerContext();

  useEffect(() => {
    pdfHeightContext.setHeight(brevPdfContainerReference?.current?.getBoundingClientRect().height ?? null);
  }, [brevPdfContainerReference, pdfHeightContext]);

  return (
    <div
      css={css`
        display: flex;
        flex: 1;
        justify-content: center;

        > :first-of-type {
          background: white;
          min-width: 336px;
          max-width: 388px;
          border-right: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-4);
          flex: 1;
        }

        > :nth-of-type(2) {
          background-color: var(--a-gray-300);
          min-width: 432px;
          max-width: 720px;
          flex: 1;
          border-left: 1px solid var(--a-gray-200);
          border-right: 1px solid var(--a-gray-200);
        }
      `}
    >
      <BrevbehandlerMeny sakId={saksId} />
      {/* vi hvar lyst til å fortsette å vise der PDF'en skal være - derfor må vi wrappe outlet'en i ev div, så css'en treffer */}
      <div ref={brevPdfContainerReference}>
        <Outlet />
      </div>
    </div>
  );
}

const BrevbehandlerMeny = (properties: { sakId: string }) => {
  //TODO - sjekk om dette er good
  //vi henter data her istedenfor i route-loaderen fordi vi vil vise stort sett lik skjermbilde
  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey,
    queryFn: () => hentAlleBrevForSak.queryFn(properties.sakId),
  });

  return (
    <VStack gap="8">
      <Heading level="1" size="small">
        Brevbehandler
      </Heading>
      <div>
        {alleBrevForSak.isPending && <Label>Henter alle brev for saken...</Label>}
        {alleBrevForSak.isError && (
          <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle brev for saken"} />
        )}
        {alleBrevForSak.isSuccess && <Saksbrev brev={alleBrevForSak.data} sakId={properties.sakId} />}
      </div>
    </VStack>
  );
};

const Saksbrev = (properties: { sakId: string; brev: BrevInfo[] }) => {
  const [åpenBrevItem, setÅpenBrevItem] = useState<Nullable<number>>(null);
  const navigate = useNavigate({ from: Route.fullPath });

  const handleOpenChange = (brevId: number) => (isOpen: boolean) => {
    setÅpenBrevItem(isOpen ? brevId : null);

    if (isOpen) {
      navigate({
        to: "/saksnummer/$saksId/brevbehandler/$brevId",
        params: { saksId: properties.sakId, brevId: brevId.toString() },
      });
    } else {
      navigate({
        to: "/saksnummer/$saksId/brevbehandler",
        params: { saksId: properties.sakId },
      });
    }
  };

  if (properties.brev.length === 0) {
    return <Alert variant="info">Fant ingen brev som er under behandling</Alert>;
  }

  return (
    <Accordion>
      {properties.brev.map((brev) => (
        <BrevItem
          brev={brev}
          key={brev.id}
          onOpenChange={handleOpenChange(brev.id)}
          open={åpenBrevItem === brev.id}
          sakId={properties.sakId}
        />
      ))}
    </Accordion>
  );
};

const BrevItem = (properties: {
  sakId: string;
  brev: BrevInfo;
  open: boolean;
  onOpenChange: (isOpen: boolean) => void;
}) => {
  const [erFerdigstilt, setErFerdigstilt] = useState<boolean>(false);

  return (
    <Accordion.Item onOpenChange={() => properties.onOpenChange(!properties.open)} open={properties.open}>
      <Accordion.Header>
        <VStack gap="2">
          <Brevtilstand />
          <Label size="small">{properties.brev.brevkode}</Label>
        </VStack>
      </Accordion.Header>
      <Accordion.Content>
        <VStack gap="8">
          <VStack gap="4">
            <div>
              <BodyShort
                css={css`
                  color: var(--a-grayalpha-700);
                `}
              >
                Mottaker
              </BodyShort>
              <HStack gap="2">
                <BodyShort>En mottaker</BodyShort>
                {!erFerdigstilt && <PencilIcon fontSize="24px" />}
              </HStack>
            </div>

            <Switch
              checked={erFerdigstilt}
              onChange={(event) => {
                if (event.target.checked) {
                  setErFerdigstilt(true);
                } else {
                  setErFerdigstilt(false);
                }
              }}
            >
              Lås for redigering
            </Switch>

            {!erFerdigstilt && (
              <VStack
                css={css`
                  align-items: flex-start;
                `}
                gap="4"
              >
                <Button
                  css={css`
                    color: #23262a;
                    border-color: #23262a;
                    box-shadow: inset 0 0 0 2px #23262a;
                  `}
                  onClick={() => {
                    //TODO: Implementer oppdatering av data
                    console.log("Oppdaterer data");
                  }}
                  size="small"
                  type="button"
                  variant="secondary"
                >
                  Oppdater data
                </Button>
                <Link
                  className="navds-button navds-button--small navds-label navds-label--small"
                  css={css`
                    color: #23262a;
                    border-color: #23262a;
                    box-shadow: inset 0 0 0 2px #23262a;
                  `}
                  params={{ saksId: properties.sakId, brevId: properties.brev.id }}
                  to="/saksnummer/$saksId/brev/$brevId"
                >
                  Fortsett redigering
                </Link>
              </VStack>
            )}

            {erFerdigstilt && (
              <RadioGroup description={"Distribusjon"} legend="" size="small">
                <Radio value={DistribusjonsMetode.Sentralprint}>Sentralprint</Radio>
                <Radio value={DistribusjonsMetode.Lokaltprint}>Lokaltprint</Radio>
              </RadioGroup>
            )}
          </VStack>

          <div>
            <BodyShort
              css={css`
                color: var(--a-grayalpha-700);
              `}
            >
              Sist endret:{" "}
              {isDateToday(properties.brev.sistredigert)
                ? formatStringDateWithTime(properties.brev.sistredigert)
                : formatStringDate(properties.brev.sistredigert)}{" "}
              av {properties.brev.sistredigertAv}
            </BodyShort>
            <BodyShort
              css={css`
                color: var(--a-grayalpha-700);
              `}
            >
              Brev opprettet: {formatStringDate(properties.brev.opprettet)}
            </BodyShort>
          </div>
        </VStack>
      </Accordion.Content>
    </Accordion.Item>
  );
};

const Brevtilstand = () => {
  return (
    <Tag
      css={css`
        align-self: flex-start;
      `}
      size="small"
      variant="info"
    >
      Tilstand
    </Tag>
  );
};
