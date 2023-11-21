import { css } from "@emotion/react";
import { Tabs } from "@navikt/ds-react";
import { useNavigate, useSearch } from "@tanstack/react-router";

import { brevvelgerRoute } from "../../tanStackRoutes";

export enum BrevvelgerTabOptions {
  BREVMALER = "brevmaler",
  E_BLANKETTER = "e-blanketter",
}

export function BrevvelgerPage() {
  const { fane } = useSearch({ from: brevvelgerRoute.id });
  const navigate = useNavigate();

  return (
    <div
      css={css`
        padding: var(--a-spacing-6);
        background: var(--a-white);
      `}
    >
      <Tabs onChange={(value) => navigate({ search: { fane: value as BrevvelgerTabOptions } })} value={fane}>
        <Tabs.List>
          <Tabs.Tab label="Brevmaler" value={BrevvelgerTabOptions.BREVMALER} />
          <Tabs.Tab label="E-blanketter" value={BrevvelgerTabOptions.E_BLANKETTER} />
        </Tabs.List>
        <Tabs.Panel value={BrevvelgerTabOptions.BREVMALER}>Brevmaler</Tabs.Panel>
        <Tabs.Panel value={BrevvelgerTabOptions.E_BLANKETTER}>E-blanketter</Tabs.Panel>
      </Tabs>
    </div>
  );
}
