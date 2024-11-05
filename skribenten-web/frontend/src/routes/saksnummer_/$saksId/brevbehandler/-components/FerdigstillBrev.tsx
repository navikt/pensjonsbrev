import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Checkbox, CheckboxGroup, HStack, Label, Modal } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect, useMemo } from "react";
import { Controller, useForm } from "react-hook-form";
import { z } from "zod";

import { hentAlleBrevForSak, sendBrev } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";
import type { BestillBrevResponse } from "~/types/brev";
import { type BrevInfo } from "~/types/brev";
import { erBrevKlar } from "~/utils/brevUtils";

import type {
  FerdigstillErrorResponse,
  FerdigstillResponser,
  FerdigstillSuccessResponse,
} from "../../kvittering/-components/FerdigstillResultatContext";
import { useFerdigstillResultatContext } from "../../kvittering/-components/FerdigstillResultatContext";
import { Route } from "../route";

export const FerdigstillOgSendBrevButton = (properties: {
  sakId: string;
  valgtBrevId?: number;
  brevInfo: BrevInfo[];
  åpneFerdigstillModal: () => void;
}) => {
  const antallBrevSomErKlarTilSending = properties.brevInfo.filter(erBrevKlar).length;
  if (properties.valgtBrevId) {
    const valgtBrev = properties.brevInfo.find((brev) => brev.id === properties.valgtBrevId);

    //safe-guard i det tilfellet bogus id er sendt
    if (!valgtBrev) {
      return <BodyShort>Fant ikke brev med id {properties.valgtBrevId}</BodyShort>;
    }

    return (
      <FerdigstillValgtBrev
        antallBrevKlarTilSending={antallBrevSomErKlarTilSending}
        brev={valgtBrev}
        sakId={properties.sakId}
        åpneFerdigstillModal={properties.åpneFerdigstillModal}
      />
    );
  }

  //hvis ingen spesifikk brev er valgt, og noen står klar til å sendes, vis knapp for å sende
  if (!properties.valgtBrevId && properties.brevInfo.some(erBrevKlar)) {
    return (
      <Button onClick={properties.åpneFerdigstillModal} size="small" type="button">
        <HStack gap="2">
          {antallBrevSomErKlarTilSending === 1 ? (
            <Label>Send 1 ferdigstilt brev</Label>
          ) : (
            <Label>Send {antallBrevSomErKlarTilSending} ferdigstilte brev</Label>
          )}
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    );
  }

  //hvis ingen brev er klare til å sendes, og ingen spesifikk brev er valgt, vis ingenting
  return null;
};

const FerdigstillValgtBrev = (properties: {
  sakId: string;
  brev: BrevInfo;
  åpneFerdigstillModal: () => void;
  antallBrevKlarTilSending: number;
}) => {
  const erLåst = useMemo(() => erBrevKlar(properties.brev), [properties.brev]);

  if (erLåst) {
    return (
      <Button
        onClick={() => {
          if (erLåst) {
            properties.åpneFerdigstillModal();
          }
        }}
        size="small"
        type="button"
      >
        <HStack gap="2">
          <Label>Ferdigstill {properties.antallBrevKlarTilSending} brev</Label>
          <ArrowRightIcon fontSize="1.5rem" title="pil-høyre" />
        </HStack>
      </Button>
    );
  }

  return null;
};

const validationSchema = z.object({
  valgteBrevSomSkalSendes: z.array(z.number()).min(1, "Du må velge minst 1 brev"),
});

const isFerdigstillSuccessResponse = (
  res: FerdigstillSuccessResponse | FerdigstillErrorResponse,
): res is FerdigstillSuccessResponse => {
  return res.status === "fulfilledWithSuccess" && !!res.response.journalpostId;
};

