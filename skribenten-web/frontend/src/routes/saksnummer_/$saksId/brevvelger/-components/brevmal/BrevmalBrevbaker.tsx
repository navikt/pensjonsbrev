import { BodyShort, Button, HStack, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { memo, useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm, useWatch } from "react-hook-form";

import { createBrev, getBrev } from "~/api/brev-queries";
import { hentAlleBrevInfoForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { Divider } from "~/components/Divider";
import { EndreMottakerModal } from "~/components/endreMottaker/EndreMottakerModal";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import type { LetterMetadata, SpraakKode } from "~/types/apiTypes";
import type { BrevInfo, BrevResponse, Mottaker, SaksbehandlerValg } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";
import { trackEvent } from "~/utils/umami";

import type { SubmitTemplateOptions } from "../../route";
import { Route } from "../../route";
import BrevmalFormWrapper from "./components/BrevmalFormWrapper";
import LetterTemplateHeading from "./components/LetterTemplate";
import SelectEnhet from "./components/SelectEnhet";
import SelectLanguage from "./components/SelectLanguage";

interface BrevbakerFormData {
  enhetsId: string;
  spraak: SpraakKode;
  saksbehandlerValg: SaksbehandlerValg;
  mottaker: Nullable<Mottaker>;
}

const EksisterendeKladdModal = (props: {
  åpen: boolean;
  onClose: () => void;
  onFormSubmit: () => void;
  sisteEksisterendeKladdId: number;
}) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const { enhetsId, vedtaksId } = Route.useSearch();
  return (
    <Modal
      header={{ heading: "Vil du bruke eksisterende kladd?" }}
      onClose={props.onClose}
      open={props.åpen}
      portal
      width={500}
    >
      <Modal.Body>
        <BodyShort>Du har en eksisterende kladd basert på samme brevmal.</BodyShort>
      </Modal.Body>
      <Modal.Footer>
        <HStack gap="space-16">
          <Button onClick={props.onFormSubmit} type="button" variant="secondary">
            Lag nytt brev
          </Button>
          <Button
            onClick={() =>
              navigate({
                to: "/saksnummer/$saksId/brev/$brevId",
                params: { brevId: props.sisteEksisterendeKladdId },
                search: { enhetsId, vedtaksId },
              })
            }
            type="button"
            variant="primary"
          >
            Ja, bruk eksisterende kladd
          </Button>
        </HStack>
      </Modal.Footer>
    </Modal>
  );
};

const BrevmalBrevbaker = (props: {
  letterTemplate: LetterMetadata;
  preferredLanguage: SpraakKode | null;
  displayLanguages: SpraakKode[];
  defaultValues: {
    spraak: SpraakKode;
    enhetsId: string;
  };
  saksId: string;
  setOnFormSubmitClick: (v: SubmitTemplateOptions) => void;
}) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate({ from: Route.fullPath });
  const [modalÅpen, setModalÅpen] = useState<boolean>(false);
  const formRef = useRef<HTMLFormElement>(null);
  const [åpnerNyttBrevOgHarKladd, setÅpnerNyttBrevOgHarKladd] = useState<boolean>(false);
  const { enhetsId, vedtaksId } = Route.useSearch();

  const alleSaksbrevQuery = useQuery({
    queryKey: hentAlleBrevInfoForSak.queryKey(props.saksId.toString()),
    queryFn: () => hentAlleBrevInfoForSak.queryFn(props.saksId.toString()),
  });

  const sistOpprettetKladd = useMemo(() => {
    return alleSaksbrevQuery.data
      ?.filter((brev: BrevInfo) => brev.brevkode === props.letterTemplate.id)
      .toSorted((a, b) => {
        return new Date(a.opprettet).getTime() - new Date(b.opprettet).getTime();
      })
      .at(-1);
  }, [alleSaksbrevQuery.data, props.letterTemplate.id]);
  const harEksisterendeKladd = !!sistOpprettetKladd;

  const opprettBrevMutation = useMutation<BrevResponse, Error, BrevbakerFormData>({
    mutationFn: async (values) =>
      createBrev(props.saksId, {
        brevkode: props.letterTemplate.id,
        spraak: values.spraak,
        avsenderEnhetsId: values.enhetsId,
        saksbehandlerValg: values.saksbehandlerValg,
        mottaker: values.mottaker,
        vedtaksId: vedtaksId ? Number.parseInt(vedtaksId, 10) : null,
      }),

    onSuccess: async (response) => {
      trackEvent("brev opprettet", {
        brevkode: props.letterTemplate.id,
        brevtittel: props.letterTemplate.name,
      });
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      return navigate({
        to: "/saksnummer/$saksId/brev/$brevId",
        params: { brevId: response.info.id },
        search: { enhetsId, vedtaksId },
      });
    },
  });

  const form = useForm<BrevbakerFormData>({
    defaultValues: {
      enhetsId: props.defaultValues.enhetsId,
      spraak: props.defaultValues.spraak,
      mottaker: null,
      saksbehandlerValg: {},
    },
  });

  const mottaker = useWatch({ control: form.control, name: "mottaker" });

  useEffect(() => {
    if (enhetsId && enhetsId !== form.getValues("enhetsId")) {
      form.setValue("enhetsId", enhetsId);
    }
  }, [enhetsId, form]);

  const handleFormSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    form.handleSubmit((values) => {
      opprettBrevMutation.mutate(values);
    })(event);
  };

  const { setOnFormSubmitClick } = props;
  useEffect(() => {
    if (harEksisterendeKladd) {
      setOnFormSubmitClick({
        onClick: () => {
          form.trigger().then((isValid) => {
            if (isValid) {
              setÅpnerNyttBrevOgHarKladd(true);
            }
          });
        },
      });
    } else {
      setOnFormSubmitClick({ onClick: () => formRef.current?.requestSubmit() });
    }
  }, [setOnFormSubmitClick, harEksisterendeKladd, form]);

  return (
    <VStack gap="space-16" height="100%">
      {åpnerNyttBrevOgHarKladd && (
        <EksisterendeKladdModal
          onClose={() => setÅpnerNyttBrevOgHarKladd(false)}
          onFormSubmit={() => {
            formRef.current?.requestSubmit();
            setÅpnerNyttBrevOgHarKladd(false);
          }}
          sisteEksisterendeKladdId={sistOpprettetKladd!.id}
          åpen={åpnerNyttBrevOgHarKladd}
        />
      )}
      <LetterTemplateHeading letterTemplate={props.letterTemplate} />
      <Divider />
      <FormProvider {...form}>
        <BrevmalFormWrapper formRef={formRef} onSubmit={handleFormSubmit}>
          <VStack flexGrow="1" gap="space-32">
            <VStack gap="space-8">
              <VStack>
                <OppsummeringAvMottaker mottaker={mottaker} saksId={props.saksId} withTitle />

                {modalÅpen && (
                  <EndreMottakerModal
                    error={null}
                    isPending={null}
                    onBekreftNyMottaker={(mottaker) => {
                      form.setValue("mottaker", mapEndreMottakerValueTilMottaker(mottaker));
                      setModalÅpen(false);
                    }}
                    onClose={() => setModalÅpen(false)}
                    resetOnBekreftState={() => form.setValue("mottaker", null)}
                    åpen={modalÅpen}
                  />
                )}
              </VStack>
              <HStack>
                <Button onClick={() => setModalÅpen(true)} size="small" type="button" variant="secondary">
                  Endre mottaker
                </Button>
                {mottaker !== null && (
                  <Button onClick={() => form.setValue("mottaker", null)} size="small" type="button" variant="tertiary">
                    Tilbakestill mottaker
                  </Button>
                )}
              </HStack>
            </VStack>
            <SelectEnhet />
            <SelectLanguage preferredLanguage={props.preferredLanguage} sorterteSpråk={props.displayLanguages} />
            <BrevmalAlternativer brevkode={props.letterTemplate.id} onlyShowRequired />
          </VStack>
          {opprettBrevMutation.isError && <ApiError error={opprettBrevMutation.error} title="Bestilling feilet" />}
        </BrevmalFormWrapper>
      </FormProvider>
    </VStack>
  );
};

export default memo(BrevmalBrevbaker);
