import { Search } from "@navikt/ds-react";

import { SearchActivityOutline } from "~/search/components/SearchActivityOutline";

/** The search box, with the activity outline running while `active`. The label is also the placeholder. */
export function SearchField({
  active,
  autoFocus,
  label,
  onChange,
  value,
}: {
  active: boolean;
  autoFocus?: boolean;
  label: string;
  onChange: (value: string) => void;
  value: string;
}) {
  return (
    <SearchActivityOutline active={active}>
      <Search
        autoFocus={autoFocus}
        label={label}
        onChange={onChange}
        onClear={() => onChange("")}
        placeholder={label}
        size="small"
        value={value}
        variant="simple"
      />
    </SearchActivityOutline>
  );
}
