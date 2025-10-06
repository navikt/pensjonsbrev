import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import React from "react";

import { AppHeader } from "~/components/AppHeader";

export const queryClient = new QueryClient();

const TanStackRouterDevtools =
  process.env.NODE_ENV === "production" || globalThis.Cypress !== undefined
    ? () => null // Render nothing in production
    : React.lazy(() =>
        // Lazy load in development
        import("@tanstack/react-router-devtools").then((response) => ({
          default: response.TanStackRouterDevtools,
        })),
      );

const ReactQueryDevtools =
  process.env.NODE_ENV === "production" || globalThis.Cypress !== undefined
    ? () => null
    : React.lazy(() =>
        import("@tanstack/react-query-devtools").then((response) => ({
          default: response.ReactQueryDevtools,
        })),
      );

export const Route = createRootRouteWithContext<{
  queryClient: typeof queryClient;
}>()({
  component: () => (
    <>
      <React.Suspense fallback="">
        <TanStackRouterDevtools initialIsOpen={false} position="top-right" />
        <ReactQueryDevtools buttonPosition="bottom-left" initialIsOpen={false} />
      </React.Suspense>
      <div>
        <AppHeader />
        <Outlet />
      </div>
    </>
  ),
  notFoundComponent: () => "Finner ikke siden",
});
