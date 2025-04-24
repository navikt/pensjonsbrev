import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BestillBrevResponse, BrevInfo } from "~/types/brev";

export type SendtBrevTilAttesteringResponser = Array<SendBrevTilAttesteringResponse>;
export type SendBrevTilAttesteringResponse = SendBrevAttesteringSuccessResponse | SendBrevAttesteringErrorResponse;

export interface SendBrevAttesteringSuccessResponse {
  status: "success";
  brevInfo: BrevInfo | undefined;
  response: BestillBrevResponse;
}

export interface SendBrevAttesteringErrorResponse {
  status: "error";
  brevInfo: BrevInfo | undefined;
  error: AxiosError<unknown, unknown>;
}

const SendBrevAttesteringContext = createContext<{
  resultat: SendtBrevTilAttesteringResponser;
  setResultat: (resultat: SendtBrevTilAttesteringResponser) => void;
}>({ resultat: [], setResultat: () => {} });

export const SendtBrevTilAttesteringResultatProvider = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<SendtBrevTilAttesteringResponser>([]);

  return (
    <SendBrevAttesteringContext.Provider value={{ resultat: resultat, setResultat: setResultat }}>
      {properties.children}
    </SendBrevAttesteringContext.Provider>
  );
};

export const useSendtBrevAttesteringResultatContext = () => useContext(SendBrevAttesteringContext);
