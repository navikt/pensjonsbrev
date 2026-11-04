import { zodResolver } from "@hookform/resolvers/zod";
import { Textarea } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useRef } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { getAvtaleLand, orderEblankett, orderLetterKeys } from "~/api/skribenten-api-endpoints";
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
  const avtalelandQuery = useQuery(getAvtaleLand);
  const validationSchema = eblankettValidationSchema.refine(
    ({ landkode }) => avtalelandQuery.isSuccess && avtalelandQuery.data.some((land) => land.kode === landkode),
    { path: ["landkode"], message: "Velg et gyldig land fra listen" },
  );

  const methods = useForm<z.infer<typeof eblankettValidationSchema>>({
    defaultValues: {
      landkode: "NOR",
      mottakerText: "",
      enhetsId: defaultValues.enhetsId,
    },
    resolver: zodResolver(validationSchema),
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
          <Textarea
            data-testid="mottaker-text-textfield"
            {...methods.register("mottakerText")}
            autoComplete="off"
            error={methods.formState.errors.mottakerText?.message}
            label="Mottaker"
          />
          <SelectAvtaleland />
          <SelectEnhet />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderEblankettMutation.data} error={orderEblankettMutation.error} />
      </FormProvider>
    </>
  );
}
