import { createContext, useContext } from "react";

import { useLandData } from "~/hooks/useLandData";

const LandDataContext = createContext<ReturnType<typeof useLandData> | null>(null);

export const LandDataProvider = ({ children }: { children: React.ReactNode }) => {
  const landDataQuery = useLandData();
  return <LandDataContext.Provider value={landDataQuery}>{children}</LandDataContext.Provider>;
};

export const useLandDataContext = () => {
  const context = useContext(LandDataContext);
  if (!context) throw new Error("useLandDataContext må brukes inni LandDataProvider");
  return context;
};
