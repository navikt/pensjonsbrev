import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Modal, Tabs } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useCallback, useState } from "react";
import type { Control } from "react-hook-form";
import { useForm } from "react-hook-form";

import { finnSamhandler2 } from "~/api/skribenten-api-endpoints";
import type { FinnSamhandlerRequest, FinnSamhandlerResponseDto } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import BekreftAvbrytelse from "./BekreftAvbrytelse";
import type { ManuellAdresseUtfyllingFormData } from "./EndreMottakerUtils";
import {
  type CombinedFormData,
  createSamhandlerValidationSchema,
  type FinnSamhandlerFormData,
  Land,
  Søketype,
} from "./EndreMottakerUtils";
import HentOgVisSamhandlerAdresse from "./HentOgVisSamhandlerAdresse";
import type { AdresseObject } from "./OppsummeringAvValgtMottaker";
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";
import UtfyllingAvManuellAdresseForm from "./UtfyllingAvManuellAdresseForm";

const EndreMottaker = (properties: { onManuellAdresseBekreft: (a: Nullable<AdresseObject>) => void }) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <div>
      <h1>Endre mottaker</h1>
      {modalÅpen && (
        <EndreMottakerModal
          onBekreftNyMottaker={(bekreftetMottaker) => {
            setModalÅpen(false);
            if (typeof bekreftetMottaker === "string") {
              properties.onManuellAdresseBekreft(null);
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: bekreftetMottaker }),
                replace: true,
              });
            } else {
              properties.onManuellAdresseBekreft(bekreftetMottaker);
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              });
            }
          }}
          onClose={() => setModalÅpen(false)}
          åpen={modalÅpen}
        />
      )}

      <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
        Endre mottaker
      </Button>
    </div>
  );
};

export default EndreMottaker;

type ModalTabs = "samhandler" | "manuellAdresse" | "oppsummering";

const EndreMottakerModal = (properties: {
  åpen: boolean;
  onBekreftNyMottaker: (id: string | AdresseObject) => void;
  onClose: () => void;
}) => {
  const [tab, setTab] = useState<ModalTabs>("samhandler");
  const [vilAvbryte, setVilAvbryte] = useState<boolean>(false);
  const [valgtSamhandler, setValgtSamhandler] = useState<Nullable<string>>(null);

  const finnSamhandlerMutation = useMutation({ mutationFn: finnSamhandler2 });

  const onFinnsamhandlerSubmit = (values: FinnSamhandlerFormData) => {
    finnSamhandlerMutation.mutate({
      samhandlerType: values.samhandlerType!,
      direkteOppslag:
        values.søketype === Søketype.DIREKTE_OPPSLAG
          ? {
              identtype: values.direkteOppslag.identtype!,
              id: values.direkteOppslag.id!,
            }
          : null,
      organisasjonsnavn:
        values.søketype === Søketype.ORGANISASJONSNAVN
          ? {
              innOgUtland: values.organisasjonsnavn.innOgUtland!,
              navn: values.organisasjonsnavn.navn!,
            }
          : null,
      personnavn:
        values.søketype === Søketype.PERSONNAVN
          ? {
              fornavn: values.personnavn.fornavn!,
              etternavn: values.personnavn.etternavn!,
            }
          : null,
    });
  };

  const defaultManuellAdresse = {
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
  const defaultFinnSamhandler = {
    søketype: Søketype.ORGANISASJONSNAVN,
    samhandlerType: null,
    direkteOppslag: { identtype: null, id: "" },
    organisasjonsnavn: { innOgUtland: null, navn: "" },
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
      header={{
        heading: vilAvbryte && form.formState.isDirty ? "Vil du avbryte endring av mottaker?" : "Endre mottaker",
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
              if (tab === "samhandler") {
                onFinnsamhandlerSubmit(values.finnSamhandler);
              } else if (tab === "manuellAdresse") {
                setTab("oppsummering");
              }
            })(event);
          }}
        >
          {vilAvbryte && form.formState.isDirty ? (
            <BekreftAvbrytelse onBekreftAvbryt={properties.onClose} onIkkeAvbryt={() => setVilAvbryte(false)} />
          ) : (
            <ModalTabs
              control={form.control}
              manuellAdresseValues={form.getValues().manuellAdresse}
              onAvbrytClick={onAvbrytClick}
              onBekreftNyMottaker={properties.onBekreftNyMottaker}
              onFinnSamhandlerSubmit={finnSamhandlerMutation}
              samhandlerValuesMedId={
                valgtSamhandler ? { ...form.getValues("finnSamhandler"), id: valgtSamhandler } : null
              }
              setSamhandler={(id) => setValgtSamhandler(id)}
              tab={{
                tab: tab,
                setTab: setTab,
              }}
            />
          )}
        </form>
      </Modal.Body>
    </Modal>
  );
};

