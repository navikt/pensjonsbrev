import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowLeftIcon, Buildings3Icon, PencilIcon, PersonIcon } from "@navikt/aksel-icons";
import type { SortState } from "@navikt/ds-react";
import { Alert, BodyShort, Button, Heading, Modal, Skeleton, Table, TextField, VStack } from "@navikt/ds-react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { sortBy } from "lodash";
import { useRef, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";
import { z } from "zod";

import { finnSamhandler, getKontaktAdresse, getNavn, hentSamhandlerAdresse } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { SamhandlerTypeSelectFormPart } from "~/components/select/SamhandlerSelect";
import type {
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  Samhandler,
  SamhandlerPostadresse,
} from "~/types/apiTypes";
import { SamhandlerTypeCode } from "~/types/apiTypes";
import { getAdresseTypeName, SAMHANDLER_ENUM_TO_TEXT } from "~/types/nameMappings";
import { capitalizeString } from "~/utils/stringUtils";

import { Route } from "../route";

export default function Adresse() {
  const { idTSSEkstern } = Route.useSearch();
  const { templateId } = Route.useParams();

  // Special case to hide mottaker for "Notat" & "Posteringsgrunnlag"
  if (templateId === "PE_IY_03_156" || templateId === "PE_OK_06_101") {
    return undefined;
  }

  return idTSSEkstern ? <SamhandlerAdresse /> : <PersonAdresse />;
}

function PersonAdresse() {
  const { sak } = Route.useLoaderData();

  const adresseQuery = useQuery({
    queryKey: getKontaktAdresse.queryKey(sak.saksId.toString()),
    queryFn: () => getKontaktAdresse.queryFn(sak.saksId.toString()),
  });

  const { data: navn } = useQuery({
    queryKey: getNavn.queryKey(sak.saksId.toString()),
    queryFn: () => getNavn.queryFn(sak.saksId.toString()),
    enabled: !!sak,
  });

  return (
    <div
      css={css`
        h3 {
          margin-bottom: var(--a-spacing-1);
        }

        button {
          margin-top: var(--a-spacing-2);
        }
      `}
    >
      <Heading level="3" size="xsmall">
        Mottaker
      </Heading>
      <div>
        {navn} ({getAdresseTypeName(adresseQuery.data?.type)})
      </div>
      <VStack gap="0">
        {adresseQuery.data && adresseQuery.data.adresselinjer.map((linje) => <span key={linje}>{linje}</span>)}
      </VStack>
      {adresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
      {adresseQuery.error && <ApiError error={adresseQuery.error} title="Fant ikke adresse" />}
      <VelgSamhandlerModal />
    </div>
  );
}

function SamhandlerAdresse() {
  const { idTSSEkstern } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(idTSSEkstern as string),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: idTSSEkstern as string }),
    enabled: !!idTSSEkstern,
  });

  return (
    <div
      css={css`
        h3 {
          margin-bottom: var(--a-spacing-1);
        }

        button {
          margin-top: var(--a-spacing-2);
        }
      `}
    >
      <Heading level="3" size="xsmall">
        Mottaker
      </Heading>
      {hentSamhandlerAdresseQuery.data && <FormattedSamhandlerAdresse adresse={hentSamhandlerAdresseQuery.data} />}
      {hentSamhandlerAdresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
      {hentSamhandlerAdresseQuery.error && (
        <ApiError error={hentSamhandlerAdresseQuery.error} title="Fant ikke adresse" />
      )}
      <Button
        css={css`
          width: fit-content;
        `}
        data-cy="change-to-user"
        icon={<PersonIcon />}
        onClick={() =>
          navigate({
            search: (s) => ({ ...s, idTSSEkstern: undefined }),
            replace: true,
          })
        }
        size="small"
        variant="secondary"
      >
        Endre til bruker
      </Button>
    </div>
  );
}

function FormattedSamhandlerAdresse({ adresse }: { adresse: SamhandlerPostadresse }) {
  const { land, linje1, postnr, poststed, navn } = adresse;

  return (
    <>
      <span>{navn} (Samhandler)</span>
      <VStack gap="0">
        <span>{linje1}</span>
        <span>
          {postnr} {poststed} {land === "NOR" ? "" : `, ${land}`}
        </span>
      </VStack>
    </>
  );
}

