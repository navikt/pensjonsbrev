import { Box, Button, Heading, HStack, Label, VStack } from "@navikt/ds-react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getRouteApi, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useMemo, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { getBrevmetadata, getBrevReservasjon } from "~/api/brev-queries";
import { getBrevV2 } from "~/api/brevV2-queries";
import { getRedigerbartVedleggV2 } from "~/api/redigerbareVedleggV2-endpoints";
import { useGuardedFormSubmit } from "~/Brevredigering/hooks/useGuardedFormSubmit";
import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";
import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { ApiError } from "~/components/ApiError";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { ActiveDocumentProvider } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { BrevOgVedleggEditor } from "~/components/brevOgVedlegg/BrevOgVedleggEditor";
import { BrevOgVedleggEditorSidepanel } from "~/components/brevOgVedlegg/BrevOgVedleggEditorSidepanel";
import { useDocumentCoordinator } from "~/components/brevOgVedlegg/useActiveDocumentCoordinator";
import { CenteredLoader } from "~/components/CenteredLoader";
import {
  countMissingFromTemplate,
  countUneditedFritekst,
  firstUneditedFritekst,
} from "~/components/ManagedLetterEditorV2/documentQueries";
import {
  ManagedLetterEditorContextV2Provider,
  useManagedLetterEditorContextV2,
} from "~/components/ManagedLetterEditorV2/ManagedLetterEditorContextV2";
import ManagedLetterEditorV2 from "~/components/ManagedLetterEditorV2/ManagedLetterEditorV2";
import { UnderskriftTextFieldV2 } from "~/components/ManagedLetterEditorV2/UnderskriftTextFieldV2";
import { useOppdaterBrevMutationV2 } from "~/components/ManagedLetterEditorV2/useOppdaterBrevMutationV2";
import ReservertBrevError from "~/components/ReservertBrevError";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import { useEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { useReleaseReservationOnPageExit } from "~/hooks/useReleaseReservationOnPageExit";
import { useUserInfo } from "~/hooks/useUserInfo";
import { type ReservasjonResponse, type SaksbehandlerValg } from "~/types/brev";
import { type BrevResponseV2 } from "~/types/brevV2";
import { trackEvent } from "~/utils/umami";

const routeApi = getRouteApi("/saksnummer_/$saksId/brev/$brevId");

export function RedigerBrevV2Page() {
  const { brevId, saksId } = routeApi.useParams();
  const { enhetsId, vedtaksId } = routeApi.useSearch();
  const navigate = routeApi.useNavigate();

  const brevQuery = useQuery({
    ...getBrevV2(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: false,
  });

  if (brevQuery.isPending) return <CenteredLoader label="Henter brev..." />;
  if (brevQuery.isError) {
    const error: AxiosError = brevQuery.error;
    if (error.response?.status === 423 && error.response.data) {
      return (
        <ReservertBrevError
          doRetry={brevQuery.refetch}
          onNeiClick={() =>
            navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId }, search: { enhetsId, vedtaksId } })
          }
          reservasjon={error.response.data as ReservasjonResponse}
        />
      );
    }
    return <ApiError error={error} title="En feil skjedde ved henting av brev" />;
  }

  return (
    <RedigeringsflateProvider redigeringsflate="saksbehandler-redigering">
      <ManagedLetterEditorContextV2Provider brev={brevQuery.data}>
        <RedigerBrevV2 brev={brevQuery.data} doReload={brevQuery.refetch} saksId={saksId} vedtaksId={vedtaksId} />
      </ManagedLetterEditorContextV2Provider>
    </RedigeringsflateProvider>
  );
}

type FormData = { saksbehandlerValg: SaksbehandlerValg };

