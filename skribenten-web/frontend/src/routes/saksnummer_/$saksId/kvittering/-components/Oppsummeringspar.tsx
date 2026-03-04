import { css } from "@emotion/react";
import { BodyShort, Label, VStack } from "@navikt/ds-react";

const Oppsummeringspar = (properties: {
  tittel: string;
  verdi: string | number;
  boldedTitle?: boolean;
  size?:
    | "small"
    | "medium"
    | {
        label: "small" | "medium";
        bodyShort: "small" | "medium" | "large";
      };
}) => {
  return (
    <VStack gap="space-4">
      {properties.boldedTitle ? (
        <Label size={typeof properties.size === "object" ? properties.size.label : properties.size}>
          {properties.tittel}
        </Label>
      ) : (
        <BodyShort
          css={css`
            color: var(--ax-text-neutral);
          `}
          size={typeof properties.size === "object" ? properties.size.bodyShort : properties.size}
          weight="semibold"
        >
          {properties.tittel}
        </BodyShort>
      )}

      <BodyShort size={typeof properties.size === "object" ? properties.size.bodyShort : properties.size}>
        {properties.verdi}
      </BodyShort>
    </VStack>
  );
};

export default Oppsummeringspar;
