import { css } from "@emotion/react";
import { BodyShort } from "@navikt/ds-react";

const Oppsummeringspar = (properties: { tittel: string; verdi: string | number }) => {
  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
      `}
    >
      <BodyShort
        css={css`
          color: var(--a-gray-600);
        `}
      >
        {properties.tittel}
      </BodyShort>
      <BodyShort>{properties.verdi}</BodyShort>
    </div>
  );
};

export default Oppsummeringspar;
