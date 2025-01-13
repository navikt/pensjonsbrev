import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, HStack, Label, Switch, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, Outlet, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { getBrev, hurtiglagreSaksbehandlerValg, oppdaterSignatur } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { AutoSavingTextField } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import type { BrevResponse, SaksbehandlerValg } from "~/types/brev";

import { nyBrevResponse } from "../../../../../../cypress/utils/brevredigeringTestUtils";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$vedtakId/redigering")({
  component: () => <Vedtak />,
});

export const ThreeSectionLayout = (props: {
  left: React.ReactNode;
  right: React.ReactNode;
  bottom: React.ReactNode;
}) => {
  return (
    <Box
      background="bg-default"
      css={css`
        display: flex;
        flex-direction: column;
        justify-content: space-between;
      `}
    >
      <div
        css={css`
          display: grid;
          grid-template-columns: 27% 73%;
          flex: 1;

          > :first-of-type {
            padding: 16px 24px;
            border-right: 1px solid var(--a-gray-200);
          }

          @media (width <= 1024px) {
            > :first-of-type {
              padding: var(--a-spacing-3);
            }
          }
        `}
      >
        <div>{props.left}</div>
        <div>{props.right}</div>
      </div>
      <HStack
        css={css`
          position: sticky;
          bottom: 0;
          left: 0;
          width: 100%;
          background: var(--a-white);

          border-top: 1px solid var(--a-gray-200);
          padding: var(--a-spacing-2) var(--a-spacing-4);
        `}
        justify={"end"}
      >
        {props.bottom}
      </HStack>
    </Box>
  );
};

interface VedtakSidemenyFormData {
  signatur: string;
  saksbehandlerValg: SaksbehandlerValg;
}

const Vedtak = () => {
  const { saksId } = Route.useParams();
  const queryClient = useQueryClient();
  const brevResponse = nyBrevResponse({});
  const navigate = useNavigate({ from: Route.fullPath });
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brevResponse));

  useEffect(() => {
    if (editorState.redigertBrevHash !== brevResponse.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: brevResponse.redigertBrev,
        redigertBrevHash: brevResponse.redigertBrevHash,
      }));
    }
  }, [brevResponse.redigertBrev, brevResponse.redigertBrevHash, editorState.redigertBrevHash, setEditorState]);

  const defaultValuesModelEditor = useMemo(
    () => ({
      saksbehandlerValg: {
        ...brevResponse.saksbehandlerValg,
      },
      signatur: brevResponse.redigertBrev.signatur.saksbehandlerNavn,
    }),
    [brevResponse.redigertBrev.signatur.saksbehandlerNavn, brevResponse.saksbehandlerValg],
  );

  const form = useForm<VedtakSidemenyFormData>({
    defaultValues: defaultValuesModelEditor,
  });

  const signaturMutation = useMutation<BrevResponse, AxiosError, string>({
    mutationFn: (signatur) => {
      //TODO - dette er den vanlige signatur for brevbaker brev, trenger vi en annen en for vedtak?
      return oppdaterSignatur(brevResponse.info.id, signatur);
    },
    onSuccess: (response) => {
      //TODO - vil vi ha en tilsvarende for vedtak?
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      //TODO - må ha lignende for vedtaksbrev?
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brevResponse.info.id) });
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
    mutationFn: (saksbehandlerValg) => {
      return hurtiglagreSaksbehandlerValg(brevResponse.info.id, saksbehandlerValg);
    },
    onSuccess: (response) => {
      //TODO - vil vi ha en tilsvarende for vedtak?
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      //TODO - må ha lignende for vedtaksbrev?
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(brevResponse.info.id) });
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

  const onSubmit = (values: VedtakSidemenyFormData, onSuccess?: () => void) => {
    //console.log("submitting - hehe", values);
    queryClient.setQueryData(["vedtak", brevResponse.info.id], () => brevResponse);
    onSuccess?.();
    // saksbehandlerValgMutation.mutate(values.saksbehandlerValg, {
    //   onSuccess: () => {
    //     signaturMutation.mutate(values.signatur, {
    //       onSuccess: onSuccess,
    //     });
    //   },
    // });
  };

  return (
    <form
      onSubmit={form.handleSubmit((v) =>
        onSubmit(v, () =>
          navigate({
            to: "/saksnummer/$saksId/vedtak/$vedtakId/forhandsvisning",
            params: { saksId, vedtakId: brevResponse.info.id.toString() },
          }),
        ),
      )}
    >
      <Outlet />
      <ThreeSectionLayout
        bottom={
          <Button icon={<ArrowRightIcon />} iconPosition="right" size="small">
            Fortsett
          </Button>
        }
        left={
          <div>
            <FormProvider {...form}>
              <VStack gap="8">
                <Heading size="small">{brevResponse.redigertBrev.title}</Heading>
                <VStack gap="4">
                  <Heading size="small">{brevResponse.redigertBrev.title}</Heading>
                  <OppsummeringAvMottaker mottaker={brevResponse.info.mottaker} saksId={saksId} withTitle />
                  <VStack>
                    <Label size="small">Distribusjonstype</Label>
                    <BodyShort size="small">{brevResponse.info.distribusjonstype}</BodyShort>
                  </VStack>
                </VStack>
                <Divider />
                <VStack gap="5">
                  <Switch size="small">Marker tekst som er lagt til manuelt</Switch>
                  <Switch size="small">Vis slettet tekst</Switch>
                  <AutoSavingTextField
                    field={"signatur"}
                    fieldType={{
                      type: "scalar",
                      nullable: false,
                      kind: "STRING",
                    }}
                    label="Underskrift"
                    onSubmit={() => signaturMutation.mutate(form.getValues("signatur"))}
                    timeoutTimer={2500}
                    type={"text"}
                  />
                </VStack>
                <Divider />
                <VStack>
                  <BrevmalAlternativer
                    brevkode={brevResponse.info.brevkode}
                    submitOnChange={() => saksbehandlerValgMutation.mutate(form.getValues("saksbehandlerValg"))}
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
            error={signaturMutation.isError || saksbehandlerValgMutation.isError}
            freeze={signaturMutation.isPending || saksbehandlerValgMutation.isPending}
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
  children?: React.ReactNode;
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
      return (
        <Tabs
          css={css`
            width: 100%;

            display: flex;
            flex-direction: column;
            gap: var(--a-spacing-6);
            margin-top: 12px;

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
              gap: var(--a-spacing-6);
              margin-top: 12px;
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
              gap: var(--a-spacing-6);
              margin-top: 12px;
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
          {props.children}
        </Tabs>
      );
    }
  }
};
