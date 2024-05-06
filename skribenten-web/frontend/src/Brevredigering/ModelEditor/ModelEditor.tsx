import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { useLoaderData } from "@tanstack/react-router";
import { useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { renderLetter } from "~/api/skribenten-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { TEST_TEMPLATE } from "~/routes/saksnummer_.$saksId.redigering.$templateId";
import type { RenderLetterResponse } from "~/types/brevbakerTypes";

import { ObjectEditor } from "./components/ObjectEditor";

const TEST_DEFAULT_VALUES = {
  ytelse: "YTELSE",
  land: "LAND",
  svartidUker: "4",
  mottattSoeknad: "2024-01-04",
  inkluderVenterSvarAFP: { uttakAlderspensjonProsent: "25", uttaksDato: "2024-01-04" },
};

export const ModelEditor = () => {
  const letterModelSpecification = useLoaderData({
    from: "/saksnummer/$saksId/redigering/$templateId",
  });

  const [editorState, setEditorState] = useState<LetterEditorState | undefined>(undefined);

  const methods = useForm({ shouldUnregister: true, defaultValues: TEST_DEFAULT_VALUES });

  const renderLetterMutation = useMutation<RenderLetterResponse, unknown, { id: string; values: unknown }>({
    mutationFn: async ({ id, values }) => {
      // In React-Hook-Form it is convential, and easiest, to keep empty inputs as an empty string.
      // However, in the api empty strings are interpreted literally, we want these to be null in the payload.
      // To deal with any nested/array properties we use this JSON trick
      const letterDataWithEmptyStringsReplaced = JSON.parse(JSON.stringify(values), (key, value) =>
        value === "" ? null : value,
      );
      return await renderLetter(id, {
        letterData: letterDataWithEmptyStringsReplaced,
        editedLetter: editorState?.renderedLetter?.editedLetter,
      });
    },
    onSuccess: (renderedLetter) => {
      setEditorState(Actions.create(renderedLetter));
    },
  });

  return (
    <>
      <FormProvider {...methods}>
        <form
          css={css`
            padding: var(--a-spacing-6) var(--a-spacing-4);
            display: flex;
            flex-direction: column;
            gap: var(--a-spacing-4);

            > {
              width: 100%;
            }
          `}
          onSubmit={methods.handleSubmit((values) => renderLetterMutation.mutate({ id: TEST_TEMPLATE, values }))}
        >
          <ObjectEditor typeName={letterModelSpecification.modelSpecification.letterModelTypeName} />
          <Button loading={renderLetterMutation.isPending} type="submit">
            Send
          </Button>
        </form>
      </FormProvider>
      {editorState ? <LetterEditor editorState={editorState} setEditorState={setEditorState} /> : <div />}
    </>
  );
};
