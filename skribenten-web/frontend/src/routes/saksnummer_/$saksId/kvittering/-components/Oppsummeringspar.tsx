import { css } from "@emotion/react";
import { BodyShort, Label } from "@navikt/ds-react";

const Oppsummeringspar = (properties: { tittel: string; verdi: string | number; boldedTitle: boolean }) => {
  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
      `}
    >
      {properties.boldedTitle ? (
        <Label>{properties.tittel}</Label>
      ) : (
        <BodyShort
          css={css`
            color: var(--a-gray-600);
          `}
        >
          {properties.tittel}
        </BodyShort>
      )}

      <BodyShort>{properties.verdi}</BodyShort>
    </div>
  );
};

export default Oppsummeringspar;
