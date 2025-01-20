import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BrevInfo } from "~/types/brev";

export type SendtBrevTilAttesteringResponser = Array<SendBrevTilAttesteringResponse>;
export type SendBrevTilAttesteringResponse = SendBrevAttesteringSuccessResponse | SendBrevAttesteringErrorResponse;

export interface SendBrevAttesteringSuccessResponse {
  status: "success";
  brevInfo: BrevInfo;
  response: BrevInfo;
}

export interface SendBrevAttesteringErrorResponse {
  status: "error";
  brevInfo: BrevInfo;
  error: AxiosError<unknown, unknown>;
}

const SendBrevAttesteringContext = createContext<{
  resultat: SendtBrevTilAttesteringResponser;
  setResultat: (resultat: SendtBrevTilAttesteringResponser) => void;
}>({ resultat: [], setResultat: () => {} });

export const SendtBrevTilAttesteringResultatContext = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<SendtBrevTilAttesteringResponser>([]);

  return (
    <SendBrevAttesteringContext.Provider value={{ resultat: resultat, setResultat: setResultat }}>
      {properties.children}
    </SendBrevAttesteringContext.Provider>
  );
};

export const useSendBrevAttesteringContext = () => useContext(SendBrevAttesteringContext);
