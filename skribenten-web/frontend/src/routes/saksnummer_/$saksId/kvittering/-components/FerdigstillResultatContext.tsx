import { createContext, useContext, useState } from "react";

import type { BestillBrevResponse } from "~/types/brev";

const FerdigstillResultatContext = createContext<{
  resultat: Array<PromiseSettledResult<BestillBrevResponse>>;
  setResultat: (resultat: Array<PromiseSettledResult<BestillBrevResponse>>) => void;
}>({
  resultat: [],
  setResultat: () => {},
});

export const FerdigstillResultatContextProvider = (properties: { children: React.ReactNode }) => {
  const [resultat, setResultat] = useState<Array<PromiseSettledResult<BestillBrevResponse>>>([]);

  return (
    <FerdigstillResultatContext.Provider value={{ resultat: resultat, setResultat: setResultat }}>
      {properties.children}
    </FerdigstillResultatContext.Provider>
  );
};

export const useFerdigstillResultatContext = () => useContext(FerdigstillResultatContext);
