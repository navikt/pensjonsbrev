import { createContext, useContext, useState } from "react";

import type { Nullable } from "~/types/Nullable";

const PDFViewerContext = createContext<{ height: Nullable<number>; setHeight: (n: Nullable<number>) => void }>({
  height: null,
  setHeight: () => {},
});

export const PDFViewerContextProvider = (properties: { children: React.ReactNode }) => {
  const [height, setHeight] = useState<Nullable<number>>(null);

  return (
    <PDFViewerContext.Provider value={{ height: height, setHeight: setHeight }}>
      {properties.children}
    </PDFViewerContext.Provider>
  );
};

export const usePDFViewerContext = () => useContext(PDFViewerContext);
