import { ArrowCirclepathReverseIcon, PencilIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading, HStack, Label, Loader, Spacer, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useEffect } from "react";

import { hentAlleBrevInfoForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import LetterTemplateTags from "~/components/LetterTemplateTags";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { SlettBrev } from "~/components/SlettBrev";
import { useEndreMottaker } from "~/hooks/useEndreMottaker";
import { type LetterMetadata } from "~/types/apiTypes";
import { type BrevInfo } from "~/types/brev";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";
import { erBrevArkivert } from "~/utils/brevUtils";
import { truncatedSha256Hash } from "~/utils/hashUtils";
import { trackEvent } from "~/utils/umami";

import Oppsummeringspar from "../../kvittering/-components/Oppsummeringspar";
import { Route, type SubmitTemplateOptions } from "../route";

export const BrevmalBrevbakerKladd = (props: {
  saksId: string;
  brevId: number;
  brevmetadata: Record<string, LetterMetadata>;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const brevQuery = useQuery({
    queryKey: hentAlleBrevInfoForSak.queryKey(props.saksId.toString()),
    queryFn: () => hentAlleBrevInfoForSak.queryFn(props.saksId.toString()),
    select: (data) => data?.find((b) => b.id === props.brevId),
  });

  const brev = brevQuery.data;
  const brevExists = brev !== undefined;
  const letterMetadataForBrev = brevExists ? props.brevmetadata[brev.brevkode] : undefined;

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

  const {
    modalÅpen,
    åpneModal,
    lukkModal,
    endreMottaker,
    resetEndreMottaker,
    endreMottakerError,
    endreMottakerIsPending,
    fjernMottaker,
    fjernMottakerIsPending,
  } = useEndreMottaker(props.saksId, props.brev.id);

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
    <VStack flexGrow="1" gap="space-16">
      <VStack align="start" gap="space-16">
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
        <VStack align="start" gap="space-8">
          <Heading size="small">{props.brev.brevtittel}</Heading>
          <HStack gap="space-8">
            {props.letterMetadata ? (
              <LetterTemplateTags letterTemplate={props.letterMetadata} />
            ) : (
              <BodyShort>Fant ikke brev-metadata for å finne brevsystem</BodyShort>
            )}
            {props.letterMetadata?.redigerbart && (
              <Tag data-color="neutral" size="small" variant="moderate">
                Redigerbar
              </Tag>
            )}
          </HStack>
        </VStack>
      </VStack>
      <Divider />
      <VStack gap="space-32">
        {erBrevArkivert(props.brev) ? (
          <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle />
        ) : (
          <VStack gap="space-8">
            {modalÅpen && (
              <EndreMottakerModal
                error={endreMottakerError}
                isPending={endreMottakerIsPending}
                onBekreftNyMottaker={endreMottaker}
                onClose={lukkModal}
                resetOnBekreftState={resetEndreMottaker}
                åpen={modalÅpen}
              />
            )}
            <HStack align="center">
              <Label size="small">Mottaker</Label>
              <Spacer />
              {props.brev.mottaker !== null && (
                <Button
                  disabled={fjernMottakerIsPending}
                  icon={<ArrowCirclepathReverseIcon />}
                  iconPosition="right"
                  loading={fjernMottakerIsPending}
                  onClick={async () => {
                    trackEvent("tilbakestill mottaker klikket", {
                      kontekst: "kladd",
                      saksId: await truncatedSha256Hash(props.saksId),
                    });
                    fjernMottaker();
                  }}
                  size="xsmall"
                  type="button"
                  variant="tertiary"
                >
                  Tilbakestill
                </Button>
              )}
              <Button
                icon={<PencilIcon />}
                iconPosition="right"
                onClick={() => {
                  trackEvent("endre mottaker klikket", { kontekst: "kladd", saksId: props.saksId });
                  åpneModal();
                }}
                size="xsmall"
                type="button"
                variant="tertiary"
              >
                Endre
              </Button>
            </HStack>
            <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle={false} />
          </VStack>
        )}
        <Oppsummeringspar boldedTitle size="small" tittel="Avsenderenhet" verdi={props.brev.avsenderEnhet.navn} />
        <Oppsummeringspar boldedTitle size="small" tittel="Språk" verdi={SPRAAK_ENUM_TO_TEXT[props.brev.spraak]} />
      </VStack>
    </VStack>
  );
};
