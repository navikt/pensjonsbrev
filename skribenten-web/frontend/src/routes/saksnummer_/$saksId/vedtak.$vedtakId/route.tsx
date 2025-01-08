import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, HStack, Label, Switch, Tabs, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";
import { useEffect, useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { AutoSavingTextField } from "~/Brevredigering/ModelEditor/components/ScalarEditor";
import { SaksbehandlerValgModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import type { SaksbehandlerValg } from "~/types/brev";

import { nyBrevResponse } from "../../../../../cypress/utils/brevredigeringTestUtils";

export const Route = createFileRoute("/saksnummer/$saksId/vedtak/$vedtakId")({
  component: () => <Vedtak />,
});

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <Box
      background="bg-default"
      css={css`
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        flex: 1;
      `}
    >
      <div
        css={css`
          display: grid;
          grid-template-columns: 25% 75%;
          flex: 1;

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
          padding: 0.5rem 1rem;
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
  const brevResponse = nyBrevResponse({});
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

  console.log("formValues", form.getValues());

  return (
    <ThreeSectionLayout
      bottom={
        <Button icon={<ArrowRightIcon />} iconPosition="right" size="small" type="button">
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
                  <BodyShort size="small">TODO - type</BodyShort>
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
                  onSubmit={() => console.log("submit")}
                  timeoutTimer={2500}
                  type={"text"}
                />
              </VStack>
              <Divider />
              <VStack>
                <BrevmalAlternativer
                  brevkode={brevResponse.info.brevkode}
                  submitOnChange={() => console.log("submit")}
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
          error={false}
          freeze={false}
          setEditorState={setEditorState}
          showDebug={false}
        />
      }
    />
  );
};

enum BrevAlternativTab {
  TEKSTER = "TEKSTER",
  OVERSTYRING = "OVERSTYRING",
}

const BrevmalAlternativer = (props: { brevkode: string; submitOnChange?: () => void }) => {
  return (
    <Tabs
      css={css`
        width: 100%;

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
          submitOnChange={props.submitOnChange}
        />
      </Tabs.Panel>
    </Tabs>
  );
};
