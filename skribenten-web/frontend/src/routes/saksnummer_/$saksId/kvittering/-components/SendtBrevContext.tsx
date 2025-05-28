import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BestillBrevError, BestillBrevResponse, BrevInfo } from "~/types/brev";

export interface BrevResult {
  status: "success" | "error";
  brevInfo: BrevInfo;
  response?: BestillBrevResponse;
  error?: AxiosError | BestillBrevError;
}

export type SendteBrevMap = Record<string, BrevResult>;

interface SendtBrevContextValue {
  sendteBrev: SendteBrevMap;
  setBrevResult: (brevId: string, result: BrevResult) => void;
  resetSendteBrev: () => void;
}

const SendtBrevContext = createContext<SendtBrevContextValue>({
  sendteBrev: {},
  setBrevResult: () => {},
  resetSendteBrev: () => {},
});

export const SendtBrevProvider = ({ children }: { children: React.ReactNode }) => {
  const [sendteBrev, setSendteBrev] = useState<SendteBrevMap>({});

  const setBrevResult = (brevId: string, result: BrevResult) =>
    setSendteBrev((current) => ({ ...current, [brevId]: result }));

  const resetSendteBrev = () => setSendteBrev({});

  return (
    <SendtBrevContext.Provider value={{ sendteBrev, setBrevResult, resetSendteBrev }}>
      {children}
    </SendtBrevContext.Provider>
  );
};

export const useSendtBrev = () => useContext(SendtBrevContext);
