import { css } from "@emotion/react";
import { BodyShort, Label } from "@navikt/ds-react";

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
    <div
      css={css`
        display: flex;
        flex-direction: column;
      `}
    >
      {properties.boldedTitle ? (
        <Label size={typeof properties.size === "object" ? properties.size.label : properties.size}>
          {properties.tittel}
        </Label>
      ) : (
        <BodyShort
          css={css`
            color: var(--a-gray-600);
          `}
          size={typeof properties.size === "object" ? properties.size.bodyShort : properties.size}
        >
          {properties.tittel}
        </BodyShort>
      )}

      <BodyShort>{properties.verdi}</BodyShort>
    </div>
  );
};

export default Oppsummeringspar;
