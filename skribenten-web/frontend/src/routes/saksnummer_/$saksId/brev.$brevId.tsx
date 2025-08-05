import { css } from "@emotion/react";
import { ArrowCirclepathIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, Box, Button, Heading, HStack, Label, Modal, Skeleton, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import {
  getBrev,
  getBrevReservasjon,
  oppdaterBrev,
  oppdaterSaksbehandlerValg,
  tilbakestillBrev,
} from "~/api/brev-queries";
import { getSakContextQuery } from "~/api/skribenten-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
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
          <Box
            background="surface-default"
            css={css`
              display: flex;
              flex: 1;
            `}
            padding="6"
          >
            <VStack align="start" gap="2">
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
          </Box>
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
        border-radius: 0.25rem;
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
        <HStack gap="4">
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

  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const brevmal = useQuery({
    ...getSakContextQuery(saksId, vedtaksId),
    select: (data) => data.brevMetadata.find((brevmal) => brevmal.id === brev.info.brevkode),
  });

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const saksbehandlerValgMutation = useMutation<BrevResponse, AxiosError, SaksbehandlerValg>({
    mutationFn: (valg) => oppdaterSaksbehandlerValg(brev.info.id, valg),
    onSuccess: onSaveSuccess,
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

  const onTekstValgAndOverstyringChange = () => {
    form.trigger().then((isValid) => {
      if (isValid) {
        saksbehandlerValgMutation.mutate(form.getValues().saksbehandlerValg);
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

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(brev.info.id),
    refetchInterval: 10_000,
  });

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  const freeze = saksbehandlerValgMutation.isPending || oppdaterBrevMutation.isPending;

  const error = saksbehandlerValgMutation.isError || oppdaterBrevMutation.isError;

  // TODO: Trenger form å være helt ytterst her? Kunne vi hatt det lenger inn i hierarkiet, f.eks i OpprettetBrevSidemenyForm.
  return (
    <FormProvider {...form}>
      <form
        onSubmit={form.handleSubmit((v) =>
          onSubmit(v, () =>
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId },
              search: { brevId: brev.info.id, enhetsId, vedtaksId },
            }),
          ),
        )}
      >
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
            background: var(--a-white);
            display: flex;
            flex-direction: column;
            border-left: 1px solid var(--a-gray-200);
            border-right: 1px solid var(--a-gray-200);
          `}
        >
          <div
            css={css`
              display: grid;
              grid-template-columns: 25% 75%;

              > :first-of-type {
                padding: var(--a-spacing-6);
                border-right: 1px solid var(--a-gray-200);
              }

              @media (width <= 1024px) {
                > :first-of-type {
                  padding: var(--a-spacing-3);
                }
              }
            `}
          >
            <VStack gap="3">
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
              background: var(--a-white);

              border-top: 1px solid var(--a-gray-200);
              padding: 0.5rem 1rem;
            `}
            justify={"space-between"}
          >
            <Button onClick={() => setVilTilbakestilleMal(true)} size="small" type="button" variant="danger">
              <HStack align={"center"} gap="1">
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
            <HStack gap="2" justify={"end"}>
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
              <Button loading={oppdaterBrevMutation.isPending} size="small">
                <HStack align={"center"} gap="2">
                  <Label size="small">Fortsett</Label> <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
                </HStack>
              </Button>
            </HStack>
          </HStack>
        </div>
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
  const specificationFormElements = usePartitionedModelSpecification(brev.info.brevkode);

  const optionalFields = specificationFormElements.status === "success" ? specificationFormElements.optionalFields : [];
  const requiredFields = specificationFormElements.status === "success" ? specificationFormElements.requiredfields : [];
  const hasOptional = optionalFields.length > 0;
  const hasRequired = requiredFields.length > 0;

  const panelStyle = css`
    &[data-state="active"] {
      display: flex;
    }
    flex-direction: column;
    gap: 1.125rem;
    margin-top: 1.125rem;
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

        .navds-tabs__scroll-button {
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
