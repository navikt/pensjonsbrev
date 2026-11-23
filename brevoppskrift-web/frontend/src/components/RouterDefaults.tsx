import { GlobalAlert } from "@navikt/ds-react";

import { ApiError } from "./ApiError";

export const NotFoundComponent = () => (
  <GlobalAlert size="small" status="warning">
    <GlobalAlert.Header>
      <GlobalAlert.Title>Fant ikke siden</GlobalAlert.Title>
    </GlobalAlert.Header>
    <GlobalAlert.Content>Siden du leter etter finnes ikke.</GlobalAlert.Content>
  </GlobalAlert>
);

export const DefaultErrorComponent = ({ error }: { error: unknown }) => (
  <ApiError error={error} text="Noe gikk galt." />
);
