import { BodyShort, HStack, Label, Tag, VStack } from "@navikt/ds-react";
import type { AkselColorRole } from "@navikt/ds-tokens/types";

export type AdresseVisningTag = {
  label: string;
  color: AkselColorRole;
};

type AdresseVisningProps = {
  tags?: AdresseVisningTag[];
  navn: string;
  adresselinjer: string[];
  withTitle?: boolean;
};

/**
 * Pure display component for rendering a recipient address.
 * All address display in the app should go through this component.
 */
const AdresseVisning = (props: AdresseVisningProps) => {
  return (
    <VStack>
      {props.withTitle && <Label size="small">Mottaker</Label>}
      {props.tags && props.tags.length > 0 && (
        <HStack>
          {props.tags.map((tag) => (
            <Tag data-color={tag.color} key={tag.label} size="xsmall" variant="strong">
              {tag.label}
            </Tag>
          ))}
        </HStack>
      )}
      <BodyShort size="small">{props.navn}</BodyShort>
      {props.adresselinjer.map((linje) =>
        linje ? (
          <BodyShort key={linje} size="small">
            {linje}
          </BodyShort>
        ) : null,
      )}
    </VStack>
  );
};

export default AdresseVisning;
