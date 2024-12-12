import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BestillBrevResponse, BrevInfo } from "~/types/brev";

export type FerdigstillResponser = Array<FerdigstillSuccessResponse | FerdigstillErrorResponse>;
export type FerdigstillResponse = FerdigstillSuccessResponse | FerdigstillErrorResponse;

export interface FerdigstillSuccessResponse {
  status: "fulfilledWithSuccess";
  brevInfo: BrevInfo;
  response: BestillBrevResponse;
}

export interface FerdigstillErrorResponse {
  status: "fulfilledWithError";
  brevInfo: BrevInfo;
  error: AxiosError<unknown, unknown>;
}

const FerdigstillResultatContext = createContext<{
  resultat: FerdigstillResponser;
  setResultat: (resultat: FerdigstillResponser) => void;
}>({
  resultat: [],
  setResultat: () => {},
});

export const FerdigstillResultatContextProvider = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<FerdigstillResponser>([]);

  return (
    <FerdigstillResultatContext.Provider value={{ resultat: resultat, setResultat: setResultat }}>
      {properties.children}
    </FerdigstillResultatContext.Provider>
  );
};

export const useFerdigstillResultatContext = () => useContext(FerdigstillResultatContext);
