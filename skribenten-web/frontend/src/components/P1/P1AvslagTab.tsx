import { PlusIcon } from "@navikt/aksel-icons";
import { BoxNew, Button, Heading, Table } from "@navikt/ds-react";
import { useFieldArray, useFormContext } from "react-hook-form";

import { SOFT_HYPHEN } from "~/Brevredigering/LetterEditor/model/utils";
import type { LandOption, P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyAvslaattRow } from "./emptyP1";
import { P1AvslagTabRow } from "./P1AvslagTabRow";

export const P1AvslagTab = ({ landListe }: { landListe: LandOption[] }) => {
  const { control, register } = useFormContext<P1RedigerbarForm>();

  const { fields, append } = useFieldArray<P1RedigerbarForm>({
    control,
    name: "avslaattePensjoner",
  });

  const addRow = () => append(emptyAvslaattRow());

  return (
    <>
      <Heading size="small" spacing>
        4. Avslått pensjon
      </Heading>

      <Table className="p1-table p1-table--zebra-stripes" css={{ minWidth: "988px" }} size="small">
        <BoxNew asChild background="accent-soft">
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>
                <span>4.1</span>
                <span>Institusjon som har avslått søknaden - med PIN/saksnummer og dato for vedtaket</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.2</span>
                <span>Pensjonstype (1), (2), (3)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.3</span>
                <span>Begrunnelse for avslag (4), (5), (6), (7), (8), (9), (10)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.4</span>
                <span>Vurderings{SOFT_HYPHEN}periode (starter den datoen samlet melding ble mottatt)</span>
              </Table.HeaderCell>
              <Table.HeaderCell>
                <span>4.5</span>
                <span>Hvor kravet om ny vurdering skal sendes</span>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Header>
        </BoxNew>

        <Table.Body>
          {fields.map((field, index) => (
            <P1AvslagTabRow control={control} index={index} key={field.id} landListe={landListe} register={register} />
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
