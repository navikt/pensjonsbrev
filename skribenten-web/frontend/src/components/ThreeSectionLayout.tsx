import { Box, HGrid, HStack, VStack } from "@navikt/ds-react";

import { VerticalDivider } from "./Divider";

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <Box asChild background="default">
      <VStack justify="space-between">
        <HGrid columns="minmax(304px, 384px) 1px auto" height="var(--main-page-content-height)" overflowY="hidden">
          <Box
            height="var(--main-page-content-height)"
            overflowY="auto"
            padding={{ xs: "space-12" }}
            paddingBlock={{ lg: "space-16" }}
            paddingInline={{ lg: "space-24" }}
          >
            {props.left}
          </Box>
          <VerticalDivider />
          {props.right}
        </HGrid>
        <Box
          asChild
          background="default"
          borderColor="neutral-subtle"
          borderWidth="1 0 0 0"
          height="var(--nav-bar-height)"
        >
          <HStack justify="end" paddingBlock="space-8" paddingInline="space-16">
            {props.bottom}
          </HStack>
        </Box>
      </VStack>
    </Box>
  );
};

export default ThreeSectionLayout;
