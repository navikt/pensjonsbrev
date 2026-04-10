import { zodResolver } from "@hookform/resolvers/zod";
import { Box, Button, HStack, Modal, Tabs, VStack } from "@navikt/ds-react";
import { type UseMutationResult, useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useCallback, useState } from "react";
import { type Control, FormProvider, useForm } from "react-hook-form";

import { finnSamhandler } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { type Adresse, type FinnSamhandlerRequestDto, type FinnSamhandlerResponseDto } from "~/types/apiTypes";
import { ManueltAdressertTil } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

import BekreftAvbrytelse from "./BekreftAvbrytelse";
import {
  type CombinedFormData,
  createSamhandlerValidationSchema,
  type EndreMottakerModalTabs,
  type FinnSamhandlerFormData,
  InnOgUtland,
  type ManuellAdresseUtfyllingFormData,
  Søketype,
} from "./EndreMottakerUtils";
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";
import UtfyllingAvManuellAdresseForm from "./UtfyllingAvManuellAdresseForm";

export const EndreMottakerModal = (properties: {
  åpen: boolean;
  onBekreftNyMottaker: (id: string | Adresse) => void;
  resetOnBekreftState: () => void;
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
  onClose: () => void;
  skalKunOppdatereSamhandler?: boolean;
}) => {
  const [tab, setTab] = useState<EndreMottakerModalTabs>("samhandler");
  const [vilAvbryte, setVilAvbryte] = useState<boolean>(false);

  const finnSamhandlerMutation = useMutation({ mutationFn: finnSamhandler });

  const onFinnsamhandlerSubmit = (values: FinnSamhandlerFormData) => {
    switch (values.søketype) {
      case null: {
        throw new Error("Teknisk feil - Fikk case 'null' ved søk for samhandler. Ble ikke denne fanget av validering?");
      }
      case Søketype.DIREKTE_OPPSLAG: {
        return finnSamhandlerMutation.mutate({
          type: Søketype.DIREKTE_OPPSLAG,
          samhandlerType: values.samhandlerType!,
          identtype: values.direkteOppslag.identtype!,
          id: values.direkteOppslag.id!,
        });
      }
      case Søketype.ORGANISASJONSNAVN: {
        return finnSamhandlerMutation.mutate({
          type: Søketype.ORGANISASJONSNAVN,
          samhandlerType: values.samhandlerType!,
          innlandUtland: values.organisasjonsnavn.innOgUtland!,
          navn: values.organisasjonsnavn.navn!,
        });
      }
      case Søketype.PERSONNAVN: {
        return finnSamhandlerMutation.mutate({
          type: Søketype.PERSONNAVN,
          samhandlerType: values.samhandlerType!,
          fornavn: values.personnavn.fornavn!,
          etternavn: values.personnavn.etternavn!,
        });
      }
    }
  };

  const defaultManuellAdresse: ManuellAdresseUtfyllingFormData = {
    adresse: {
      navn: "",
      linje1: "",
      linje2: "",
      linje3: "",
      manueltAdressertTil: ManueltAdressertTil.BRUKER,
      postnr: null,
      poststed: null,
      //default value skal være norge. Siden vi henter alle landkodene i backend, hardkoder vi norges verdi.
      land: "NO",
    },
  };
  const defaultFinnSamhandler = {
    søketype: null,
    samhandlerType: null,
    direkteOppslag: { identtype: null, id: "" },
    organisasjonsnavn: { innOgUtland: InnOgUtland.ALLE, navn: "" },
    personnavn: { fornavn: "", etternavn: "" },
  };

  const form = useForm<CombinedFormData>({
    defaultValues: {
      manuellAdresse: defaultManuellAdresse,
      finnSamhandler: defaultFinnSamhandler,
    },
    resolver: zodResolver(createSamhandlerValidationSchema(tab)),
  });

  const onAvbrytClick = useCallback(() => {
    setVilAvbryte(true);
    if (!form.formState.isDirty) {
      properties.onClose();
    }
  }, [form.formState.isDirty, properties]);

  return (
    <Modal
      data-cy="endre-mottaker-modal"
      header={{
        closeButton: false,
        heading:
          vilAvbryte && form.formState.dirtyFields.manuellAdresse
            ? "Vil du avbryte endring av mottaker?"
            : "Endre mottaker",
      }}
      onBeforeClose={() => {
        if (form.formState.dirtyFields.manuellAdresse) {
          setVilAvbryte(true);
          return false;
        }
        return true;
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      /*
      ved å ha modalen som en portal vil ikke browseren klage på at vi kommer til å ha en form inni en annen form.
      vi vil likevel få litt andre tekniske problemer som event propagation. Denne kan vi likevel bare stoppe..
      */
      portal
      width={vilAvbryte && form.formState.dirtyFields.manuellAdresse ? 480 : 700}
    >
      <Modal.Body>
        <form
          onSubmit={(event) => {
            event.stopPropagation();
            form.handleSubmit((values) => {
              if (tab === "samhandler") {
                onFinnsamhandlerSubmit(values.finnSamhandler);
              } else if (tab === "manuellAdresse") {
                properties.resetOnBekreftState();
                setTab("oppsummering");
              }
              form.reset(values);
            })(event);
          }}
        >
          <FormProvider {...form}>
            {vilAvbryte && form.formState.dirtyFields.manuellAdresse ? (
              <BekreftAvbrytelse onBekreftAvbryt={properties.onClose} onIkkeAvbryt={() => setVilAvbryte(false)} />
            ) : (
              <ModalTabs
                control={form.control}
                error={properties.error}
                isPending={properties.isPending}
                manuellAdresseValues={form.getValues("manuellAdresse")}
                onAvbrytClick={onAvbrytClick}
                onBekreftNyMottaker={properties.onBekreftNyMottaker}
                onClose={properties.onClose}
                onFinnSamhandlerSubmit={finnSamhandlerMutation}
                resetOnBekreftState={properties.resetOnBekreftState}
                skalKunOppdatereSamhandler={properties.skalKunOppdatereSamhandler}
                tab={{
                  tab: tab,
                  setTab: setTab,
                }}
              />
            )}
          </FormProvider>
        </form>
      </Modal.Body>
    </Modal>
  );
};

const ModalTabs = (properties: {
  tab: {
    tab: EndreMottakerModalTabs;
    setTab: (tab: EndreMottakerModalTabs) => void;
  };
  control: Control<CombinedFormData>;
  onAvbrytClick: () => void;
  onClose: () => void;
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequestDto, unknown>;
  onBekreftNyMottaker: (id: string | Adresse) => void;
  resetOnBekreftState: () => void;
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
  manuellAdresseValues: Nullable<ManuellAdresseUtfyllingFormData>;
  skalKunOppdatereSamhandler?: boolean;
}) => {
  const [selectedSamhandler, setSelectedSamhandler] = useState<Nullable<string>>(null);

  return (
    <div>
      {properties.tab.tab === "oppsummering" && properties.manuellAdresseValues ? (
        <OppsummeringAvValgtMottaker
          adresse={{ ...properties.manuellAdresseValues.adresse }}
          error={properties.error}
          isPending={properties.isPending}
          onAvbryt={properties.onAvbrytClick}
          onBekreft={() => properties.onBekreftNyMottaker({ ...properties.manuellAdresseValues!.adresse })}
          onTilbake={{
            fn: () => properties.tab.setTab("manuellAdresse"),
            plassering: "top",
          }}
          samhandlerType={null}
        />
      ) : (
        <VStack gap="space-16">
          <Tabs
            onChange={(s) => {
              setSelectedSamhandler(null);
              properties.tab.setTab(s as EndreMottakerModalTabs);
            }}
            size="small"
            value={properties.tab.tab === "oppsummering" ? "samhandler" : properties.tab.tab}
          >
            <Tabs.List>
              <Tabs.Tab label="Finn samhandler" value="samhandler" />
              {properties.skalKunOppdatereSamhandler ? null : (
                <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />
              )}
            </Tabs.List>
            <Box marginBlock="space-16 space-0">
              <Tabs.Panel value="samhandler">
                <SøkOgVelgSamhandlerForm
                  control={properties.control}
                  onFinnSamhandlerSubmit={properties.onFinnSamhandlerSubmit}
                  onSelectedChange={(id) => {
                    properties.resetOnBekreftState();
                    setSelectedSamhandler(id);
                  }}
                  selectedSamhandler={selectedSamhandler}
                />
              </Tabs.Panel>
              <Tabs.Panel value="manuellAdresse">
                <UtfyllingAvManuellAdresseForm
                  control={properties.control}
                  onCloseIntent={properties.onAvbrytClick}
                  onSubmit={() => {
                    properties.tab.setTab("oppsummering");
                  }}
                />
              </Tabs.Panel>
            </Box>
          </Tabs>

          {properties.tab.tab === "samhandler" && (
            <HStack justify="space-between">
              <Button onClick={properties.onClose} size="small" type="button" variant="tertiary">
                Avbryt
              </Button>
              <Button
                data-cy="lagre-samhandler"
                disabled={!selectedSamhandler}
                loading={properties.isPending ?? false}
                onClick={() => {
                  if (selectedSamhandler) {
                    properties.onBekreftNyMottaker(selectedSamhandler);
                  }
                }}
                size="small"
                type="button"
              >
                Lagre
              </Button>
            </HStack>
          )}
          {properties.error && properties.tab.tab === "samhandler" && (
            <ApiError error={properties.error} title="En feil skjedde" />
          )}
        </VStack>
      )}
    </div>
  );
};