function RedigerBrevV2(props: {
  brev: BrevResponseV2;
  doReload: () => void;
  saksId: string;
  vedtaksId: string | undefined;
}) {
  const { brev, saksId, vedtaksId } = props;
  const navigate = routeApi.useNavigate();
  const { enhetsId, vedlegg: activeVedlegg } = routeApi.useSearch();
  const editorStartTime = useRef(Date.now());
  const currentUser = useUserInfo();
  const queryClient = useQueryClient();
  const { editorState, setEditorState, savePendingChanges, registerSaveErrorReset } = useManagedLetterEditorContextV2();

  const navigateToDocument = useCallback(
    async (vedleggId: string | undefined) => {
      await navigate({ search: (prev) => ({ ...prev, vedlegg: vedleggId }), replace: true });
    },
    [navigate],
  );
  const documentCoordinator = useDocumentCoordinator({
    saksId,
    brevId: brev.info.id,
    activeVedleggId: activeVedlegg,
    redigeringsflate: "saksbehandler-redigering",
    navigateToDocument,
    savePendingChanges,
    fetchSavedMissingFromTemplateCount: async (vedleggId) =>
      countMissingFromTemplate(
        await queryClient.fetchQuery({
          ...getRedigerbartVedleggV2(saksId, brev.info.id, vedleggId, "saksbehandler-redigering"),
          staleTime: Number.POSITIVE_INFINITY,
        }),
      ),
  });

  const brevmal = useQuery({
    ...getBrevmetadata,
    select: (data) => data.find((brevmal) => brevmal.id === brev.info.brevkode),
  });

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.debug === "true" || search?.debug === true,
  });

  const oppdaterBrevMutation = useOppdaterBrevMutationV2();

  const defaultValues = useMemo(
    () => ({ saksbehandlerValg: { ...editorState.saksbehandlerValg } }),
    [editorState.saksbehandlerValg],
  );
  const form = useForm<FormData>({ defaultValues });

  const redigertBrev = editorState.redigertBrev;
  const { getWarning } = useEditorWarnings({
    brevkode: brev.info.brevkode,
    form,
    propertyUsage: brev.propertyUsage ?? undefined,
    countUneditedFritekst: useCallback(() => countUneditedFritekst(redigertBrev), [redigertBrev]),
    countMissingFromTemplate: useCallback(() => countMissingFromTemplate(redigertBrev), [redigertBrev]),
    getMissingFromTemplateVedleggCount: documentCoordinator.getMissingFromTemplateCount,
  });

  const navigateToBrevbehandler = () =>
    navigate({
      to: "/saksnummer/$saksId/brevbehandler",
      params: { saksId },
      search: { brevId: brev.info.id, enhetsId, vedtaksId },
    });

  const navigateToBrevvelger = async () => {
    if (!(await documentCoordinator.savePendingDocuments())) return;
    await navigate({
      to: "/saksnummer/$saksId/brevvelger",
      params: { saksId },
      search: (search) => ({ ...search, brevId: brev.info.id }),
    });
  };

  const onTekstValgAndOverstyringChange = () => {
    void form.trigger().then((isValid) => {
      if (!isValid) return;
      oppdaterBrevMutation.reset();
      oppdaterBrevMutation.mutate({ saksbehandlerValg: form.getValues().saksbehandlerValg, recordHistory: true });
    });
  };

  const onSubmit = async (values: FormData, navigateDone?: () => void) => {
    if (activeVedlegg !== undefined && !(await documentCoordinator.savePendingDocuments())) return;
    oppdaterBrevMutation.reset();
    oppdaterBrevMutation.mutate(
      { saksbehandlerValg: values.saksbehandlerValg, frigiReservasjon: true },
      {
        onSuccess: () => {
          const varighetSekunder = Math.round((Date.now() - editorStartTime.current) / 1000);
          trackEvent("tid brukt i editor", {
            brevId: brev.info.id,
            brevkode: brev.info.brevkode,
            spraak: brev.info.spraak,
            varighetSekunder,
            varighetMinutter: Math.round(varighetSekunder / 60),
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
    onInvalidSubmit: () => void documentCoordinator.selectDocument(undefined),
    onWarnModalClosed: (warn) => {
      if (warn?.kind === "fritekst" || warn?.kind === "fritekstOgTekstValg") {
        const focus = firstUneditedFritekst(editorState.redigertBrev);
        if (focus) setEditorState((s) => ({ ...s, focus }));
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
    beforeRelease: documentCoordinator.savePendingDocuments,
    currentUserNavIdent: currentUser?.navident,
    reservationOwnerNavIdent: reservasjonQuery.data?.reservertAv.id,
  });

  useEffect(() => {
    form.reset(defaultValues);
  }, [defaultValues, form]);

  const freeze = oppdaterBrevMutation.isPending;
  const error = oppdaterBrevMutation.isError;
  const hasUnsavedValg = form.formState.isDirty;

  useEffect(() => {
    registerSaveErrorReset(hasUnsavedValg ? null : oppdaterBrevMutation.reset);
    return () => registerSaveErrorReset(null);
  }, [hasUnsavedValg, oppdaterBrevMutation.reset, registerSaveErrorReset]);

  return (
    <FormProvider {...form}>
      <Box asChild background="default" maxWidth="1106px" minWidth="945px">
        <VStack asChild height="100%" marginInline="auto">
          <form onSubmit={guardedSubmit}>
            <WarnModal {...warnModalProps} />
            <ReservertBrevError
              doRetry={props.doReload}
              onNeiClick={() =>
                navigate({ to: "/saksnummer/$saksId/brevvelger", params: { saksId }, search: { enhetsId, vedtaksId } })
              }
              reservasjon={reservasjonQuery.data}
            />
            <ActiveDocumentProvider
              activeVedleggId={documentCoordinator.activeVedleggId}
              onSelectDocument={documentCoordinator.selectDocument}
              redigeringsflate="saksbehandler-redigering"
              registerVedleggMissingFromTemplate={documentCoordinator.registerVedleggMissingFromTemplate}
              registerVedleggSave={documentCoordinator.registerVedleggSave}
            >
              <ThreeSectionLayout
                bottom={
                  <HStack justify="space-between" width="100%">
                    <Button
                      disabled={documentCoordinator.savingPendingDocuments}
                      onClick={navigateToBrevvelger}
                      size="small"
                      type="button"
                      variant="tertiary"
                    >
                      Tilbake til brevvelger
                    </Button>
                    <Button loading={freeze || documentCoordinator.savingPendingDocuments} size="small" type="submit">
                      <HStack align="center" gap="space-8">
                        <Label size="small">Fortsett</Label>
                      </HStack>
                    </Button>
                  </HStack>
                }
                bottomJustify="space-between"
                left={
                  <BrevOgVedleggEditorSidepanel
                    brevId={brev.info.id}
                    brevmalPanel={
                      <VStack gap="space-12" paddingBlock="space-16 space-0">
                        <Heading size="small" spacing>
                          {brevmal.data?.name}
                        </Heading>
                        <BrevmalAlternativer
                          brevkode={brev.info.brevkode}
                          propertyUsage={brev.propertyUsage ?? undefined}
                          submitOnChange={onTekstValgAndOverstyringChange}
                        />
                        <UnderskriftTextFieldV2 of="Saksbehandler" />
                      </VStack>
                    }
                    saksId={saksId}
                  />
                }
                right={
                  <BrevOgVedleggEditor
                    brev={brev}
                    freeze={freeze}
                    renderBrev={() => (
                      <ManagedLetterEditorV2 canReset error={error} freeze={freeze} showDebug={showDebug} />
                    )}
                    saksId={saksId}
                  />
                }
                rightColumnWidth="minmax(640px, 694px)"
              />
            </ActiveDocumentProvider>
          </form>
        </VStack>
      </Box>
    </FormProvider>
  );
}
