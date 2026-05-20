import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Hide, Label, Switch, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrevAttestering, getBrevReservasjon, oppdaterBrev } from "~/api/brev-queries";
import { attesterBrev } from "~/api/sak-api-endpoints";
import {
  createLetterSnapshot,
  createSaksbehandlerValgEndretHistoryEntry,
  type LetterSnapshot,
} from "~/Brevredigering/LetterEditor/history";
import { ApiError } from "~/components/ApiError";
import ArkivertBrev from "~/components/ArkivertBrev";
import AttestForbiddenModal from "~/components/AttestForbiddenModal";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { CenteredLoader } from "~/components/CenteredLoader";
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
import {
  type BrevResponse,
  type OppdaterBrevRequest,
  type ReservasjonResponse,
  type SaksbehandlerValg,
} from "~/types/brev";
import { type AttestForbiddenReason } from "~/utils/parseAttest403";
import { queryFold } from "~/utils/tanstackUtils";
import { trackEvent } from "~/utils/umami";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/redigering")({
  component: () => <VedtakWrapper />,
});

const vedtakSidemenySchema = z.object({
  attestantSignatur: z.string().min(1, "Underskrift må oppgis"),
  saksbehandlerValg: z.custom<SaksbehandlerValg>(),
});

type VedtakSidemenyFormData = z.infer<typeof vedtakSidemenySchema>;
type OppdaterBrevMutationVariables = OppdaterBrevRequest & {
  historySnapshot?: LetterSnapshot;
};

const VedtakWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const { vedtaksId, enhetsId } = Route.useSearch();

  const hentBrevQuery = useQuery({
    ...getBrevAttestering(saksId, Number(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
  });

  return queryFold({
    query: hentBrevQuery,
    initial: () => null,
    pending: () => (
      <Box asChild background="default" paddingBlock="space-32 space-0">
        <CenteredLoader label="Henter brev..." verticalStrategy="flexGrow" />
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
                search: { vedtaksId, enhetsId, brevId: Number(brevId) },
              })
            }
            reservasjon={err.response.data as ReservasjonResponse}
          />
        );
      }

      if (err.response?.status === 409) {
        return <ArkivertBrev saksId={saksId} />;
      }

      if (err.response?.status === 403) {
        const axiosError = err as AxiosError & {
          forbidReason?: AttestForbiddenReason;
        };
        const reason = axiosError.forbidReason;
        if (reason) {
          return (
            <AttestForbiddenModal
              onClose={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId },
                  search: { vedtaksId, enhetsId, brevId: Number(brevId) },
                })
              }
              reason={reason}
            />
          );
        }
      }

      return (
        <Box background="default" flexGrow="1">
          <ApiError error={err} title="En feil skjedde ved henting av vedtaksbrev" />
        </Box>
      );
    },
    success: (brev) => {
      const brevUtenAttestantSignatur = {
        ...brev,
        redigertBrev: {
          ...brev.redigertBrev,
          signatur: {
            ...brev.redigertBrev.signatur,
            attesterendeSaksbehandlerNavn: undefined,
          },
        },
      };
      return (
        <ManagedLetterEditorContextProvider brev={brevUtenAttestantSignatur}>
          <Vedtak brev={brevUtenAttestantSignatur} doReload={hentBrevQuery.refetch} saksId={saksId} />
        </ManagedLetterEditorContextProvider>
      );
    },
  });
};

