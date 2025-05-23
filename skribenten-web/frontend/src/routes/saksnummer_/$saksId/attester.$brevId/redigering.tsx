import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Label, Loader, Switch, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useCallback, useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import {
  attesteringBrevKeys,
  getBrevAttesteringQuery,
  getBrevReservasjon,
  oppdaterAttestantSignatur,
  oppdaterBrevtekst,
  oppdaterSaksbehandlerValg,
} from "~/api/brev-queries";
import { attesterBrev, hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { AutoSavingTextField } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { ApiError } from "~/components/ApiError";
import ArkivertBrev from "~/components/ArkivertBrev";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ReservertBrevError from "~/components/ReservertBrevError";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import type { BrevResponse, OppdaterBrevRequest, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import type { EditedLetter } from "~/types/brevbakerTypes";
import { queryFold } from "~/utils/tanstackUtils";

export const Route = createFileRoute("/saksnummer_/$saksId/attester/$brevId/redigering")({
  component: () => <VedtakWrapper />,
});

interface VedtakSidemenyFormData {
  attestantSignatur: string;
  saksbehandlerValg: SaksbehandlerValg;
}

const VedtakWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const { vedtaksId, enhetsId } = Route.useSearch();

  const hentBrevQuery = useQuery({
    ...getBrevAttesteringQuery(saksId, Number(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
  });

  return queryFold({
    query: hentBrevQuery,
    initial: () => null,
    pending: () => (
      <Box
        background="bg-default"
        css={css`
          display: flex;
          flex-direction: column;
          flex: 1;
          align-items: center;
          padding-top: var(--a-spacing-8);
        `}
      >
        <VStack align="center" gap="1">
          <Loader size="3xlarge" title="henter brev..." />
          <Heading size="large">Henter brev....</Heading>
        </VStack>
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
                search: { vedtaksId, enhetsId },
              })
            }
            reservasjon={err.response.data as ReservasjonResponse}
          />
        );
      }
      if (err.response?.status === 409) {
        return <ArkivertBrev saksId={saksId} />;
      }

      return (
        <Box
          background="bg-default"
          css={css`
            display: flex;
            flex-direction: column;
            flex: 1;
            align-items: center;
            padding-top: var(--a-spacing-8);
          `}
        >
          <ApiError error={err} title={"En feil skjedde ved henting av vedtaksbrev"} />
        </Box>
      );
    },
    success: (brev) => <Vedtak brev={brev} doReload={hentBrevQuery.refetch} saksId={saksId} />,
  });
};

function useHurtiglagreMutation<T>(
  brevId: number,
  setEditorState: React.Dispatch<React.SetStateAction<LetterEditorState>>,
  mutationFunction: (brevId: number, body: T) => Promise<BrevResponse>,
) {
  const queryClient = useQueryClient();
  return useMutation<BrevResponse, AxiosError, T>({
    mutationFn: (body) => mutationFunction(brevId, body),
    onSuccess: (response) => {
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
      setEditorState((prev) => ({
        ...prev,
        redigertBrev: response.redigertBrev,
        redigertBrevHash: response.redigertBrevHash,
        saksbehandlerValg: response.saksbehandlerValg,
        info: response.info,
        isDirty: false,
      }));
    },
  });
}

