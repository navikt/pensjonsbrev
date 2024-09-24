import { createContext, useContext, useState } from "react";

import type { Mottaker } from "~/types/brev";
import type { Nullable } from "~/types/Nullable";

const MottakerContext = createContext<{
  mottaker: Nullable<Mottaker>;
  setMottaker: (mottaker: Nullable<Mottaker>) => void;
}>({
  mottaker: null,
  setMottaker: () => {},
});

export const MottakerContextProvider = (properties: { children: React.ReactNode }) => {
  const [mottaker, setMottaker] = useState<Nullable<Mottaker>>(null);

  return (
    <MottakerContext.Provider value={{ mottaker: mottaker, setMottaker: setMottaker }}>
      {properties.children}
    </MottakerContext.Provider>
  );
};

export const useMottakerContext = () => useContext(MottakerContext);
