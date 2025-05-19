import { css } from "@emotion/react";
import { BodyShort, Button, HStack, Modal, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { memo, useEffect, useMemo, useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { createBrev, getBrev } from "~/api/brev-queries";
import { hentAlleBrevForSak } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { Divider } from "~/components/Divider";
import OppsummeringAvMottaker from "~/components/OppsummeringAvMottaker";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";
import type { BrevInfo, BrevResponse, Mottaker, SaksbehandlerValg } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";
import { mapEndreMottakerValueTilMottaker } from "~/utils/AdresseUtils";

import type { SubmitTemplateOptions } from "../../route";
import { Route } from "../../route";
import { EndreMottakerModal } from "../endreMottaker/EndreMottaker";
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
        <HStack gap="4">
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
    queryKey: hentAlleBrevForSak.queryKey(props.saksId.toString()),
    queryFn: () => hentAlleBrevForSak.queryFn(props.saksId.toString()),
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
        vedtaksId: vedtaksId ? Number.parseInt(vedtaksId) : null,
      }),

    onSuccess: async (response) => {
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

  useEffect(() => {
    if (enhetsId && enhetsId !== form.getValues("enhetsId")) {
      form.setValue("enhetsId", enhetsId);
    }
  }, [enhetsId, form]);

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
    <VStack
      css={css`
        height: 100%;
      `}
      gap="4"
    >
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
        <BrevmalFormWrapper formRef={formRef} onSubmit={form.handleSubmit((v) => opprettBrevMutation.mutate(v))}>
          <VStack
            css={css`
              flex: 1;
            `}
            gap="8"
          >
            <VStack gap="2">
              <VStack>
                <OppsummeringAvMottaker mottaker={form.watch("mottaker")} saksId={props.saksId} withTitle />

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
                {form.watch("mottaker") !== null && (
                  <Button onClick={() => form.setValue("mottaker", null)} size="small" type="button" variant="tertiary">
                    Tilbakestill mottaker
                  </Button>
                )}
              </HStack>
            </VStack>
            <SelectEnhet />
            <SelectLanguage preferredLanguage={props.preferredLanguage} sorterteSpråk={props.displayLanguages} />
            <BrevmalAlternativer brevkode={props.letterTemplate.id} displaySingle="required" />
          </VStack>
          {opprettBrevMutation.isError && <ApiError error={opprettBrevMutation.error} title="Bestilling feilet" />}
        </BrevmalFormWrapper>
      </FormProvider>
    </VStack>
  );
};

export default memo(BrevmalBrevbaker);
