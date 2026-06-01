import { Alert, Box, Button, Heading, HGrid, HStack, Label, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrev, getBrevmetadata, getBrevReservasjon, oppdaterBrev } from "~/api/brev-queries";
import { WarnModal, type WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";
import { createLetterSnapshot, createSaksbehandlerValgEndretHistoryEntry } from "~/Brevredigering/LetterEditor/history";
import {
  collectAllIds,
  collectNewIds,
  findLastInsertedFocus,
  hasAnyTekstvalgBeenToggledOn,
  InsertedTekstValgHighlightProvider,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import { CenteredLoader } from "~/components/CenteredLoader";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { UnderskriftTextField } from "~/components/ManagedLetterEditor/UnderskriftTextField";
import ReservertBrevError from "~/components/ReservertBrevError";
import { useBrevEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import {
  type BrevResponse,
  type OppdaterBrevRequest,
  type ReservasjonResponse,
  type SaksbehandlerValg,
} from "~/types/brev";
import { getErrorMessage } from "~/utils/errorUtils";
import { queryFold } from "~/utils/tanstackUtils";
import { trackEvent } from "~/utils/umami";

export const Route = createFileRoute("/saksnummer_/$saksId/brev/$brevId")({
  params: {
    parse: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  },
  component: () => <RedigerBrevPage />,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();

  const search = Route.useSearch();
  const navigate = Route.useNavigate();

  const isSpecialCaseErrorStatus = (status: number | undefined) => [404, 409, 423].includes(Number(status));
  const queryRetries = 3;
  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (failureCount: number, error: AxiosError) => {
      // console.log(failureCount, error.response?.status);
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
        !errorMessage || errorMessage === "Noe gikk galt"
          ? undefined
          : /[.!?]$/.test(errorMessage)
            ? errorMessage
            : `${errorMessage}.`;

      return (
        <Box asChild background="default" marginInline="auto" maxWidth="1106px" minWidth="945px">
          <VStack align="center" flexGrow="1" gap="space-8" justify="center" padding="space-16">
            <CenteredLoader label="Henter brev..." />
            <Alert size="small" variant="warning">
              Klarte ikke hente brevet (forsøk {failureCount} av {queryRetries + 1}).{" "}
              {retryErrorMessage ? `${retryErrorMessage} ` : ""}
              Prøver på nytt...
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
          <Box asChild background="default">
            <VStack align="start" flexGrow="1" gap="space-8" padding="space-24">
              <Label size="small">Brevet er arkivert, og kan derfor ikke redigeres.</Label>
              <Box asChild paddingInline="space-0">
                <Button
                  onClick={() =>
                    navigate({
                      to: "/saksnummer/$saksId/brevbehandler",
                      params: { saksId },
                      search: { enhetsId, vedtaksId },
                    })
                  }
                  size="small"
                  variant="tertiary"
                >
                  Gå til brevbehandler
                </Button>
              </Box>
            </VStack>
          </Box>
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

type OppdaterBrevMutationVariables = OppdaterBrevRequest & {
  historySnapshot?: ReturnType<typeof createLetterSnapshot>;
};

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

  const [warnOpen, setWarnOpen] = useState(false);
  const [warn, setWarn] = useState<{
    kind: WarnModalKind;
    count?: number;
  } | null>(null);

  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  // Tracks the latest server-known letter and saksbehandlerValg for event handlers
  // and mutation callbacks. These refs do not affect rendering.
  const lastSeenIdsRef = useRef<ReadonlySet<number>>(collectAllIds(brev.redigertBrev));
  const previousValgRef = useRef(brev.saksbehandlerValg);

  const previousIdsRef = useRef<ReadonlySet<number> | null>(null);
  const highlightTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const [highlightedIds, setHighlightedIds] = useState<ReadonlySet<number>>(() => new Set<number>());

  useEffect(() => {
    lastSeenIdsRef.current = collectAllIds(brev.redigertBrev);
  }, [brev.redigertBrev]);

  useEffect(() => {
    previousValgRef.current = editorState.saksbehandlerValg;
  }, [editorState.saksbehandlerValg]);

  useEffect(
    () => () => {
      if (highlightTimerRef.current) {
        clearTimeout(highlightTimerRef.current);
      }
    },
    [],
  );

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

  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevMutationVariables>({
    mutationFn: (values) => {
      // Mark the editor as saving so onSaveSuccess will apply the response
      // (it ignores responses while the editor is DIRTY).
      setEditorState((previousState) => ({ ...previousState, saveStatus: "SAVE_PENDING" }));
      return oppdaterBrev({
        saksId: Number.parseInt(saksId, 10),
        brevId: brev.info.id,
        request: {
          redigertBrev: values.redigertBrev,
          saksbehandlerValg: values.saksbehandlerValg,
        },
      });
    },
    onSuccess: (response, variables) => {
      const previousIds = previousIdsRef.current;
      const historySnapshot = variables.historySnapshot;
      previousIdsRef.current = null;

      onSaveSuccess(
        response,
        historySnapshot
          ? {
              createHistoryEntry: () =>
                createSaksbehandlerValgEndretHistoryEntry(historySnapshot, createLetterSnapshot(response)),
            }
          : undefined,
      );

      // The editor went DIRTY while the request was in flight (user typed);
      // onSaveSuccess discarded the response, so do not flash or move the cursor based on a letter the user is not seeing.
      let responseWasApplied = true;
      setEditorState((current) => {
        responseWasApplied = current.saveStatus !== "DIRTY";
        return current;
      });
      if (!previousIds || !responseWasApplied) return;

      const lastSeenIds = lastSeenIdsRef.current;
      const newIds = new Set<number>();
      for (const id of collectNewIds(previousIds, response.redigertBrev)) {
        // Ignore ids that already existed in the letter before this save.
        if (!lastSeenIds.has(id)) newIds.add(id);
      }
      if (newIds.size === 0) return;

      setHighlightedIds(newIds);
      const focus = findLastInsertedFocus(response.redigertBrev, newIds);
      if (focus) {
        setEditorState((s) => ({ ...s, focus }));
      }
      if (highlightTimerRef.current) clearTimeout(highlightTimerRef.current);
      highlightTimerRef.current = setTimeout(() => setHighlightedIds(new Set<number>()), 2200);
    },
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

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
    propertyUsage: brev.propertyUsage ?? [],
  });

  const onTekstValgAndOverstyringChange = () => {
    form.trigger().then((isValid) => {
      if (isValid) {
        const updatedValg = form.getValues().saksbehandlerValg;
        // Only highlight if a tekstvalg was toggled ON — not on toggle-off or overstyring edits.
        if (hasAnyTekstvalgBeenToggledOn(previousValgRef.current, updatedValg)) {
          previousIdsRef.current = collectAllIds(editorState.redigertBrev);
        }
        previousValgRef.current = updatedValg;
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

  const guardedSubmit = form.handleSubmit((values) => {
    const warning = getWarning();
    if (warning) {
      setWarn(warning);
      setWarnOpen(true);
      return;
    }
    onSubmit(values, navigateToBrevbehandler);
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(brev.info.id),
    refetchInterval: 10_000,
  });

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  const freeze = oppdaterBrevMutation.isPending || editorState.saveStatus === "SAVE_PENDING";

  const error = oppdaterBrevMutation.isError;

  const saveDirtyLetter = (state: {
    redigertBrev: typeof editorState.redigertBrev;
    saksbehandlerValg: typeof editorState.saksbehandlerValg;
  }) =>
    oppdaterBrev({
      saksId: Number.parseInt(saksId, 10),
      brevId: brev.info.id,
      frigiReservasjon: false,
      request: {
        redigertBrev: state.redigertBrev,
        saksbehandlerValg: state.saksbehandlerValg,
      },
    });

  // TODO: disable SaksbehandlerValgModelEditor during SAVE_PENDING

  // TODO: Trenger form å være helt ytterst her? Kunne vi hatt det lenger inn i hierarkiet, f.eks i OpprettetBrevSidemenyForm.
  return (
    <FormProvider {...form}>
      <Box asChild background="default" maxWidth="1106px" minWidth="945px">
        <VStack asChild flexGrow="1" marginInline="auto">
          <form onSubmit={guardedSubmit}>
            <WarnModal
              count={warn?.count ?? 0}
              kind={warn?.kind ?? "fritekst"}
              onClose={() => {
                setWarnOpen(false);
                setWarn(null);
              }}
              onFortsett={() => {
                setWarnOpen(false);
                setWarn(null);
                onSubmit(form.getValues(), navigateToBrevbehandler);
              }}
              open={warnOpen}
            />
            <ReservertBrevError
              doRetry={doReload}
              onNeiClick={() => navigate({ to: BrevvelgerRoute.fullPath, search: { enhetsId, vedtaksId } })}
              reservasjon={reservasjonQuery.data}
            />
            <HGrid columns="minmax(304px, 384px) minmax(640px, 694px)" height="var(--main-page-content-height)">
              <Box
                asChild
                borderColor="neutral-subtle"
                borderWidth="0 1 0 0"
                overflowY="auto"
                padding={{ sm: "space-12", lg: "space-24" }}
              >
                <VStack gap="space-12">
                  <Heading size="small" spacing>
                    {brevmal.data?.name}
                  </Heading>
                  <OpprettetBrevSidemenyForm brev={brev} submitOnChange={onTekstValgAndOverstyringChange} />
                  <UnderskriftTextField of="Saksbehandler" />
                </VStack>
              </Box>
              <InsertedTekstValgHighlightProvider ids={highlightedIds}>
                <ManagedLetterEditor
                  brev={brev}
                  error={error}
                  freeze={freeze}
                  saveDirtyLetter={saveDirtyLetter}
                  showDebug={showDebug}
                />
              </InsertedTekstValgHighlightProvider>
            </HGrid>
            <Box
              asChild
              background="default"
              borderColor="neutral-subtle"
              borderWidth="1 0 0 0"
              height="var(--nav-bar-height)"
            >
              <HStack justify="space-between" paddingBlock="space-8" paddingInline="space-16">
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
            </Box>
          </form>
        </VStack>
      </Box>
    </FormProvider>
  );
}

enum BrevSidemenyTabs {
  TEKSTVALG = "TEKSTVALG",
  OVERSTYRING = "OVERSTYRING",
}

// TODO: Funksjonelt er denne komponenten ganske lik BrevmalAlternativer.tsx. Se på om vi kan bruke samme komponent.
const OpprettetBrevSidemenyForm = ({ brev, submitOnChange }: { brev: BrevResponse; submitOnChange?: () => void }) => {
  const specificationFormElements = usePartitionedModelSpecification(
    brev.info.brevkode,
    brev.propertyUsage ?? undefined,
  );

  const optionalFields = specificationFormElements.status === "success" ? specificationFormElements.optionalFields : [];
  const requiredFields = specificationFormElements.status === "success" ? specificationFormElements.requiredFields : [];
  const hasOptional = optionalFields.length > 0;
  const hasRequired = requiredFields.length > 0;

  if (!hasOptional && !hasRequired) {
    return (
      <SaksbehandlerValgModelEditor
        brevkode={brev.info.brevkode}
        fieldsToRender="optional"
        specificationFormElements={specificationFormElements}
        submitOnChange={submitOnChange}
      />
    );
  }

  if (hasOptional && !hasRequired) {
    return (
      <>
        <Heading size="xsmall">Tekstvalg</Heading>
        <SaksbehandlerValgModelEditor
          brevkode={brev.info.brevkode}
          fieldsToRender="optional"
          specificationFormElements={specificationFormElements}
          submitOnChange={submitOnChange}
        />
      </>
    );
  }

  if (hasRequired && !hasOptional)
    return (
      <>
        <Heading size="xsmall">Overstyring</Heading>
        <SaksbehandlerValgModelEditor
          brevkode={brev.info.brevkode}
          fieldsToRender="required"
          specificationFormElements={specificationFormElements}
          submitOnChange={submitOnChange}
        />
      </>
    );

  const defaultTab = BrevSidemenyTabs.TEKSTVALG;

  return (
    <Tabs defaultValue={defaultTab} fill size="small">
      <Tabs.List>
        <Tabs.Tab label="Tekstvalg" value={BrevSidemenyTabs.TEKSTVALG} />
        <Tabs.Tab label="Overstyring" value={BrevSidemenyTabs.OVERSTYRING} />
      </Tabs.List>
      <Tabs.Panel value={BrevSidemenyTabs.TEKSTVALG}>
        <Box marginBlock="space-20 space-0">
          <SaksbehandlerValgModelEditor
            brevkode={brev.info.brevkode}
            fieldsToRender="optional"
            specificationFormElements={specificationFormElements}
            submitOnChange={submitOnChange}
          />
        </Box>
      </Tabs.Panel>
      <Tabs.Panel value={BrevSidemenyTabs.OVERSTYRING}>
        <Box marginBlock="space-20 space-0">
          <SaksbehandlerValgModelEditor
            brevkode={brev.info.brevkode}
            fieldsToRender="required"
            specificationFormElements={specificationFormElements}
            submitOnChange={submitOnChange}
          />
        </Box>
      </Tabs.Panel>
    </Tabs>
  );
};
