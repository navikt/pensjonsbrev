import { PlusIcon } from "@navikt/aksel-icons";
import { BoxNew, Button, Heading, Table } from "@navikt/ds-react";
import { useFieldArray, useFormContext } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { LandOption, P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyInnvilgetRow } from "./emptyP1";
import { P1InnvilgetTabRow } from "./P1InnvilgetTabRow";

export const P1InnvilgetTab = ({ landListe }: { landListe: LandOption[] }) => {
  const { control, register } = useFormContext<P1RedigerbarForm>();

  const { fields, append } = useFieldArray<P1RedigerbarForm>({
    control,
    name: "innvilgedePensjoner",
  });

  const addRow = () => append(emptyInnvilgetRow());

  return (
    <>
      <Heading size="small" spacing>
        3. Innvilget pensjon
      </Heading>

      <Table className="p1-table p1-table--zebra-stripes" css={{ minWidth: "1200px" }} size="small">
        <BoxNew asChild background="accent-soft">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>
                <span>3.1</span>
                <span>Institusjon som gir pensjonen - med PIN/saks{SOFT_HYPHEN}nummer og dato for vedtaket</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.2</span>
                <span>Pensjonstype (1), (2), (3)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.3</span>
                <span>Dato for første utbetaling</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.4</span>
                <span>
                  Brutto{SOFT_HYPHEN}beløp med betalings{SOFT_HYPHEN}hyppighet og valuta
                </span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.5</span>
                <span>Pensjonen er innvilget: (4), (5), (6)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.6</span>
                <span>Pensjonen er redusert: (7), (8)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.7</span>
                <span>Vurderings{SOFT_HYPHEN}periode (starter den datoen samlet melding ble mottatt)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>3.8</span>
                <span>Hvor kravet om ny vurdering skal sendes</span>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Header>
        </BoxNew>

        <Table.Body>
          {fields.map((field, index) => (
            <P1InnvilgetTabRow
              control={control}
              index={index}
              key={field.id}
              landListe={landListe}
              register={register}
            />
          ))}
        </Table.Body>
      </Table>

      <BoxNew asChild marginBlock="space-16 0" minWidth="fit-content" width="100%">
        <Button icon={<PlusIcon />} onClick={addRow} size="small" type="button" variant="secondary">
          Legg til ny rad
        </Button>
      </BoxNew>
    </>
  );
};
