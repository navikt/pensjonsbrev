import type { AxiosError } from "axios";

export function getErrorMessage(error: Error): string {
  const data = (error as AxiosError).response?.data;
  if (typeof data === "string") {
    return data;
  } else if (data && typeof data === "object" && "melding" in data) {
    return (data as { melding: string }).melding;
  }
  return "Noe gikk galt";
}
