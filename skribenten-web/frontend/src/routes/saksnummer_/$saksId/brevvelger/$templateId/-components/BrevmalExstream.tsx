import { zodResolver } from "@hookform/resolvers/zod";
import { TextField, VStack } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect, useMemo } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { orderExstreamLetter } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import type { SpraakKode } from "~/types/apiTypes";
import { type LetterMetadata, type OrderExstreamLetterRequest } from "~/types/apiTypes";

import { Route } from "../route";
import Adresse from "./Adresse";
import BestillOgRedigerButton from "./BestillOgRedigerButton";
import LetterTemplateHeading from "./LetterTemplate";
import SelectEnhet from "./SelectEnhet";
import SelectLanguage from "./SelectLanguage";
import SelectSensitivity from "./SelectSensitivity";
import { byggExstreamOnSubmitRequest, createValidationSchema } from "./TemplateUtils";

export default function BrevmalForExstream({
  letterTemplate,
  preferredLanguage,
}: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
}) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId, idTSSEkstern } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderExstreamLetterRequest>({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const sorterteSpråk = useMemo(() => {
    return letterTemplate.spraak.toSorted();
  }, [letterTemplate.spraak]);

  const defaultValues = useMemo(() => {
    return {
      /*
        TODO - bug - react hook form håndterer dårlig hvis et felt er undefined som default value. når man resetter formet etter å ha endret
        template vil verdien som var saksbehandler hadde valgt henge igjen, selv om selve input feltet viser at det er tomt
        Dette skjer ut til å skje kun første gangen man endrer template
      */
      isSensitive: undefined,
      brevtittel: "",
      spraak: preferredLanguage && sorterteSpråk.includes(preferredLanguage) ? preferredLanguage : sorterteSpråk[0],
    };
  }, [preferredLanguage, sorterteSpråk]);

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
            <Adresse />
            <SelectEnhet />
            {letterTemplate.redigerbarBrevtittel && (
              <TextField
                {...methods.register("brevtittel")}
                autoComplete="on"
                data-cy="brev-title-textfield"
                description="Gi brevet en kort og forklarende tittel."
                error={methods.formState.errors.brevtittel?.message}
                label="Endre tittel"
                size="medium"
              />
            )}
            {/* Utfylling av språk vil vi ikke gjøre dersom templaten er 'Notat' */}
            {letterTemplate.id !== "PE_IY_03_156" && (
              <SelectLanguage preferredLanguage={preferredLanguage} sorterteSpråk={sorterteSpråk} />
            )}
            <SelectSensitivity />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}
