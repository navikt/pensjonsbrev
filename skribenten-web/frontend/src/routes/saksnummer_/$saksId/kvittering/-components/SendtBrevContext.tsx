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

export interface DelautomatiskBrevResult {
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
  delautomatiskBrevResults: DelautomatiskBrevResult[];
  addDelautomatiskBrevResult: (result: DelautomatiskBrevResult) => void;
}

const SendtBrevContext = createContext<SendtBrevContextValue>({
  sendteBrev: {},
  setBrevResult: () => {},
  resetSendteBrev: () => {},
  delautomatiskBrevResults: [],
  addDelautomatiskBrevResult: () => {},
});

export const SendtBrevProvider = ({ children }: { children: React.ReactNode }) => {
  const [sendteBrev, setSendteBrev] = useState<SendteBrevMap>({});
  const [delautomatiskBrevResults, setDelautomatiskBrevResults] = useState<DelautomatiskBrevResult[]>([]);

  const setBrevResult = (brevId: string, result: BrevResult) =>
    setSendteBrev((current) => ({ ...current, [brevId]: result }));

  const resetSendteBrev = () => {
    setSendteBrev({});
    setDelautomatiskBrevResults([]);
  };

  const addDelautomatiskBrevResult = (result: DelautomatiskBrevResult) =>
    setDelautomatiskBrevResults((current) => [...current, result]);

  return (
    <SendtBrevContext.Provider
      value={{ sendteBrev, setBrevResult, resetSendteBrev, delautomatiskBrevResults, addDelautomatiskBrevResult }}
    >
      {children}
    </SendtBrevContext.Provider>
  );
};

export const useSendtBrev = () => useContext(SendtBrevContext);
