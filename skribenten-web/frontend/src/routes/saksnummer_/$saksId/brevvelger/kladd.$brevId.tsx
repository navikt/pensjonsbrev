import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading, HStack, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import LetterTemplateTags from "~/components/LetterTemplateTags";
import { SlettBrev } from "~/components/SlettBrev";
import { type LetterMetadata } from "~/types/apiTypes";
import type { BrevInfo } from "~/types/brev";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import Oppsummeringspar from "../kvittering/-components/Oppsummeringspar";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger/kladd/$brevId")({
  loader: async ({ context: { queryClient, getSakContextQueryOptions } }) => {
    const sakContext = await queryClient.ensureQueryData(getSakContextQueryOptions);
    return { letterTemplates: sakContext.brevMetadata };
  },
  component: KladdBrev,
});

function KladdBrev() {
  const { saksId, brevId } = Route.useParams();
  const { letterTemplates } = Route.useLoaderData();

  const brevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(saksId.toString()),
    select: (data) => data?.find((b) => b.id.toString() === brevId),
  });

  const brev = brevQuery.data;
  const brevExists = brev !== undefined;
  const letterMetadataForBrev = brevExists ? letterTemplates.find((l) => l.id === brev!.brevkode) : undefined;

  return (
    <div>
      {brevQuery.isPending && <Loader />}
      {brevQuery.isError && <ApiError error={brevQuery.error} title="Klarte ikke hente brev" />}
      {brevQuery.isSuccess && brevExists && (
        <Brevmal brev={brev!} letterMetadata={letterMetadataForBrev!} saksId={saksId} />
      )}
      {brevQuery.isSuccess && !brevExists && <BrevIkkeFunnet brevId={brevId} />}
    </div>
  );
}

const BrevIkkeFunnet = (props: { brevId: string }) => {
  return (
    <div>
      <BodyShort>Brev med id {props.brevId} ble ikke funnet</BodyShort>
    </div>
  );
};

const Brevmal = (props: { saksId: string; brev: BrevInfo; letterMetadata: LetterMetadata }) => {
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <div
      css={css`
        display: grid;
        grid-template-rows: 1fr auto;
        height: 100%;
      `}
    >
      <VStack gap="4">
        <VStack gap="4">
          <SlettBrev
            brevId={props.brev.id}
            buttonText="Slett kladd"
            modalTexts={{
              heading: "Vil du slette kladden?",
              body: "Kladden vil bli slettet, og du kan ikke angre denne handlingen.",
              buttonYes: "Ja, slett kladden",
              buttonNo: "Nei, behold kladden",
            }}
            onSlettSuccess={() => navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId: props.saksId } })}
            sakId={props.saksId}
          />

          <div>
            <Heading size="small">{props.brev.brevtittel}</Heading>
            <LetterTemplateTags letterTemplate={props.letterMetadata} />
          </div>
        </VStack>

        <Divider />

        <VStack gap="8">
          <EndreMottakerMedOppsummeringOgApiHåndtering brev={props.brev} saksId={props.saksId} withOppsummeringTitle />
          <Oppsummeringspar
            boldedTitle
            tittel={"Avsenderenhet"}
            verdi={props.brev.avsenderEnhet?.navn ?? "Enhet er ikke registrert i brevet"}
          />
          <Oppsummeringspar boldedTitle tittel={"Språk"} verdi={SPRAAK_ENUM_TO_TEXT[props.brev.spraak]} />
        </VStack>
      </VStack>

      <HStack justify={"end"}>
        <Button
          onClick={() => {
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId: props.saksId },
            });
          }}
          size="small"
          type="button"
          variant="tertiary"
        >
          Gå til brevbehandler
        </Button>
        <Button size="small">
          <HStack
            align={"center"}
            onClick={() => {
              navigate({
                to: "/saksnummer/$saksId/brev/$brevId",
                params: { saksId: props.saksId, brevId: props.brev.id },
              });
            }}
          >
            Åpne brev <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
          </HStack>
        </Button>
      </HStack>
    </div>
  );
};