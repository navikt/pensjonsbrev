import { Alert, Box, Button, Heading, HStack, Label, Tabs, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useEffect, useMemo, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrev, getBrevmetadata, getBrevReservasjon } from "~/api/brev-queries";
import { useGuardedFormSubmit } from "~/Brevredigering/hooks/useGuardedFormSubmit";
import { useOppdaterBrevAutosave } from "~/Brevredigering/hooks/useOppdaterBrevAutosave";
import { findFirstUneditedFritekstFocus } from "~/Brevredigering/LetterEditor/actions/common";
import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";
import { createLetterSnapshot } from "~/Brevredigering/LetterEditor/history";
import { useTekstvalgInsertHighlight } from "~/Brevredigering/LetterEditor/hooks/useTekstvalgInsertHighlight";
import { InsertedTekstValgHighlightProvider } from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import ArkivertBrev from "~/components/ArkivertBrev";
import { CenteredLoader } from "~/components/CenteredLoader";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { UnderskriftTextField } from "~/components/ManagedLetterEditor/UnderskriftTextField";
import ReservertBrevError from "~/components/ReservertBrevError";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import { useBrevEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { useReleaseReservationOnPageExit } from "~/hooks/useReleaseReservationOnPageExit";
import { useUserInfo } from "~/hooks/useUserInfo";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import { type BrevResponse, type ReservasjonResponse, type SaksbehandlerValg } from "~/types/brev";
import { genericErrorMessage, getErrorMessage } from "~/utils/errorUtils";
import { queryFold } from "~/utils/tanstackUtils";
import { trackEvent } from "~/utils/umami";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";

export const Route = createFileRoute("/saksnummer_/$saksId/brev/$brevId")({
  params: {
    parse: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  },
  component: () => <RedigerBrevPage />,
});

const queryRetries = 3;
const isSpecialCaseErrorStatus = (status: number | undefined) =>
  status === 404 || status === 409 || status === 422 || status === 423;

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();

  const search = Route.useSearch();
  const navigate = Route.useNavigate();

  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (failureCount: number, error: AxiosError) => {
      return failureCount < queryRetries && !isSpecialCaseErrorStatus(error.response?.status);
    },
    throwOnError: (error: AxiosError) => !isSpecialCaseErrorStatus(error.response?.status),
  });

  useEffect(() => {
    const brev = brevQuery.data;
    if (!brev) return;

    const vedtaksIdFromBrev = brev.info.vedtaksId;
    if (vedtaksIdFromBrev == null) return;

    const vedtaksIdAsString = String(vedtaksIdFromBrev);

    if (search.vedtaksId !== vedtaksIdAsString) {
      navigate({
        search: (prev) => ({
          ...prev,
          vedtaksId: vedtaksIdAsString,
        }),
        replace: true,
      });
    }
  }, [brevQuery.data, search.vedtaksId, navigate]);

  return queryFold({
    query: brevQuery,
    initial: () => null,
    pending: () => (
      <Box asChild background="default" marginInline="auto" maxWidth="1106px" minWidth="945px">
        <HStack align="stretch" flexGrow="1" gap="space-16" justify="space-around" padding="space-16" wrap={false}>
          <CenteredLoader label="Henter brev..." />
        </HStack>
      </Box>
    ),
    retrying: (failureCount, failureReason) => {
      const errorMessage = getErrorMessage(failureReason).trim();
      const retryErrorMessage =
        !errorMessage || errorMessage === genericErrorMessage
          ? undefined
          : /[.!?]$/.test(errorMessage)
            ? errorMessage
            : `${errorMessage}.`;

      return (
        <Box asChild background="default" marginInline="auto" maxWidth="1106px" minWidth="945px">
          <VStack align="center" flexGrow="1" gap="space-8" justify="center" padding="space-16">
            <CenteredLoader label="Henter brev..." />
            <Alert size="small" variant="warning">
              Klarte ikke å hente brevet. Prøver på nytt (forsøk {failureCount + 1} av {queryRetries + 1})...
              {retryErrorMessage && <p>{`Feilårsak: ${retryErrorMessage}`}</p>}
            </Alert>
          </VStack>
        </Box>
      );
    },
    error: (error) => {
      const errorStatus = error.response?.status;
      if (errorStatus === 423 && error.response?.data) {
        return (
          <ReservertBrevError
            doRetry={brevQuery.refetch}
            onNeiClick={() => navigate({ to: BrevvelgerRoute.fullPath, search: { enhetsId, vedtaksId } })}
            reservasjon={error.response.data as ReservasjonResponse}
          />
        );
      }
      if (errorStatus === 409) {
        return (
          <ArkivertBrev
            onGaTilBrevbehandler={() =>
              navigate({
                to: "/saksnummer/$saksId/brevbehandler",
                params: { saksId },
                search: { enhetsId, vedtaksId },
              })
            }
          />
        );
      }
      if (errorStatus === 404) {
        return (
          <VStack align="center" flexGrow="1" gap="space-8" padding="space-24">
            <ApiError error={error} title="Finner ikke brevet i databasen" />
            <HStack gap="space-8">
              <Button
                onClick={() =>
                  navigate({
                    to: "/saksnummer/$saksId/brevvelger",
                    params: { saksId },
                    search: { enhetsId, vedtaksId },
                  })
                }
                size="small"
                variant="secondary"
              >
                Gå til brevvelger
              </Button>
              <Button
                onClick={() =>
                  navigate({
                    to: "/saksnummer/$saksId/brevbehandler",
                    params: { saksId },
                    search: { enhetsId, vedtaksId },
                  })
                }
                size="small"
                variant="secondary"
              >
                Gå til brevbehandler
              </Button>
            </HStack>
          </VStack>
        );
      }
      return <ApiError error={error} title="En feil skjedde ved henting av brev" />;
    },
    success: (brev) => (
      <ManagedLetterEditorContextProvider brev={brev}>
        <RedigerBrev brev={brev} doReload={brevQuery.refetch} saksId={saksId} vedtaksId={vedtaksId} />
      </ManagedLetterEditorContextProvider>
    ),
  });
}

