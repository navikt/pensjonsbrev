import { css } from "@emotion/react";
import { QueryClient } from "@tanstack/react-query";
import { Outlet, rootRouteWithContext, Route, Router } from "@tanstack/react-router";
import React from "react";

import { getLetterTemplate, getSak } from "./api/skribenten-api-endpoints";
import { AppHeader } from "./components/AppHeader";
import { BrevvelgerPage, BrevvelgerTabOptions } from "./pages/Brevvelger/BrevvelgerPage";
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

export const queryClient = new QueryClient();

const rootRoute = rootRouteWithContext<{
  queryClient: typeof queryClient;
}>()({
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
          @media (width <= 1700px) {
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

const velgSaksnummerRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "saksnummer",
  component: VelgSakPage,
});

export const sakRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "saksnummer/$sakId",
  beforeLoad: ({ params: { sakId } }) => {
    const getSakQueryOptions = {
      queryKey: getSak.queryKey(sakId),
      queryFn: () => getSak.queryFn(sakId),
    };

    return { getSakQueryOptions };
  },
  load: async ({ context: { queryClient, getSakQueryOptions } }) => {
    await queryClient.ensureQueryData(getSakQueryOptions);
  },
  component: SakPage,
});

export const brevvelgerRoute = new Route({
  getParentRoute: () => sakRoute,
  path: "brevvelger",
  load: async ({ context: { queryClient, getSakQueryOptions } }) => {
    const { sakType } = await queryClient.ensureQueryData(getSakQueryOptions);
    await queryClient.ensureQueryData(getSakQueryOptions);

    const getLetterTemplateQuery = {
      queryKey: getLetterTemplate.queryKey(sakType),
      queryFn: () => getLetterTemplate.queryFn(sakType),
    };

    await queryClient.ensureQueryData(getLetterTemplateQuery);
  },
  validateSearch: (search): { fane: BrevvelgerTabOptions } => ({
    fane:
      search.fane === BrevvelgerTabOptions.E_BLANKETTER
        ? BrevvelgerTabOptions.E_BLANKETTER
        : BrevvelgerTabOptions.BREVMALER,
  }),
  component: BrevvelgerPage,
});

const indexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "/",
  load: ({ navigate, preload }) => {
    if (!preload) {
      navigate({ to: velgSaksnummerRoute.id });
    }
  },
  component: undefined,
});

const notFoundRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "*",
  component: () => "Siden finnes ikke",
});

const routeTree = rootRoute.addChildren([
  indexRoute,
  velgSaksnummerRoute,
  sakRoute.addChildren([brevvelgerRoute]),
  notFoundRoute,
]);

export const router = new Router({ routeTree, defaultPreload: "intent", context: { queryClient } });

declare module "@tanstack/react-router" {
  interface Register {
    // This infers the type of our router and registers it across your entire project
    router: typeof router;
  }
}
