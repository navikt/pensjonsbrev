import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/templates')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/templates"!</div>
}