const Vedtak = (props: { saksId: string; brev: BrevResponse; doReload: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });

  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));

  const showDebug = useSearch({
    strict: false,
    select: (search: Record<string, unknown>) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(props.brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(props.brev.info.id),
    refetchInterval: 10_000,
  });

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: { ...props.brev.saksbehandlerValg },
      attestantSignatur: props.brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn,
    }),
    [props.brev.redigertBrev.signatur.attesterendeSaksbehandlerNavn, props.brev.saksbehandlerValg],
  );

  const form = useForm<VedtakSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  const brevtekstMutation = useHurtiglagreMutation<EditedLetter>(
    props.brev.info.id,
    setEditorState,
    (brevId, redigertBrev) => {
      applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      return oppdaterBrevtekst(brevId, redigertBrev);
    },
  );

  const attestantSignaturMutation = useHurtiglagreMutation(
    props.brev.info.id,
    setEditorState,
    oppdaterAttestantSignatur,
  );

  const saksbehandlerValgMutation = useHurtiglagreMutation(
    props.brev.info.id,
    setEditorState,
    oppdaterSaksbehandlerValg,
  );

  const wrappedSaksbehandlerValgMutation = (values: SaksbehandlerValg) => {
    form.setValue("saksbehandlerValg", values);
    saksbehandlerValgMutation.mutate(values);
  };

  const autoSave = useCallback(() => {
    if (editorState.isDirty) {
      brevtekstMutation.mutate(editorState.redigertBrev);
    }
  }, [brevtekstMutation, editorState.isDirty, editorState.redigertBrev]);

  useEffect(() => {
    const timeoutId = setTimeout(autoSave, 5000);
    return () => clearTimeout(timeoutId);
  }, [autoSave]);

  // If the user isn’t editing and the server’s version has changed, update local redigertBrev and its hash
  useEffect(() => {
    if (!editorState.isDirty && editorState.redigertBrevHash !== props.brev.redigertBrevHash) {
      setEditorState((s) => ({
        ...s,
        redigertBrev: props.brev.redigertBrev,
        redigertBrevHash: props.brev.redigertBrevHash,
      }));
    }
  }, [editorState.isDirty, editorState.redigertBrevHash, props.brev.redigertBrev, props.brev.redigertBrevHash]);

  // Dynamically register each key in props.brev.saksbehandlerValg
  // so React-Hook-Form knows about all those nested fields and their initial values
  useEffect(() => {
    Object.entries(props.brev.saksbehandlerValg).forEach(([key, value]) => {
      form.register(`saksbehandlerValg.${key}`, { value });
    });
  }, [form, props.brev.saksbehandlerValg]);

  // Whenever server-derived defaultValuesModelEditor changes,
  // reset the form values unless the user has already edited them.
  useEffect(() => {
    if (!form.formState.isDirty) {
      form.reset(defaultValuesModelEditor);
    }
  }, [defaultValuesModelEditor, form]);

  const attesterMutation = useMutation<Blob, AxiosError, OppdaterBrevRequest>({
    mutationFn: (requestData) =>
      attesterBrev({ saksId: props.saksId, brevId: props.brev.info.id, request: requestData }),
  });

  const onSubmit = (values: VedtakSidemenyFormData, onSuccess?: () => void) => {
    attesterMutation.mutate(
      {
        saksbehandlerValg: values.saksbehandlerValg,
        redigertBrev: editorState.redigertBrev,
        signatur: values.attestantSignatur,
      },
      { onSuccess: onSuccess },
    );
  };

  const freeze =
    brevtekstMutation.isPending ||
    saksbehandlerValgMutation.isPending ||
    attestantSignaturMutation.isPending ||
    attesterMutation.isPending;

  const error =
    brevtekstMutation.isError ||
    saksbehandlerValgMutation.isError ||
    attestantSignaturMutation.isError ||
    attesterMutation.isError;

  return (
    <form
      onSubmit={form.handleSubmit((v) => {
        onSubmit(v, () => {
          navigate({
            to: "/saksnummer/$saksId/attester/$brevId/forhandsvisning",
            params: { saksId: props.saksId, brevId: props.brev.info.id.toString() },
            search: {
              vedtaksId: props.brev.info?.vedtaksId?.toString(),
              enhetsId: props.brev.info.avsenderEnhet?.enhetNr?.toString(),
            },
          });
        });
      })}
    >
      <ThreeSectionLayout
        bottom={
          <Button icon={<ArrowRightIcon />} iconPosition="right" loading={freeze} size="small">
            Fortsett
          </Button>
        }
        left={
          <FormProvider {...form}>
            <VStack gap="8">
              <Heading size="small">{props.brev.redigertBrev.title}</Heading>
              <VStack gap="4">
                <OppsummeringAvMottaker mottaker={props.brev.info.mottaker} saksId={props.saksId} withTitle />
                <VStack>
                  <Label size="small">Distribusjonstype</Label>
                  <BodyShort size="small">{props.brev.info.distribusjonstype}</BodyShort>
                </VStack>
              </VStack>
              <Divider />
              <VStack gap="5">
                <Switch size="small">Marker tekst som er lagt til manuelt</Switch>
                <Switch size="small">Vis slettet tekst</Switch>
                <AutoSavingTextField
                  autocomplete="Underskrift"
                  field={"attestantSignatur"}
                  fieldType={{
                    type: "scalar",
                    nullable: false,
                    kind: "STRING",
                    displayText: null,
                  }}
                  label="Underskrift"
                  onSubmit={() => attestantSignaturMutation.mutate(form.getValues("attestantSignatur"))}
                  timeoutTimer={2500}
                  type={"text"}
                />
              </VStack>
              <Divider />
              <VStack>
                <BrevmalAlternativer
                  brevkode={props.brev.info.brevkode}
                  submitOnChange={() => {
                    const values = form.getValues("saksbehandlerValg");
                    wrappedSaksbehandlerValgMutation(values);
                  }}
                  withTitle
                />
              </VStack>
            </VStack>
          </FormProvider>
        }
        right={
          <>
            <ReservertBrevError
              doRetry={props.doReload}
              onNeiClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId: props.saksId },
                  search: {
                    vedtaksId: props.brev.info?.vedtaksId?.toString(),
                    enhetsId: props.brev.info.avsenderEnhet?.enhetNr?.toString(),
                  },
                })
              }
              reservasjon={reservasjonQuery.data}
            />
            <LetterEditor
              editorHeight="var(--main-page-content-height)"
              editorState={editorState}
              error={error}
              freeze={freeze}
              setEditorState={setEditorState}
              showDebug={showDebug}
            />
          </>
        }
      />
    </form>
  );
};
