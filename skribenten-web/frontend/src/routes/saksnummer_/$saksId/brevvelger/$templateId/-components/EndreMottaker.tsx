import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ExternalLinkIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  Button,
  Heading,
  HStack,
  Link,
  Modal,
  Select,
  Table,
  Tabs,
  TextField,
  VStack,
} from "@navikt/ds-react";
import { useMutation } from "@tanstack/react-query";
import { useState } from "react";
import type { Control } from "react-hook-form";
import { Controller, useForm, useWatch } from "react-hook-form";

import { finnSamhandler2 } from "~/api/skribenten-api-endpoints";
import { SamhandlerTypeSelect } from "~/components/select/SamhandlerSelect";
import type { FinnSamhandlerRequest, Samhandler, SamhandlerTypeCode } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";
import { capitalizeString } from "~/utils/stringUtils";

import { SamhandlerSearchResults } from "./Adresse";
import type { SamhandlerFormDataType } from "./TemplateUtils";
import {
  createSamhandlerValidationSchema,
  Identtype,
  identtypeToText,
  InnOgUtland,
  Land,
  Søketype,
  TypeMottaker,
} from "./TemplateUtils";

const EndreMottaker = () => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  return (
    <div>
      <h1>Endre mottaker</h1>
      {modalÅpen && <EndreMottakerModal onClose={() => setModalÅpen(false)} åpen={modalÅpen} />}

      <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
        Endre mottaker
      </Button>
    </div>
  );
};

export default EndreMottaker;

