import { BoxNew, HGrid, HStack, VStack } from "@navikt/ds-react";

const ThreeSectionLayout = (props: { left: React.ReactNode; right: React.ReactNode; bottom: React.ReactNode }) => {
  return (
    <BoxNew asChild background="default">
      <VStack flexGrow="1" justify="space-between">
        <HGrid columns="minmax(304px, 384px) auto" flexGrow="1">
          <BoxNew
            borderColor="neutral-subtle"
            borderWidth="0 1 0 0"
            padding={{ xs: "space-12" }}
            paddingBlock={{ lg: "space-16" }}
            paddingInline={{ lg: "space-24" }}
          >
            {props.left}
          </BoxNew>
          {props.right}
        </HGrid>
        <BoxNew asChild background="default" borderColor="neutral-subtle" borderWidth="1 0 0 0">
          <HStack bottom="0" justify="end" left="0" paddingBlock="space-8" paddingInline="space-16" position="sticky">
            {props.bottom}
          </HStack>
        </BoxNew>
      </VStack>
    </BoxNew>
  );
};

export default ThreeSectionLayout;
