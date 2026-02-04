import { useQuery } from "@tanstack/react-query";

import {
  hentLandForManuellUtfyllingAvAdresse,
  hentLandForManuellUtfyllingAvAdresseForP1,
} from "~/api/skribenten-api-endpoints";

export const useLandData = () => useQuery(hentLandForManuellUtfyllingAvAdresse);

export const useLandDataP1 = () => useQuery(hentLandForManuellUtfyllingAvAdresseForP1);
