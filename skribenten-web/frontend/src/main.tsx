import "@navikt/ds-css";
import "./appStyles.css";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { createRouter, NotFoundRoute, RouterProvider } from "@tanstack/react-router";
import React from "react";
import ReactDOM from "react-dom/client";

import { ApiError } from "~/components/ApiError";

import { Route as rootRoute } from "./routes/__root";
import { routeTree } from "./routeTree.gen";

const queryClient = new QueryClient();

const notFoundRoute = new NotFoundRoute({
  getParentRoute: () => rootRoute,
  component: () => "Finner ikke siden",
});

// Set up a Router instance
const router = createRouter({
  routeTree,
  notFoundRoute,
  defaultErrorComponent: ({ error }) => <ApiError error={error} title="Noe gikk galt." />,
  context: {
    queryClient,
  },
  defaultPreload: "intent",
  // Since we're using React Query, we don't want loader calls to ever be stale
  // This will ensure that the loader is always called when the route is preloaded or visited
  defaultPreloadStaleTime: 0,
});

// Register things for typesafety
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

ReactDOM.createRoot(document.querySelector("#root") as HTMLElement).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ReactQueryDevtools buttonPosition="bottom-left" initialIsOpen={false} />
      <RouterProvider router={router} />
    </QueryClientProvider>
  </React.StrictMode>,
);
