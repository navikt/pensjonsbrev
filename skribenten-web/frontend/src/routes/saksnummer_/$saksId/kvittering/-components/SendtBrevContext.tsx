import { type AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import { type BestillBrevError, type BestillBrevResponse, type BrevInfo } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";

export interface BrevResult {
  status: "success" | "error";
  brevInfo: BrevInfo;
  response?: BestillBrevResponse;
  error?: AxiosError | BestillBrevError;
}

export type SendteBrevMap = Record<string, BrevResult>;

export interface DelautomatiskBrevResultat {
  status: "success" | "error";
  brevtittel: string;
  idTSSEkstern: Nullable<string>;
  journalpostId: Nullable<number>;
  error: Nullable<AxiosError | Error>;
}

interface SendtBrevContextValue {
  sendteBrev: SendteBrevMap;
  setBrevResult: (brevId: string, result: BrevResult) => void;
  resetSendteBrev: () => void;
  sendteDelautomatiskeBrev: DelautomatiskBrevResultat[];
  leggTilDelautomatiskBrevResultat: (resultat: DelautomatiskBrevResultat) => void;
}

const SendtBrevContext = createContext<SendtBrevContextValue>({
  sendteBrev: {},
  setBrevResult: () => {},
  resetSendteBrev: () => {},
  sendteDelautomatiskeBrev: [],
  leggTilDelautomatiskBrevResultat: () => {},
});

export const SendtBrevProvider = ({ children }: { children: React.ReactNode }) => {
  const [sendteBrev, setSendteBrev] = useState<SendteBrevMap>({});
  const [sendteDelautomatiskeBrev, setSendteDelautomatiskeBrev] = useState<DelautomatiskBrevResultat[]>([]);

  const setBrevResult = (brevId: string, result: BrevResult) =>
    setSendteBrev((current) => ({ ...current, [brevId]: result }));

  const resetSendteBrev = () => {
    setSendteBrev({});
    setSendteDelautomatiskeBrev([]);
  };

  const leggTilDelautomatiskBrevResultat = (resultat: DelautomatiskBrevResultat) =>
    setSendteDelautomatiskeBrev((current) => [...current, resultat]);

  return (
    <SendtBrevContext.Provider
      value={{ sendteBrev, setBrevResult, resetSendteBrev, sendteDelautomatiskeBrev, leggTilDelautomatiskBrevResultat }}
    >
      {children}
    </SendtBrevContext.Provider>
  );
};

export const useSendtBrev = () => useContext(SendtBrevContext);
