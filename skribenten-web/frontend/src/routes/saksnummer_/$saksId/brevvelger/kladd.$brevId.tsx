import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading, HStack, Loader, Tag, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useState } from "react";

import { delvisOppdaterBrev, fjernOverstyrtMottaker, hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import { mapEndreMottakerValueTilMottaker } from "~/types/AdresseUtils";
import { BrevSystem, type LetterMetadata } from "~/types/apiTypes";
import type { BrevInfo, DelvisOppdaterBrevResponse, Mottaker } from "~/types/brev";

import { SlettBrev } from "../brevbehandler/-components/PDFViewerTopBar";
import Oppsummeringspar from "../kvittering/-components/Oppsummeringspar";
import { EndreMottakerModal } from "./$templateId/-components/endreMottaker/EndreMottaker";

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
  });

  const brevExists = brevQuery?.data?.some((b) => b.id.toString() === brevId) ?? false;
  const brev = brevExists ? brevQuery!.data!.find((b) => b.id.toString() === brevId) : undefined;
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
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);

  const mottakerMutation = useMutation<DelvisOppdaterBrevResponse, AxiosError, Mottaker>({
    mutationFn: (mottaker) =>
      delvisOppdaterBrev({
        saksId: props.saksId,
        brevId: props.brev.id,
        mottaker: mottaker,
      }),
    onSuccess: (response) => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? response.info : brev)),
      );
      setModalÅpen(false);
    },
  });

  const fjernMottakerMutation = useMutation<void, AxiosError>({
    mutationFn: () => fjernOverstyrtMottaker({ saksId: props.saksId, brevId: props.brev.id }),
    onSuccess: () => {
      queryClient.setQueryData(hentAlleBrevForSak.queryKey(props.saksId), (currentBrevInfo: BrevInfo[]) =>
        currentBrevInfo.map((brev) => (brev.id === props.brev.id ? { ...props.brev, mottaker: null } : brev)),
      );
    },
  });

  return (
    <div
      css={css`
        display: grid;
        grid-template-rows: 1fr auto;
        height: 100%;
      `}
    >
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

        <Heading size="small">{props.brev.brevtittel}</Heading>
        <BrevTags letterMetadata={props.letterMetadata} />
        <Divider />

        <VStack gap="2">
          <VStack>
            <OppsummeringAvMottaker mottaker={props.brev.mottaker} saksId={props.saksId} withTitle />

            {modalÅpen && (
              <EndreMottakerModal
                error={mottakerMutation.error}
                isPending={mottakerMutation.isPending}
                onBekreftNyMottaker={(mottaker) => {
                  mottakerMutation.mutate(mapEndreMottakerValueTilMottaker(mottaker));
                }}
                onClose={() => setModalÅpen(false)}
                resetOnBekreftState={() => mottakerMutation.reset()}
                åpen={modalÅpen}
              />
            )}
          </VStack>
          <HStack>
            <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
              Endre mottaker
            </Button>
            {props.brev.mottaker !== null && (
              <Button
                loading={fjernMottakerMutation.isPending}
                onClick={() => fjernMottakerMutation.mutate()}
                size="small"
                type="button"
                variant="tertiary"
              >
                Tilbakestill mottaker
              </Button>
            )}
          </HStack>
        </VStack>

        <Oppsummeringspar boldedTitle tittel={"Avsenderenhet"} verdi={"TODO"} />
        <Oppsummeringspar boldedTitle tittel={"Språk"} verdi={"TODO"} />
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

const BrevsystemTag = (props: { letterMetadata: LetterMetadata }) => {
  switch (props.letterMetadata.brevsystem) {
    case BrevSystem.Exstream: {
      return (
        <Tag size="small" variant="alt1-moderate">
          Exstream
        </Tag>
      );
    }
    case BrevSystem.DokSys: {
      return (
        <Tag size="small" variant="alt3-moderate">
          Doksys
        </Tag>
      );
    }
    case BrevSystem.Brevbaker: {
      return (
        <Tag size="small" variant="alt2-moderate">
          Brevbaker
        </Tag>
      );
    }
  }
};

const BrevTags = (props: { letterMetadata: LetterMetadata }) => (
  <HStack gap="2">
    <Tag size="small" variant="warning">
      Kladd
    </Tag>
    <BrevsystemTag letterMetadata={props.letterMetadata} />
  </HStack>
);
