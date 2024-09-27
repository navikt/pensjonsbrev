import { zodResolver } from "@hookform/resolvers/zod";
import { TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderExstreamLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { SpraakKode } from "~/types/apiTypes";
import { type LetterMetadata, type OrderExstreamLetterRequest } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import EndreMottaker from "../endreMottaker/EndreMottaker";
import HentOgVisAdresse from "../endreMottaker/HentOgVisAdresse";
import BrevmalFormWrapper, { OrderLetterResult, useSubmitBrevmalButton } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";
import SelectSensitivity from "./components/SelectSensitivity";
import { byggExstreamOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalForExstream({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
  templateId,
  saksId,
  setNestebutton,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: Nullable<SpraakKode>;
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
  const { vedtaksId, idTSSEkstern } = Route.useSearch();
  const formRef = useRef<HTMLFormElement>(null);
  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderExstreamLetterRequest>({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
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

  const { reset: resetMutation } = orderLetterMutation;
  const { reset: resetForm } = methods;
  useEffect(() => {
    //ved template endring vil vi resette formet - men beholde preferredLanguage hvis den finnes
    resetForm(defaultValues);
    resetMutation();
  }, [templateId, defaultValues, resetForm, resetMutation]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <BrevmalFormWrapper
          formRef={formRef}
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderLetterMutation.mutate(
              byggExstreamOnSubmitRequest({
                template: letterTemplate,
                idTSSEkstern: idTSSEkstern ?? null,
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
          {/*Special case to hide mottaker for "Notat" & "Posteringsgrunnlag" */}
          {templateId !== "PE_IY_03_156" && templateId !== "PE_OK_06_101" && (
            <VStack gap="2">
              <HentOgVisAdresse sakId={saksId} samhandlerId={idTSSEkstern} showMottakerTitle />
              <EndreMottaker />
            </VStack>
          )}

          <SelectEnhet />
          {letterTemplate.redigerbarBrevtittel && (
            <TextField
              {...methods.register("brevtittel")}
              autoComplete="on"
              data-cy="brev-title-textfield"
              description="Gi brevet en kort og forklarende tittel."
              error={methods.formState.errors.brevtittel?.message}
              label="Endre tittel"
              size="small"
            />
          )}
          {/* Utfylling av språk vil vi ikke gjøre dersom templaten er 'Notat' */}
          {letterTemplate.id !== "PE_IY_03_156" && (
            <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={displayLanguages} />
          )}
          <SelectSensitivity />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderLetterMutation.data} error={orderLetterMutation.error} />
      </FormProvider>
    </>
  );
}
