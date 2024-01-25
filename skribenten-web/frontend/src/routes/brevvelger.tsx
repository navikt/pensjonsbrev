import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/brevvelger')({
  component: () => <div>Hello /brevvelger!</div>
})