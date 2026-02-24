import { Box, HGrid, HStack, VStack } from "@navikt/ds-react";

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <Box asChild background="default">
      <VStack flexGrow="1" justify="space-between">
        <HGrid columns="minmax(304px, 384px) auto" flexGrow="1">
          <Box
            borderColor="neutral-subtle"
            borderWidth="0 1 0 0"
            height="var(--main-page-content-height)"
            overflowY="auto"
            padding={{ xs: "space-12" }}
            paddingBlock={{ lg: "space-16" }}
            paddingInline={{ lg: "space-24" }}
          >
            {props.left}
          </Box>
          {props.right}
        </HGrid>
        <Box asChild background="default" borderColor="neutral-subtle" borderWidth="1 0 0 0">
          <HStack
            bottom="space-0"
            justify="end"
            left="space-0"
            paddingBlock="space-8"
            paddingInline="space-16"
            position="sticky"
          >
            {props.bottom}
          </HStack>
        </Box>
      </VStack>
    </Box>
  );
};

export default ThreeSectionLayout;
