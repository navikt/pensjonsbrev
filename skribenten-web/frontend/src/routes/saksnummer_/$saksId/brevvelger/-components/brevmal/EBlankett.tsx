import { zodResolver } from "@hookform/resolvers/zod";
import { Label, Radio, RadioGroup, Select, Textarea, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useId, useRef, useState } from "react";
import { FormProvider, useForm, useWatch } from "react-hook-form";
import { z } from "zod";

import { getAvtaleLand, orderEblankett, orderLetterKeys } from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { type LetterMetadata, type OrderEblankettRequest } from "~/types/apiTypes";
import { trackEvent } from "~/utils/innblikk";

import { Route, type SubmitTemplateOptions } from "../../route";
import BrevmalFormWrapper, { OrderLetterResult } from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectAvtaleland from "./components/SelectAvtaleland";
import SelectEnhet from "./components/SelectEnhet";
import { byggEBlankettOnSubmitRequest } from "./TemplateUtils";

const eblankettValidationSchema = z.object({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Vennligst fyll inn mottaker").max(200, "Mottaker kan ikke være lengre enn 200 tegn"),
  enhetsId: z.string().min(1, "Obligatorisk"),
});

const mottakere = [
  {
    // https://www.av.fo/
    id: "almannaverkid",
    landkode: "FRO",
    navn: "Almannaverkið",
    landnavn: "Færøyene",
    adresse: `Almannaverkið
Staravegur 18
Postrúm 3096
110 Tórshavn
FÆRØYENE`,
  },
  {
    // https://naalakkersuisut.gl/Departementer/Sociale_Anlig_Arbsmarked_Familier_og_Indenrigsan/Sociale-Anliggender?sc_lang=da
    id: "sociale-anliggender",
    landkode: "GRL",
    navn: "Sociale Anliggender (udsatte voksne, ældre)",
    landnavn: "Grønland",
    adresse: `Departementet for Sociale Anliggender, Arbejdsmarked, Familier og Indenrigsanliggender
Sociale Anliggender
Imaneq 1 A
Postboks 970
3900 Nuuk
GRØNLAND`,
  },
  {
    // https://naalakkersuisut.gl/Departementer/Sociale_Anlig_Arbsmarked_Familier_og_Indenrigsan/Socialeomraadet?sc_lang=da
    id: "socialeomraadet",
    landkode: "GRL",
    navn: "Socialeområdet (udsatte voksne, ældre, familie)",
    landnavn: "Grønland",
    adresse: `Departementet for Sociale Anliggender, Arbejdsmarked, Familier og Indenrigsanliggender
Socialeområdet
Imaneq 1 A
Postboks 970
3900 Nuuk
GRØNLAND`,
  },
  {
    // https://www.gov.im/about-the-government/departments/the-treasury/social-security-division
    id: "isle-of-man",
    landkode: "GBR",
    navn: "Social Security Division",
    landnavn: "Isle of Man",
    adresse: `The Treasury
Social Security Division
Markwell House
Market Street
Douglas
ISLE OF MAN`,
  },
  {
    // https://www.gov.je/pages/contacts.aspx?contactId=201
    id: "jersey",
    landkode: "GBR",
    navn: "Employment, Social Sec. & Housing",
    landnavn: "Jersey",
    adresse: `Government of Jersey
Employment, Social Security and Housing Department
Union Street
St Helier
JERSEY, CHANNEL ISLANDS`,
  },
] as const;

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
  const mottakerId = useId();
  const mottakerLabelId = `${mottakerId}-label`;
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
  const landkode = useWatch({ control: methods.control, name: "landkode" });
  const mottakerText = useWatch({ control: methods.control, name: "mottakerText" });
  const [valgtMottaker, setValgtMottaker] = useState("");
  const tilgjengeligeMottakere = mottakere.filter((mottaker) => landkode === "NOR" || mottaker.landkode === landkode);

  const velgMottaker = (id: string) => {
    setValgtMottaker(id);
    setValue("mottakerText", mottakere.find((mottaker) => mottaker.id === id)?.adresse ?? "", {
      shouldDirty: true,
    });
    methods.clearErrors("mottakerText");
  };

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
          <SelectAvtaleland
            onLandChange={(kode) => {
              const mottaker = mottakere.find((mottaker) => mottaker.landkode === kode);
              if (mottaker) {
                velgMottaker(mottaker.id);
              } else if (kode !== "NOR") {
                velgMottaker("");
              }
            }}
          />
          <VStack gap={tilgjengeligeMottakere.length > 1 ? "space-12" : "space-8"}>
            <VStack gap="space-8">
              <Label htmlFor={mottakerId} id={mottakerLabelId} size="small">
                Mottaker
              </Label>
              {tilgjengeligeMottakere.length === 2 ? (
                <RadioGroup
                  hideLegend
                  legend="Velg mottaker"
                  onChange={velgMottaker}
                  size="small"
                  value={valgtMottaker}
                >
                  {tilgjengeligeMottakere.map((mottaker) => (
                    <Radio key={mottaker.id} value={mottaker.id}>
                      {mottaker.landkode === "GBR" ? `${mottaker.navn} (${mottaker.landnavn})` : mottaker.navn}
                    </Radio>
                  ))}
                  <Radio value="">Egendefinert</Radio>
                </RadioGroup>
              ) : tilgjengeligeMottakere.length > 2 ? (
                <Select
                  hideLabel
                  label="Velg mottaker"
                  onChange={(event) => velgMottaker(event.target.value)}
                  size="small"
                  value={valgtMottaker}
                >
                  <option value="">Velg mottaker</option>
                  {tilgjengeligeMottakere.map((mottaker) => (
                    <option key={mottaker.id} value={mottaker.id}>
                      {`${mottaker.navn} (${mottaker.landnavn})`}
                    </option>
                  ))}
                </Select>
              ) : null}
            </VStack>
            <Textarea
              data-testid="mottaker-text-textfield"
              {...methods.register("mottakerText")}
              aria-labelledby={mottakerLabelId}
              autoComplete="off"
              error={methods.formState.errors.mottakerText?.message}
              hideLabel
              id={mottakerId}
              label="Rediger mottaker"
              maxLength={200}
              onChange={(event) => {
                setValgtMottaker("");
                setValue("mottakerText", event.target.value, { shouldDirty: true });
                methods.clearErrors("mottakerText");
              }}
              size="small"
              value={mottakerText}
            />
          </VStack>
          <SelectEnhet />
        </BrevmalFormWrapper>

        <OrderLetterResult data={orderEblankettMutation.data} error={orderEblankettMutation.error} />
      </FormProvider>
    </>
  );
}
