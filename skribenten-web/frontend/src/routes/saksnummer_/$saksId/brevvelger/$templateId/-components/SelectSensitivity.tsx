import { css } from "@emotion/react";
import { Radio, RadioGroup } from "@navikt/ds-react";
import { Controller } from "react-hook-form";

function SelectSensitivity() {
  return (
    <Controller
      name="isSensitive"
      render={({ field, fieldState }) => (
        <RadioGroup
          data-cy="is-sensitive"
          legend="Er brevet sensitivt?"
          {...field}
          css={css`
            .navds-label {
              margin-bottom: 0.25rem;
            }

            .navds-radio-buttons {
              margin: 0 !important;
            }

            .navds-radio__label {
              padding: var(--a-spacing-2) 0;
            }
          `}
          error={fieldState.error?.message}
          size="small"
          value={field.value ?? null}
        >
          <Radio value>Ja</Radio>
          <Radio value={false}>Nei</Radio>
        </RadioGroup>
      )}
    />
  );
}

export default SelectSensitivity;
