import { css } from "@emotion/react";
import { Outlet, RootRoute, Route, Router } from "@tanstack/react-router";
import React from "react";

import { App } from "./App";
import { AppHeader } from "./components/AppHeader";
import { SakPage } from "./pages/Brevvelger/SakPage";
import { VelgSakPage } from "./pages/Brevvelger/VelgSakPage";

const TanStackRouterDevtools =
  process.env.NODE_ENV === "production"
    ? () => null // Render nothing in production
    : React.lazy(() =>
        // Lazy load in development
        import("@tanstack/router-devtools").then((result) => ({
          default: result.TanStackRouterDevtools,
          // For Embedded Mode
          // default: res.TanStackRouterDevtoolsPanel
        })),
      );

const rootRoute = new RootRoute({
  component: Root,
});

function Root() {
  return (
    <>
      <React.Suspense fallback="">
        <TanStackRouterDevtools position="bottom-right" />
      </React.Suspense>
      <div
        css={css`
          @media (max-width: calc(var(--largest-width + 150px))) {
            --page-margins: 75px;
          }
        `}
      >
        <AppHeader />
        <main className="page-margins">
          <Outlet />
        </main>
      </div>
    </>
  );
}

const indexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "/",
  component: App,
});

const velgSaksnummerRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "saksnummer",
  component: VelgSakPage,
});

const sakRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "saksnummer/$sakId",
  component: SakPage,
});

const notFoundRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "*",
  component: () => "Siden finnes ikke",
});

const routeTree = rootRoute.addChildren([indexRoute, velgSaksnummerRoute, sakRoute, notFoundRoute]);

export const router = new Router({ routeTree });

declare module "@tanstack/react-router" {
  interface Register {
    // This infers the type of our router and registers it across your entire project
    router: typeof router;
  }
}
