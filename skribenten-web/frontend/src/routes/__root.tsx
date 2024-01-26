import { QueryClient } from "@tanstack/react-query";
import { Outlet, rootRouteWithContext } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import React from "react";

import { AppHeader } from "~/components/AppHeader";

export const queryClient = new QueryClient();

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
