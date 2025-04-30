import { createContext, useContext, useState } from "react";

import type { BrevInfo } from "~/types/brev";

const BrevInfoContext = createContext<{
  brevListKlarTilAttestering: BrevInfo[];
  setBrevListKlarTilAttestering: (brevInfoList: BrevInfo[]) => void;
}>({
  brevListKlarTilAttestering: [],
  setBrevListKlarTilAttestering: () => {},
});

export const BrevInfoKlarTilAttesteringProvider = (properties: { children: React.ReactNode }) => {
  const [brevListKlarTilAttestering, setBrevListKlarTilAttestering] = useState<BrevInfo[]>([]);

  return (
    <BrevInfoContext.Provider value={{ brevListKlarTilAttestering, setBrevListKlarTilAttestering }}>
      {properties.children}
    </BrevInfoContext.Provider>
  );
};

export const useBrevInfoKlarTilAttestering = () => useContext(BrevInfoContext);
