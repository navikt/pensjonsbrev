import { css } from "@emotion/react";
import { BodyShort, Heading, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useEffect } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import LetterTemplateTags from "~/components/LetterTemplateTags";
import { SlettBrev } from "~/components/SlettBrev";
import { type LetterMetadata } from "~/types/apiTypes";
import type { BrevInfo } from "~/types/brev";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import Oppsummeringspar from "../../kvittering/-components/Oppsummeringspar";
import type { SubmitTemplateOptions } from "../route";
import { Route } from "../route";

export const BrevmalBrevbakerKladd = (props: {
  saksId: string;
  brevId: string;
  letterTemplates: LetterMetadata[];
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const brevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(props.saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(props.saksId.toString()),
    select: (data) => data?.find((b) => b.id.toString() === props.brevId),
  });

  const brev = brevQuery.data;
  const brevExists = brev !== undefined;
  const letterMetadataForBrev = brevExists ? props.letterTemplates.find((l) => l.id === brev!.brevkode) : undefined;

  return (
    <div>
      {brevQuery.isPending && <Loader />}
      {brevQuery.isError && <ApiError error={brevQuery.error} title="Klarte ikke hente brev" />}
      {brevQuery.isSuccess && !brevExists && <BrevIkkeFunnet brevId={props.brevId} />}
      {brevQuery.isSuccess && brevExists && (
        <Brevmal
          brev={brev}
          letterMetadata={letterMetadataForBrev}
          saksId={props.saksId}
          setOnFormSubmitClick={props.setOnFormSubmitClick}
        />
      )}
    </div>
  );
};

const BrevIkkeFunnet = (props: { brevId: string }) => {
  return (
    <div>
      <BodyShort>Brev med id {props.brevId} ble ikke funnet</BodyShort>
    </div>
  );
};

const Brevmal = (props: {
  saksId: string;
  brev: BrevInfo;
  letterMetadata?: LetterMetadata;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const navigate = useNavigate({ from: Route.fullPath });

  useEffect(() => {
    props.setOnFormSubmitClick({
      onClick: () => {
        navigate({
          to: "/saksnummer/$saksId/brev/$brevId",
          params: { saksId: props.saksId, brevId: props.brev.id },
        });
      },
    });
  }, [props.setOnFormSubmitClick]);

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
            {props.letterMetadata ? (
              <LetterTemplateTags letterTemplate={props.letterMetadata} />
            ) : (
              <BodyShort>Fant ikke brev-metadata for å finne brevsystem</BodyShort>
            )}
          </div>
        </VStack>

        <Divider />

        <VStack gap="8">
          <EndreMottakerMedOppsummeringOgApiHåndtering brev={props.brev} saksId={props.saksId} withOppsummeringTitle />
          <Oppsummeringspar
            boldedTitle
            size="small"
            tittel={"Avsenderenhet"}
            verdi={props.brev.avsenderEnhet?.navn ?? "Enhet er ikke registrert i brevet"}
          />
          <Oppsummeringspar boldedTitle size="small" tittel={"Språk"} verdi={SPRAAK_ENUM_TO_TEXT[props.brev.spraak]} />
        </VStack>
      </VStack>
    </div>
  );
};