const ModalTabs = (properties: {
  tab: {
    tab: ModalTabs;
    setTab: (tab: ModalTabs) => void;
  };
  control: Control<CombinedFormData>;
  onAvbrytClick: () => void;
  setSamhandler: (id: string) => void;
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequest, unknown>;
  onBekreftNyMottaker: (id: string | AdresseObject) => void;
  samhandlerValuesMedId: Nullable<SamhandlerValuesMedId>;
  manuellAdresseValues: Nullable<ManuellAdresseUtfyllingFormData>;
}) => {
  return (
    <div>
      {properties.tab.tab === "oppsummering" ? (
        <OppsummeringsTab
          manuellAdresseValues={properties.manuellAdresseValues}
          onBekreftNyMottaker={properties.onBekreftNyMottaker}
          onCloseIntent={properties.onAvbrytClick}
          onTilbake={(from) => properties.tab.setTab(from)}
          samhandlerValues={properties.samhandlerValuesMedId}
        />
      ) : (
        <Tabs onChange={(s) => properties.tab.setTab(s as ModalTabs)} value={properties.tab.tab}>
          <Tabs.List>
            <Tabs.Tab label="Finn samhandler" value="samhandler" />
            <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />
          </Tabs.List>
          <div
            css={css`
              margin-top: 1rem;
            `}
          >
            <Tabs.Panel value="samhandler">
              <SøkOgVelgSamhandlerForm
                control={properties.control}
                onCloseIntent={properties.onAvbrytClick}
                onFinnSamhandlerSubmit={properties.onFinnSamhandlerSubmit}
                onSamhandlerValg={(id) => {
                  properties.setSamhandler(id);
                  properties.tab.setTab("oppsummering");
                }}
              />
            </Tabs.Panel>
            <Tabs.Panel value="manuellAdresse">
              <UtfyllingAvManuellAdresseForm
                control={properties.control}
                onCloseIntent={properties.onAvbrytClick}
                onSubmit={() => properties.tab.setTab("oppsummering")}
              />
            </Tabs.Panel>
          </div>
        </Tabs>
      )}
    </div>
  );
};

type SamhandlerValuesMedId = { id: string } & FinnSamhandlerFormData;

const OppsummeringsTab = (properties: {
  samhandlerValues: Nullable<SamhandlerValuesMedId>;
  manuellAdresseValues: Nullable<ManuellAdresseUtfyllingFormData>;
  onBekreftNyMottaker: (id: string | AdresseObject) => void;
  onCloseIntent: () => void;
  onTilbake: (from: "samhandler" | "manuellAdresse") => void;
}) => {
  if (!properties.samhandlerValues && !properties.manuellAdresseValues) {
    throw new Error(
      "Teknisk feil Forventet at enten adresse for samhandler, eller manuell oppslag er supplert til oppsummeringen",
    );
  }

  if (properties.samhandlerValues) {
    return (
      <HentOgVisSamhandlerAdresse
        id={properties.samhandlerValues.id}
        onBekreftNyMottaker={() => properties.onBekreftNyMottaker(properties.samhandlerValues!.id)}
        onCloseIntent={properties.onCloseIntent}
        onTilbakeTilSøk={() => properties.onTilbake("samhandler")}
        typeMottaker={properties.samhandlerValues!.samhandlerType!}
      />
    );
  }

  if (properties.manuellAdresseValues) {
    const adresse: AdresseObject = {
      ...properties.manuellAdresseValues.adresse,
      adresselinje3: "",
    };

    return (
      <OppsummeringAvValgtMottaker
        adresse={adresse}
        onAvbryt={properties.onCloseIntent}
        onBekreft={() => properties.onBekreftNyMottaker(adresse)}
        onTilbake={{
          fn: () => properties.onTilbake("manuellAdresse"),
          plassering: "top",
        }}
        type={properties.manuellAdresseValues.typeMottaker!}
      />
    );
  }
};
