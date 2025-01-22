import { css } from "@emotion/react";
import { ArrowCirclepathIcon, ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyLong, Box, Button, Heading, HStack, Label, Modal, Skeleton, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import {
  getBrev,
  getBrevReservasjon,
  oppdaterBrev,
  oppdaterSaksbehandlerValg,
  oppdaterSignatur,
  tilbakestillBrev,
} from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { AutoSavingTextField } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { ApiError } from "~/components/ApiError";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import type { BrevResponse, OppdaterBrevRequest, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import { queryFold } from "~/utils/tanstackUtils";

import { BrevmalAlternativer } from "./vedtak.$brevId/redigering/route";

export const Route = createFileRoute("/saksnummer/$saksId/brev/$brevId")({
  parseParams: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  component: RedigerBrevPage,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
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
          <ReservertBrevError
            doRetry={brevQuery.refetch}
            onNeiClick={() => navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId } })}
            reservasjon={error.response.data as ReservasjonResponse}
          />
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
    success: (data) => (
      <ManagedLetterEditorContextProvider brev={data}>
        <RedigerBrev brev={data} doReload={brevQuery.refetch} saksId={saksId} />
      </ManagedLetterEditorContextProvider>
    ),
  });
}

export const ReservertBrevError = ({
  reservasjon,
  doRetry,
  onNeiClick,
}: {
  reservasjon?: ReservasjonResponse;
  doRetry: () => void;
  onNeiClick: () => void;
}) => {
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
          <Button onClick={onNeiClick} type="button" variant="tertiary">
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

function RedigerBrev({ brev, doReload, saksId }: { brev: BrevResponse; doReload: () => void; saksId: string }) {
  const navigate = useNavigate({ from: Route.fullPath });
  const [vilTilbakestilleMal, setVilTilbakestilleMal] = useState(false);
  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const saksbehandlerValgMutation = useMutation<BrevResponse, AxiosError, SaksbehandlerValg>({
    mutationFn: (values) => oppdaterSaksbehandlerValg(brev.info.id, values),
    onSuccess: (response) => onSaveSuccess(response),
  });

  const signaturMutation = useMutation<BrevResponse, AxiosError, string>({
    mutationFn: (signatur) => oppdaterSignatur(brev.info.id, signatur),
    onSuccess: (response) => onSaveSuccess(response),
  });

  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevRequest>({
    mutationFn: (values) =>
      oppdaterBrev({
        saksId: Number.parseInt(saksId),
        brevId: brev.info.id,
        request: {
          redigertBrev: values.redigertBrev,
          saksbehandlerValg: values.saksbehandlerValg,
          signatur: values.signatur,
        },
      }),
  });

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: { ...brev.saksbehandlerValg },
      signatur: brev.redigertBrev.signatur.saksbehandlerNavn,
    }),
    [brev.redigertBrev.signatur.saksbehandlerNavn, brev.saksbehandlerValg],
  );

  const form = useForm<RedigerBrevSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  const onTekstValgAndOverstyringChange = () => {
    form.trigger().then((isValid) => {
      if (isValid) {
        saksbehandlerValgMutation.mutate(form.getValues().saksbehandlerValg, {
          onSuccess: () => {
            signaturMutation.mutate(form.getValues().signatur);
          },
        });
      }
    });
  };

  const onSubmit = (values: RedigerBrevSidemenyFormData, onSuccess?: () => void) => {
    oppdaterBrevMutation.mutate(
      {
        redigertBrev: editorState.redigertBrev,
        saksbehandlerValg: values.saksbehandlerValg,
        signatur: values.signatur,
      },
      {
        onSuccess: (response) => {
          onSaveSuccess(response);
          onSuccess?.();
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

  const freeze = saksbehandlerValgMutation.isPending || signaturMutation.isPending || oppdaterBrevMutation.isPending;
  const error = saksbehandlerValgMutation.isError || signaturMutation.isError || oppdaterBrevMutation.isError;

  return (
    <FormProvider {...form}>
      <form
        onSubmit={form.handleSubmit((v) =>
          onSubmit(v, () =>
            navigate({
              to: "/saksnummer/$saksId/brevbehandler",
              params: { saksId },
              search: { brevId: brev.info.id },
            }),
          ),
        )}
      >
        <ThreeSectionLayout
          bottom={
            <HStack
              css={css`
                width: 100%;
              `}
              justify={"space-between"}
            >
              {vilTilbakestilleMal && (
                <TilbakestillMalModal
                  brevId={brev.info.id}
                  onClose={() => setVilTilbakestilleMal(false)}
                  resetEditor={(brevResponse) => setEditorState(Actions.create(brevResponse))}
                  åpen={vilTilbakestilleMal}
                />
              )}
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
                <Button loading={oppdaterBrevMutation.isPending} size="small">
                  <HStack align={"center"} gap="2">
                    <Label size="small">Fortsett</Label> <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
                  </HStack>
                </Button>
              </HStack>
            </HStack>
          }
          left={
            <VStack gap="3">
              <Heading size="small">{brev.redigertBrev.title}</Heading>
              <BrevmalAlternativer brevkode={brev.info.brevkode} submitOnChange={onTekstValgAndOverstyringChange} />
              <AutoSavingTextField
                field={"signatur"}
                fieldType={{
                  type: "scalar",
                  nullable: false,
                  kind: "STRING",
                }}
                onSubmit={onTekstValgAndOverstyringChange}
                timeoutTimer={2500}
                type={"text"}
              />
            </VStack>
          }
          right={
            <>
              <ReservertBrevError
                doRetry={doReload}
                onNeiClick={() => navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId } })}
                reservasjon={reservasjonQuery.data}
              />
              <ManagedLetterEditor brev={brev} error={error} freeze={freeze} />
            </>
          }
        />
      </form>
    </FormProvider>
  );
}
