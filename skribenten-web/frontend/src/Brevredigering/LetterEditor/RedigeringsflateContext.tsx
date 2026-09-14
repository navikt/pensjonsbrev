import { createContext, type ReactNode, useContext } from "react";

export type Redigeringsflate = "saksbehandler-redigering" | "attestant-redigering";

const RedigeringsflateContext = createContext<Redigeringsflate | null>(null);

export const RedigeringsflateProvider = (props: { children: ReactNode; redigeringsflate: Redigeringsflate }) => (
  <RedigeringsflateContext.Provider value={props.redigeringsflate}>{props.children}</RedigeringsflateContext.Provider>
);

export const useRedigeringsflate = (): Redigeringsflate => {
  const redigeringsflate = useContext(RedigeringsflateContext);
  if (!redigeringsflate) {
    throw new Error("useRedigeringsflate must be used within a <RedigeringsflateProvider>");
  }
  return redigeringsflate;
};