const samhandlerSearchValidationSchema = z.object({
  samhandlerType: z.nativeEnum(SamhandlerTypeCode, { required_error: "Obligatorisk" }),
  navn: z.string().min(1, "Obligatorisk"),
});

function VelgSamhandlerModal() {
  const reference = useRef<HTMLDialogElement>(null);
  const navigate = useNavigate({ from: Route.fullPath });
  const { letterTemplate } = Route.useLoaderData();
  const { idTSSEkstern } = Route.useSearch();
  const [selectedIdTSSEkstern, setSelectedIdTSSEkstern] = useState<string | undefined>(undefined);

  const methods = useForm<z.infer<typeof samhandlerSearchValidationSchema>>({
    defaultValues: {
      samhandlerType: undefined,
      navn: undefined,
    },
    resolver: zodResolver(samhandlerSearchValidationSchema),
  });

  const finnSamhandlerMutation = useMutation<
    FinnSamhandlerResponseDto,
    AxiosError<Error> | Error,
    FinnSamhandlerRequestDto
  >({
    mutationFn: async (request) => {
      return await finnSamhandler(request);
    },
  });

  if (letterTemplate.brevsystem !== "EXSTREAM") {
    return <></>;
  }

  const selectedSamhandler = finnSamhandlerMutation.data?.samhandlere?.find(
    (samhandler) => samhandler.idTSSEkstern === selectedIdTSSEkstern,
  );

  return (
    <>
      <Button
        data-cy="toggle-samhandler-button"
        icon={idTSSEkstern ? <PencilIcon /> : <Buildings3Icon />}
        onClick={() => reference.current?.showModal()}
        size="small"
        type="button"
        variant="secondary"
      >
        {idTSSEkstern ? "Endre" : "Endre til samhandler"}
      </Button>

      <Modal header={{ heading: "Finn samhandler" }} portal ref={reference} width={600}>
        <Modal.Body>
          {selectedIdTSSEkstern === undefined && (
            <FormProvider {...methods}>
              <VStack
                as="form"
                gap="4"
                id="skjema"
                method="dialog"
                onSubmit={(event) => {
                  // NOTE: It is important to stop propagation of the event here, otherwise the main form will trigger - and potentially order a letter.
                  // Though the modal is rendered outside the other form in the DOM, React still consideres them children and will propagate this submitEvent.
                  event?.stopPropagation();
                  methods.handleSubmit((values) => finnSamhandlerMutation.mutate(values))(event);
                }}
              >
                <SamhandlerTypeSelectFormPart />
                <TextField
                  autoComplete="off"
                  error={methods.formState.errors.navn?.message}
                  label="Navn"
                  {...methods.register("navn")}
                />
                <Button
                  css={css`
                    width: fit-content;
                    align-self: flex-start;
                  `}
                  form="skjema"
                  loading={finnSamhandlerMutation.isPending}
                  size="small"
                >
                  Søk
                </Button>
                {finnSamhandlerMutation.data?.samhandlere.length === 0 && (
                  <Alert size="small" variant="info">
                    Ingen treff
                  </Alert>
                )}
                {finnSamhandlerMutation.error && (
                  <ApiError error={finnSamhandlerMutation.error} title="Kunne ikke hente samhandlere." />
                )}
                <SamhandlerSearchResults
                  onSelect={(id) => {
                    setSelectedIdTSSEkstern(id);
                  }}
                  samhandlere={finnSamhandlerMutation.data?.samhandlere ?? []}
                />
              </VStack>
            </FormProvider>
          )}
          {selectedSamhandler && (
            <VStack gap="4">
              <Heading level="2" size="small">
                {SAMHANDLER_ENUM_TO_TEXT[selectedSamhandler.samhandlerType]}
              </Heading>
              <VerifySamhandler idTSSEkstern={selectedSamhandler.idTSSEkstern} />
              <Button
                css={css`
                  width: fit-content;
                  align-self: flex-start;
                `}
                icon={<ArrowLeftIcon />}
                onClick={() => setSelectedIdTSSEkstern(undefined)}
                size="small"
                variant="tertiary"
              >
                Tilbake til søk
              </Button>
            </VStack>
          )}
        </Modal.Body>
        <Modal.Footer>
          {selectedIdTSSEkstern && (
            <Button
              data-cy="bekreft-ny-mottaker"
              onClick={() => {
                reference.current?.close();
                navigate({
                  search: (s) => ({ ...s, idTSSEkstern: selectedIdTSSEkstern }),
                  replace: true,
                });
              }}
              size="small"
              variant="primary"
            >
              Bekreft ny mottaker
            </Button>
          )}
          <Button onClick={() => reference.current?.close()} size="small" type="button" variant="secondary">
            Avbryt
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

function VerifySamhandler({ idTSSEkstern }: { idTSSEkstern: string }) {
  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(idTSSEkstern),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: idTSSEkstern }),
  });

  if (hentSamhandlerAdresseQuery.isPending) {
    return (
      <VStack gap="4">
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
        <Skeleton height={30} variant="rectangle" width="100%" />
      </VStack>
    );
  }

  if (hentSamhandlerAdresseQuery.isError) {
    return <ApiError error={hentSamhandlerAdresseQuery.error} title="Fant ikke samhandleradresse" />;
  }

  return (
    <Table>
      <Table.Body>
        <InversedTableRow label="Navn" value={hentSamhandlerAdresseQuery.data.navn} />
        <InversedTableRow label="Adresselinje 1" value={hentSamhandlerAdresseQuery.data.linje1} />
        <InversedTableRow label="Adresselinje 2" value={hentSamhandlerAdresseQuery.data.linje2} />
        <InversedTableRow label="Adresselinje 3" value={hentSamhandlerAdresseQuery.data.linje3} />
        <InversedTableRow label="Postnummer" value={hentSamhandlerAdresseQuery.data.postnr} />
        <InversedTableRow label="Poststed" value={hentSamhandlerAdresseQuery.data.poststed} />
        <InversedTableRow label="Land" value={hentSamhandlerAdresseQuery.data.land} />
      </Table.Body>
    </Table>
  );
}

