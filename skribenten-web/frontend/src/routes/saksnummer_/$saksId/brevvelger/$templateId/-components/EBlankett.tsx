import { zodResolver } from "@hookform/resolvers/zod";
import { BodyShort, TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { orderEblankett } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderEblankettRequest } from "~/types/apiTypes";

import { Route } from "../route";
import BestillOgRedigerButton from "./BestillOgRedigerButton";
import LetterTemplateHeading from "./LetterTemplate";
import SelectAvtaleland from "./SelectAvtaleland";
import SelectEnhet from "./SelectEnhet";
import SelectSensitivity from "./SelectSensitivity";
import { byggEBlankettOnSubmitRequest } from "./TemplateUtils";

const eblankettValidationSchema = z.object({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Vennligst fyll inn mottaker"),
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

export default function Eblankett({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { saksId } = Route.useParams();

  const { vedtaksId } = Route.useSearch();

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

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
      <FormProvider {...methods}>
        <form
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
          <VStack gap="8">
            <TextField
              data-cy="mottaker-text-textfield"
              {...methods.register("mottakerText")}
              autoComplete="off"
              error={methods.formState.errors.mottakerText?.message}
              label="Mottaker"
              size="medium"
            />
            <SelectAvtaleland />
            <SelectEnhet />
            <SelectSensitivity />
          </VStack>
          <BestillOgRedigerButton orderMutation={orderEblankettMutation} />
        </form>
      </FormProvider>
    </>
  );
}
