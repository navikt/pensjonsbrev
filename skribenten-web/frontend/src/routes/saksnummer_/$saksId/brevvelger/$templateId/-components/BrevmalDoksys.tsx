import { zodResolver } from "@hookform/resolvers/zod";
import { VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderDoksysLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderDoksysLetterRequest, SpraakKode } from "~/types/apiTypes";

import { Route } from "../route";
import BestillOgRedigerButton from "./BestillOgRedigerButton";
import HentOgVisAdresse from "./endreMottaker/HentOgVisAdresse";
import LetterTemplateHeading from "./LetterTemplate";
import SelectEnhet from "./SelectEnhet";
import SelectLanguage from "./SelectLanguage";
import { byggDoksysOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalForDoksys({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  displayLanguages: SpraakKode[];
  defaultValues: {
    isSensitive: undefined;
    brevtittel: string;
    spraak: SpraakKode;
    enhetsId: string;
  };
}) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderDoksysLetterRequest>({
    mutationFn: (payload) => orderDoksysLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

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
      <HentOgVisAdresse sakId={saksId} showMottakerTitle />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderLetterMutation.mutate(
              byggDoksysOnSubmitRequest({
                template: letterTemplate,
                vedtaksId: vedtaksId ?? null,
                formValues: {
                  enhetsId: submittedValues.enhetsId,
                  isSensitive: submittedValues.isSensitive,
                  spraak: submittedValues.spraak ?? null,
                  brevtittel: submittedValues.brevtittel ?? null,
                },
              }),
            ),
          )}
        >
          <VStack gap="8">
            <SelectEnhet />
            <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={displayLanguages} />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
