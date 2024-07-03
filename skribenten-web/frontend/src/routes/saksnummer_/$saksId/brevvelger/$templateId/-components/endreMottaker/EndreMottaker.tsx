import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { BodyShort, Button, Heading, Modal, Tabs, VStack } from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useCallback, useState } from "react";
import React from "react";
import type { Control } from "react-hook-form";
import { useForm } from "react-hook-form";

import { finnSamhandler2, getKontaktAdresse, getNavn, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type {
  Adresse,
  FinnSamhandlerRequest,
  FinnSamhandlerResponseDto,
  KontaktAdresseResponse,
} from "~/types/apiTypes";
import { getAdresseTypeName } from "~/types/nameMappings";
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
import OppsummeringAvValgtMottaker from "./OppsummeringAvValgtMottaker";
import SøkOgVelgSamhandlerForm from "./SøkOgVelgSamhandlerForm";
import UtfyllingAvManuellAdresseForm from "./UtfyllingAvManuellAdresseForm";

const EndreMottaker = (properties: {
  onManuellAdresseBekreft: (a: Nullable<Adresse>) => void;
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

export const HentOgVisAdresse = (properties: { sakId: string; samhandlerId?: string }) => {
  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(properties.samhandlerId as string),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: properties.samhandlerId as string }),
    enabled: !!properties.samhandlerId,
  });

  const adresseQuery = useQuery({
    queryKey: getKontaktAdresse.queryKey(properties.sakId),
    queryFn: () => getKontaktAdresse.queryFn(properties.sakId),
    enabled: !properties.samhandlerId,
  });

  return (
    <div>
      {!properties.samhandlerId && (
        <div>
          {adresseQuery.isSuccess && <MottakerAdresseOppsummering adresse={adresseQuery.data} erSamhandler={false} />}
          {adresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
          {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
        </div>
      )}
      {properties.samhandlerId && (
        <div>
          {hentSamhandlerAdresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
          {hentSamhandlerAdresseQuery.data && (
            <MottakerAdresseOppsummering adresse={hentSamhandlerAdresseQuery.data} erSamhandler />
          )}
          {hentSamhandlerAdresseQuery.error && (
            <ApiError error={hentSamhandlerAdresseQuery.error} title="Fant ikke adresse" />
          )}
        </div>
      )}
    </div>
  );
};

/**
 *
 * @param erSamhandler - burde settes dersom adressen er en Adresse, og ikke en KontaktAdresseResponse
 */
const MottakerAdresseOppsummering = (properties: {
  adresse: Adresse | KontaktAdresseResponse;
  erSamhandler?: boolean;
}) => {
  return (
    <div>
      {erAdresseKontaktAdresse(properties.adresse) ? (
        <ValgtKontaktAdresseOppsummering adresse={properties.adresse} />
      ) : (
        <ValgtAdresseOppsummering adresse={properties.adresse} erSamhandler={properties.erSamhandler ?? false} />
      )}
    </div>
  );
};

const ValgtKontaktAdresseOppsummering = (properties: { adresse: KontaktAdresseResponse }) => {
  const { saksId } = Route.useParams();
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(saksId),
    queryFn: () => getNavn.queryFn(saksId),
  });

  return (
    <div>
      <div>
        {navn} ({getAdresseTypeName(properties.adresse.type)})
      </div>
      <VStack gap="0">
        {properties.adresse.adresselinjer.map((linje) => (
          <span key={linje}>{linje}</span>
        ))}
      </VStack>
    </div>
  );
};

const ValgtAdresseOppsummering = (properties: { adresse: Adresse; erSamhandler: boolean }) => {
  return (
    <div>
      <BodyShort>
        {properties.adresse.navn} {properties.erSamhandler && "(Samhandler)"}
      </BodyShort>
      <VStack gap="0">
        <BodyShort>{properties.adresse.linje1}</BodyShort>
        <BodyShort>
          {properties.adresse.postnr} {properties.adresse.poststed}{" "}
          {properties.adresse.land === "NOR" ? "" : `, ${properties.adresse.land}`}
        </BodyShort>
      </VStack>
    </div>
  );
};

export const erAdresseEnVanligAdresse = (adresse: Adresse | KontaktAdresseResponse) =>
  "linje1" in adresse && "linje2" in adresse && "postnr" in adresse && "poststed" in adresse && "land" in adresse;

export const erAdresseKontaktAdresse = (adresse: Adresse | KontaktAdresseResponse): adresse is KontaktAdresseResponse =>
  "adresseString" in adresse && "adresselinjer" in adresse && "type" in adresse;

export default EndreMottaker;

type ModalTabs = "samhandler" | "manuellAdresse" | "oppsummering";

const EndreMottakerModal = (properties: {
  åpen: boolean;
  onBekreftNyMottaker: (id: string | Adresse) => void;
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
    tab: ModalTabs;
    setTab: (tab: ModalTabs) => void;
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
