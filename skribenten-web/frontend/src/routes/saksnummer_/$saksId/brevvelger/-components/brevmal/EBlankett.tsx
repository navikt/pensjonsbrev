import { zodResolver } from "@hookform/resolvers/zod";
import { TextField } from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { orderEblankett, orderLetterKeys } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { type LetterMetadata, type OrderEblankettRequest } from "~/types/apiTypes";
import { trackEvent } from "~/utils/umami";

import { Route, type SubmitTemplateOptions } from "../../route";
import BrevmalFormWrapper, { OrderLetterResult } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectAvtaleland from "./components/SelectAvtaleland";
import SelectEnhet from "./components/SelectEnhet";
import { byggEBlankettOnSubmitRequest } from "./TemplateUtils";

const eblankettValidationSchema = z.object({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Vennligst fyll inn mottaker"),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

export default function EBlankett({
  letterTemplate,
  defaultValues,
  saksId,
  setOnFormSubmitClick,
}: {
  letterTemplate: LetterMetadata;
  defaultValues: { enhetsId: string };
  saksId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) {
  const { enhetsId: urlEnhetsId } = Route.useSearch();
  const formRef = useRef<HTMLFormElement>(null);

  const methods = useForm<z.infer<typeof eblankettValidationSchema>>({
    defaultValues: {
      landkode: "NOR",
      mottakerText: "",
      enhetsId: defaultValues.enhetsId,
    },
    resolver: zodResolver(eblankettValidationSchema),
  });

  const { getValues, setValue } = methods;

  useEffect(() => {
    const enhetsId = urlEnhetsId ?? "";

    if (getValues("enhetsId") !== enhetsId) {
      setValue("enhetsId", enhetsId);
    }
  }, [urlEnhetsId, getValues, setValue]);

  const orderEblankettMutation = useMutation<string, AxiosError<Error> | Error, OrderEblankettRequest>({
    mutationFn: (payload) => orderEblankett(saksId, payload),
    onSuccess: (callbackUrl, variables) => {
      trackEvent("brev opprettet", {
        brevkode: letterTemplate.id,
        brevtittel: letterTemplate.name,
        brevsystem: "exstream",
        dokumentkategori: letterTemplate.dokumentkategoriCode || "E_BLANKETT",
        enhetsId: variables.enhetsId,
        landkode: variables.landkode,
      });
      window.open(callbackUrl);
    },
    mutationKey: orderLetterKeys.brevsystem("e-blankett"),
  });

  useEffect(() => {
    setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit() });
  }, [setOnFormSubmitClick]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <BrevmalFormWrapper
          formRef={formRef}
          onSubmit={methods.handleSubmit((submittedValues) =>
            orderEblankettMutation.mutate(
              byggEBlankettOnSubmitRequest({
                template: letterTemplate,
                formValues: submittedValues,
              }),
            ),
          )}
        >
          <TextField
            data-testid="mottaker-text-textfield"
            {...methods.register("mottakerText")}
            autoComplete="off"
            error={methods.formState.errors.mottakerText?.message}
            label="Mottaker"
            size="small"
          />
          <SelectAvtaleland />
          <SelectEnhet />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderEblankettMutation.data} error={orderEblankettMutation.error} />
      </FormProvider>
    </>
  );
}
