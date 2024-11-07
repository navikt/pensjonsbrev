import { css } from "@emotion/react";
import { ArrowCirclepathIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, Box, Button, Heading, HStack, Label, Modal, Skeleton, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { type Dispatch, type SetStateAction, useCallback, useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import {
  getBrev,
  getBrevReservasjon,
  hurtiglagreBrev,
  hurtiglagreSaksbehandlerValg,
  oppdaterSignatur,
  tilbakestillBrev,
} from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { getSakContext } from "~/api/skribenten-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { newItem, newItemList, newLiteral, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { AutoSavingTextField } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { SaksbehandlerValgModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import type { BrevResponse, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";
import { queryFold } from "~/utils/tanstackUtils";

import { nyBrevInfo, nyBrevResponse, nyRedigertBrev } from "../../../../cypress/utils/brevredigeringTestUtils";

export const Route = createFileRoute("/saksnummer/$saksId/brev/$brevId")({
  parseParams: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  component: RedigerBrevPage,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
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
                as={Link}
                css={css`
                  padding: 4px 0;
                `}
                params={{ saksId: saksId }}
                size="small"
                to="/saksnummer/$saksId/brevbehandler"
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
    success: (data) => <RedigerBrev brev={data} doReload={brevQuery.refetch} saksId={saksId} vedtaksId={undefined} />,
  });
}

const ReservertBrevError = ({ reservasjon, doRetry }: { reservasjon?: ReservasjonResponse; doRetry: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
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
          <Button onClick={() => navigate({ to: BrevvelgerRoute.fullPath })} type="button" variant="tertiary">
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
  //brev,
  doReload,
  saksId,
  vedtaksId,
}: {
  //brev: BrevResponse;
  doReload: () => void;
  saksId: string;
  vedtaksId: string | undefined;
}) {
  const brev = useMemo(() => {
    return nyBrevResponse({
      info: nyBrevInfo({
        id: 8,
      }),
      redigertBrev: nyRedigertBrev({
        blocks: [
          {
            id: null,
            editable: true,
            content: [
              newLiteral(
                "Dette er kun et avsnitt. Et langt avsnitt. lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst lang tekst",
              ),
            ],
            deletedContent: [],
            type: "PARAGRAPH",
          },
          {
            id: 822_540_105,
            editable: true,
            content: [
              newLiteral("Dette er et avsnitt, med en punktliste i samme blocken"),
              newItemList(newItem("Punkt 1"), newItem("Punkt 2"), newItem("Punkt 3")),
            ],
            deletedContent: [],
            type: "PARAGRAPH",
          },
          // {
          //   id: 822_540_105,
          //   editable: true,
          //   content: [
          //     newLiteral("Dette er et avsnitt"),
          //     newVariable({ text: " og en variable" }),
          //     newLiteral(" med en punktliste i samme blocken"),
          //     newItemList(newItem("Punkt 1"), newItem("Punkt 2"), newItem("Punkt 3")),
          //   ],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
          // {
          //   id: 173_660_319,
          //   editable: true,
          //   content: [
          //     newItemList(newItem("Kun punktliste 1"), newItem("kun punktliste 2"), newItem("kun punktliste 3")),
          //   ],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
          // {
          //   id: 173_660_319,
          //   editable: true,
          //   content: [
          //     newItemList(
          //       newItem("Punkt 1 - avsnitt etter punktliste"),
          //       newItem("Punkt 2 - avsnitt etter punktliste"),
          //       newItem("Punkt 3 - avsnitt etter punktliste"),
          //     ),
          //     newLiteral("Avsnitt etter punktliste i samme block"),
          //     newVariable({ text: " og en variable" }),
          //     newLiteral("slutt text"),
          //   ],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
          // {
          //   id: 173_660_319,
          //   editable: true,
          //   content: [newItemList(newItem("itemList som skal merges på starten"))],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
          // {
          //   id: 173_660_319,
          //   editable: true,
          //   content: [newLiteral("Skal merges i midten")],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
          // {
          //   id: 173_660_319,
          //   editable: true,
          //   content: [newItemList(newItem("itemList som skal merges på slutten"))],
          //   deletedContent: [],
          //   type: "PARAGRAPH",
          // },
        ],
      }),
      saksbehandlerValg: {
        mottattSoeknad: "2024-09-10",
        ytelse: "Supplerende stønad",
        land: "Spania",
        svartidUker: "3",
      },
    });
  }, []);

  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brev));
  const brevmal = useQuery({
    queryKey: getSakContext.queryKey(saksId, vedtaksId),
    queryFn: () => getSakContext.queryFn(saksId, vedtaksId),
    select: (data) => data.brevMetadata.find((brevmal) => brevmal.id === brev.info.brevkode),
  });

  const navigate = useNavigate({ from: Route.fullPath });
  const showDebug = useSearch({
    strict: false,
    select: (search: { debug?: string | boolean }) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const saksbehandlerValgMutation = useHurtiglagreMutation(brev.info.id, setEditorState, hurtiglagreSaksbehandlerValg);
  const signaturMutation = useHurtiglagreMutation(brev.info.id, setEditorState, oppdaterSignatur);
  const redigertBrevMutation = useHurtiglagreMutation(
    brev.info.id,
    setEditorState,
    (brevId, args: { redigertBrev: EditedLetter; frigiReservasjon?: boolean }) => {
      applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      return hurtiglagreBrev(brevId, args.redigertBrev, args.frigiReservasjon);
    },
  );

  const onSubmit = (values: RedigerBrevSidemenyFormData, onSuccess?: () => void) => {
    saksbehandlerValgMutation.mutate(values.saksbehandlerValg, {
      onSuccess: () => {
        signaturMutation.mutate(values.signatur, {
          onSuccess: onSuccess,
        });
      },
    });
  };

  /*const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(brev.info.id),
    refetchInterval: 10_000,
  });*/

  /*useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.isDirty) {
        redigertBrevMutation.mutate({ redigertBrev: editorState.redigertBrev });
      }
    }, 5000);
    return () => clearTimeout(timoutId);
  }, [editorState.isDirty, editorState.redigertBrev, redigertBrevMutation]);*/

  useEffect(() => {
    if (editorState.redigertBrevHash !== brev.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: brev.redigertBrev,
        redigertBrevHash: brev.redigertBrevHash,
      }));
    }
  }, [brev.redigertBrev, brev.redigertBrevHash, editorState.redigertBrevHash, setEditorState]);

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: {
        ...brev.saksbehandlerValg,
      },
      signatur: brev.redigertBrev.signatur.saksbehandlerNavn,
    }),
    [brev.redigertBrev.signatur.saksbehandlerNavn, brev.saksbehandlerValg],
  );
  const formRef = useRef<HTMLFormElement>(null);

  const form = useForm<RedigerBrevSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  const requestSubmit = useCallback(async () => {
    formRef.current?.requestSubmit();
  }, [formRef]);

  return (
    <FormProvider {...form}>
      <form onSubmit={form.handleSubmit((v) => onSubmit(v))} ref={formRef}>
        {/* <ReservertBrevError doRetry={doReload} reservasjon={reservasjonQuery.data} /> */}
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
              <Heading size="small">{brevmal.data?.name}</Heading>
              <OpprettetBrevSidemenyForm brev={brev} submitOnChange={requestSubmit} />
            </VStack>
            <LetterEditor
              editorHeight={"var(--main-page-content-height)"}
              editorState={editorState}
              error={redigertBrevMutation.isError || saksbehandlerValgMutation.isError || signaturMutation.isError}
              freeze={
                redigertBrevMutation.isPending || saksbehandlerValgMutation.isPending || signaturMutation.isPending
              }
              setEditorState={setEditorState}
              showDebug={showDebug}
            />
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
                onClick={() => {
                  navigate({
                    to: "/saksnummer/$saksId/brevvelger",
                    params: { saksId: saksId },
                    search: (s) => ({ ...s, brevId: brev.info.id.toString() }),
                  });
                }}
                size="small"
                type="button"
                variant="tertiary"
              >
                Tilbake til brevvelger
              </Button>
              <Button
                loading={
                  redigertBrevMutation.isPending || saksbehandlerValgMutation.isPending || signaturMutation.isPending
                }
                onClick={async () => {
                  onSubmit(form.getValues(), () => {
                    redigertBrevMutation.mutate(
                      { redigertBrev: editorState.redigertBrev, frigiReservasjon: true },
                      {
                        onSuccess: () => {
                          navigate({
                            to: "/saksnummer/$saksId/brevbehandler",
                            params: { saksId },
                            search: { brevId: brev.info.id },
                          });
                        },
                      },
                    );
                  });
                }}
                size="small"
                type="button"
              >
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

function useHurtiglagreMutation<T>(
  brevId: number,
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>,
  mutationFunction: (brevId: number, body: T) => Promise<BrevResponse>,
) {
  const queryClient = useQueryClient();

  return useMutation<BrevResponse, AxiosError, T>({
    mutationFn: async (body) => mutationFunction(brevId, body),
    onSuccess: (response: BrevResponse) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: response.redigertBrev,
        redigertBrevHash: response.redigertBrevHash,
        saksbehandlerValg: response.saksbehandlerValg,
        info: response.info,
        isDirty: false,
      }));
    },
  });
}

