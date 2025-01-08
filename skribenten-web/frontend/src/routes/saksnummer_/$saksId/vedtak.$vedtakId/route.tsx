import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Box, Button, Heading, HStack, Label, Switch, TextField, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";

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

const Vedtak = () => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(nyBrevResponse({})));

  return (
    <ThreeSectionLayout
      bottom={
        <Button icon={<ArrowRightIcon />} iconPosition="right" size="small" type="button">
          Fortsett
        </Button>
      }
      left={
        <div>
          <VStack gap="8">
            <Heading size="small">brev tittel</Heading>
            <VStack gap="4">
              <Heading size="small">brev tittel</Heading>
              <OppsummeringAvMottaker
                mottaker={{
                  type: "NorskAdresse",
                  navn: "Navn Navnesen",
                  postnummer: "1234",
                  poststed: "Oslo",
                  adresselinje1: "Adresselinje 1",
                  adresselinje2: null,
                  adresselinje3: null,
                }}
                saksId={""}
                withTitle
              />
              <VStack>
                <Label size="small">Distribusjonstype</Label>
                <BodyShort size="small">Type</BodyShort>
              </VStack>
            </VStack>
            <Divider />
            <VStack gap="5">
              <Switch size="small">Marker tekst som er lagt til manuelt</Switch>
              <Switch size="small">Vis slettet tekst</Switch>
              <TextField autoComplete="name" label="Underskrift" size="small" />
            </VStack>
            <Divider />
            <VStack>{/* <SaksbehandlerValgModelEditor brevkode={"testy"} fieldsToRender={"required"} /> */}</VStack>
          </VStack>
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
