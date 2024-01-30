import { css } from "@emotion/react";
import { Button } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { useLoaderData } from "@tanstack/react-router";
import { FormProvider, useForm } from "react-hook-form";

import { renderLetter } from "~/api/skribenten-api-endpoints";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { TEST_TEMPLATE } from "~/routes/saksnummer_.$sakId.redigering.$templateId";
import type { RenderedLetter } from "~/types/brevbakerTypes";

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
    from: "/saksnummer/$sakId/redigering/$templateId",
  });

  const methods = useForm({ shouldUnregister: true, defaultValues: TEST_DEFAULT_VALUES });

  const renderLetterMutation = useMutation<RenderedLetter, unknown, { id: string; values: unknown }>({
    mutationFn: async ({ id, values }) => {
      return await renderLetter(id, { letterData: values, editedLetter: undefined });
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
      {renderLetterMutation.data ? <LetterEditor initialState={renderLetterMutation.data} /> : <div />}
    </>
  );
};
