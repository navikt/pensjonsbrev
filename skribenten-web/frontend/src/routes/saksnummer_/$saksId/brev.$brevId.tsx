import { css } from "@emotion/react";
import { ArrowCirclepathIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, BoxNew, Button, Heading, HStack, Label, Modal, Skeleton, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getBrev, getBrevmetadataQuery, getBrevReservasjon, oppdaterBrev, tilbakestillBrev } from "~/api/brev-queries";
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
import { useBrevEditorWarnings } from "~/hooks/useBrevEditorWarnings";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import type { BrevResponse, OppdaterBrevRequest, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import { queryFold } from "~/utils/tanstackUtils";

export const Route = createFileRoute("/saksnummer_/$saksId/brev/$brevId")({
  params: {
    parse: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  },
  component: () => <RedigerBrevPage />,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const { enhetsId, vedtaksId } = Route.useSearch();
  const navigate = Route.useNavigate();
  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (_, error: AxiosError) => error && error.response?.status !== 423 && error.response?.status !== 409,
    throwOnError: (error: AxiosError) => error.response?.status !== 423 && error.response?.status !== 409,
  });

  return queryFold({
    query: brevQuery,
    initial: () => null,
    pending: () => (
      <div
        css={css`
          display: flex;
          flex: 1;
        `}
      >
        <Skeleton height={"auto"} variant="rectangle" width={"33%"} />
        <Skeleton height={"auto"} variant="rectangle" width={"66%"} />
      </div>
    ),
    error: (error) => {
      if (error.response?.status === 423 && error.response?.data) {
        return (
          <ReservertBrevError doRetry={brevQuery.refetch} reservasjon={error.response.data as ReservasjonResponse} />
        );
      }
      if (error.response?.status === 409) {
        return (
          <BoxNew
            background="default"
            css={css`
              display: flex;
              flex: 1;
            `}
            padding="6"
          >
            <VStack align="start" gap="space-8">
              <Label size="small">Brevet er arkivert, og kan derfor ikke redigeres.</Label>
              <Button
                css={css`
                  padding: 4px 0;
                `}
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
            </VStack>
          </BoxNew>
        );
      }
      return <ApiError error={error} title={"En feil skjedde ved henting av brev"} />;
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
        header={{ heading: "Brevet redigeres av noen andre", closeButton: false }}
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
            onClick={() => navigate({ to: BrevvelgerRoute.fullPath, search: { enhetsId, vedtaksId } })}
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

const TilbakestillMalModal = (props: {
  brevId: number;
  åpen: boolean;
  onClose: () => void;
  resetEditor: (brevResponse: BrevResponse) => void;
}) => {
  const queryClient = useQueryClient();
  const tilbakestillMutation = useMutation<BrevResponse, Error>({
    mutationFn: () => tilbakestillBrev(props.brevId),
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(props.brevId), response);
      props.resetEditor(response);
      props.onClose();
    },
  });

  return (
    <Modal
      css={css`
        border-radius: var(--ax-radius-4);
      `}
      header={{
        heading: "Vil du tilbakestille brevmalen?",
      }}
      onClose={props.onClose}
      open={props.åpen}
      portal
      width={600}
    >
      <Modal.Body>
        <BodyLong>Innholdet du har endret eller lagt til i brevet vil bli slettet.</BodyLong>
        <BodyLong>Du kan ikke angre denne handlingen.</BodyLong>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button onClick={props.onClose} type="button" variant="tertiary">
            Nei, behold brevet
          </Button>

          <Button
            loading={tilbakestillMutation.isPending}
            onClick={() => tilbakestillMutation.mutate()}
            type="button"
            variant="danger"
          >
            Ja, tilbakestill malen
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
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
  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);

  const [warnOpen, setWarnOpen] = useState(false);
  const [warn, setWarn] = useState<{ kind: WarnModalKind; count?: number } | null>(null);

  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const navigateToBrevbehandler = () =>
    navigate({
      to: "/saksnummer/$saksId/brevbehandler",
      params: { saksId },
      search: { brevId: brev.info.id, enhetsId, vedtaksId },
    });

  const brevmal = useQuery({
    ...getBrevmetadataQuery,
    select: (data) => data.find((brevmal) => brevmal.id === brev.info.brevkode),
  });

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevRequest>({
    mutationFn: (values) =>
      oppdaterBrev({
        saksId: Number.parseInt(saksId),
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
      <form
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-self: center;

          @media (width <= 1023px) {
            align-self: start;
          }
          min-width: 946px;
          max-width: 1106px;
          background: var(--ax-bg-default);
          border-left: 1px solid var(--ax-neutral-300);
          border-right: 1px solid var(--ax-neutral-300);
        `}
        onSubmit={guardedSubmit}
      >
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
        <div
          css={css`
            display: grid;
            grid-template-columns: minmax(304px, 384px) minmax(640px, 720px);

            > :first-of-type {
              padding: var(--ax-space-24);
              border-right: 1px solid var(--ax-neutral-300);
              height: var(--main-page-content-height);
              overflow-y: auto;
            }

            @media (width <= 1024px) {
              > :first-of-type {
                padding: var(--ax-space-12);
              }
            }
          `}
        >
          <VStack gap="space-12">
            <Heading size="small" spacing>
              {brevmal.data?.name}
            </Heading>
            <OpprettetBrevSidemenyForm brev={brev} submitOnChange={onTekstValgAndOverstyringChange} />
            <UnderskriftTextField of="Saksbehandler" />
          </VStack>
          <ManagedLetterEditor brev={brev} error={error} freeze={freeze} showDebug={showDebug} />
        </div>
        <HStack
          css={css`
            position: sticky;
            bottom: 0;
            left: 0;
            width: 100%;
            background: var(--ax-bg-default);

            border-top: 1px solid var(--ax-neutral-300);
            padding: var(--ax-space-8) var(--ax-space-16);
          `}
          justify={"space-between"}
        >
          <Button onClick={() => setVilTilbakestilleMal(true)} size="small" type="button" variant="danger">
            <HStack align="center" gap="space-4">
              <ArrowCirclepathIcon
                css={css`
                  transform: scaleX(-1);
                `}
                fontSize="1.5rem"
                title="Tilbakestill mal"
              />
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
              variant="tertiary"
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
      </form>
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

  const panelStyle = css`
    /* stylelint-disable-next-line nesting-selector-no-missing-scoping-root */
    &[data-state="active"] {
      display: flex;
    }
    flex-direction: column;
    gap: var(--ax-space-20);
    margin-top: var(--ax-space-20);
  `;

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
    <Tabs
      css={css`
        width: 100%;

        .aksel-tabs__scroll-button {
          /* vi har bare 2 tabs, så det gir ikke mening tab listen skal være scrollbar. Den tar i tillegg mye ekstra plass når skjermen er <1024px */
          display: none;
        }
      `}
      defaultValue={defaultTab}
      fill
      size="small"
    >
      <Tabs.List
        css={css`
          display: grid;
          grid-template-columns: repeat(2, 1fr);
        `}
      >
        <Tabs.Tab label="Tekstvalg" value={BrevSidemenyTabs.TEKSTVALG} />
        <Tabs.Tab label="Overstyring" value={BrevSidemenyTabs.OVERSTYRING} />
      </Tabs.List>

      <Tabs.Panel css={panelStyle} value={BrevSidemenyTabs.TEKSTVALG}>
        <SaksbehandlerValgModelEditor
          brevkode={brev.info.brevkode}
          fieldsToRender="optional"
          specificationFormElements={specificationFormElements}
          submitOnChange={submitOnChange}
        />
      </Tabs.Panel>

      <Tabs.Panel css={panelStyle} value={BrevSidemenyTabs.OVERSTYRING}>
        <SaksbehandlerValgModelEditor
          brevkode={brev.info.brevkode}
          fieldsToRender="required"
          specificationFormElements={specificationFormElements}
          submitOnChange={submitOnChange}
        />
      </Tabs.Panel>
    </Tabs>
  );
};