const Vedtak = (props: { saksId: string; brev: BrevResponse; doReload: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();
  const attesteringStartTime = useRef(Date.now());

  const [forbidReason, setForbidReason] = useState<AttestForbiddenReason | null>(null);
  const [unexpectedError, setUnexpectedError] = useState<AxiosError | null>(null);

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.debug === "true" || search?.debug === true,
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(props.brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(props.brev.info.id),
    refetchInterval: 10_000,
  });

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: { ...editorState.saksbehandlerValg },
      attestantSignatur: "",
    }),
    [editorState.saksbehandlerValg],
  );

  const form = useForm<VedtakSidemenyFormData>({
    resolver: zodResolver(vedtakSidemenySchema),
    defaultValues: defaultValuesModelEditor,
  });

  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevMutationVariables>({
    mutationFn: (values) => {
      setEditorState((s) => ({ ...s, saveStatus: "SAVE_PENDING" }));
      return oppdaterBrev({
        saksId: Number.parseInt(props.saksId, 10),
        brevId: props.brev.info.id,
        frigiReservasjon: false,
        request: {
          redigertBrev: values.redigertBrev,
          saksbehandlerValg: values.saksbehandlerValg,
        },
      });
    },
    onSuccess: (response, variables) => {
      const historySnapshot = variables.historySnapshot;
      onSaveSuccess(
        response,
        historySnapshot
          ? {
              createHistoryEntry: () =>
                createSaksbehandlerValgEndretHistoryEntry(historySnapshot, createLetterSnapshot(response)),
            }
          : undefined,
      );
    },
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  const attesterMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevRequest>({
    mutationFn: (requestData) =>
      attesterBrev({
        saksId: props.saksId,
        brevId: props.brev.info.id,
        request: requestData,
      }),

    onSuccess: (response) => onSaveSuccess(response),
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

  const freeze =
    oppdaterBrevMutation.isPending || attesterMutation.isPending || editorState.saveStatus === "SAVE_PENDING";
  const error = oppdaterBrevMutation.isError || attesterMutation.isError;

  const saveDirtyLetter = (state: {
    redigertBrev: typeof editorState.redigertBrev;
    saksbehandlerValg: typeof editorState.saksbehandlerValg;
  }) =>
    oppdaterBrev({
      saksId: Number.parseInt(props.saksId, 10),
      brevId: props.brev.info.id,
      frigiReservasjon: false,
      request: {
        redigertBrev: state.redigertBrev,
        saksbehandlerValg: state.saksbehandlerValg,
      },
    });
  // TODO: disable BrevmalAlternativer during SAVE_PENDING

  useEffect(() => {
    form.reset({
      ...defaultValuesModelEditor,
      attestantSignatur: form.getValues("attestantSignatur"),
    });
  }, [defaultValuesModelEditor, form]);

  useEffect(() => {
    form.setValue("attestantSignatur", editorState.redigertBrev.signatur.attesterendeSaksbehandlerNavn ?? "", {
      shouldValidate: form.formState.isSubmitted,
    });
  }, [editorState.redigertBrev.signatur.attesterendeSaksbehandlerNavn, form]);

  return (
    <form
      onSubmit={form.handleSubmit((v) => {
        onSubmit(v, () => {
          const varighetSekunder = Math.round((Date.now() - attesteringStartTime.current) / 1000);
          trackEvent("tid brukt i attestering", {
            brevId: props.brev.info.id,
            brevkode: props.brev.info.brevkode,
            varighetSekunder,
            varighetMinutter: Math.round(varighetSekunder / 60),
          });
          trackEvent("brev attestert", {
            brevId: props.brev.info.id,
            brevkode: props.brev.info.brevkode,
          });
          navigate({
            to: "/saksnummer/$saksId/attester/$brevId/forhandsvisning",
            params: {
              saksId: props.saksId,
              brevId: props.brev.info.id.toString(),
            },
            search: {
              vedtaksId: props.brev.info?.vedtaksId?.toString(),
              enhetsId: props.brev.info.avsenderEnhet.enhetNr.toString(),
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
            <VStack gap="space-32">
              <Heading size="small">{props.brev.info.brevtittel}</Heading>
              <VStack gap="space-16">
                <OppsummeringAvMottaker mottaker={props.brev.info.mottaker} saksId={props.saksId} withTitle />
                <VStack>
                  <Label size="small">Distribusjonstype</Label>
                  <BodyShort size="small">{props.brev.info.distribusjonstype}</BodyShort>
                </VStack>
              </VStack>
              <Divider />
              <VStack gap="space-20">
                <Hide above="sm" asChild>
                  <Switch size="small">Marker tekst som er lagt til manuelt</Switch>
                </Hide>
                <Hide above="sm" asChild>
                  <Switch size="small">Vis slettet tekst</Switch>
                </Hide>
                <UnderskriftTextField
                  controlled
                  error={form.formState.errors.attestantSignatur?.message}
                  of="Attestant"
                />
              </VStack>
              <Divider />
              <VStack>
                <BrevmalAlternativer
                  brevkode={props.brev.info.brevkode}
                  submitOnChange={() => {
                    oppdaterBrevMutation.mutate({
                      redigertBrev: editorState.redigertBrev,
                      saksbehandlerValg: form.getValues("saksbehandlerValg"),
                      historySnapshot: createLetterSnapshot(editorState),
                    });
                  }}
                  withTitle
                />
              </VStack>
            </VStack>
          </FormProvider>
        }
        right={
          <>
            <ManagedLetterEditor
              brev={props.brev}
              error={error}
              freeze={freeze}
              saveDirtyLetter={saveDirtyLetter}
              showDebug={showDebug}
            />
            {/* Modal som ikke tar opp plass i DOM her */}
            <ReservertBrevError
              doRetry={props.doReload}
              onNeiClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId: props.saksId },
                  search: {
                    vedtaksId: props.brev.info?.vedtaksId?.toString(),
                    enhetsId: props.brev.info.avsenderEnhet.enhetNr.toString(),
                  },
                })
              }
              reservasjon={reservasjonQuery.data}
            />
          </>
        }
      />
    </form>
  );
};
