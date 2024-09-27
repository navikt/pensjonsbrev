import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderDoksysLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { LetterMetadata, OrderDoksysLetterRequest, SpraakKode } from "~/types/apiTypes";

import { Route } from "../../route";
import HentOgVisAdresse from "../endreMottaker/HentOgVisAdresse";
import BrevmalFormWrapper, { OrderLetterResult, useSubmitBrevmalButton } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";
import { byggDoksysOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalForDoksys({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
  templateId,
  saksId,
  setNestebutton,
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
  templateId: string;
  saksId: string;
  setNestebutton: (el: React.ReactNode) => void;
}) {
  const formRef = useRef<HTMLFormElement>(null);
  const { vedtaksId } = Route.useSearch();
  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderDoksysLetterRequest>({
    mutationFn: (payload) => orderDoksysLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const validationSchema = createValidationSchema(letterTemplate);

  useSubmitBrevmalButton({
    onClick: () => formRef.current?.requestSubmit(),
    onMount: setNestebutton,
    status: orderLetterMutation.status,
  });

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
        <BrevmalFormWrapper
          formRef={formRef}
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
          <SelectEnhet />
          <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={displayLanguages} />
        </BrevmalFormWrapper>
        <OrderLetterResult data={orderLetterMutation.data} error={orderLetterMutation.error} />
      </FormProvider>
    </>
  );
}
