import { zodResolver } from "@hookform/resolvers/zod";
import { BodyShort, TextField } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { orderEblankett } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderEblankettRequest } from "~/types/apiTypes";

import type { SubmitBrevmalButtonOptions } from "../../route";
import { Route } from "../../route";
import BrevmalFormWrapper, { OrderLetterResult } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectAvtaleland from "./components/SelectAvtaleland";
import SelectEnhet from "./components/SelectEnhet";
import SelectSensitivity from "./components/SelectSensitivity";
import { byggEBlankettOnSubmitRequest } from "./TemplateUtils";

const eblankettValidationSchema = z.object({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Vennligst fyll inn mottaker"),
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

export default function Eblankett({
  letterTemplate,
  setSubmitBrevmalButtonOptions,
}: {
  letterTemplate: LetterMetadata;
  setSubmitBrevmalButtonOptions: (s: SubmitBrevmalButtonOptions) => void;
}) {
  const { saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();
  const formRef = useRef<HTMLFormElement>(null);

  const methods = useForm<z.infer<typeof eblankettValidationSchema>>({
    defaultValues: {
      landkode: "",
      mottakerText: "",
    },
    resolver: zodResolver(eblankettValidationSchema),
  });

  const orderEblankettMutation = useMutation<string, AxiosError<Error> | Error, OrderEblankettRequest>({
    mutationFn: (payload) => orderEblankett(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  useEffect(() => {
    setSubmitBrevmalButtonOptions({
      onClick: () => formRef.current?.requestSubmit(),
      status: orderEblankettMutation.status,
    });
  }, [setSubmitBrevmalButtonOptions, orderEblankettMutation.status]);
  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
      <FormProvider {...methods}>
        <BrevmalFormWrapper
          formRef={formRef}
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderEblankettMutation.mutate(
              byggEBlankettOnSubmitRequest({
                template: letterTemplate,
                vedtaksId: vedtaksId,
                formValues: submittedValues,
              }),
            ),
          )}
        >
          <TextField
            data-cy="mottaker-text-textfield"
            {...methods.register("mottakerText")}
            autoComplete="off"
            error={methods.formState.errors.mottakerText?.message}
            label="Mottaker"
            size="small"
          />
          <SelectAvtaleland />
          <SelectEnhet />
          <SelectSensitivity />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderEblankettMutation.data} error={orderEblankettMutation.error} />
      </FormProvider>
    </>
  );
}
