import { zodResolver } from "@hookform/resolvers/zod";
import { VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useMemo } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderDoksysLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderDoksysLetterRequest, SpraakKode } from "~/types/apiTypes";

import Adresse from "./-Adresse";
import BestillOgRedigerButton from "./-BestillOgRedigerButton";
import LetterTemplateHeading from "./-LetterTemplate";
import SelectEnhet from "./-SelectEnhet";
import SelectLanguage from "./-SelectLanguage";
import { byggDoksysOnSubmitRequest, createValidationSchema } from "./-TemplateUtils";
import { Route } from "./route";

export default function BrevmalForDoksys({
  letterTemplate,
  preferredLanguage,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
}) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderDoksysLetterRequest>({
    mutationFn: (payload) => orderDoksysLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const sorterteSpråk = useMemo(() => {
    return letterTemplate.spraak.toSorted();
  }, [letterTemplate.spraak]);

  const defaultValues = useMemo(() => {
    return {
      isSensitive: undefined,
      brevtittel: "",
      // preferredLanguage finnes ikke nødvendigvis akkurat ved side last - Når vi får den lastet, vil vi ha den forhåndsvalgt, hvis brevet også støtter på språket.
      spraak: preferredLanguage && sorterteSpråk.includes(preferredLanguage) ? preferredLanguage : sorterteSpråk[0],
    };
  }, [preferredLanguage, sorterteSpråk]);

  const validationSchema = createValidationSchema(letterTemplate);

  const methods = useForm<z.infer<typeof validationSchema>>({
    defaultValues: defaultValues,
    resolver: zodResolver(validationSchema),
  });

  const { reset } = orderLetterMutation;
  const { reset: resetForm } = methods;
  useEffect(() => {
    reset();
    resetForm(defaultValues);
  }, [templateId, reset, resetForm, defaultValues]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <Adresse />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderLetterMutation.mutate(
              byggDoksysOnSubmitRequest({
                template: letterTemplate,
                vedtaksId: vedtaksId,
                formValues: submittedValues,
              }),
            ),
          )}
        >
          <VStack gap="4">
            <SelectEnhet />
            <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={sorterteSpråk} />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
