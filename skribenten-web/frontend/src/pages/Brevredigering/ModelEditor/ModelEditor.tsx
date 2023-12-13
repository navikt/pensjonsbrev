import { Button } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";

import { getTemplate } from "~/api/skribenten-api-endpoints";

import { ObjectEditor } from "./components/ObjectEditor";

const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export const ModelEditor = () => {
  const letterModelSpecification = useQuery({
    queryKey: getTemplate.queryKey(TEST_TEMPLATE),
    queryFn: () => getTemplate.queryFn(TEST_TEMPLATE),
  }).data?.modelSpecification;

  const methods = useForm({});

  console.log(methods.watch());

  if (!letterModelSpecification) {
    return <></>;
  }

  return (
    <FormProvider {...methods}>
      <form onSubmit={methods.handleSubmit((values) => console.log(values))}>
        <ObjectEditor typeName={letterModelSpecification.letterModelTypeName} />
        <Button type="submit">Send</Button>
      </form>
    </FormProvider>
  );
};
