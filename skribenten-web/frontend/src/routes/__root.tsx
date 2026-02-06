import { BoxNew } from "@navikt/ds-react";
import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet, useRouterState } from "@tanstack/react-router";
import React, { useEffect } from "react";

import { AppHeader } from "~/components/AppHeader";
import { trackEvent, trackPageView } from "~/utils/umami";

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

/**
 * Umami Route Tracker Component
 * Tracks page views on route changes using TanStack Router
 */
const UmamiRouteTracker = () => {
  const routerState = useRouterState();
  const pathname = routerState.location.pathname;

  useEffect(() => {
    // Track page view on route change
    trackEvent("navigasjon side lastet", {
      side: pathname,
    });
    trackPageView(pathname);
  }, [pathname]);

  return null;
};

export const Route = createRootRouteWithContext<{
  queryClient: typeof queryClient;
}>()({
  component: () => (
    <>
      {/* Umami route tracking */}
      <UmamiRouteTracker />
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
