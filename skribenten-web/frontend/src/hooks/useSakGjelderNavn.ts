import { useQuery } from "@tanstack/react-query";
import { useSearch } from "@tanstack/react-router";

import { getSakContext } from "~/api/skribenten-api-endpoints";
import type { SakDto } from "~/types/apiTypes";
import { humanizeName } from "~/utils/stringUtils";

export const useSakGjelderNavnFormatert = ({ saksId }: { saksId: string }) => {
  const { vedtaksId } = useSearch({ strict: false });
  const sak = useQuery(getSakContext(saksId, vedtaksId));
  return sak.data?.sak ? formatName(sak.data.sak) : undefined;
};

function formatName(sak: SakDto): string {
  const navn = `${sak.navn.fornavn}${sak.navn.mellomnavn ? " " + sak.navn.mellomnavn : ""} ${sak.navn.etternavn}`;
  return humanizeName(navn);
}
