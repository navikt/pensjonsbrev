import { QueryClient } from "@tanstack/react-query";
import { Outlet, rootRouteWithContext, Route, Router } from "@tanstack/react-router";
import React from "react";

import { getLetterTemplate, getSak } from "./api/skribenten-api-endpoints";
import { AppHeader } from "./components/AppHeader";
import { RedigeringPage } from "./pages/Brevredigering/RedigeringPage";
import { BrevvelgerPage, BrevvelgerTabOptions } from "./pages/Brevvelger/BrevvelgerPage";
import { ChooseSakPage } from "./pages/Brevvelger/ChooseSakPage";
import { SakBreadcrumbsPage } from "./pages/Brevvelger/SakBreadcrumbsPage";
import { SelectedTemplate } from "./pages/Brevvelger/SelectedTemplate";

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
      <div>
        <AppHeader />
        <main className="page-margins">
          <Outlet />
        </main>
      </div>
    </>
  );
}

const chooseSakPageRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "saksnummer",
  component: ChooseSakPage,
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
  component: SakBreadcrumbsPage,
});

export const brevvelgerRoute = new Route({
  getParentRoute: () => sakRoute,
  path: "brevvelger",
  validateSearch: (search): { fane: BrevvelgerTabOptions } => ({
    fane:
      search.fane === BrevvelgerTabOptions.E_BLANKETTER
        ? BrevvelgerTabOptions.E_BLANKETTER
        : BrevvelgerTabOptions.BREVMALER,
  }),
  load: async ({ context: { queryClient, getSakQueryOptions } }) => {
    const { sakType } = await queryClient.ensureQueryData(getSakQueryOptions);

    const getLetterTemplateQuery = {
      queryKey: getLetterTemplate.queryKey(sakType),
      queryFn: () => getLetterTemplate.queryFn(sakType),
    };

    await queryClient.ensureQueryData(getLetterTemplateQuery);
  },
  component: BrevvelgerPage,
});

export const selectedTemplateRoute = new Route({
  getParentRoute: () => brevvelgerRoute,
  path: "$templateId",
  component: SelectedTemplate,
});

export const redigeringRoute = new Route({
  getParentRoute: () => sakRoute,
  path: "$templateId/redigering",
  component: RedigeringPage,
});

const indexRoute = new Route({
  getParentRoute: () => rootRoute,
  path: "/",
  load: ({ navigate, preload }) => {
    if (!preload) {
      navigate({ to: chooseSakPageRoute.id });
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
  chooseSakPageRoute,
  sakRoute.addChildren([brevvelgerRoute.addChildren([selectedTemplateRoute]), redigeringRoute]),
  notFoundRoute,
]);

export const router = new Router({ routeTree, defaultPreload: "intent", context: { queryClient } });

declare module "@tanstack/react-router" {
  interface Register {
    // This infers the type of our router and registers it across your entire project
    router: typeof router;
  }
}
