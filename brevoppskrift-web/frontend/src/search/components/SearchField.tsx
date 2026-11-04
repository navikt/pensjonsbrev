import { Search } from "@navikt/ds-react";

import { SearchActivityOutline } from "~/search/components/SearchActivityOutline";

const LABEL = "Søk etter innholdet eller brevmal";

/** The search box, with the activity outline running while `active`. */
export function SearchField({
  active,
  autoFocus,
  onChange,
  value,
}: {
  active: boolean;
  autoFocus?: boolean;
  onChange: (value: string) => void;
  value: string;
}) {
  return (
    <SearchActivityOutline active={active}>
      <Search
        autoFocus={autoFocus}
        label={LABEL}
        onChange={onChange}
        onClear={() => onChange("")}
        placeholder={LABEL}
        size="small"
        value={value}
        variant="simple"
      />
    </SearchActivityOutline>
  );
}
