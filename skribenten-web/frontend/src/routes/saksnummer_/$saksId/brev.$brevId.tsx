import { css } from "@emotion/react";
import { BodyLong, Button, Modal } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React, { type Dispatch, type SetStateAction, useEffect, useState } from "react";
import { z } from "zod";

import { getBrev, getBrevReservasjon, hurtiglagreBrev, hurtiglagreSaksbehandlerValg } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { ModelEditor } from "~/Brevredigering/ModelEditor/ModelEditor";
import { Route as BrevvelgerRoute } from "~/routes/saksnummer_/$saksId/brevvelger/route";
import type { BrevResponse, ReservasjonResponse } from "~/types/brev";

export const Route = createFileRoute("/saksnummer/$saksId/brev/$brevId")({
  parseParams: ({ brevId }) => ({ brevId: z.coerce.number().parse(brevId) }),
  component: RedigerBrevPage,
});

function RedigerBrevPage() {
  const { brevId, saksId } = Route.useParams();
  const brevQuery = useQuery({
    queryKey: getBrev.queryKey(brevId),
    queryFn: () => getBrev.queryFn(saksId, brevId),
    staleTime: Number.POSITIVE_INFINITY,
    retry: (_, error: AxiosError) => error && error.response?.status !== 423,
    throwOnError: (error: AxiosError) => error.response?.status !== 423,
  });
  if (brevQuery.error?.response?.data) {
    return (
      <ReservertBrevError
        doRetry={brevQuery.refetch}
        reservasjon={brevQuery.error.response.data as ReservasjonResponse}
      />
    );
  } else if (brevQuery.data) {
    return <RedigerBrev brev={brevQuery.data} doReload={brevQuery.refetch} />;
  } else {
    return <div>Laster...</div>;
  }
}

const ReservertBrevError = ({ reservasjon, doRetry }: { reservasjon?: ReservasjonResponse; doRetry: () => void }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  if (reservasjon) {
    return (
      <Modal
        header={{ heading: "Brevet redigeres av noen andre", closeButton: false }}
        onClose={() => {}}
        open={!reservasjon.vellykket}
        width={478}
      >
        <Modal.Body>
          <BodyLong>
            Brevet er utilgjengelig for deg fordi {reservasjon.reservertAv.navn} har brevet åpent. Ønsker du å forsøke å
            åpne brevet på nytt?
          </BodyLong>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={doRetry} type="button">
            Ja, åpne på nytt
          </Button>
          <Button onClick={() => navigate({ to: BrevvelgerRoute.fullPath })} type="button" variant="tertiary">
            Nei, gå til brevbehandler
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
};

const RedigerBrev = ({ brev, doReload }: { brev: BrevResponse; doReload: () => void }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brev));

  const saksbehandlerValgMutation = useHurtiglagreMutation(brev.info.id, setEditorState, hurtiglagreSaksbehandlerValg);
  const redigertBrevMutation = useHurtiglagreMutation(brev.info.id, setEditorState, hurtiglagreBrev);

  const reservasjonQuery = useQuery({
    queryKey: getBrevReservasjon.querykey(brev.info.id),
    queryFn: () => getBrevReservasjon.queryFn(brev.info.id),
    refetchInterval: 10_000,
  });

  useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.isDirty) {
        redigertBrevMutation.mutate(editorState.redigertBrev);
      }
    }, 5000);
    return () => clearTimeout(timoutId);
  }, [editorState.isDirty, editorState.redigertBrev, redigertBrevMutation]);

  useEffect(() => {
    if (editorState.redigertBrevHash !== brev.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: brev.redigertBrev,
        redigertBrevHash: brev.redigertBrevHash,
      }));
    }
  }, [brev.redigertBrev, brev.redigertBrevHash, editorState.redigertBrevHash, setEditorState]);

  return (
    <>
      <ReservertBrevError doRetry={doReload} reservasjon={reservasjonQuery.data} />
      <div
        css={css`
          background: var(--a-white);
          display: grid;
          grid-template-columns: minmax(380px, 400px) 1fr;
          flex: 1;
          border-left: 1px solid var(--a-gray-200);
          border-right: 1px solid var(--a-gray-200);

          > form:first-of-type {
            padding: var(--a-spacing-4);
            border-right: 1px solid var(--a-gray-200);
          }
        `}
      >
        <ModelEditor
          brevkode={brev.info.brevkode}
          defaultValues={brev.saksbehandlerValg}
          disableSubmit={saksbehandlerValgMutation.isPending}
          onSubmit={saksbehandlerValgMutation.mutate}
        />
        <LetterEditor
          editorState={editorState}
          freeze={redigertBrevMutation.isPending || saksbehandlerValgMutation.isPending}
          setEditorState={setEditorState}
        />
      </div>
    </>
  );
};

function useHurtiglagreMutation<T>(
  brevId: number,
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>,
  mutationFunction: (brevId: number, body: T) => Promise<BrevResponse>,
) {
  const queryClient = useQueryClient();

  return useMutation<BrevResponse, AxiosError, T>({
    mutationFn: async (saksbehandlerValg) => {
      applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      return mutationFunction(brevId, saksbehandlerValg);
    },
    onSuccess: (response: BrevResponse) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: response.redigertBrev,
        redigertBrevHash: response.redigertBrevHash,
        saksbehandlerValg: response.saksbehandlerValg,
        info: response.info,
        isDirty: false,
      }));
    },
  });
}
