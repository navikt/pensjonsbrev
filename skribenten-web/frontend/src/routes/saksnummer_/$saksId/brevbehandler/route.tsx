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
import { useState } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BrevInfo } from "~/types/brev";

import { DistribusjonsMetode } from "./-BrevbehandlerUtils";

export const Route = createFileRoute("/saksnummer/$saksId/brevbehandler")({
  component: Brevbehandler,
});

function Brevbehandler() {
  const { saksId } = Route.useParams();

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
          min-width: 432px;
          max-width: 720px;
          flex: 1;
          border-left: 1px solid var(--a-gray-200);
          border-right: 1px solid var(--a-gray-200);

          background: pink;
        }
      `}
    >
      <BrevbehandlerMeny sakId={saksId} />
      <div>
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
        {alleBrevForSak.isError && (
          <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle brev for saken"} />
        )}
        {alleBrevForSak.isPending && <Label>Henter alle brev for saken...</Label>}
        {alleBrevForSak.isSuccess && <Saksbrev brev={alleBrevForSak.data} sakId={properties.sakId} />}
      </div>
    </VStack>
  );
};

const Saksbrev = (properties: { sakId: string; brev: BrevInfo[] }) => {
  if (properties.brev.length === 0) {
    return <Alert variant="info">Fant ingen brev som er under behandling</Alert>;
  }

  return (
    <Accordion>
      {properties.brev.map((brev) => (
        <BrevItem brev={brev} key={brev.id} sakId={properties.sakId} />
      ))}
    </Accordion>
  );
};

const BrevItem = (properties: { sakId: string; brev: BrevInfo }) => {
  const [erFerdigstilt, setErFerdigstilt] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <Accordion.Item>
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
                  navigate({
                    to: "/saksnummer/$saksId/brevbehandler/$brevId",
                    params: { saksId: properties.sakId, brevId: properties.brev.id.toString() },
                  });
                } else {
                  setErFerdigstilt(false);
                  navigate({
                    to: "/saksnummer/$saksId/brevbehandler",
                    params: { saksId: properties.sakId },
                  });
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
              Sist endret: {properties.brev.sistredigert} av {properties.brev.sistredigertAv}
            </BodyShort>
            <BodyShort
              css={css`
                color: var(--a-grayalpha-700);
              `}
            >
              Brev opprettet: {properties.brev.opprettet}
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