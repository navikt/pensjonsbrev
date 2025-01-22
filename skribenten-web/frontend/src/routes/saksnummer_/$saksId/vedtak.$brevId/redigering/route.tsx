import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, Label, Loader, Switch, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import {
  getBrev,
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
import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import ThreeSectionLayout from "~/components/ThreeSectionLayout";
import type { BrevResponse, ReservasjonResponse, SaksbehandlerValg } from "~/types/brev";
import { queryFold } from "~/utils/tanstackUtils";

import { ReservertBrevError } from "../../brev.$brevId";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$brevId/redigering")({
  component: () => <VedtakWrapper />,
});

interface VedtakSidemenyFormData {
  attestantSignatur: string;
  saksbehandlerValg: SaksbehandlerValg;
}

const VedtakWrapper = () => {
  const { saksId, brevId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });
  const hentBrevQuery = useQuery({
    queryKey: getBrev.queryKey(Number.parseInt(brevId)),
    queryFn: () => getBrev.queryFn(saksId, Number.parseInt(brevId)),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (_, error: AxiosError) => error && error.response?.status !== 423 && error.response?.status !== 409,
    throwOnError: (error: AxiosError) => error.response?.status !== 423 && error.response?.status !== 409,
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
            onNeiClick={() => navigate({ to: "/saksnummer/$saksId/brevbehandler", params: { saksId } })}
            reservasjon={err.response.data as ReservasjonResponse}
          />
        );
      }
      if (err.response?.status === 409) {
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

/**
 * TODO - refaktorer mutations til å bruke noe lignende som i brevEditor for saksbehandler. Mulig vi kan ha noe felles siden dem begge gjør mye av det samme
 */
const Vedtak = (props: { saksId: string; brev: BrevResponse; doReload: () => void }) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));

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

  const attestantSignaturMutation = useMutation<BrevResponse, AxiosError, string>({
    mutationFn: (signatur) => oppdaterAttestantSignatur(props.brev.info.id, signatur),
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.info.id) });
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

  const saksbehandlerValgMutation = useMutation<BrevResponse, AxiosError, SaksbehandlerValg>({
    mutationFn: (saksbehandlerValg) => oppdaterSaksbehandlerValg(props.brev.info.id, saksbehandlerValg),
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.info.id) });
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

  const attesterMutation = useMutation<Blob, AxiosError>({
    mutationFn: () => attesterBrev({ saksId: props.saksId, brevId: props.brev.info.id }),
  });

  const { mutate: brevtekstMutation } = useMutation<BrevResponse, AxiosError>({
    mutationFn: () => {
      applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      return oppdaterBrevtekst(props.brev.info.id, editorState.redigertBrev);
    },
    onSuccess: (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.info.id) });
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

  //TODO - muligens 1 api som tar inn all dette innholdet
  const onSubmit = (values: VedtakSidemenyFormData, onSuccess?: () => void) => {
    attestantSignaturMutation.mutate(values.attestantSignatur, {
      onSuccess: () =>
        saksbehandlerValgMutation.mutate(values.saksbehandlerValg, {
          onSuccess: () =>
            brevtekstMutation(void 0, {
              onSuccess: () => attesterMutation.mutate(void 0, { onSuccess: onSuccess }),
            }),
        }),
    });
  };

  const freeze =
    saksbehandlerValgMutation.isPending || attestantSignaturMutation.isPending || attesterMutation.isPending;

  const error = saksbehandlerValgMutation.isError || attestantSignaturMutation.isError || attesterMutation.isError;

  useEffect(() => {
    form.reset(defaultValuesModelEditor);
  }, [defaultValuesModelEditor, form]);

  useEffect(() => {
    if (editorState.redigertBrevHash !== props.brev.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: props.brev.redigertBrev,
        redigertBrevHash: props.brev.redigertBrevHash,
      }));
    }
  }, [props.brev.redigertBrev, props.brev.redigertBrevHash, editorState.redigertBrevHash, setEditorState]);

  useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.isDirty) {
        brevtekstMutation();
      }
    }, 2000);
    return () => clearTimeout(timoutId);
  }, [editorState.isDirty, editorState.redigertBrev, brevtekstMutation]);

  return (
    <form
      onSubmit={form.handleSubmit((v) =>
        onSubmit(v, () =>
          navigate({
            to: "/saksnummer/$saksId/vedtak/$brevId/forhandsvisning",
            params: { saksId: props.saksId, brevId: props.brev.info.id.toString() },
          }),
        ),
      )}
    >
      <ThreeSectionLayout
        bottom={
          <Button icon={<ArrowRightIcon />} iconPosition="right" loading={freeze} size="small">
            Fortsett
          </Button>
        }
        left={
          <div>
            <ReservertBrevError
              doRetry={props.doReload}
              onNeiClick={() =>
                navigate({
                  to: "/saksnummer/$saksId/brevbehandler",
                  params: { saksId: props.saksId },
                })
              }
              reservasjon={reservasjonQuery.data}
            />
            <FormProvider {...form}>
              <VStack gap="8">
                <Heading size="small">{props.brev.redigertBrev.title}</Heading>
                <VStack gap="4">
                  <Heading size="small">{props.brev.redigertBrev.title}</Heading>
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
                    field={"attestantSignatur"}
                    fieldType={{
                      type: "scalar",
                      nullable: false,
                      kind: "STRING",
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
                    submitOnChange={() => saksbehandlerValgMutation.mutate(form.getValues("saksbehandlerValg"))}
                    withTitle
                  />
                </VStack>
              </VStack>
            </FormProvider>
          </div>
        }
        right={
          <LetterEditor
            editorHeight={"var(--main-page-content-height)"}
            editorState={editorState}
            error={error}
            freeze={freeze}
            setEditorState={setEditorState}
            showDebug={false}
          />
        }
      />
    </form>
  );
};

enum BrevAlternativTab {
  TEKSTER = "TEKSTER",
  OVERSTYRING = "OVERSTYRING",
}

//TODO - flytt til egen fil
export const BrevmalAlternativer = (props: {
  brevkode: string;
  submitOnChange?: () => void;

  /**
   * Kan velge hvilke felter som skal vises. Default er at begge vises (dersom dem finnes, ellers bare den som finnes)
   */
  displaySingle?: "required" | "optional";
  withTitle?: boolean;
}) => {
  const specificationFormElements = usePartitionedModelSpecification(props.brevkode);

  switch (specificationFormElements.status) {
    case "error": {
      return <ApiError error={specificationFormElements.error} title={"En feil skjedde"} />;
    }
    case "pending": {
      return <BodyShort size="small">Henter skjema for saksbehandler valg...</BodyShort>;
    }
    case "success": {
      if (
        specificationFormElements.optionalFields.length === 0 &&
        specificationFormElements.requiredfields.length === 0
      ) {
        return (
          <VStack gap="3">
            {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
            <BodyShort size="small">Det finnes ikke tekstalternativer i denne malen.</BodyShort>
          </VStack>
        );
      }

      if (props.displaySingle) {
        if (props.displaySingle === "required" && specificationFormElements.requiredfields.length > 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"required"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        if (props.displaySingle === "optional" && specificationFormElements.optionalFields.length > 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"optional"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }
      } else {
        if (specificationFormElements.optionalFields.length === 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"required"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        if (specificationFormElements.requiredfields.length === 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"optional"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        return (
          <VStack gap="3">
            {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
            <Tabs
              css={css`
                width: 100%;

                display: flex;
                flex-direction: column;
                gap: var(--a-spacing-5);

                .navds-tabs__scroll-button {
                  /* vi har bare 2 tabs, så det gir ikke mening tab listen skal være scrollbar. Den tar i tillegg mye ekstra plass når skjermen er <1024px */
                  display: none;
                }
              `}
              defaultValue={BrevAlternativTab.TEKSTER}
              fill
              size="small"
            >
              <Tabs.List
                css={css`
                  display: grid;
                  grid-template-columns: repeat(2, 1fr);
                `}
              >
                <Tabs.Tab label="Tekster" value={BrevAlternativTab.TEKSTER} />
                <Tabs.Tab label="Overstyring" value={BrevAlternativTab.OVERSTYRING} />
              </Tabs.List>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--a-spacing-5);
                `}
                value={BrevAlternativTab.TEKSTER}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender={"optional"}
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--a-spacing-5);
                `}
                value={BrevAlternativTab.OVERSTYRING}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender={"required"}
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
            </Tabs>
          </VStack>
        );
      }
      return null;
    }
  }
};
