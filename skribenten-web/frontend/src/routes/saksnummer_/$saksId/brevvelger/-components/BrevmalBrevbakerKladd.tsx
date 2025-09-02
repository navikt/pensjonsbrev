import { BodyShort, Heading, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useEffect } from "react";

import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import EndreMottakerMedOppsummeringOgApiHåndtering from "~/components/EndreMottakerMedApiHåndtering";
import LetterTemplateTags from "~/components/LetterTemplateTags";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { SlettBrev } from "~/components/SlettBrev";
import { type LetterMetadata } from "~/types/apiTypes";
import type { BrevInfo } from "~/types/brev";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";
import { erBrevArkivert } from "~/utils/brevUtils";

import Oppsummeringspar from "../../kvittering/-components/Oppsummeringspar";
import type { SubmitTemplateOptions } from "../route";
import { Route } from "../route";

export const BrevmalBrevbakerKladd = (props: {
  saksId: string;
  brevId: number;
  letterTemplates: LetterMetadata[];
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const brevQuery = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(props.saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(props.saksId.toString()),
    select: (data) => data?.find((b) => b.id === props.brevId),
  });

  const brev = brevQuery.data;
  const brevExists = brev !== undefined;
  const letterMetadataForBrev = brevExists ? props.letterTemplates.find((l) => l.id === brev!.brevkode) : undefined;

  return (
    <>
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
    </>
  );
};

const BrevIkkeFunnet = (props: { brevId: number }) => {
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
  const { saksId, brev, setOnFormSubmitClick } = props;
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();

  useEffect(() => {
    setOnFormSubmitClick({
      onClick: () => {
        if (erBrevArkivert(brev)) {
          return navigate({
            to: "/saksnummer/$saksId/brevbehandler",
            params: { saksId: saksId },
            search: { brevId: brev.id, enhetsId, vedtaksId },
          });
        } else {
          return navigate({
            to: "/saksnummer/$saksId/brev/$brevId",
            params: { saksId: saksId, brevId: brev.id },
            search: { enhetsId, vedtaksId },
          });
        }
      },
    });
  }, [setOnFormSubmitClick, saksId, brev, navigate, enhetsId, vedtaksId]);

  return (
    <>
      <VStack flexGrow="1" gap="4">
        <VStack gap="4">
          {!erBrevArkivert(props.brev) && (
            <SlettBrev
              brevId={props.brev.id}
              buttonText="Slett kladd"
              modalTexts={{
                heading: "Vil du slette kladden?",
                body: "Kladden vil bli slettet, og du kan ikke angre denne handlingen.",
                buttonYes: "Ja, slett kladden",
                buttonNo: "Nei, behold kladden",
              }}
              onSlettSuccess={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevvelger",
                  params: { saksId: props.saksId },
                  search: { enhetsId, vedtaksId },
                })
              }
              sakId={props.saksId}
            />
          )}
          <VStack gap="2">
            <Heading size="small">{props.brev.brevtittel}</Heading>
            {props.letterMetadata ? (
              <LetterTemplateTags letterTemplate={props.letterMetadata} />
            ) : (
              <BodyShort>Fant ikke brev-metadata for å finne brevsystem</BodyShort>
            )}
          </VStack>
        </VStack>
        <Divider />
        <VStack gap="8">
          {erBrevArkivert(props.brev) ? (
            <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle />
          ) : (
            <EndreMottakerMedOppsummeringOgApiHåndtering
              brev={props.brev}
              saksId={props.saksId}
              withGap
              withOppsummeringTitle
            />
          )}
          <Oppsummeringspar
            boldedTitle
            size="small"
            tittel={"Avsenderenhet"}
            verdi={props.brev.avsenderEnhet?.navn ?? "Enhet er ikke registrert i brevet"}
          />
          <Oppsummeringspar boldedTitle size="small" tittel={"Språk"} verdi={SPRAAK_ENUM_TO_TEXT[props.brev.spraak]} />
        </VStack>
      </VStack>
    </>
  );
};
