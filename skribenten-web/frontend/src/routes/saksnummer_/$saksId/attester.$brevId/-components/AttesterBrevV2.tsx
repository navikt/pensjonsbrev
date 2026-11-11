import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Label, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { getRouteApi, useSearch } from "@tanstack/react-router";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrevReservasjon } from "~/api/brev-queries";
import { attesterBrevV2, getBrevAttesteringV2 } from "~/api/brevV2-queries";
import { getFeatureToggle } from "~/api/skribenten-api-endpoints";
import { AttestantDiffProviderV2 } from "~/Brevredigering/EditorV2/diff/AttestantDiffContextV2";
import { useAttestantLetterDiffV2 } from "~/Brevredigering/EditorV2/diff/useAttestantLetterDiffV2";
import { useGuardedFormSubmit } from "~/Brevredigering/hooks/useGuardedFormSubmit";
import { WarnModal } from "~/Brevredigering/LetterEditor/components/warnModal";
import { AttestantDiffAnnouncement } from "~/Brevredigering/LetterEditor/diff/AttestantDiffAnnouncement";
import { AttestantDiffMenu } from "~/Brevredigering/LetterEditor/diff/AttestantDiffMenu";
import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { ApiError } from "~/components/ApiError";
import AttestForbiddenModal from "~/components/AttestForbiddenModal";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { ActiveDocumentProvider } from "~/components/brevOgVedlegg/ActiveDocumentContext";
import { BrevOgVedleggEditor } from "~/components/brevOgVedlegg/BrevOgVedleggEditor";
import { BrevOgVedleggEditorSidepanel } from "~/components/brevOgVedlegg/BrevOgVedleggEditorSidepanel";
import { useDocumentCoordinator } from "~/components/brevOgVedlegg/useActiveDocumentCoordinator";
import { CenteredLoader } from "~/components/CenteredLoader";
import { Divider } from "~/components/Divider";
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
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ReservertBrevError from "~/components/ReservertBrevError";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import { type DiffToggles } from "~/hooks/useAttestantLetterDiff";
import { useEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { useReleaseReservationOnPageExit } from "~/hooks/useReleaseReservationOnPageExit";
import { useUserInfo } from "~/hooks/useUserInfo";
import { type ReservasjonResponse, type SaksbehandlerValg } from "~/types/brev";
import { type BrevResponseV2 } from "~/types/brevV2";
import { type AttestForbiddenReason } from "~/utils/parseAttest403";
import { trackEvent } from "~/utils/umami";

const routeApi = getRouteApi("/saksnummer_/$saksId/attester/$brevId/redigering");

const sidemenySchema = z.object({
  attestantSignatur: z.string().min(1, "Underskrift må oppgis"),
  saksbehandlerValg: z.custom<SaksbehandlerValg>(),
});

type SidemenyFormData = z.infer<typeof sidemenySchema>;

type AttestError = AxiosError & { forbidReason?: AttestForbiddenReason };

const utenAttestantSignatur = (brev: BrevResponseV2): BrevResponseV2 => ({
  ...brev,
  redigertBrev: {
    ...brev.redigertBrev,
    signatur: { ...brev.redigertBrev.signatur, attesterendeSaksbehandlerNavn: undefined },
  },
});

export function AttesterBrevV2Page() {
  const { saksId, brevId } = routeApi.useParams();
  const { vedtaksId, enhetsId } = routeApi.useSearch();
  const navigate = routeApi.useNavigate();
  const brevQuery = useQuery({
    ...getBrevAttesteringV2(saksId, Number(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
    retry: false,
  });

  const navigateToBrevbehandler = () =>
    navigate({
      to: "/saksnummer/$saksId/brevbehandler",
      params: { saksId },
      search: { vedtaksId, enhetsId, brevId: Number(brevId) },
    });

  if (brevQuery.isPending) {
    return (
      <Box asChild background="default" paddingBlock="space-32 space-0">
        <CenteredLoader label="Henter brev..." verticalStrategy="flexGrow" />
      </Box>
    );
  }
  if (brevQuery.isError) {
    const error: AttestError = brevQuery.error;
    if (error.response?.status === 423 && error.response.data) {
      return (
        <ReservertBrevError
          doRetry={brevQuery.refetch}
          onNeiClick={navigateToBrevbehandler}
          reservasjon={error.response.data as ReservasjonResponse}
        />
      );
    }
    if (error.forbidReason) {
      return <AttestForbiddenModal onClose={navigateToBrevbehandler} reason={error.forbidReason} />;
    }
    return (
      <Box background="default" flexGrow="1">
        <ApiError error={error} title="En feil skjedde ved henting av vedtaksbrev" />
      </Box>
    );
  }

  const brev = utenAttestantSignatur(brevQuery.data);
  return (
    <RedigeringsflateProvider redigeringsflate="attestant-redigering">
      <ManagedLetterEditorContextV2Provider brev={brev}>
        <AttesterBrevV2 brev={brev} doReload={brevQuery.refetch} saksId={saksId} />
      </ManagedLetterEditorContextV2Provider>
    </RedigeringsflateProvider>
  );
}

function AttesterBrevV2(props: { saksId: string; brev: BrevResponseV2; doReload: () => void }) {
  const navigate = routeApi.useNavigate();
  const { vedlegg: activeVedlegg } = routeApi.useSearch();
  const { editorState, setEditorState, saveLetterOperation, registerSaveErrorReset, savePendingChanges } =
    useManagedLetterEditorContextV2();
  const redigertBrev = editorState.redigertBrev;
  const attesteringStartTime = useRef(Date.now());
  const currentUser = useUserInfo();
  const [forbidReason, setForbidReason] = useState<AttestForbiddenReason | null>(null);
  const [unexpectedError, setUnexpectedError] = useState<AxiosError | null>(null);

  const navigateToDocument = useCallback(
    async (vedleggId: string | undefined) => {
      await navigate({ search: (prev) => ({ ...prev, vedlegg: vedleggId }), replace: true });
    },
    [navigate],
  );
  const documentCoordinator = useDocumentCoordinator({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    activeVedleggId: activeVedlegg,
    redigeringsflate: "attestant-redigering",
    navigateToDocument,
    savePendingChanges,
  });
  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.debug === "true" || search?.debug === true,
  });

  const diffFeatureToggle = useQuery(getFeatureToggle("diff"));
  const [diffMenuElement, setDiffMenuElement] = useState<HTMLDivElement | null>(null);
  const [diffTogglesElement, setDiffTogglesElement] = useState<HTMLDivElement | null>(null);
  const attestantDiff = useAttestantLetterDiffV2({
    saksId: props.saksId,
    brevId: props.brev.info.id,
    savedLetter: redigertBrev,
    savedHash: editorState.redigertBrevHash,
    isSaved: editorState.saveStatus === "SAVED",
  });
  const { disableDiffMode } = attestantDiff;

  useEffect(() => {
    if (editorState.saveStatus === "DIRTY") disableDiffMode();
  }, [editorState.saveStatus, disableDiffMode]);

  const handleDiffToggle = async (toggle: keyof DiffToggles, checked: boolean) => {
    attestantDiff.setToggle(toggle, checked);
    if (!checked) return;
    try {
      await savePendingChanges();
    } catch {
      disableDiffMode();
    }
  };

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(props.brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(props.brev.info.id),
    refetchInterval: 10_000,
  });

  useReleaseReservationOnPageExit({
    enabled: reservasjonQuery.isSuccess,
    brevId: props.brev.info.id,
    beforeRelease: documentCoordinator.savePendingDocuments,
    currentUserNavIdent: currentUser?.navident,
    reservationOwnerNavIdent: reservasjonQuery.data?.reservertAv.id,
  });

  const defaultValues = useMemo(
    () => ({ saksbehandlerValg: { ...editorState.saksbehandlerValg }, attestantSignatur: "" }),
    [editorState.saksbehandlerValg],
  );

  const form = useForm<SidemenyFormData>({ resolver: zodResolver(sidemenySchema), defaultValues });

  const { getWarning } = useEditorWarnings({
    warnAboutMissingFromTemplate: false,
    brevkode: props.brev.info.brevkode,
    form,
    propertyUsage: props.brev.propertyUsage ?? undefined,
    countUneditedFritekst: useCallback(() => countUneditedFritekst(redigertBrev), [redigertBrev]),
    countMissingFromTemplate: useCallback(() => countMissingFromTemplate(redigertBrev), [redigertBrev]),
  });

  const attesterMutation = useMutation<BrevResponseV2, AttestError, void>({
    mutationFn: () =>
      saveLetterOperation((state) =>
        attesterBrevV2({
          saksId: props.saksId,
          brevId: props.brev.info.id,
          request: { redigertBrev: state.redigertBrev },
          frigiReservasjon: false,
        }),
      ),
    onError: (err) => {
      if (err.forbidReason) {
        setForbidReason(err.forbidReason);
        return;
      }
      setUnexpectedError(err);
    },
  });

  const onSubmit = async (onSuccess?: () => void) => {
    if (activeVedlegg !== undefined && !(await documentCoordinator.savePendingDocuments())) return;
    attesterMutation.reset();
    attesterMutation.mutate(undefined, { onSuccess });
  };

  const freeze = attesterMutation.isPending;
  const error = attesterMutation.isError;

  const resetSaveErrors = useCallback(() => attesterMutation.reset(), [attesterMutation.reset]);

  useEffect(() => {
    registerSaveErrorReset(resetSaveErrors);
    return () => registerSaveErrorReset(null);
  }, [registerSaveErrorReset, resetSaveErrors]);

  useEffect(() => {
    form.reset({ ...defaultValues, attestantSignatur: form.getValues("attestantSignatur") });
  }, [defaultValues, form]);

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
      spraak: props.brev.info.spraak,
      varighetSekunder,
      varighetMinutter: Math.round(varighetSekunder / 60),
    });
    trackEvent("brev attestert", {
      brevId: props.brev.info.id,
      brevkode: props.brev.info.brevkode,
      spraak: props.brev.info.spraak,
    });
    void navigate({
      to: "/saksnummer/$saksId/attester/$brevId/forhandsvisning",
      params: { saksId: props.saksId, brevId: props.brev.info.id.toString() },
      search: {
        vedtaksId: props.brev.info.vedtaksId?.toString(),
        enhetsId: props.brev.info.avsenderEnhet.enhetNr.toString(),
      },
    });
  };

  const { guardedSubmit, warnModalProps } = useGuardedFormSubmit({
    form,
    getWarning,
    onConfirmedSubmit: () => onSubmit(proceedToForhandsvisning),
    onInvalidSubmit: () => void documentCoordinator.selectDocument(undefined),
    onWarnModalClosed: (warn) => {
      if (warn?.kind === "fritekst" || warn?.kind === "fritekstOgTekstValg") {
        const focus = firstUneditedFritekst(editorState.redigertBrev);
        if (focus) setEditorState((s) => ({ ...s, focus }));
      }
    },
  });

  return (
    <VStack asChild height="100%">
      <form onSubmit={guardedSubmit}>
        <WarnModal {...warnModalProps} fortsettLabel="Fortsett til forhåndsvisning" />
        {forbidReason && <AttestForbiddenModal onClose={() => setForbidReason(null)} reason={forbidReason} />}
        {unexpectedError && <ApiError error={unexpectedError} title="Uventet feil ved attestering" />}
        <ActiveDocumentProvider
          activeVedleggId={documentCoordinator.activeVedleggId}
          onSelectDocument={documentCoordinator.selectDocument}
          redigeringsflate="attestant-redigering"
          registerVedleggSave={documentCoordinator.registerVedleggSave}
        >
          <ThreeSectionLayout
            bottom={
              <Button
                icon={<ArrowRightIcon />}
                iconPosition="right"
                loading={freeze || documentCoordinator.savingPendingDocuments}
                size="small"
              >
                Fortsett
              </Button>
            }
            left={
              <BrevOgVedleggEditorSidepanel
                brevId={props.brev.info.id}
                brevmalPanel={
                  <FormProvider {...form}>
                    <VStack gap="space-32" paddingBlock="space-16 space-0">
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
                        <UnderskriftTextFieldV2
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
                          readOnly
                        />
                      </VStack>
                    </VStack>
                  </FormProvider>
                }
                saksId={props.saksId}
              />
            }
            right={
              <BrevOgVedleggEditor
                brev={props.brev}
                freeze={freeze}
                renderBrev={() => (
                  <AttestantDiffProviderV2
                    diff={attestantDiff.activeDiff}
                    diffHash={attestantDiff.diffHash}
                    disableDiffMode={disableDiffMode}
                    showDeletions={attestantDiff.showDeletions}
                    showInsertions={attestantDiff.showInsertions}
                  >
                    <ManagedLetterEditorV2
                      diffToggleMenu={
                        diffFeatureToggle.data?.enabled === true && (
                          <>
                            <AttestantDiffMenu
                              onToggle={(toggle, checked) => void handleDiffToggle(toggle, checked)}
                              ref={setDiffMenuElement}
                              status={attestantDiff.status}
                              toggles={attestantDiff.toggles}
                              togglesRef={setDiffTogglesElement}
                            />
                            <AttestantDiffAnnouncement menu={diffMenuElement} toggles={diffTogglesElement} />
                          </>
                        )
                      }
                      error={error}
                      freeze={freeze}
                      showDebug={showDebug}
                    />
                  </AttestantDiffProviderV2>
                )}
                saksId={props.saksId}
              />
            }
          />
          <ReservertBrevError
            doRetry={props.doReload}
            onNeiClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brevbehandler",
                params: { saksId: props.saksId },
                search: {
                  vedtaksId: props.brev.info.vedtaksId?.toString(),
                  enhetsId: props.brev.info.avsenderEnhet.enhetNr.toString(),
                },
              })
            }
            reservasjon={reservasjonQuery.data}
          />
        </ActiveDocumentProvider>
      </form>
    </VStack>
  );
}
