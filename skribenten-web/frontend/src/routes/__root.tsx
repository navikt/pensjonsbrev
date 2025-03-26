import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import React from "react";

import { AppHeader } from "~/components/AppHeader";

export const queryClient = new QueryClient();

const TanStackRouterDevtools =
  process.env.NODE_ENV === "production"
    ? () => null // Render nothing in production
    : React.lazy(() =>
        // Lazy load in development
        import("@tanstack/react-router-devtools").then((response) => ({
          default: response.TanStackRouterDevtools,
        })),
      );

export const Route = createRootRouteWithContext<{
  queryClient: typeof queryClient;
}>()({
  component: () => (
    <>
      <React.Suspense fallback="">
        <TanStackRouterDevtools position="top-right" />
      </React.Suspense>
      <div>
        <AppHeader />
        <Outlet />
      </div>
    </>
  ),
  notFoundComponent: () => "Finner ikke siden",
});
