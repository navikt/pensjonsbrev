import { ApiError } from "./ApiError";

export const NotFoundComponent = () => <div>"Finner ikke siden"</div>;

export const DefaultErrorComponent = ({ error }: { error: unknown }) => (
  <ApiError error={error} text="Noe gikk galt." />
);
