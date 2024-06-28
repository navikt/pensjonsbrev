import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, HStack, Link, Select, TextField, VStack } from "@navikt/ds-react";
import { useEffect } from "react";
import { Controller, useForm } from "react-hook-form";

import type { Nullable } from "~/types/Nullable";

import type { ManuellAdresseUtfyllingFormData } from "./EndreMottakerUtils";
import { Land, leggTilManuellSamhandlerFormDataSchema, TypeMottaker } from "./EndreMottakerUtils";

const UtfyllingAvManuellAdresseForm = (properties: {
  defaultValues: Nullable<ManuellAdresseUtfyllingFormData>;
  onSubmit: (values: ManuellAdresseUtfyllingFormData) => void;
  harEndringer: (b: boolean) => void;
  onCloseIntent: (values: ManuellAdresseUtfyllingFormData) => void;
}) => {
  const defaultValues = properties.defaultValues ?? {
    typeMottaker: null,
    adresse: {
      navn: "",
      adresselinje1: "",
      adresselinje2: "",
      postnummer: "",
      poststed: "",
      land: Land.Norge,
    },
  };

  const form = useForm<ManuellAdresseUtfyllingFormData>({
    defaultValues: defaultValues,
    resolver: zodResolver(leggTilManuellSamhandlerFormDataSchema),
  });

  useEffect(() => {
    if (form.formState.isDirty) {
      properties.harEndringer(true);
    } else {
      properties.harEndringer(false);
    }
  }, [form.formState.isDirty, properties]);

  return (
    <form
      onSubmit={(event) => {
        event.stopPropagation();
        form.handleSubmit(properties.onSubmit)(event);
      }}
    >
      <VStack gap="6">
        <VStack gap="4">
          <Alert variant="warning">
            <Heading size="xsmall">Manuell adresseendringsrutine</Heading>
            {/* TODO - finn ut hva adressen er */}
            <Link href="https://www.nav.no" target="_blank">
              Les rutinen for endring av adresse her {<ExternalLinkIcon />}
            </Link>
          </Alert>
          <Alert variant="info">
            <BodyShort>
              Brev sendes til brukers folkeregistrerte adresse eller annen foretrukken kanal. Legg til mottaker dersom
              brev skal sendes til utenlandsk adresse, fullmektig, verge eller dødsbo.
            </BodyShort>
          </Alert>

          <Controller
            control={form.control}
            name="typeMottaker"
            render={({ field, fieldState }) => (
              <Select
                description="Privatperson, samhandler, institusjon, offentlig"
                label="Type mottaker"
                {...field}
                error={fieldState.error?.message}
                value={field.value ?? ""}
              >
                <option disabled value="">
                  Velg
                </option>
                <option value={TypeMottaker.PrivatPerson}>Privatperson</option>
                <option value={TypeMottaker.Samhandler}>Samhandler</option>
                <option value={TypeMottaker.Institusjon}>Institusjon</option>
                <option value={TypeMottaker.Offentlig}>Offentlig</option>
              </Select>
            )}
          />

          <Controller
            control={form.control}
            name="adresse.navn"
            render={({ field, fieldState }) => <TextField label="Navn" {...field} error={fieldState.error?.message} />}
          />

          <Controller
            control={form.control}
            name="adresse.adresselinje1"
            render={({ field, fieldState }) => (
              <TextField label="Adresselinje 1" {...field} error={fieldState.error?.message} />
            )}
          />

          <Controller
            control={form.control}
            name="adresse.adresselinje2"
            render={({ field, fieldState }) => (
              <TextField label="Adresselinje 2" {...field} error={fieldState.error?.message} />
            )}
          />

          <HStack gap="4">
            <Controller
              control={form.control}
              name="adresse.postnummer"
              render={({ field, fieldState }) => (
                <TextField
                  css={css`
                    width: 25%;
                  `}
                  label="Postnummer"
                  {...field}
                  error={fieldState.error?.message}
                />
              )}
            />
            <Controller
              control={form.control}
              name="adresse.poststed"
              render={({ field, fieldState }) => (
                <TextField
                  css={css`
                    width: 25%;
                  `}
                  label="Poststed"
                  {...field}
                  error={fieldState.error?.message}
                />
              )}
            />
          </HStack>

          <Controller
            control={form.control}
            name="adresse.land"
            render={({ field, fieldState }) => (
              <Select
                css={css`
                  align-self: flex-start;
                  width: 60%;
                `}
                label="Land *"
                {...field}
                error={fieldState.error?.message}
                value={field.value ?? ""}
              >
                {Object.values(Land).map((land) => (
                  <option key={land} value={land}>
                    {land}
                  </option>
                ))}
              </Select>
            )}
          />
        </VStack>
        <HStack
          css={css`
            align-self: flex-end;
          `}
          gap="4"
        >
          <Button
            onClick={() => properties.onCloseIntent(form.getValues())}
            size="small"
            type="button"
            variant="tertiary"
          >
            Avbryt
          </Button>
          <Button size="small">Gå videre</Button>
        </HStack>
      </VStack>
    </form>
  );
};

export default UtfyllingAvManuellAdresseForm;
