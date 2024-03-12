import { QueryClient } from "@tanstack/react-query";
import { Outlet, rootRouteWithContext } from "@tanstack/react-router";
import React from "react";

import { AppHeader } from "~/components/AppHeader";

export const queryClient = new QueryClient();

const TanStackRouterDevtools =
  process.env.NODE_ENV === "production"
    ? () => null // Render nothing in production
    : React.lazy(() =>
        // Lazy load in development
        import("@tanstack/router-devtools").then((response) => ({
          default: response.TanStackRouterDevtools,
        })),
      );

export const Route = rootRouteWithContext<{
  queryClient: typeof queryClient;
}>()({
  component: () => (
    <>
      <React.Suspense fallback="">
        <TanStackRouterDevtools position="bottom-right" />
      </React.Suspense>
      <div>
        <AppHeader />
        <main className="page-margins">
          <Outlet />
        </main>
      </div>
    </>
  ),
});
