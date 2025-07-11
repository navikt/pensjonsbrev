import { createFileRoute, redirect } from "@tanstack/react-router";
import type { AxiosError } from "axios";

import { getBrevInfoQuery } from "~/api/brev-queries";
import { ApiError } from "~/components/ApiError";
import AttestForbiddenModal from "~/components/AttestForbiddenModal";
import { queryClient } from "~/routes/__root";
import type { AttestForbiddenReason } from "~/utils/parseAttest403";

export const Route = createFileRoute("/aapne/brev/$brevId")({
  loader: async ({ params: { brevId } }) => {
    const brevIdNum = Number(brevId);

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

    switch (brevInfo.status.type) {
      case "Kladd":
      case "UnderRedigering":
        throw redirect({
          to: "/saksnummer/$saksId/brev/$brevId",
          params: { saksId: String(brevInfo.saksId), brevId: brevIdNum },
        });

      case "Attestering": {
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
  return <ApiError error={error as AxiosError} title={`Kunne ikke åpne brev ${brevId}`} />;
}
