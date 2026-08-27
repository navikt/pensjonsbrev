import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Hide, Label, Switch, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrevAttestering, getBrevReservasjon } from "~/api/brev-queries";
import { attesterBrev } from "~/api/sak-api-endpoints";
import { useGuardedFormSubmit } from "~/Brevredigering/hooks/useGuardedFormSubmit";
import { useOppdaterBrevAutosave } from "~/Brevredigering/hooks/useOppdaterBrevAutosave";
import { findFirstUneditedFritekstFocus } from "~/Brevredigering/LetterEditor/actions/common";
import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";
import { createLetterSnapshot } from "~/Brevredigering/LetterEditor/history";
import { useTekstvalgInsertHighlight } from "~/Brevredigering/LetterEditor/hooks/useTekstvalgInsertHighlight";
import { InsertedTekstValgHighlightProvider } from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
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
import { AktivtDokumentProvider } from "~/components/vedlegg/AktivtDokumentContext";
import { AktivtDokumentEditor } from "~/components/vedlegg/AktivtDokumentEditor";
import { BrevEditorSidepanel } from "~/components/vedlegg/BrevEditorSidepanel";
import { useDokumentEditorController } from "~/components/vedlegg/useDokumentEditorController";
import { useBrevEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { useReleaseReservationOnPageExit } from "~/hooks/useReleaseReservationOnPageExit";
import { useUserInfo } from "~/hooks/useUserInfo";
import { baseSearchSchema } from "~/routes/saksnummer_/$saksId/route";
import {
  type BrevResponse,
  type OppdaterAttesteringRequest,
  type ReservasjonResponse,
  type SaksbehandlerValg,
} from "~/types/brev";
import { type AttestForbiddenReason } from "~/utils/parseAttest403";
import { queryFold } from "~/utils/tanstackUtils";
import { trackEvent } from "~/utils/umami";

const attesteringSearchSchema = baseSearchSchema.extend({
  vedlegg: z.coerce.string().optional(),
});

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/redigering")({
  validateSearch: (search) => attesteringSearchSchema.parse(search),
  component: () => <VedtakWrapper />,
});

const vedtakSidemenySchema = z.object({
  attestantSignatur: z.string().min(1, "Underskrift må oppgis"),
  saksbehandlerValg: z.custom<SaksbehandlerValg>(),
});

type VedtakSidemenyFormData = z.infer<typeof vedtakSidemenySchema>;

const queryRetries = 3;
const shouldSkipRetry = (status: number | undefined) =>
  status === 403 || status === 404 || status === 409 || status === 422 || status === 423;

const VedtakWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const { vedtaksId, enhetsId } = Route.useSearch();

  const hentBrevQuery = useQuery({
    ...getBrevAttestering(saksId, Number(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (failureCount: number, error: AxiosError) =>
      failureCount < queryRetries && !shouldSkipRetry(error.response?.status),
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
        return (
          <ArkivertBrev
            onGaTilBrevbehandler={() =>
              navigate({
                to: "/saksnummer/$saksId/brevbehandler",
                params: { saksId },
                search: { vedtaksId, enhetsId, brevId: Number(brevId) },
              })
            }
          />
        );
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

      if (err.response?.status === 500) {
        return (
          <Box asChild background="default">
            <VStack align="center" flexGrow="1" gap="space-8" padding="space-24">
              <ApiError error={err} title="En feil skjedde ved henting av vedtaksbrev" />
              <Button
                onClick={() =>
                  navigate({
                    to: "/saksnummer/$saksId/brevbehandler",
                    params: { saksId },
                    search: { vedtaksId, enhetsId, brevId: Number(brevId) },
                  })
                }
                size="small"
                variant="secondary"
              >
                Gå til brevbehandler
              </Button>
            </VStack>
          </Box>
        );
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
  const { vedlegg: aktivVedlegg } = Route.useSearch();
  const { editorState, redigertBrev, setEditorState, onSaveSuccess, registrerNullstillLagringsfeil } =
    useManagedLetterEditorContext();
  const attesteringStartTime = useRef(Date.now());
  const currentUser = useUserInfo();

  const [forbidReason, setForbidReason] = useState<AttestForbiddenReason | null>(null);
  const [unexpectedError, setUnexpectedError] = useState<AxiosError | null>(null);

  const navigateToDocument = useCallback(
    (vedleggId: string | undefined) => navigate({ search: (prev) => ({ ...prev, vedlegg: vedleggId }), replace: true }),
    [navigate],
  );
  const dokumentEditor = useDokumentEditorController({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    aktivVedleggId: aktivVedlegg,
    navigateToDocument,
  });

  const { highlightedIds: highlightedInsertedTekstvalgIds, beforeTekstvalgChange } = useTekstvalgInsertHighlight({
    lagretRedigertBrev: props.brev.redigertBrev,
    editorState,
    setEditorState,
  });

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.debug === "true" || search?.debug === true,
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(props.brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(props.brev.info.id),
    refetchInterval: 10_000,
  });

  useReleaseReservationOnPageExit({
    enabled: reservasjonQuery.isSuccess,
    brevId: props.brev.info.id,
    currentUserNavIdent: currentUser?.navident,
    reservationOwnerNavIdent: reservasjonQuery.data?.reservertAv.id,
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

  const { getWarning } = useBrevEditorWarnings({
    brevkode: props.brev.info.brevkode,
    form,
    redigertBrev: redigertBrev,
    propertyUsage: props.brev.propertyUsage ?? undefined,
  });

  const { oppdaterBrevMutation } = useOppdaterBrevAutosave({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    setEditorState,
    onSaveSuccess,
  });

  const attesterMutation = useMutation<BrevResponse, AxiosError, OppdaterAttesteringRequest>({
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

  const onSubmit = async (values: VedtakSidemenyFormData, onSuccess?: () => void) => {
    if (!(await dokumentEditor.lagreAktivtDokument())) return;

    attesterMutation.reset();
    oppdaterBrevMutation.reset();
    attesterMutation.mutate(
      {
        saksbehandlerValg: values.saksbehandlerValg,
        redigertBrev: redigertBrev,
      },
      { onSuccess: onSuccess },
    );
  };

  const freeze = oppdaterBrevMutation.isPending || attesterMutation.isPending;
  const error = oppdaterBrevMutation.isError || attesterMutation.isError;

  const resetSaveErrors = useCallback(() => {
    attesterMutation.reset();
    oppdaterBrevMutation.reset();
  }, [attesterMutation.reset, oppdaterBrevMutation.reset]);

  // Autolagringen bor i editor-konteksten, så den må få vite hvordan rutens lagringsfeil nullstilles.
  useEffect(() => {
    registrerNullstillLagringsfeil(resetSaveErrors);
    return () => registrerNullstillLagringsfeil(null);
  }, [registrerNullstillLagringsfeil, resetSaveErrors]);

  // TODO: disable BrevmalAlternativer during SAVE_PENDING

  useEffect(() => {
    form.reset({
      ...defaultValuesModelEditor,
      attestantSignatur: form.getValues("attestantSignatur"),
    });
  }, [defaultValuesModelEditor, form]);

  useEffect(() => {
    form.setValue("attestantSignatur", redigertBrev.signatur.attesterendeSaksbehandlerNavn ?? "", {
      shouldValidate: form.formState.isSubmitted,
    });
  }, [redigertBrev.signatur.attesterendeSaksbehandlerNavn, form]);

  const proceedToForhandsvisning = () => {
    const varighetSekunder = Math.round((Date.now() - attesteringStartTime.current) / 1000);
    trackEvent("tid brukt i attestering", {
      brevId: props.brev.info.id,
      brevkode: props.brev.info.brevkode,
      varighetSekunder,
      varighetMinutter: Math.round(varighetSekunder / 60),
      enhetsId: props.brev.info.avsenderEnhet.enhetNr,
    });
    trackEvent("brev attestert", {
      brevId: props.brev.info.id,
      brevkode: props.brev.info.brevkode,
      enhetsId: props.brev.info.avsenderEnhet.enhetNr,
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
  };

  const submitAttest = (values: VedtakSidemenyFormData) => onSubmit(values, proceedToForhandsvisning);

  const { guardedSubmit, warnModalProps } = useGuardedFormSubmit({
    form,
    getWarning,
    onConfirmedSubmit: submitAttest,
    onWarnModalClosed: (warn) => {
      if (warn?.kind === "fritekst" || warn?.kind === "fritekstOgTekstValg") {
        const focus = findFirstUneditedFritekstFocus(editorState.redigertBrev);
        if (focus) {
          setEditorState((s) => ({ ...s, focus }));
        }
      }
    },
  });

  return (
    <VStack asChild height="100%">
      <form onSubmit={guardedSubmit}>
        <WarnModal {...warnModalProps} fortsettLabel="Fortsett til forhåndsvisning" />
        {forbidReason && <AttestForbiddenModal onClose={() => setForbidReason(null)} reason={forbidReason} />}

        {unexpectedError && <ApiError error={unexpectedError} title="Uventet feil ved attestering" />}

        <AktivtDokumentProvider
          aktivVedleggId={dokumentEditor.aktivVedleggId}
          onVelgDokument={dokumentEditor.velgDokument}
          redigeringsflate="attestant-redigering"
          registrerLagring={dokumentEditor.registrerLagring}
        >
          <ThreeSectionLayout
            bottom={
              <Button
                icon={<ArrowRightIcon />}
                iconPosition="right"
                loading={freeze || dokumentEditor.lagrerAktivtDokument}
                size="small"
              >
                Fortsett
              </Button>
            }
            left={
              <BrevEditorSidepanel
                brevId={props.brev.info.id}
                brevmalPanel={
                  <FormProvider {...form}>
                    <VStack gap="space-32">
                      <Heading size="small">{props.brev.info.brevtittel}</Heading>
                      <VStack gap="space-16">
                        <OppsummeringAvMottaker
                          mottaker={props.brev.info.mottaker ?? null}
                          saksId={props.saksId}
                          withTitle
                        />
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
                          propertyUsage={props.brev.propertyUsage ?? undefined}
                          submitOnChange={() => {
                            const updatedValg = form.getValues("saksbehandlerValg");
                            beforeTekstvalgChange(updatedValg, redigertBrev);
                            oppdaterBrevMutation.mutate({
                              redigertBrev: redigertBrev,
                              saksbehandlerValg: updatedValg,
                              historySnapshot: createLetterSnapshot({ ...editorState, redigertBrev }),
                            });
                          }}
                        />
                      </VStack>
                    </VStack>
                  </FormProvider>
                }
                saksId={props.saksId}
              />
            }
            right={
              <AktivtDokumentEditor
                brev={props.brev}
                freeze={freeze}
                renderBrev={() => (
                  <InsertedTekstValgHighlightProvider ids={highlightedInsertedTekstvalgIds}>
                    <ManagedLetterEditor brev={props.brev} error={error} freeze={freeze} showDebug={showDebug} />
                  </InsertedTekstValgHighlightProvider>
                )}
                saksId={props.saksId}
              />
            }
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
        </AktivtDokumentProvider>
      </form>
    </VStack>
  );
};
