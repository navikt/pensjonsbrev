import { Alert, Heading } from "@navikt/ds-react";
import { createFileRoute, redirect } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { css } from "@emotion/react";

import { getUserInfo } from "~/api/bff-endpoints";
import { getBrevInfoQuery } from "~/api/brev-queries";
import { ApiError } from "~/components/ApiError";
import AttestForbiddenModal from "~/components/AttestForbiddenModal";
import { queryClient } from "~/routes/__root";
import type { AttestForbiddenReason } from "~/utils/parseAttest403";

export const Route = createFileRoute("/aapne/brev/$brevId")({
  loader: async ({ params: { brevId } }) => {
    const brevIdNum = Number(brevId);

    if (isNaN(brevIdNum) || !Number.isInteger(brevIdNum) || brevIdNum <= 0) {
      throw new Error("Ugyldig brev-ID mottatt fra Pesys. Gå tilbake til Pesys og prøv igjen.");
    }

    let brevInfo;
    try {
      brevInfo = await queryClient.ensureQueryData(getBrevInfoQuery(brevIdNum));
    } catch (error) {
      const axiosError = error as AxiosError & { forbidReason?: AttestForbiddenReason };

      if (axiosError.response?.status === 403 && axiosError.forbidReason) {
        return { reason: axiosError.forbidReason };
      }
      throw error;
    }

    const currentUser = await queryClient.ensureQueryData(getUserInfo);
    const isOriginalCreator = currentUser.navident === brevInfo.opprettetAv.id;

    switch (brevInfo.status.type) {
      case "Kladd":
      case "UnderRedigering":
        throw redirect({
          to: "/saksnummer/$saksId/brev/$brevId",
          params: { saksId: String(brevInfo.saksId), brevId: brevIdNum },
        });

      case "Attestering": {
        if (isOriginalCreator) {
          throw redirect({
            to: "/saksnummer/$saksId/brevbehandler",
            params: { saksId: String(brevInfo.saksId) },
            search: { brevId: brevIdNum },
          });
        }
        throw redirect({
          to: "/saksnummer/$saksId/attester/$brevId/redigering",
          params: { saksId: String(brevInfo.saksId), brevId: String(brevIdNum) },
        });
      }

      case "Klar":
      case "Arkivert":
        throw redirect({
          to: "/saksnummer/$saksId/brevbehandler",
          params: { saksId: String(brevInfo.saksId) },
          search: { brevId: brevIdNum },
        });
    }
  },

  component: () => {
    return <AttestGuard />;
  },

  errorComponent: BrevOpenError,
});

function AttestGuard() {
  const data = Route.useLoaderData() as { reason?: AttestForbiddenReason; saksId?: string };

  if (!data?.reason) return null;

  return <AttestForbiddenModal onClose={() => null} reason={data.reason} />;
}

function BrevOpenError({ error }: { error: unknown }) {
  const { brevId } = Route.useParams();

  if (error instanceof Error && error.message.includes("Ugyldig brev-ID")) {
    return (
      <div
        css={css`
          display: flex;
          justify-content: center;
          align-items: center;
          margin-top: var(--a-spacing-4);
        `}
      >
        <Alert
          css={css`
            width: 100%;
            max-width: 512px;
          `}
          size="medium"
          variant="error"
        >
          <Heading level="2" size="small">
            Ugyldig lenke fra Pesys
          </Heading>

          <div
            css={css`
              margin-top: 4px;
            `}
          >
            {error.message}
          </div>
          <div>Brev-ID som ble mottatt: {brevId}</div>
        </Alert>
      </div>
    );
  }

  return <ApiError error={error as AxiosError} title={`Kunne ikke åpne brev ${brevId}`} />;
}