enum BrevSidemenyTabs {
  TEKSTVALG = "TEKSTVALG",
  OVERSTYRING = "OVERSTYRING",
}

const OpprettetBrevSidemenyForm = (props: { brev: BrevResponse; submitOnChange?: () => void }) => {
  return (
    <Tabs
      css={css`
        width: 100%;

        .navds-tabs__scroll-button {
          /* vi har bare 2 tabs, så det gir ikke mening tab listen skal være scrollbar. Den tar i tillegg mye ekstra plass når skjermen er <1024px */
          display: none;
        }
      `}
      defaultValue={BrevSidemenyTabs.TEKSTVALG}
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
      <Tabs.Panel
        css={css`
          display: flex;
          flex-direction: column;
          gap: var(--a-spacing-6);
          margin-top: 12px;
        `}
        value={BrevSidemenyTabs.TEKSTVALG}
      >
        <SaksbehandlerValgModelEditor
          brevkode={props.brev.info.brevkode}
          fieldsToRender={"optional"}
          submitOnChange={props.submitOnChange}
        />
        <AutoSavingTextField
          field={"signatur"}
          fieldType={{
            type: "scalar",
            nullable: false,
            kind: "STRING",
          }}
          onSubmit={props.submitOnChange}
          timeoutTimer={2500}
          type={"text"}
        />
      </Tabs.Panel>
      <Tabs.Panel
        css={css`
          display: flex;
          flex-direction: column;
          gap: var(--a-spacing-6);
          margin-top: 12px;
        `}
        value={BrevSidemenyTabs.OVERSTYRING}
      >
        <SaksbehandlerValgModelEditor
          brevkode={props.brev.info.brevkode}
          fieldsToRender={"required"}
          submitOnChange={props.submitOnChange}
        />
        <AutoSavingTextField
          field={"signatur"}
          fieldType={{
            type: "scalar",
            nullable: false,
            kind: "STRING",
          }}
          onSubmit={props.submitOnChange}
          timeoutTimer={2500}
          type={"text"}
        />
      </Tabs.Panel>
    </Tabs>
  );
};