interface RedigerBrevSidemenyFormData {
  signatur: string;
  saksbehandlerValg: SaksbehandlerValg;
}

function RedigerBrev({
  brev,
  doReload,
  saksId,
  vedtaksId,
}: {
  brev: BrevResponse;
  doReload: () => void;
  saksId: string;
  vedtaksId: string | undefined;
}) {
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId } = Route.useSearch();
  const editorStartTime = useRef(Date.now());
  const currentUser = useUserInfo();

  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const { highlightedIds, beforeTekstvalgChange, onAfterSave } = useTekstvalgInsertHighlight({
    redigertBrev: brev.redigertBrev,
    saksbehandlerValg: editorState.saksbehandlerValg,
    setEditorState,
  });

  const navigateToBrevbehandler = () =>
    navigate({
      to: "/saksnummer/$saksId/brevbehandler",
      params: { saksId },
      search: { brevId: brev.info.id, enhetsId, vedtaksId },
    });

  const brevmal = useQuery({
    ...getBrevmetadata,
    select: (data) => data.find((brevmal) => brevmal.id === brev.info.brevkode),
  });

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.debug === "true" || search?.debug === true,
  });

  const oppdaterBrevAutosave = useOppdaterBrevAutosave({
    saksId,
    brevId: brev.info.id,
    setEditorState,
    onSaveSuccess,
    onAfterSave,
  });
  const { oppdaterBrevMutation, saveDirtyLetter } = oppdaterBrevAutosave;

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: {
        ...editorState.saksbehandlerValg,
      },
    }),
    [editorState.saksbehandlerValg],
  );

  const form = useForm<RedigerBrevSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  const { getWarning } = useBrevEditorWarnings({
    brevkode: brev.info.brevkode,
    form,
    redigertBrev: editorState.redigertBrev,
    propertyUsage: brev.propertyUsage ?? undefined,
  });

  const onTekstValgAndOverstyringChange = () => {
    form.trigger().then((isValid) => {
      if (isValid) {
        const updatedValg = form.getValues().saksbehandlerValg;
        beforeTekstvalgChange(updatedValg, editorState.redigertBrev);
        oppdaterBrevMutation.mutate({
          redigertBrev: editorState.redigertBrev,
          saksbehandlerValg: updatedValg,
          historySnapshot: createLetterSnapshot(editorState),
        });
      }
    });
  };

  const onSubmit = (values: RedigerBrevSidemenyFormData, navigateDone?: () => void) => {
    oppdaterBrevMutation.mutate(
      {
        redigertBrev: editorState.redigertBrev,
        saksbehandlerValg: values.saksbehandlerValg,
        // This is the final "done editing" submit (navigates to brevbehandler), so release the
        // reservation lock — unlike the tekstvalg/overstyring autosave, which must keep it held.
        frigiReservasjon: true,
      },
      {
        onSuccess: () => {
          const varighetSekunder = Math.round((Date.now() - editorStartTime.current) / 1000);
          trackEvent("tid brukt i editor", {
            brevId: brev.info.id,
            brevkode: brev.info.brevkode,
            varighetSekunder,
            varighetMinutter: Math.round(varighetSekunder / 60),
            enhetsId,
          });
          navigateDone?.();
        },
      },
    );
  };

  const { guardedSubmit, warnModalProps } = useGuardedFormSubmit({
    form,
    getWarning,
    onConfirmedSubmit: (values) => onSubmit(values, navigateToBrevbehandler),
    onWarnModalClosed: (warn) => {
      if (warn?.kind === "fritekst" || warn?.kind === "fritekstOgTekstValg") {
        const focus = findFirstUneditedFritekstFocus(editorState.redigertBrev);
        if (focus) {
          setEditorState((s) => ({ ...s, focus }));
        }
      }
    },
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(brev.info.id),
    refetchInterval: 10_000,
  });

  useReleaseReservationOnPageExit({
    enabled: reservasjonQuery.isSuccess,
    brevId: brev.info.id,
    currentUserNavIdent: currentUser?.navident,
    reservationOwnerNavIdent: reservasjonQuery.data?.reservertAv.id,
  });

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  const freeze = oppdaterBrevMutation.isPending;
  const error = oppdaterBrevMutation.isError;

  // TODO: disable SaksbehandlerValgModelEditor during SAVE_PENDING

  return (
    <FormProvider {...form}>
      <Box asChild background="default" maxWidth="1106px" minWidth="945px">
        <VStack asChild height="100%" marginInline="auto">
          <form onSubmit={guardedSubmit}>
            <WarnModal {...warnModalProps} />
            <ReservertBrevError
              doRetry={doReload}
              onNeiClick={() => navigate({ to: BrevvelgerRoute.fullPath, search: { enhetsId, vedtaksId } })}
              reservasjon={reservasjonQuery.data}
            />
            <ThreeSectionLayout
              bottom={
                <HStack justify="space-between" width="100%">
                  <Button
                    onClick={() =>
                      navigate({
                        to: "/saksnummer/$saksId/brevvelger",
                        params: { saksId: saksId },
                        search: (s) => ({ ...s, brevId: brev.info.id }),
                      })
                    }
                    size="small"
                    type="button"
                    variant="tertiary"
                  >
                    Tilbake til brevvelger
                  </Button>
                  <Button loading={oppdaterBrevMutation.isPending} size="small" type="submit">
                    <HStack align="center" gap="space-8">
                      <Label size="small">Fortsett</Label>
                    </HStack>
                  </Button>
                </HStack>
              }
              bottomJustify="space-between"
              left={
                <VStack gap="space-12">
                  <Heading size="small" spacing>
                    {brevmal.data?.name}
                  </Heading>
                  <BrevmalAlternativer
                    brevkode={brev.info.brevkode}
                    propertyUsage={brev.propertyUsage ?? undefined}
                    submitOnChange={onTekstValgAndOverstyringChange}
                  />
                  <UnderskriftTextField of="Saksbehandler" />
                </VStack>
              }
              right={
                <InsertedTekstValgHighlightProvider ids={highlightedIds}>
                  <ManagedLetterEditor
                    brev={brev}
                    error={error}
                    freeze={freeze}
                    saveDirtyLetter={saveDirtyLetter}
                    showDebug={showDebug}
                  />
                </InsertedTekstValgHighlightProvider>
              }
              rightColumnWidth="minmax(640px, 694px)"
            />
          </form>
        </VStack>
      </Box>
    </FormProvider>
  );
}
