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
            .aksel-label {
              margin-bottom: 0.25rem;
            }

            .aksel-radio-buttons {
              margin: 0 !important;
            }

            .aksel-radio__label {
              padding: var(--ax-space-8) 0;
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
