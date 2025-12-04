import { BoxNew } from "@navikt/ds-react";
import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import React from "react";

import { AppHeader } from "~/components/AppHeader";

export const queryClient = new QueryClient();

const isProdOrCypressTest = process.env.NODE_ENV === "production" || globalThis.Cypress !== undefined;
const { css, Global } = isProdOrCypressTest ? { css: () => "", Global: () => null } : await import("@emotion/react");
const TanStackRouterDevtools = isProdOrCypressTest
  ? () => null
  : React.lazy(() =>
      import("@tanstack/react-router-devtools").then((response) => ({
        default: response.TanStackRouterDevtools,
      })),
    );
const ReactQueryDevtools = isProdOrCypressTest
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
        {!isProdOrCypressTest && (
          <Global
            styles={css`
              .TanStackRouterDevtools > button {
                & div:nth-of-type(2),
                div:nth-of-type(3) {
                  display: none;
                }
              }
            `}
          />
        )}
        <TanStackRouterDevtools initialIsOpen={false} position="top-right" />
        <ReactQueryDevtools buttonPosition="bottom-left" initialIsOpen={false} />
      </React.Suspense>
      <BoxNew background="neutral-moderate">
        <AppHeader />
        <Outlet />
      </BoxNew>
    </>
  ),
  notFoundComponent: () => "Finner ikke siden",
});
