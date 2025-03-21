/* prettier-ignore-start */

/* eslint-disable */

// @ts-nocheck

// noinspection JSUnusedGlobalSymbols

// This file is auto-generated by TanStack Router

// Import Routes

import { Route as rootRoute } from './routes/__root'
import { Route as TemplatesImport } from './routes/templates'
import { Route as IndexImport } from './routes/index'
import { Route as TemplateMalTypeTemplateIdImport } from './routes/template.$malType.$templateId'

// Create/Update Routes

const TemplatesRoute = TemplatesImport.update({
  path: '/templates',
  getParentRoute: () => rootRoute,
} as any)

const IndexRoute = IndexImport.update({
  path: '/',
  getParentRoute: () => rootRoute,
} as any)

const TemplateMalTypeTemplateIdRoute = TemplateMalTypeTemplateIdImport.update({
  path: '/template/$malType/$templateId',
  getParentRoute: () => rootRoute,
} as any)

// Populate the FileRoutesByPath interface

declare module '@tanstack/react-router' {
  interface FileRoutesByPath {
    '/': {
      preLoaderRoute: typeof IndexImport
      parentRoute: typeof rootRoute
    }
    '/templates': {
      preLoaderRoute: typeof TemplatesImport
      parentRoute: typeof rootRoute
    }
    '/template/$malType/$templateId': {
      preLoaderRoute: typeof TemplateMalTypeTemplateIdImport
      parentRoute: typeof rootRoute
    }
  }
}

// Create and export the route tree

export const routeTree = rootRoute.addChildren([
  IndexRoute,
  TemplatesRoute,
  TemplateMalTypeTemplateIdRoute,
])

/* prettier-ignore-end */
