import * as React from 'react'
import { Outlet, createRootRoute } from '@tanstack/react-router'
import {TanStackRouterDevtools} from "@tanstack/react-router-devtools";

export const Route = createRootRoute({
  component: RootComponent,
})
function RootComponent() {
  return (
      <>
          <React.Suspense fallback="">
              <TanStackRouterDevtools position="bottom-right" />
          </React.Suspense>
          <main className="page-margins">
              <Outlet />
          </main>
      </>
  )
}
