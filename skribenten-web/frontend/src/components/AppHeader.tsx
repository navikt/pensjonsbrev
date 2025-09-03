import { css } from "@emotion/react";
import { BodyShort, Dropdown, InternalHeader, Link, Spacer } from "@navikt/ds-react";
import { Link as RouterLink } from "@tanstack/react-router";

import { useUserInfo } from "~/hooks/useUserInfo";
import feedbackUrl from "~/utils/feedbackUrl";

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
      <Spacer />

      <Link
        css={css`
          color: inherit;
          padding-inline: var(--a-spacing-4);
        `}
        href={feedbackUrl}
        target="_blank"
      >
        <BodyShort size="small">Gi oss en tilbakemelding på teams her</BodyShort>
      </Link>

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
