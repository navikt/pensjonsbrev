import { zodResolver } from "@hookform/resolvers/zod";
import { VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderDoksysLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderDoksysLetterRequest } from "~/types/apiTypes";

import Adresse from "./-Adresse";
import BestillOgRedigerButton from "./-BestillOgRedigerButton";
import { baseOrderLetterValidationSchema } from "./-BrevmalExstream";
import LetterTemplateHeading from "./-LetterTemplate";
import SelectEnhet from "./-SelectEnhet";
import SelectLanguage from "./-SelectLanguage";
import { Route } from "./route";

export default function BrevmalForDoksys({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderDoksysLetterRequest>({
    mutationFn: (payload) => orderDoksysLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const { reset } = orderLetterMutation;
  useEffect(() => {
    //reset();
  }, [templateId, reset]);

  const methods = useForm<z.infer<typeof baseOrderLetterValidationSchema>>({
    resolver: zodResolver(baseOrderLetterValidationSchema),
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <Adresse />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              vedtaksId,
              ...submittedValues,
            };
            return orderLetterMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="4">
            <SelectEnhet />
            <SelectLanguage letterTemplate={letterTemplate} />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
