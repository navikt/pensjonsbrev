import type { UseQueryResult } from "@tanstack/react-query";
import type { AxiosError } from "axios";

export function queryFold<TData, TError>(
  query: UseQueryResult<TData, TError>,
  initial: () => JSX.Element | null,
  pending: () => JSX.Element,
  error: (error: AxiosError) => JSX.Element,
  success: (data: TData) => JSX.Element,
): JSX.Element | null {
  if (query.isLoading) return pending();
  if (query.isError) return error(query.error as AxiosError);
  if (query.isSuccess) return success(query.data);
  return initial();
}
