import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Label, Loader, Switch, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { getBrevAttesteringQuery, getBrevReservasjon, oppdaterSaksbehandlerValg } from "~/api/brev-queries";
import { attesterBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import ArkivertBrev from "~/components/ArkivertBrev";
import AttestForbiddenModal from "~/components/AttestForbiddenModal";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { Divider } from "~/components/Divider";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { UnderskriftTextField } from "~/components/ManagedLetterEditor/UnderskriftTextField";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ReservertBrevError from "~/components/ReservertBrevError";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import type { BrevResponse, OppdaterBrevRequest, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import type { AttestForbiddenReason } from "~/utils/parseAttest403";
import { queryFold } from "~/utils/tanstackUtils";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/redigering")({
  component: () => <VedtakWrapper />,
});

interface VedtakSidemenyFormData {
  attestantSignatur: string;
  saksbehandlerValg: SaksbehandlerValg;
}

const VedtakWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const { vedtaksId, enhetsId } = Route.useSearch();

  const hentBrevQuery = useQuery({
    ...getBrevAttesteringQuery(saksId, Number(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
  });

  return queryFold({
    query: hentBrevQuery,
    initial: () => null,
    pending: () => (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <VStack align="center" gap="1">
          <Loader size="3xlarge" title="henter brev..." />
          <Heading size="large">Henter brev....</Heading>
        </VStack>
      </Box>
    ),
    error: (err) => {
      if (err.response?.status === 423 && err.response?.data) {
        return (
          <ReservertBrevError
            doRetry={hentBrevQuery.refetch}
            onNeiClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brevbehandler",
                params: { saksId },
                search: { vedtaksId, enhetsId },
              })
            }
            reservasjon={err.response.data as ReservasjonResponse}
          />
        );
      }
      if (err.response?.status === 409) {
        return <ArkivertBrev saksId={saksId} />;
      }

      return (
        <Box
          background="bg-default"
          css={css`
            display: flex;
            flex-direction: column;
            flex: 1;
            align-items: center;
            padding-top: var(--a-spacing-8);
          `}
        >
          <ApiError error={err} title={"En feil skjedde ved henting av vedtaksbrev"} />
        </Box>
      );
    },
    success: (brev) => (
      <ManagedLetterEditorContextProvider brev={brev}>
        <Vedtak brev={brev} doReload={hentBrevQuery.refetch} saksId={saksId} />
      </ManagedLetterEditorContextProvider>
    ),
  });
};

const Vedtak = (props: { saksId: string; brev: BrevResponse; doReload: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { editorState, onSaveSuccess } = useManagedLetterEditorContext();

  const [forbidReason, setForbidReason] = useState<AttestForbiddenReason | null>(null);
  const [unexpectedError, setUnexpectedError] = useState<AxiosError | null>(null);

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(props.brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(props.brev.info.id),
    refetchInterval: 10_000,
  });

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: { ...props.brev.saksbehandlerValg },
      attestantSignatur: props.brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn,
    }),
    [props.brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn, props.brev.saksbehandlerValg],
  );

  const form = useForm<VedtakSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  const saksbehandlerValgMutation = useMutation<BrevResponse, AxiosError, SaksbehandlerValg>({
    mutationFn: (saksbehandlerValg) => oppdaterSaksbehandlerValg(props.brev.info.id, saksbehandlerValg),
    onSuccess: (response) => onSaveSuccess(response),
  });

  const attesterMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevRequest>({
    mutationFn: (requestData) =>
      attesterBrev({ saksId: props.saksId, brevId: props.brev.info.id, request: requestData }),

    onSuccess: onSaveSuccess,
    onError: (err) => {
      const reason = (err as AxiosError & { forbidReason?: AttestForbiddenReason }).forbidReason;

      if (reason) {
        setForbidReason(reason);
        return;
      }
      setUnexpectedError(err);
    },
  });

  const onSubmit = (values: VedtakSidemenyFormData, onSuccess?: () => void) => {
    attesterMutation.mutate(
      {
        saksbehandlerValg: values.saksbehandlerValg,
        redigertBrev: editorState.redigertBrev,
      },
      { onSuccess: onSuccess },
    );
  };

  const freeze = saksbehandlerValgMutation.isPending || attesterMutation.isPending;
  const error = saksbehandlerValgMutation.isError || attesterMutation.isError;

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  return (
    <form
      onSubmit={form.handleSubmit((v) => {
        onSubmit(v, () => {
          navigate({
            to: "/saksnummer/$saksId/attester/$brevId/forhandsvisning",
            params: { saksId: props.saksId, brevId: props.brev.info.id.toString() },
            search: {
              vedtaksId: props.brev.info?.vedtaksId?.toString(),
              enhetsId: props.brev.info.avsenderEnhet?.enhetNr?.toString(),
            },
          });
        });
      })}
    >
      {forbidReason && <AttestForbiddenModal onClose={() => setForbidReason(null)} reason={forbidReason} />}

      {unexpectedError && <ApiError error={unexpectedError} title="Uventet feil ved attestering" />}

      <ThreeSectionLayout
        bottom={
          <Button icon={<ArrowRightIcon />} iconPosition="right" loading={freeze} size="small">
            Fortsett
          </Button>
        }
        left={
          <FormProvider {...form}>
            <VStack gap="8">
              <Heading size="small">{props.brev.redigertBrev.title}</Heading>
              <VStack gap="4">
                <OppsummeringAvMottaker mottaker={props.brev.info.mottaker} saksId={props.saksId} withTitle />
                <VStack>
                  <Label size="small">Distribusjonstype</Label>
                  <BodyShort size="small">{props.brev.info.distribusjonstype}</BodyShort>
                </VStack>
              </VStack>
              <Divider />
              <VStack gap="5">
                <Switch
                  css={css`
                    display: none;
                  `}
                  size="small"
                >
                  Marker tekst som er lagt til manuelt
                </Switch>
                <Switch
                  css={css`
                    display: none;
                  `}
                  size="small"
                >
                  Vis slettet tekst
                </Switch>
                <UnderskriftTextField of="Attestant" />
              </VStack>
              <Divider />
              <VStack>
                <BrevmalAlternativer
                  brevkode={props.brev.info.brevkode}
                  submitOnChange={() => saksbehandlerValgMutation.mutate(form.getValues("saksbehandlerValg"))}
                  withTitle
                />
              </VStack>
            </VStack>
          </FormProvider>
        }
        right={
          <>
            <ReservertBrevError
              doRetry={props.doReload}
              onNeiClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId: props.saksId },
                  search: {
                    vedtaksId: props.brev.info?.vedtaksId?.toString(),
                    enhetsId: props.brev.info.avsenderEnhet?.enhetNr?.toString(),
                  },
                })
              }
              reservasjon={reservasjonQuery.data}
            />
            <ManagedLetterEditor brev={props.brev} error={error} freeze={freeze} showDebug={showDebug} />
          </>
        }
      />
    </form>
  );
};
