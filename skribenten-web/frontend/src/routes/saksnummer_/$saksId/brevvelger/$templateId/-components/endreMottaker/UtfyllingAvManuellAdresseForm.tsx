import { css } from "@emotion/react";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, HStack, Link, Select, TextField, VStack } from "@navikt/ds-react";
import type { Control } from "react-hook-form";
import { Controller } from "react-hook-form";

import type { CombinedFormData } from "./EndreMottakerUtils";
import { Land, TypeMottaker } from "./EndreMottakerUtils";

const UtfyllingAvManuellAdresseForm = (properties: {
  control: Control<CombinedFormData>;
  onSubmit: () => void;
  onCloseIntent: () => void;
}) => {
  return (
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
          control={properties.control}
          name="manuellAdresse.typeMottaker"
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
          control={properties.control}
          name="manuellAdresse.adresse.navn"
          render={({ field, fieldState }) => <TextField label="Navn" {...field} error={fieldState.error?.message} />}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.adresselinje1"
          render={({ field, fieldState }) => (
            <TextField label="Adresselinje 1" {...field} error={fieldState.error?.message} />
          )}
        />

        <Controller
          control={properties.control}
          name="manuellAdresse.adresse.adresselinje2"
          render={({ field, fieldState }) => (
            <TextField label="Adresselinje 2" {...field} error={fieldState.error?.message} />
          )}
        />

        <HStack gap="4">
          <Controller
            control={properties.control}
            name="manuellAdresse.adresse.postnummer"
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
            control={properties.control}
            name="manuellAdresse.adresse.poststed"
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
          control={properties.control}
          name="manuellAdresse.adresse.land"
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
        <Button onClick={properties.onCloseIntent} size="small" type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button size="small">Gå videre</Button>
      </HStack>
    </VStack>
  );
};

export default UtfyllingAvManuellAdresseForm;
