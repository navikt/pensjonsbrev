import { css } from "@emotion/react";
import { Label, Loader, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";

import { hentPdfForBrev, hentPdfForBrevFunction } from "~/api/sak-api-endpoints";
import { ApiError } from "~/components/ApiError";

import PDFViewer from "../-components/PDFViewer";

export const BrevForhåndsvisning = (properties: { sakId: string; brevId: string }) => {
  const hentPdfQuery = useQuery({
    queryKey: hentPdfForBrev.queryKey(properties.brevId),
    queryFn: () => hentPdfForBrevFunction(properties.sakId, properties.brevId),
  });

  return (
    <VStack
      css={css`
        height: 100%;
      `}
      justify={"center"}
    >
      {hentPdfQuery.isPending && (
        <VStack align="center" justify="center">
          <Loader size="3xlarge" title="Henter..." />
          <Label>Henter brev...</Label>
        </VStack>
      )}
      {hentPdfQuery.isError && <ApiError error={hentPdfQuery.error} title={"Kunne ikke hente PDF"} />}
      {hentPdfQuery.isSuccess && hentPdfQuery.data !== null && (
        <PDFViewer
          brevId={properties.brevId}
          pdf={hentPdfQuery.data}
          sakId={properties.sakId}
          viewerHeight={"var(--main-page-content-height)"}
        />
      )}
    </VStack>
  );
};

export default BrevForhåndsvisning;
