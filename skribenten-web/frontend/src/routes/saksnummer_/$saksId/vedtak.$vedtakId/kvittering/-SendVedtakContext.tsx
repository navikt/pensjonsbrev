import type { AxiosError } from "axios";
import { createContext, useContext, useState } from "react";

import type { BestillBrevResponse, BrevResponse } from "~/types/brev";

export type SendVedtakResponse = SendVedtakSuccessResponse | SendVedtakErrorResponse | SendVedtakInitial;

export interface SendVedtakSuccessResponse {
  status: "success";
  vedtak: BrevResponse;
  response: BestillBrevResponse;
}

export interface SendVedtakErrorResponse {
  status: "error";
  vedtak: BrevResponse;
  error: AxiosError<unknown, unknown>;
}

export interface SendVedtakInitial {
  status: "initial";
}

const SendVedtakContext = createContext<{
  response: SendVedtakResponse;
  setVedtakResponse: (vedtak: SendVedtakErrorResponse | SendVedtakSuccessResponse) => void;
}>({
  response: { status: "initial" },
  setVedtakResponse: () => {},
});

export const SendVedtakContextProvider = (properties: { children: React.ReactNode }) => {
  const [vedtakResponse, setVedtakResponse] = useState<SendVedtakResponse>({ status: "initial" });

  return (
    <SendVedtakContext.Provider value={{ response: vedtakResponse, setVedtakResponse }}>
      {properties.children}
    </SendVedtakContext.Provider>
  );
};

export const useSendVedtakContext = () => useContext(SendVedtakContext);