const EndreMottakerModal = (properties: { åpen: boolean; onClose: () => void }) => {
  //dette er for å markere SB intensjon om å avbryte. Dette brukes for å gi oss en ekstra guard dersom SB har skrevet inn noe info
  //const [vilAvbryte, setVilAvbryte] = useState<boolean>(false);
  const [tab, setActiveTab] = useState<"samhandler" | "manuellAdresse">("samhandler");

  const mutation = useMutation({ mutationFn: finnSamhandler2 });

  const onFinnSamhandlerSubmit = async (data: SamhandlerFormDataType) => {
    const requestObject: FinnSamhandlerRequest = {
      samhandlerType: data.finnSamhandler.samhandlerType!,
      direkteOppslag:
        data.finnSamhandler.søketype === Søketype.DIREKTE_OPPSLAG
          ? {
              identtype: data.finnSamhandler.direkteOppslag.identtype!,
              id: data.finnSamhandler.direkteOppslag.id!,
            }
          : null,
      organisasjonsnavn:
        data.finnSamhandler.søketype === Søketype.ORGANISASJONSNAVN
          ? {
              innOgUtland: data.finnSamhandler.organisasjonsnavn.innOgUtland!,
              navn: data.finnSamhandler.organisasjonsnavn.navn!,
            }
          : null,
      personnavn:
        data.finnSamhandler.søketype === Søketype.PERSONNAVN
          ? {
              fornavn: data.finnSamhandler.personnavn.fornavn!,
              etternavn: data.finnSamhandler.personnavn.etternavn!,
            }
          : null,
    };

    mutation.mutate(requestObject);
  };

  const form = useForm<SamhandlerFormDataType>({
    defaultValues: {
      finnSamhandler: {
        søketype: null,
        samhandlerType: null,
        direkteOppslag: { identtype: null, id: "" },
        organisasjonsnavn: { innOgUtland: null, navn: "" },
        personnavn: { fornavn: "", etternavn: "" },
      },
      leggTilManuellSamhandler: {
        typeMottaker: null,
        navn: "",
        adresselinje1: "",
        adresselinje2: "",
        postnummer: "",
        poststed: "",
        land: Land.Norge,
      },
    },
    resolver: zodResolver(createSamhandlerValidationSchema(tab)),
  });

  return (
    <Modal
      header={{
        heading: "Endre mottaker",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      /*
        ved å ha modalen som en portal vil ikke browseren klage på at vi kommer til å ha en form inni en annen form.
        vi vil likevel få litt andre tekniske problemer som event propagation
        En løsning for dette ville være å flytte EndreMottaker-formen til parent formen - men det vil jo komme med sine nedsider også. 
      */
      portal
      width={600}
    >
      <Modal.Body>
        <form
          onSubmit={(event) => {
            event.stopPropagation();
            form.handleSubmit((values) => {
              //TODO - ved samhandler success skal vi bare rendre samhandlere, dem skal velge samhandler, 'navigere' til en oppsummering
              // ved manuell, skal man direkte til oppsummeringen
              switch (tab) {
                case "samhandler": {
                  onFinnSamhandlerSubmit(values);
                  break;
                }
                case "manuellAdresse": {
                  throw new Error('Not implemented yet: "manuellAdresse" case');
                }
              }
            })(event);
          }}
        >
          <ModalTabs
            control={form.control}
            error={mutation.isError}
            loading={mutation.isPending}
            onClose={properties.onClose}
            setActiveTab={(v) => setActiveTab(v)}
            success={mutation.data?.samhandlere ?? []}
            tab={tab}
          />
        </form>
      </Modal.Body>
    </Modal>
  );
};

const ModalTabs = (properties: {
  tab: "samhandler" | "manuellAdresse";
  setActiveTab: (tab: "samhandler" | "manuellAdresse") => void;
  control: Control<SamhandlerFormDataType>;
  loading: boolean;
  error: boolean;
  success: Samhandler[];
  onClose: () => void;
}) => {
  return (
    <Tabs onChange={(v) => properties.setActiveTab(v as "samhandler" | "manuellAdresse")} value={properties.tab}>
      <Tabs.List>
        <Tabs.Tab label="Finn samhandler" value="samhandler" />
        <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />
      </Tabs.List>
      <div
        css={css`
          margin-top: 1rem;
        `}
      >
        <SamhandlerTab
          control={properties.control}
          error={properties.error}
          loading={properties.loading}
          onClose={properties.onClose}
          success={properties.success}
        />
        <UtfyllingAvManuellAdresse control={properties.control} onClose={properties.onClose} />
      </div>
    </Tabs>
  );
};

const SamhandlerTab = (properties: {
  control: Control<SamhandlerFormDataType>;
  loading: boolean;
  error: boolean;
  success: Samhandler[];
  onClose: () => void;
}) => {
  const søketype: Nullable<Søketype> = useWatch({
    control: properties.control,
    name: "finnSamhandler.søketype",
  });

  return (
    <Tabs.Panel value="samhandler">
      <VStack gap="6">
        <VStack gap="4">
          <Controller
            control={properties.control}
            name="finnSamhandler.søketype"
            render={({ field, fieldState }) => (
              <Select
                error={fieldState.error?.message}
                id={field.name}
                label="Søketype"
                {...field}
                value={field.value ?? ""}
              >
                <option disabled value={""}>
                  Klikk for å velge søketype
                </option>
                <option value={Søketype.DIREKTE_OPPSLAG}>Direkte oppslag</option>
                <option value={Søketype.ORGANISASJONSNAVN}>Organisasjonsnavn</option>
                <option value={Søketype.PERSONNAVN}>Personnavn</option>
              </Select>
            )}
          />

          {søketype === Søketype.DIREKTE_OPPSLAG && <SamhandlerDirekteOppslag control={properties.control} />}
          {søketype === Søketype.ORGANISASJONSNAVN && <SamhandlerOrganisasjonsnavn control={properties.control} />}
          {søketype === Søketype.PERSONNAVN && <SamhandlerPersonnavn control={properties.control} />}
        </VStack>
        {properties.error && (
          <Alert variant="error">Feil skjedde under søk - TODO gi en mer deskriptiv melding om hva som gikk feil</Alert>
        )}
        {søketype && (
          <Button
            css={css`
              align-self: flex-start;
            `}
            loading={properties.loading}
            size="small"
          >
            Søk
          </Button>
        )}

        {properties.success.length > 0 && (
          <SamhandlerSearchResults
            onSelect={() => {
              //TODO
            }}
            samhandlere={properties.success}
          />
        )}

        <Button
          css={css`
            align-self: flex-end;
          `}
          onClick={properties.onClose}
          type="button"
          variant="tertiary"
        >
          Avbryt
        </Button>
      </VStack>
    </Tabs.Panel>
  );
};

const SamhandlerDirekteOppslag = (properties: { control: Control<SamhandlerFormDataType> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name={"finnSamhandler.direkteOppslag.identtype"}
        render={({ field, fieldState }) => (
          <Select
            error={fieldState.error?.message}
            id={field.name}
            label="Identtype"
            {...field}
            value={field.value ?? ""}
          >
            <option value="">Klikk for å velge identtype</option>
            {Object.values(Identtype).map((identtype) => (
              <option key={identtype} value={identtype}>
                {identtypeToText(identtype)}
              </option>
            ))}
          </Select>
        )}
      />

      <Controller
        control={properties.control}
        name={"finnSamhandler.direkteOppslag.id"}
        render={({ field, fieldState }) => (
          <TextField {...field} error={fieldState.error?.message} label="ID" size="medium" value={field.value ?? ""} />
        )}
      />
    </VStack>
  );
};

const SamhandlerOrganisasjonsnavn = (properties: { control: Control<SamhandlerFormDataType> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.organisasjonsnavn.innOgUtland"
        render={({ field, fieldState }) => (
          <Select label="Inn-/utland" {...field} error={fieldState.error?.message} value={field.value ?? ""}>
            <option disabled value="">
              Klikk for å velge inn-/utland
            </option>
            <option value={InnOgUtland.INNLAND}>Innland</option>
            <option value={InnOgUtland.UTLAND}>Utland</option>
            <option value={InnOgUtland.ALLE}>Alle</option>
          </Select>
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.organisasjonsnavn.navn"
        render={({ field, fieldState }) => (
          <TextField
            label="Navn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};

const SamhandlerPersonnavn = (properties: { control: Control<SamhandlerFormDataType> }) => {
  return (
    <VStack gap="4">
      <Controller
        control={properties.control}
        name="finnSamhandler.samhandlerType"
        render={({ field, fieldState }) => (
          <SamhandlerTypeSelect error={fieldState.error} onChange={field.onChange} value={field.value ?? ""} />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.personnavn.fornavn"
        render={({ field, fieldState }) => (
          <TextField
            label="Fornavn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
      <Controller
        control={properties.control}
        name="finnSamhandler.personnavn.etternavn"
        render={({ field, fieldState }) => (
          <TextField
            label="Etternavn"
            {...field}
            error={fieldState.error?.message}
            size="medium"
            value={field.value ?? ""}
          />
        )}
      />
    </VStack>
  );
};

const UtfyllingAvManuellAdresse = (properties: { control: Control<SamhandlerFormDataType>; onClose: () => void }) => {
  return (
    <Tabs.Panel value="manuellAdresse">
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
            name="leggTilManuellSamhandler.typeMottaker"
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
            name="leggTilManuellSamhandler.navn"
            render={({ field, fieldState }) => <TextField label="Navn" {...field} error={fieldState.error?.message} />}
          />

          <Controller
            control={properties.control}
            name="leggTilManuellSamhandler.adresselinje1"
            render={({ field, fieldState }) => (
              <TextField label="Adresselinje 1" {...field} error={fieldState.error?.message} />
            )}
          />

          <Controller
            control={properties.control}
            name="leggTilManuellSamhandler.adresselinje2"
            render={({ field, fieldState }) => (
              <TextField label="Adresselinje 2" {...field} error={fieldState.error?.message} />
            )}
          />

          <HStack gap="4">
            <Controller
              control={properties.control}
              name="leggTilManuellSamhandler.postnummer"
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
              name="leggTilManuellSamhandler.poststed"
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
            name="leggTilManuellSamhandler.land"
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
          <Button onClick={properties.onClose} size="small" type="button" variant="tertiary">
            Avbryt
          </Button>
          <Button size="small">Gå videre</Button>
        </HStack>
      </VStack>
    </Tabs.Panel>
  );
};

interface AdresseObject {
  adresselinje1: string;
  adresselinje2: string;
  adresselinje3: string;
  postnummer: string;
  poststed: string;
  land: string;
}

const OppsummeringAvValgtMottaker = (properties: {
  type: SamhandlerTypeCode | TypeMottaker;
  navn: string;
  adresse: AdresseObject;
  onAvbryt: () => void;
  onBekreft: (adresse: AdresseObject) => void;
}) => {
  return (
    <VStack gap="4">
      <Table>
        <Table.Body>
          <InversedTableRow label="Type" value={properties.type} />
          <InversedTableRow label="Navn" value={properties.navn} />
          <InversedTableRow label="Adresselinje 1" value={properties.adresse.adresselinje1} />
          <InversedTableRow label="Adresselinje 2" value={properties.adresse.adresselinje2} />
          <InversedTableRow label="Adresselinje 3" value={properties.adresse.adresselinje3} />
          <InversedTableRow label="Postnummer" value={properties.adresse.postnummer} />
          <InversedTableRow label="Poststed" value={properties.adresse.poststed} />
          <InversedTableRow label="Land" value={properties.adresse.land} />
        </Table.Body>
      </Table>
      <HStack>
        <Button onClick={properties.onAvbryt} type="button" variant="tertiary">
          Avbryt
        </Button>
        <Button onClick={() => properties.onBekreft(properties.adresse)} type="button">
          Bekreft ny mottaker
        </Button>
      </HStack>
    </VStack>
  );
};

function InversedTableRow({ label, value }: { label: string; value?: string }) {
  if (!value) {
    return <></>;
  }

  return (
    <Table.Row>
      <Table.HeaderCell scope="row">{label}</Table.HeaderCell>
      <Table.DataCell>{capitalizeString(value)}</Table.DataCell>
    </Table.Row>
  );
}
