import { css } from "@emotion/react";
import { Accordion, Label, Tag } from "@navikt/ds-react";

import type { FerdigstillResponse } from "./FerdigstillResultatContext";

const ResultatAccordionHeader = (properties: { sakId: string; resultat: FerdigstillResponse }) => {
  const { tag, tittel } = hentTagOgTittelForHeader(properties.resultat);

  return (
    <Accordion.Header>
      <div
        css={css`
          display: flex;
          flex-direction: column;
          align-items: flex-start;
        `}
      >
        {tag}
        <Label>{tittel}</Label>
      </div>
    </Accordion.Header>
  );
};

const hentTagOgTittelForHeader = (resultat: FerdigstillResponse) => {
  switch (resultat.status) {
    case "fulfilledWithError": {
      const tag = (
        <Tag size="small" variant={"error"}>
          Brev ble ikke sendt
        </Tag>
      );

      return { tag, tittel: resultat.brevInfo.brevkode };
    }
    case "fulfilledWithSuccess": {
      const tag = resultat.response.journalpostId ? (
        /*
                if(lokaltprint){
                  return <Tag variant={"info"} size="small">Sendt til lokalprint</Tag>
                  } else {
            */
        //teknisk sett er den bare journalført
        <Tag size="small" variant={"success"}>
          Sendt til mottaker
        </Tag>
      ) : (
        <Tag size="small" variant={"error"}>
          Brev ble ikke sendt
        </Tag>
      );

      return { tag, tittel: resultat.brevInfo.brevkode };
    }
  }
};

export default ResultatAccordionHeader;
