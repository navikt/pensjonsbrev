import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button, Modal, Tabs } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useCallback, useState } from "react";
import type { Control } from "react-hook-form";
import { useForm } from "react-hook-form";

import { finnSamhandler } from "~/api/skribenten-api-endpoints";
import type { Adresse, FinnSamhandlerRequestDto, FinnSamhandlerResponseDto } from "~/types/apiTypes";
import type { Nullable } from "~/types/Nullable";

import { Route } from "../../route";
import BekreftAvbrytelse from "./BekreftAvbrytelse";
import type { EndreMottakerModalTabs, ManuellAdresseUtfyllingFormData } from "./EndreMottakerUtils";
import {
  type CombinedFormData,
  createSamhandlerValidationSchema,
  type FinnSamhandlerFormData,
  InnOgUtland,
  Søketype,
} from "./EndreMottakerUtils";
import HentOgVisSamhandlerAdresse from "./HentOgVisSamhandlerAdresse";
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";
import UtfyllingAvManuellAdresseForm from "./UtfyllingAvManuellAdresseForm";

const EndreMottaker = (properties: {
  //kan være undefined fordi vi ikke kan gjøre noe manuellAdresse enda
  onManuellAdresseBekreft?: (a: Nullable<Adresse>) => void;
}) => {
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <div>
      {modalÅpen && (
        <EndreMottakerModal
          error={null}
          isPending={false}
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
          resetOnBekreftState={() => {}}
          skalKunOppdatereSamhandler
          åpen={modalÅpen}
        />
      )}
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
  TODO - flytt ut til generel components mappe siden den nå benyttes i flere steder

  litt om hvorfor denne er laget som den er:
  Vi har 1 modal, som skal deles opp i 3 tabs. 2 av dem er velgbare, mens 1 tab (oppsummering) er intern.
  Modalen-bodyen skal reflektere 3 tilstander - Valg av mottaker, oppsummering, og avbrytelse
  Oppsummering og avbyrtelse skal 'fjerne' alt det andre, derfor kreves det at modal har en state som vet hva den skal rendre
  Vi klarer også å holde på diverse tilstander mye enklere, som f.eks sammhandler søk dersom dem navigerer seg tilbake fra oppsummering
*/
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
  const [valgtSamhandler, setValgtSamhandler] = useState<Nullable<string>>(null);

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
      css={css`
        border-radius: 0.25rem;
      `}
      data-cy="endre-mottaker-modal"
      header={{
        heading: vilAvbryte && form.formState.isDirty ? "Vil du avbryte endring av mottaker?" : "Endre mottaker",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      /*
      ved å ha modalen som en portal vil ikke browseren klage på at vi kommer til å ha en form inni en annen form.
      vi vil likevel få litt andre tekniske problemer som event propagation. Denne kan vi likevel bare stoppe..
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
                properties.resetOnBekreftState();
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
              error={properties.error}
              isPending={properties.isPending}
              manuellAdresseValues={valgtSamhandler ? null : form.getValues("manuellAdresse")}
              onAvbrytClick={onAvbrytClick}
              onBekreftNyMottaker={properties.onBekreftNyMottaker}
              onFinnSamhandlerSubmit={finnSamhandlerMutation}
              resetOnBekreftState={properties.resetOnBekreftState}
              samhandlerValuesMedId={
                valgtSamhandler ? { ...form.getValues("finnSamhandler"), id: valgtSamhandler } : null
              }
              setSamhandler={(id) => setValgtSamhandler(id)}
              skalKunOppdatereSamhandler={properties.skalKunOppdatereSamhandler}
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
  onFinnSamhandlerSubmit: UseMutationResult<FinnSamhandlerResponseDto, Error, FinnSamhandlerRequestDto, unknown>;
  onBekreftNyMottaker: (id: string | Adresse) => void;
  resetOnBekreftState: () => void;
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
  samhandlerValuesMedId: Nullable<SamhandlerValuesMedId>;
  manuellAdresseValues: Nullable<ManuellAdresseUtfyllingFormData>;
  skalKunOppdatereSamhandler?: boolean;
}) => {
  return (
    <div>
      {properties.tab.tab === "oppsummering" ? (
        <OppsummeringsTab
          error={properties.error}
          isPending={properties.isPending}
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

            {properties.skalKunOppdatereSamhandler ? null : (
              <Tabs.Tab label="Legg til manuelt" value="manuellAdresse" />
            )}
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
                  properties.resetOnBekreftState();
                  properties.setSamhandler(id);
                  properties.tab.setTab("oppsummering");
                }}
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
  error: Nullable<AxiosError>;
  isPending: Nullable<boolean>;
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
        error={properties.error}
        id={properties.samhandlerValues.id}
        isPending={properties.isPending}
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
        error={properties.error}
        isPending={properties.isPending}
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
