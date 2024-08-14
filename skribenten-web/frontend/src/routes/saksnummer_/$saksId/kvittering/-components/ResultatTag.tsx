import { Tag } from "@navikt/ds-react";

import type { BestillBrevResponse } from "~/types/brev";

const ResultatTag = (properties: { resultat: PromiseSettledResult<BestillBrevResponse> }) => {
  switch (properties.resultat.status) {
    case "rejected": {
      return (
        <Tag size="small" variant={"error"}>
          Brev ble ikke sendt
        </Tag>
      );
    }
    case "fulfilled": {
      return properties.resultat.value.journalpostId ? (
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
    }
  }
};

export default ResultatTag;
