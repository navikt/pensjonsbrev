import { zodResolver } from "@hookform/resolvers/zod";
import { TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderExstreamLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { SpraakKode } from "~/types/apiTypes";
import { type LetterMetadata, type OrderExstreamLetterRequest } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../route";
import BestillOgRedigerButton from "./BestillOgRedigerButton";
import EndreMottaker from "./endreMottaker/EndreMottaker";
import HentOgVisAdresse from "./endreMottaker/HentOgVisAdresse";
import LetterTemplateHeading from "./LetterTemplate";
import SelectEnhet from "./SelectEnhet";
import SelectLanguage from "./SelectLanguage";
import SelectSensitivity from "./SelectSensitivity";
import { byggExstreamOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalForExstream({
  letterTemplate,
  preferredLanguage,
  displayLanguages,
  defaultValues,
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
}) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId, idTSSEkstern } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderExstreamLetterRequest>({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const validationSchema = createValidationSchema(letterTemplate);

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
  }, [templateId, defaultValues, resetMutation, resetForm]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <form
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
          <VStack gap="8">
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
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
