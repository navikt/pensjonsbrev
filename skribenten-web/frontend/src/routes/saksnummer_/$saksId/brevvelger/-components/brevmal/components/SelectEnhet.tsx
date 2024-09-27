import { Select } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useFormContext } from "react-hook-form";

import { getEnheter } from "~/api/skribenten-api-endpoints";

import { Route } from "../../../route";

function SelectEnhet() {
  const enheterQuery = useQuery(getEnheter);
  const { enhetsId } = Route.useSearch();
  const { register, setValue, formState } = useFormContext();
  const navigate = useNavigate({ from: Route.fullPath });
  const options = enheterQuery.data ?? [];

  if (enhetsId) {
    setValue("enhetsId", enhetsId);
  }
  return (
    <Select
      {...register("enhetsId")}
      data-cy="avsenderenhet-select"
      error={formState.errors.enhetsId?.message?.toString()}
      label="Avsenderenhet"
      onChangeCapture={(element) => {
        return navigate({
          search: (s) => ({ ...s, enhetsId: element.currentTarget.value }),
          replace: true,
        });
      }}
      size="small"
      value={enhetsId}
    >
      <option value={""}>Velg enhet</option>
      {options.map((option) => (
        <option key={option.id} value={option.id}>
          {option.navn}
        </option>
      ))}
    </Select>
  );
}

export default SelectEnhet;
