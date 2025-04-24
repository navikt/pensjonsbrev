import type { BestillBrevResponse, BrevInfo } from "~/types/brev";
import { Distribusjonstype } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

export interface KvittertBrev {
  apiStatus: "error" | "success";
  context: "sendBrev" | "attestering";
  brevFørHandling: BrevInfo | undefined;
  attesteringResponse: Nullable<BrevInfo>;
  sendtBrevResponse: Nullable<BestillBrevResponse>;
}

export const distribusjonstypeTilText = (d: Distribusjonstype) => {
  switch (d) {
    case Distribusjonstype.SENTRALPRINT: {
      return "Sentral print";
    }
    case Distribusjonstype.LOKALPRINT: {
      return "Lokal print";
    }
  }
};

export const toKvittertBrev = (args: {
  status: "error" | "success";
  context: "sendBrev" | "attestering";
  brevFørHandling: BrevInfo | undefined;
  bestillBrevResponse: Nullable<BestillBrevResponse>;
  attesterResponse: Nullable<BrevInfo>;
}): KvittertBrev => ({
  apiStatus: args.status,
  context: args.context,
  brevFørHandling: args.brevFørHandling,
  attesteringResponse: args.attesterResponse,
  sendtBrevResponse: args.bestillBrevResponse,
});

export const kvitteringSorteringsPrioritet: Record<"error" | "lokalprint" | "attestering" | "sentralprint", number> = {
  error: 1,
  lokalprint: 2,
  attestering: 3,
  sentralprint: 4,
};

export const getPriorityKey = (
  apiStatus: "error" | "success",
  context: "sendBrev" | "attestering",
  brevInfo: BrevInfo,
) => {
  if (apiStatus === "error") {
    return "error";
  }
  if (context === "attestering") {
    return "attestering";
  } else {
    return brevInfo.distribusjonstype === Distribusjonstype.LOKALPRINT ? "lokalprint" : "sentralprint";
  }
};
