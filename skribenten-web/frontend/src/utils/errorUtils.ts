import type { AxiosError } from "axios";

export function getErrorTitle(error: Error): string | undefined {
  const data = (error as AxiosError).response?.data;
  if (data && typeof data === "object" && "tittel" in data) {
    return (data as { tittel: string }).tittel;
  }
  return undefined;
}

export function getErrorMessage(error: Error): string {
  const data = (error as AxiosError).response?.data;
  if (typeof data === "string") {
    return data;
  } else if (data && typeof data === "object" && "melding" in data) {
    return (data as { melding: string }).melding;
  }
  return "Noe gikk galt";
}