export const FerdigstillOgSendBrevModal = (properties: { sakId: string; åpen: boolean; onClose: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const queryClient = useQueryClient();

  const ferdigstillBrevContext = useFerdigstillResultatContext();

  const bestillBrevMutation = useMutation<BestillBrevResponse, Error, number>({
    mutationFn: (brevId) => sendBrev(properties.sakId, brevId),
  });

  const alleBrevForSak = useQuery({
    queryKey: hentAlleBrevForSak.queryKey(properties.sakId),
    queryFn: () => hentAlleBrevForSak.queryFn(properties.sakId),
  });

  const alleFerdigstilteBrev = useMemo(() => {
    if (alleBrevForSak.isSuccess) {
      return alleBrevForSak.data.filter(erBrevKlar);
    }
    return [];
  }, [alleBrevForSak.data, alleBrevForSak.isSuccess]);

  const form = useForm<z.infer<typeof validationSchema>>({
    defaultValues: { valgteBrevSomSkalSendes: [] },
    resolver: zodResolver(validationSchema),
  });

  useEffect(() => {
    form.setValue(
      "valgteBrevSomSkalSendes",
      alleFerdigstilteBrev.map((brev) => brev.id),
    );
  }, [alleFerdigstilteBrev, form]);

  const onSendValgteBrev = async (values: { valgteBrevSomSkalSendes: number[] }) => {
    const brevSomSkalSendes = values.valgteBrevSomSkalSendes
      .map((brevId) => alleFerdigstilteBrev.find((brev) => brev.id === brevId))
      .filter((brev): brev is BrevInfo => !!brev);

    const requests = brevSomSkalSendes.map((brevInfo) =>
      bestillBrevMutation.mutateAsync(brevInfo.id).then(
        //vi har fortsatt behov for informasjon i brevet, så vi returnerer brevinfo sammen med responsen
        (response) => ({ status: "fulfilledWithSuccess" as const, brevInfo, response }),
        (error: AxiosError) => ({ status: "fulfilledWithError" as const, brevInfo, error }),
      ),
    );

    const resultat: FerdigstillResponser = await Promise.allSettled(requests).then((result) =>
      result.map((response) => {
        switch (response.status) {
          //fordi vi håndterer vanlige caser av rejected i mutation, vil resultatet 'alltid' være fulfilled
          case "fulfilled": {
            return response.value;
          }
          //en safe-guard dersom noe uventet går feil? Kan se om vi kan håndtere dette på en bedre måte
          case "rejected": {
            throw new Error(`Feil ved sending av minst 1 brev. Original error: ${response.reason}`);
          }
        }
      }),
    );
    ferdigstillBrevContext.setResultat(resultat);
    const sendteBrev = new Set(resultat.filter(isFerdigstillSuccessResponse).map((res) => res.brevInfo.id));
    queryClient.setQueryData(hentAlleBrevForSak.queryKey(properties.sakId), (currentBrevInfo: BrevInfo[]) =>
      currentBrevInfo.filter((brev) => !sendteBrev.has(brev.id)),
    );
    navigate({ to: "/saksnummer/$saksId/kvittering", params: { saksId: properties.sakId } });
  };

  return (
    <Modal
      css={css`
        border-radius: 0.25rem;
      `}
      header={{
        heading: "Vil du ferdigstille, og sende disse brevene?",
      }}
      onClose={properties.onClose}
      open={properties.åpen}
      portal
      width={600}
    >
      <form onSubmit={form.handleSubmit(onSendValgteBrev)}>
        <Modal.Body>
          <div
            css={css`
              margin-bottom: 1rem;
            `}
          >
            <div>
              <BodyShort>
                Brevene du ferdigstiller og sender vil bli lagt til i brukers dokumentoversikt. Du kan ikke angre denne
                handlingen.
              </BodyShort>
              <br />
              <BodyShort>Kun brev du har valgt å ferdigstille vil bli sendt.</BodyShort>
            </div>
            <br />
            <div>
              {alleBrevForSak.isPending && <Label>Henter alle ferdigstilte brev...</Label>}
              {alleBrevForSak.isError && (
                <ApiError error={alleBrevForSak.error} title={"Klarte ikke å hente alle ferdigstilte for saken"} />
              )}
              {alleBrevForSak.isSuccess && (
                <Controller
                  control={form.control}
                  name="valgteBrevSomSkalSendes"
                  render={({ field, fieldState }) => (
                    <CheckboxGroup
                      data-cy="ferdigstillbrev-valgte-brev"
                      error={fieldState.error?.message}
                      hideLegend
                      legend="Velg brev som skal sendes"
                      onChange={field.onChange}
                      value={field.value}
                    >
                      {alleFerdigstilteBrev.map((brev) => (
                        <Checkbox key={brev.id} value={brev.id}>
                          {brev.brevtittel}
                        </Checkbox>
                      ))}
                    </CheckboxGroup>
                  )}
                />
              )}
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <HStack gap="4">
            <Button onClick={properties.onClose} type="button" variant="tertiary">
              Avbryt
            </Button>

            <Button loading={bestillBrevMutation.isPending} type="submit">
              Ja, send valgte brev
            </Button>
          </HStack>
        </Modal.Footer>
      </form>
    </Modal>
  );
};
