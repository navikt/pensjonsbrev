import { ArrowCirclepathReverseIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, Box, Button, Heading, HGrid, HStack, Label, Modal, Skeleton, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrev, getBrevmetadata, getBrevReservasjon, oppdaterBrev } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { WarnModal, type WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";
import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { UnderskriftTextField } from "~/components/ManagedLetterEditor/UnderskriftTextField";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { useBrevEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import type { BrevResponse, OppdaterBrevRequest, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
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

  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (_, error: AxiosError) => error && error.response?.status !== 423 && error.response?.status !== 409,
    throwOnError: (error: AxiosError) => error.response?.status !== 423 && error.response?.status !== 409,
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
          <Skeleton height="auto" variant="rectangle" width="33%" />
          <Skeleton height="auto" variant="rectangle" width="66%" />
        </HStack>
      </Box>
    ),
    error: (error) => {
      if (error.response?.status === 423 && error.response?.data) {
        return (
          <ReservertBrevError doRetry={brevQuery.refetch} reservasjon={error.response.data as ReservasjonResponse} />
        );
      }
      if (error.response?.status === 409) {
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
      return <ApiError error={error} title="En feil skjedde ved henting av brev" />;
    },
    success: (brev) => (
      <ManagedLetterEditorContextProvider brev={brev}>
        <RedigerBrev brev={brev} doReload={brevQuery.refetch} saksId={saksId} vedtaksId={vedtaksId} />
      </ManagedLetterEditorContextProvider>
    ),
  });
}

const ReservertBrevError = ({ reservasjon, doRetry }: { reservasjon?: ReservasjonResponse; doRetry: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();
  if (reservasjon) {
    return (
      <Modal
        header={{
          heading: "Brevet redigeres av noen andre",
          closeButton: false,
        }}
        onClose={() => {}}
        open={!reservasjon.vellykket}
        width={478}
      >
        <Modal.Body>
          <BodyLong>
            Brevet er utilgjengelig for deg fordi {reservasjon.reservertAv.navn} har brevet åpent. Ønsker du å forsøke å
            åpne brevet på nytt?
          </BodyLong>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={doRetry} type="button">
            Ja, åpne på nytt
          </Button>
          <Button
            onClick={() =>
              navigate({
                to: BrevvelgerRoute.fullPath,
                search: { enhetsId, vedtaksId },
              })
            }
            type="button"
            variant="tertiary"
          >
            Nei, gå til brevbehandler
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
};

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
  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);

  const [warnOpen, setWarnOpen] = useState(false);
  const [warn, setWarn] = useState<{
    kind: WarnModalKind;
    count?: number;
  } | null>(null);

  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

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

  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevRequest>({
    mutationFn: (values) =>
      oppdaterBrev({
        saksId: Number.parseInt(saksId, 10),
        brevId: brev.info.id,
        request: {
          redigertBrev: values.redigertBrev,
          saksbehandlerValg: values.saksbehandlerValg,
        },
      }),
    onSuccess: onSaveSuccess,
  });

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: {
        ...brev.saksbehandlerValg,
      },
    }),
    [brev.saksbehandlerValg],
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
        oppdaterBrevMutation.mutate({
          redigertBrev: editorState.redigertBrev,
          saksbehandlerValg: form.getValues().saksbehandlerValg,
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
            brevkode: brev.info.brevkode,
            varighetSekunder,
            varighetMinutter: Math.round(varighetSekunder / 60),
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

  const freeze = oppdaterBrevMutation.isPending;

  const error = oppdaterBrevMutation.isError;

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
            <ReservertBrevError doRetry={doReload} reservasjon={reservasjonQuery.data} />
            {vilTilbakestilleMal && (
              <TilbakestillMalModal
                brevId={brev.info.id}
                onClose={() => setVilTilbakestilleMal(false)}
                resetEditor={(brevResponse) => setEditorState(Actions.create(brevResponse))}
                åpen={vilTilbakestilleMal}
              />
            )}
            <HGrid columns="minmax(304px, 384px) minmax(640px, 694px)">
              <Box
                asChild
                borderColor="neutral-subtle"
                borderWidth="0 1 0 0"
                height="var(--main-page-content-height)"
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
              <ManagedLetterEditor brev={brev} error={error} freeze={freeze} showDebug={showDebug} />
            </HGrid>
            <Box
              asChild
              background="default"
              borderColor="neutral-subtle"
              borderWidth="1 0 0 0"
              bottom="space-0"
              left="space-0"
              position="sticky"
            >
              <HStack justify="space-between" paddingBlock="space-8" paddingInline="space-16">
                <Button
                  data-color="danger"
                  onClick={() => setVilTilbakestilleMal(true)}
                  size="small"
                  type="button"
                  variant="primary"
                >
                  <HStack align="center" gap="space-4">
                    <ArrowCirclepathReverseIcon fontSize="1.5rem" title="Tilbakestill mal" />
                    Tilbakestill malen
                  </HStack>
                </Button>
                <HStack gap="space-8" justify="end">
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
                    variant="secondary"
                  >
                    Tilbake til brevvelger
                  </Button>
                  <Button loading={oppdaterBrevMutation.isPending} size="small" type="submit">
                    <HStack align="center" gap="space-8">
                      <Label size="small">Fortsett</Label> <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
                    </HStack>
                  </Button>
                </HStack>
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
