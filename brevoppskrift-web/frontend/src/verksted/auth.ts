import { type QueryClient } from "@tanstack/react-query";
import { redirect } from "@tanstack/react-router";

import { ApiProblem, workshopApi, workshopKeys } from "./api";

export function workshopLoginUrl(href = globalThis.location.href): string {
  const url = new URL(href, globalThis.location.origin);
  const workshopPath =
    url.pathname === "/opprett" || url.pathname === "/arbeidsutkast" || url.pathname.startsWith("/arbeidsutkast/");
  const destination =
    url.origin === globalThis.location.origin && workshopPath
      ? `${url.pathname}${url.search}${url.hash}`
      : "/arbeidsutkast";
  return `/oauth2/login?redirect=${encodeURIComponent(destination)}`;
}

export async function requireWorkshopLogin({
  preload,
  context,
  location,
}: {
  preload: boolean;
  context: { queryClient: QueryClient };
  location: { href: string };
}): Promise<void> {
  if (preload) return;
  try {
    await context.queryClient.fetchQuery({
      queryKey: workshopKeys.me,
      queryFn: workshopApi.me,
      retry: false,
      staleTime: 0,
    });
  } catch (error) {
    if (error instanceof ApiProblem && error.status === 401) {
      throw redirect({ href: workshopLoginUrl(location.href), reloadDocument: true });
    }
    // Other failures are displayed by the workshop pages with their existing retry action.
  }
}
