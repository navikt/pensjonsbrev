import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Heading, Modal, Tabs } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useCallback, useState } from "react";
import React from "react";
import type { Control } from "react-hook-form";
import { useForm } from "react-hook-form";

import { finnSamhandler2 } from "~/api/skribenten-api-endpoints";
import type { Adresse, FinnSamhandlerRequest, FinnSamhandlerResponseDto } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import BekreftAvbrytelse from "./BekreftAvbrytelse";
import type { EndreMottakerModalTabs, ManuellAdresseUtfyllingFormData } from "./EndreMottakerUtils";
import {
  type CombinedFormData,
  createSamhandlerValidationSchema,
  type FinnSamhandlerFormData,
  Søketype,
} from "./EndreMottakerUtils";
import HentOgVisSamhandlerAdresse from "./HentOgVisSamhandlerAdresse";
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";
import UtfyllingAvManuellAdresseForm from "./UtfyllingAvManuellAdresseForm";

const EndreMottaker = (properties: {
  //kan være undefined fordi vi ikke kan gjøre noe manuellAdresse enda
  onManuellAdresseBekreft?: (a: Nullable<Adresse>) => void;
  children?: React.ReactNode;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <div>
      {modalÅpen && (
        <EndreMottakerModal
          onBekreftNyMottaker={(bekreftetMottaker) => {
            setModalÅpen(false);
            //hvis mottaker er en string, betyr det at det er en samhandler
            if (typeof bekreftetMottaker === "string") {
              properties.onManuellAdresseBekreft?.(null);
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: bekreftetMottaker }),
                replace: true,
              });
            } else {
              //hvis mottaker er en adresse, betyr det at det er en manuell adresse
              properties.onManuellAdresseBekreft?.(bekreftetMottaker);
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
      <Heading level="3" size="xsmall">
        Mottaker
      </Heading>
      {properties.children}
      <Button
        data-cy="toggle-endre-mottaker-modal"
        onClick={() => setModalÅpen(true)}
        size="small"
        type="button"
        variant="secondary"
      >
        Endre mottaker
      </Button>
    </div>
  );
};

export default EndreMottaker;

/*
  litt om hvorfor denne er laget som den er:
  Vi har 1 modal, som skal deles opp i 3 tabs. 2 av dem er velgbare, mens 1 tab (oppsummering) er intern.
  Modalen-bodyen skal reflektere 3 tilstander - Valg av mottaker, oppsummering, og avbrytelse
  Oppsummering og avbyrtelse skal 'fjerne' alt det andre, derfor kreves det at modal har en state som vet hva den skal rendre
  En mulighet er som det er gjort nå - ha hele formet i modalen, og rendrer sine children basert på state. 
  En annen måte kunne ha vært å ha forms i hver tab, mens parenten sender callbacks til children ved deres onSubmit. 
  Med litt prøving av begge deler, har jeg landet på at det er enklere å ha alt i en form, selv om det fortsatt er litt komplisert.
*/
const EndreMottakerModal = (properties: {
  åpen: boolean;
  onBekreftNyMottaker: (id: string | Adresse) => void;
  onClose: () => void;
}) => {
  const [tab, setTab] = useState<EndreMottakerModalTabs>("samhandler");
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

  const defaultManuellAdresse: ManuellAdresseUtfyllingFormData = {
    typeMottaker: null,
    adresse: {
      navn: "",
      linje1: "",
      linje2: "",
      postnr: "",
      poststed: "",
      //default value skal være norge. Siden vi henter alle landkodene i backend, hardkoder vi norges verdi.
      land: "NO",
    },
  };
  const defaultFinnSamhandler = {
    søketype: null,
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
                setValgtSamhandler(null);
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
              manuellAdresseValues={valgtSamhandler ? null : form.getValues("manuellAdresse")}
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
    tab: EndreMottakerModalTabs;
    setTab: (tab: EndreMottakerModalTabs) => void;
  };
  control: Control<CombinedFormData>;
  onAvbrytClick: () => void;
  setSamhandler: (id: string) => void;
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequest, unknown>;
  onBekreftNyMottaker: (id: string | Adresse) => void;
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
        <Tabs onChange={(s) => properties.tab.setTab(s as EndreMottakerModalTabs)} value={properties.tab.tab}>
          <Tabs.List>
            <Tabs.Tab label="Finn samhandler" value="samhandler" />
            {/* Vi har enda ikke backend støtte for å legge til manuell adresse. Midlertidig har vi bare fjernet muligheten til å komme inn i formet */}
            {/* <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" /> */}
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
  onBekreftNyMottaker: (id: string | Adresse) => void;
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
    const adresse: Adresse = {
      ...properties.manuellAdresseValues.adresse,
      linje3: "",
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