function InversedTableRow({ label, value }: { label: string; value?: string }) {
  if (!value) {
    return <></>;
  }

  return (
    <Table.Row>
      <Table.HeaderCell scope="row">{label}</Table.HeaderCell>
      <Table.DataCell>{capitalizeString(value)}</Table.DataCell>
    </Table.Row>
  );
}

function SamhandlerSearchResults({
  samhandlere,
  onSelect,
}: {
  samhandlere: Samhandler[];
  onSelect: (idTSSEkstern: string) => void;
}) {
  const [sort, setSort] = useState<SortState | undefined>({
    orderBy: "navn",
    direction: "ascending",
  });

  if (samhandlere.length === 0) {
    return <></>;
  }

  const handleSort = (sortKey: string | undefined) => {
    if (sortKey) {
      setSort({
        orderBy: sortKey,
        direction: sort && sortKey === sort.orderBy && sort.direction === "ascending" ? "descending" : "ascending",
      });
    } else {
      // eslint-disable-next-line unicorn/no-useless-undefined
      setSort(undefined);
    }
  };

  const sortedSamhandlereAscending = sort?.orderBy ? sortBy(samhandlere, sort.orderBy) : samhandlere;
  const reversed = sort?.direction === "descending";

  const sortedSamhandlere = reversed ? sortedSamhandlereAscending.reverse() : sortedSamhandlereAscending;

  return (
    <VStack gap="2">
      <BodyShort size="small">{sortedSamhandlere.length} treff</BodyShort>
      <Table onSortChange={(sortKey) => handleSort(sortKey)} sort={sort}>
        <Table.Header>
          <Table.Row>
            <Table.ColumnHeader colSpan={2} sortKey="navn" sortable>
              Navn
            </Table.ColumnHeader>
          </Table.Row>
        </Table.Header>
        <Table.Body>
          {sortedSamhandlere.map((samhandler) => (
            <Table.Row key={samhandler.idTSSEkstern}>
              <Table.DataCell
                css={css`
                  font-weight: var(--a-font-weight-regular);
                `}
                scope="row"
              >
                {capitalizeString(samhandler.navn)}
              </Table.DataCell>
              <Table.DataCell align="right">
                <Button
                  data-cy="velg-samhandler"
                  onClick={() => onSelect(samhandler.idTSSEkstern)}
                  size="small"
                  type="button"
                  variant="secondary-neutral"
                >
                  Velg
                </Button>
              </Table.DataCell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </VStack>
  );
}
