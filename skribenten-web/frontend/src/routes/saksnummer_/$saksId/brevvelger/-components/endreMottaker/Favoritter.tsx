import { StarIcon } from "@navikt/aksel-icons";
import { StarFillIcon } from "@navikt/aksel-icons";
import { Alert, Button, Table, Tag, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";

import { getMottakerFavoritter } from "~/api/me-endpoints";
import { getKontaktAdresse, getNavn } from "~/api/skribenten-api-endpoints";
import type { SamhandlerTypeCode } from "~/types/apiTypes";
import { SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";
import { humanizeName } from "~/utils/stringUtils";

import { Route } from "../../route";

export interface FavorittSamhandlerMottaker {
  navn: string;
  type: SamhandlerTypeCode;
  id: string;
}

const Favoritter = () => {
  const { saksId } = Route.useParams();
  //TODO - finn ut om alerten skal være en one-time alert eller ikke
  const [visAlert, setVisAlert] = useState(true);

  const mottakerFavoritter = useQuery({
    queryKey: ["mottakerFavoritter"],
    queryFn: () => getMottakerFavoritter(),
  });

  return (
    <VStack gap="4">
      {visAlert && (
        <Alert closeButton onClose={() => setVisAlert(false)} size="small" variant="info">
          Favoritter lagres på deg, ikke på bruker.
        </Alert>
      )}

      <Table size="small">
        <Table.Body>
          <BrukerVergeRow saksId={saksId} />

          {mottakerFavoritter.data?.map((samhandler) => <SamhandlerRow key={samhandler.id} samhandler={samhandler} />)}
        </Table.Body>
      </Table>
    </VStack>
  );
};

const BrukerVergeRow = (props: { saksId: string }) => {
  const { fødselsnummer } = Route.useLoaderData();
  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(fødselsnummer),
    queryFn: () => getNavn.queryFn(props.saksId),
  });
  const kontaktAdresse = useQuery({
    queryKey: getKontaktAdresse.queryKey(props.saksId),
    queryFn: () => getKontaktAdresse.queryFn(props.saksId),
  });

  const isVerge =
    kontaktAdresse.data?.type === "VERGE_PERSON_POSTADRESSE" ||
    kontaktAdresse.data?.type === "VERGE_SAMHANDLER_POSTADRESSE";

  return (
    <div>
      {kontaktAdresse.isError && (
        <Alert size="small" variant="error">
          Kunne ikke hente bruker/verge
        </Alert>
      )}
      {kontaktAdresse.isSuccess &&
        (isVerge ? (
          <Table.Row>
            <Table.DataCell>
              <Button
                disabled
                icon={<StarIcon fontSize="1.5rem" title="Stjerne-ikon - Kan ikke fjerne bruker fra favoritter" />}
                size="small"
                variant="tertiary-neutral"
              />
            </Table.DataCell>
            {/* TODO - hva gjør vi med verge navnet? */}
            <Table.DataCell></Table.DataCell>
            <Table.DataCell>
              <DataCellTag type="verge" />
            </Table.DataCell>
            <Table.DataCell>
              <VelgKnapp onClick={() => console.log("velger verge")} />
            </Table.DataCell>
          </Table.Row>
        ) : (
          <Table.Row>
            <Table.DataCell>
              <Button
                disabled
                icon={<StarIcon fontSize="1.5rem" title="Stjerne-ikon - Kan ikke fjerne bruker fra favoritter" />}
                size="small"
                variant="tertiary-neutral"
              />
            </Table.DataCell>

            <Table.DataCell>{navn && humanizeName(navn)}</Table.DataCell>
            <Table.DataCell>
              <DataCellTag type="bruker" />
            </Table.DataCell>
            <Table.DataCell>
              <VelgKnapp onClick={() => console.log("velger verge")} />
            </Table.DataCell>
          </Table.Row>
        ))}
    </div>
  );
};

const SamhandlerRow = (props: { samhandler: FavorittSamhandlerMottaker }) => {
  return (
    <Table.Row>
      <Table.DataCell>
        <Button
          icon={<StarFillIcon fontSize="1.5rem" title="Stjerne-ikon - samhandler er lagt til som favoritt" />}
          onClick={() => console.log(`fjernet samhandler med id ${props.samhandler.id} fra favoritter`)}
          size="small"
          variant="tertiary-neutral"
        />
      </Table.DataCell>
      <Table.DataCell>{props.samhandler.navn}</Table.DataCell>
      <Table.DataCell>
        <DataCellTag type={{ samhandlerKode: props.samhandler.type }} />
      </Table.DataCell>
      <Table.DataCell>
        <VelgKnapp onClick={() => console.log(`valgt samhandler med id ${props.samhandler.id} `)} />
      </Table.DataCell>
    </Table.Row>
  );
};

const VelgKnapp = (props: { onClick: () => void }) => {
  return (
    <Button onClick={props.onClick} size="small" variant="secondary-neutral">
      Velg
    </Button>
  );
};

const DataCellTag = (props: { type: "bruker" | "verge" | { samhandlerKode: SamhandlerTypeCode } }) => {
  if (typeof props.type === "object") {
    return (
      <Tag size="small" variant="alt2">
        {SAMHANDLER_ENUM_TO_TEXT[props.type.samhandlerKode]}
      </Tag>
    );
  } else {
    switch (props.type) {
      case "bruker": {
        return (
          <Tag size="small" variant="alt1">
            Bruker
          </Tag>
        );
      }
      case "verge": {
        return (
          <Tag size="small" variant="alt3">
            Verge
          </Tag>
        );
      }
    }
  }
};

export default Favoritter;
