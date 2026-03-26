import { LeaveIcon } from "@navikt/aksel-icons";
import { ActionMenu, Box, InternalHeader, Link, Spacer } from "@navikt/ds-react";
import { Link as RouterLink } from "@tanstack/react-router";

import { useUserInfo } from "~/hooks/useUserInfo";

export function AppHeader() {
  return (
    <Box asChild borderColor="neutral-subtleA" borderWidth="0 0 1 0" height="var(--header-height)">
      <InternalHeader>
        <InternalHeader.Title as="h1">
          <RouterLink
            css={{ color: "inherit", textDecoration: "inherit" }}
            params={(current) => current}
            search={(current) => current}
            to="/"
          >
            Skribenten
          </RouterLink>
        </InternalHeader.Title>
        <Spacer />
        <Link
          css={{ marginRight: "var(--ax-space-16)" }}
          data-color="neutral"
          href="https://navikt.github.io/pensjonsbrev/docs/main/index.html"
          rel="noopener noreferrer"
          target="_blank"
        >
          Dokumentasjon og veiledning
        </Link>
        <UserDropdown />
      </InternalHeader>
    </Box>
  );
}

function UserDropdown() {
  const userInfo = useUserInfo();

  return (
    <ActionMenu>
      <ActionMenu.Trigger>
        <InternalHeader.UserButton
          css={{ svg: { width: "24px", height: "24px" } }}
          description={userInfo?.navident ? `id: ${userInfo.navident}` : ""}
          name={userInfo?.name ?? ""}
        />
      </ActionMenu.Trigger>
      <ActionMenu.Content align="end">
        <ActionMenu.Group aria-label="Handlinger">
          <ActionMenu.Item as="a" href="/bff/logout">
            Logg ut <Spacer /> <LeaveIcon aria-hidden fontSize="1.5rem" />
          </ActionMenu.Item>
        </ActionMenu.Group>
      </ActionMenu.Content>
    </ActionMenu>
  );
}
