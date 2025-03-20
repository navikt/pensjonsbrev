import { css } from "@emotion/react";
import { Dropdown, InternalHeader } from "@navikt/ds-react";
import { Link as RouterLink } from "@tanstack/react-router";

import { useUserInfo } from "~/hooks/useUserInfo";

export function AppHeader() {
  return (
    <InternalHeader
      css={css`
        position: sticky;
        top: 0;
        z-index: var(--a-z-index-popover);
      `}
    >
      <InternalHeader.Title as="h1">
        <RouterLink
          css={css`
            color: inherit;
            text-decoration: inherit;
          `}
          params={(current) => current}
          search={(current) => current}
          to="/"
        >
          Skribenten
        </RouterLink>
      </InternalHeader.Title>
      <nav css={{ marginLeft: "auto", display: "flex" }}>
        <UserDropdown />
      </nav>
    </InternalHeader>
  );
}

function UserDropdown() {
  const userInfo = useUserInfo();

  return (
    <>
      <Dropdown>
        <InternalHeader.UserButton as={Dropdown.Toggle} name={userInfo?.name ?? ""} />
        <Dropdown.Menu>
          <Dropdown.Menu.List>
            <Dropdown.Menu.List.Item as="a" href="/bff/logout">
              Logg ut
            </Dropdown.Menu.List.Item>
          </Dropdown.Menu.List>
        </Dropdown.Menu>
      </Dropdown>
    </>
  );
}
