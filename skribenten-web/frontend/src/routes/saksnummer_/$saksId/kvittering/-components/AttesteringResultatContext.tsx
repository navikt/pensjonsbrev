import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BrevInfo } from "~/types/brev";

export type AttesteringResponser = Array<AttesteringResponse>;
export type AttesteringResponse = AttesteringSuccessResponse | AttesteringErrorResponse;

export interface AttesteringSuccessResponse {
  status: "success";
  brevInfo: BrevInfo;
}

export interface AttesteringErrorResponse {
  status: "error";
  brevInfo: BrevInfo;
  error: AxiosError<unknown, unknown>;
}

const AttesteringContext = createContext<{
  resultat: AttesteringResponser;
  setResultat: (resultat: AttesteringResponser) => void;
}>({ resultat: [], setResultat: () => {} });

export const AttesteringResultatProvider = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<AttesteringResponser>([]);

  return (
    <AttesteringContext.Provider value={{ resultat, setResultat }}>{properties.children}</AttesteringContext.Provider>
  );
};

export const useAttesteringResultat = () => useContext(AttesteringContext);
