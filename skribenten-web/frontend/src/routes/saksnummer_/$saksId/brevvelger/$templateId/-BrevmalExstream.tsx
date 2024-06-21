import { zodResolver } from "@hookform/resolvers/zod";
import { TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { orderExstreamLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { type LetterMetadata, type OrderExstreamLetterRequest, SpraakKode } from "~/types/apiTypes";

import Adresse from "./-Adresse";
import BestillOgRedigerButton from "./-BestillOgRedigerButton";
import LetterTemplateHeading from "./-LetterTemplate";
import SelectEnhet from "./-SelectEnhet";
import SelectLanguage from "./-SelectLanguage";
import SelectSensitivity from "./-SelectSensitivity";
import { Route } from "./route";

export const baseOrderLetterValidationSchema = z.object({
  spraak: z.nativeEnum(SpraakKode, { required_error: "Obligatorisk" }),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

const exstreamOrderLetterValidationSchema = baseOrderLetterValidationSchema.extend({
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
  brevtittel: z.string().optional(),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

const exstreamWithTitleOrderLetterValidationSchema = exstreamOrderLetterValidationSchema.extend({
  brevtittel: z.string().min(1, "Du må ha tittel for dette brevet"),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

export default function BrevmalForExstream({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId, idTSSEkstern } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderExstreamLetterRequest>({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const validationSchema = letterTemplate.redigerbarBrevtittel
    ? exstreamWithTitleOrderLetterValidationSchema
    : exstreamOrderLetterValidationSchema;

  const methods = useForm<z.infer<typeof validationSchema>>({
    defaultValues: {
      isSensitive: undefined,
      brevtittel: "",
    },
    resolver: zodResolver(validationSchema),
  });

  const { reset: resetMutation } = orderLetterMutation;
  const { reset: resetForm } = methods;
  useEffect(() => {
    resetForm();
    resetMutation();
  }, [templateId, resetMutation, resetForm]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              vedtaksId,
              idTSSEkstern,
              ...submittedValues,
            };
            return orderLetterMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="8">
            <Adresse />
            <SelectEnhet />
            {letterTemplate.redigerbarBrevtittel ? (
              <TextField
                {...methods.register("brevtittel")}
                autoComplete="on"
                data-cy="brev-title-textfield"
                description="Gi brevet en kort og forklarende tittel."
                error={methods.formState.errors.brevtittel?.message}
                label="Endre tittel"
                size="medium"
              />
            ) : undefined}
            <SelectLanguage letterTemplate={letterTemplate} />
            <SelectSensitivity />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
