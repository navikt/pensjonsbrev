import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BestillBrevResponse, BrevInfo } from "~/types/brev";

export type SendtBrevResponser = Array<SendtBrevSuccessResponse | SendtBrevErrorResponse>;
export type SendtBrevResponse = SendtBrevSuccessResponse | SendtBrevErrorResponse;

export interface SendtBrevSuccessResponse {
  status: "success";
  brevInfo: BrevInfo | undefined;
  response: BestillBrevResponse;
}

export interface SendtBrevErrorResponse {
  status: "error";
  brevInfo: BrevInfo | undefined;
  error: AxiosError<unknown, unknown>;
}

const SendtBrevResultatContext = createContext<{
  resultat: SendtBrevResponser;
  setResultat: (resultat: SendtBrevResponser) => void;
}>({ resultat: [], setResultat: () => {} });

export const SendtBrevResultatContextProvider = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<SendtBrevResponser>([]);

  return (
    <SendtBrevResultatContext.Provider value={{ resultat: resultat, setResultat: setResultat }}>
      {properties.children}
    </SendtBrevResultatContext.Provider>
  );
};

export const useSendtBrevResultatContext = () => useContext(SendtBrevResultatContext);
