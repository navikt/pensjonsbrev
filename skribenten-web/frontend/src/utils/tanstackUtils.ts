import { type UseQueryResult } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { type JSX } from "react";

declare module "@tanstack/react-query" {
  interface Register {
    defaultError: AxiosError;
  }
}

export function queryFold<TData>(args: {
  query: UseQueryResult<TData, AxiosError>;
  initial: () => JSX.Element | null;
  pending: () => JSX.Element;
  retrying?: (failureCount: number, failureReason: AxiosError) => JSX.Element;
  error: (error: AxiosError) => JSX.Element;
  success: (data: TData) => JSX.Element;
}): JSX.Element | null {
  if (args.query.isLoading) {
    if (args.retrying && args.query.failureCount > 0 && args.query.failureReason) {
      return args.retrying(args.query.failureCount, args.query.failureReason);
    }
    return args.pending();
  }
  if (args.query.isError) return args.error(args.query.error);
  if (args.query.isSuccess) return args.success(args.query.data);
  return args.initial();
}
