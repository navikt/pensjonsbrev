import { Button } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";

import { getTemplate, renderLetter } from "~/api/skribenten-api-endpoints";
import { LetterEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";
import type { RenderedLetter } from "~/types/brevbakerTypes";

import { ObjectEditor } from "./components/ObjectEditor";

const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export const ModelEditor = () => {
  const letterModelSpecification = useQuery({
    queryKey: getTemplate.queryKey(TEST_TEMPLATE),
    queryFn: () => getTemplate.queryFn(TEST_TEMPLATE),
  }).data?.modelSpecification;

  const methods = useForm({});

  const renderLetterMutation = useMutation<RenderedLetter, unknown, { id: string; values: unknown }>({
    mutationFn: async ({ id, values }) => {
      console.log(values);
      return await renderLetter(id, { letterData: values, editedLetter: undefined });
    },
  });

  console.log(methods.watch());

  if (!letterModelSpecification) {
    return <></>;
  }

  return (
    <>
      <FormProvider {...methods}>
        <form onSubmit={methods.handleSubmit((values) => renderLetterMutation.mutate({ id: TEST_TEMPLATE, values }))}>
          <ObjectEditor typeName={letterModelSpecification.letterModelTypeName} />
          <Button type="submit">Send</Button>
        </form>
      </FormProvider>
      <div>{renderLetterMutation.data && <LetterEditor initialState={renderLetterMutation.data} />}</div>
    </>
  );
};
