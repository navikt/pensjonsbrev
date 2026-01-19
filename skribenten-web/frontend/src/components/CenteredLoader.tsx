import { Heading, Loader, VStack } from "@navikt/ds-react";

export type CenteredLoaderProps = {
  label: string;
  verticalStrategy?: "flexGrow" | "height" | "none";
};

export const CenteredLoader = ({ label, verticalStrategy = "none" }: CenteredLoaderProps) => (
  <VStack
    align="center"
    gap="space-4"
    justify="center"
    {...(verticalStrategy === "flexGrow" ? { flexGrow: "1" } : verticalStrategy === "height" ? { height: "100%" } : {})}
  >
    <Loader size="3xlarge" title={label} />
    <Heading size="large">{label}</Heading>
  </VStack>
);
