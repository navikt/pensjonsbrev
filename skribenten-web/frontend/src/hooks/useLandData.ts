import { useQuery } from "@tanstack/react-query";

import { hentLandForManuellUtfyllingAvAdresse } from "~/api/skribenten-api-endpoints";

export const useLandData = () => useQuery(hentLandForManuellUtfyllingAvAdresse);
