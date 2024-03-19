import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  ArrowLeftIcon,
  ArrowRightIcon,
  Buildings3Icon,
  PencilIcon,
  PersonIcon,
  StarFillIcon,
  StarIcon,
} from "@navikt/aksel-icons";
import type { SortState } from "@navikt/ds-react";
import { Skeleton } from "@navikt/ds-react";
import {
  Alert,
  BodyShort,
  Button,
  Heading,
  Link,
  Modal,
  Radio,
  RadioGroup,
  Select,
  Table,
  Tag,
  TextField,
  VStack,
} from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, notFound, useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { sortBy } from "lodash";
import React, { useEffect, useRef, useState } from "react";
import { Controller, FormProvider, useForm, useFormContext } from "react-hook-form";
import { z } from "zod";

import {
  addFavoritt,
  deleteFavoritt,
  finnSamhandler,
  getAvtaleLand,
  getFavoritter,
  getKontaktAdresse,
  getLetterTemplate,
  getNavn,
  hentSamhandlerAdresse,
  orderDoksysLetter,
  orderEblankett,
  orderExstreamLetter,
} from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import { SamhandlerTypeSelectFormPart } from "~/components/select/SamhandlerSelect";
import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import type {
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  LetterMetadata,
  OrderDoksysLetterRequest,
  OrderEblankettRequest,
  OrderExstreamLetterRequest,
  Samhandler,
  SamhandlerPostadresse,
} from "~/types/apiTypes";
import { BrevSystem, SamhandlerTypeCode, SpraakKode } from "~/types/apiTypes";
import { getAdresseTypeName, SAMHANDLER_ENUM_TO_TEXT, SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";
import { capitalizeString } from "~/utils/stringUtils";

export const Route = createFileRoute("/saksnummer/$saksId/brevvelger/$templateId")({
  component: SelectedTemplate,
  loaderDeps: ({ search: { vedtaksId } }) => ({ vedtaksId }),
  validateSearch: (search: Record<string, unknown>): { idTSSEkstern?: string } => ({
    idTSSEkstern: search.idTSSEkstern?.toString(),
  }),
  loader: async ({ context: { queryClient, getSakQueryOptions }, params: { templateId }, deps: { vedtaksId } }) => {
    const sak = await queryClient.ensureQueryData(getSakQueryOptions);

    const letterTemplates = await queryClient.ensureQueryData({
      queryKey: getLetterTemplate.queryKey({ sakType: sak.sakType, vedtaksId }),
      queryFn: () => getLetterTemplate.queryFn(sak.sakType, { vedtaksId }),
    });

    const letterTemplate = letterTemplates.find((letterMetadata) => letterMetadata.id === templateId);

    if (!letterTemplate) {
      throw notFound();
    }

    return { letterTemplate, sak };
  },
  notFoundComponent: () => {
    // eslint-disable-next-line react-hooks/rules-of-hooks -- this works and is used as an example in the documentation: https://tanstack.com/router/latest/docs/framework/react/guide/not-found-errors#data-loading-inside-notfoundcomponent
    const { templateId } = Route.useParams();
    return (
      <Alert
        css={css`
          height: fit-content;
        `}
        size="small"
        variant="info"
      >
        Fant ikke brevmal med id {templateId}
      </Alert>
    );
  },
});

export function SelectedTemplate() {
  const { letterTemplate } = Route.useLoaderData();

  return (
    <VStack
      css={css`
        /* Override form elements to be same size as xsmall headings */
        label,
        legend {
          font-size: var(--a-font-size-heading-xsmall);
        }

        form {
          display: flex;
          flex-direction: column;
          height: 100%;
          justify-content: space-between;
        }
      `}
      gap="4"
    >
      <FavoriteButton />
      <Brevmal letterTemplate={letterTemplate} />
    </VStack>
  );
}

function Brevmal({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  if (letterTemplate.dokumentkategoriCode === "E_BLANKETT") {
    return <Eblankett letterTemplate={letterTemplate} />;
  }

  switch (letterTemplate.brevsystem) {
    case BrevSystem.DokSys: {
      return <BrevmalForDoksys letterTemplate={letterTemplate} />;
    }
    case BrevSystem.Exstream: {
      return <BrevmalForExstream letterTemplate={letterTemplate} />;
    }
    case BrevSystem.Brevbaker: {
      return <div>TODO</div>;
    }
  }
}

const baseOrderLetterValidationSchema = z.object({
  spraak: z.nativeEnum(SpraakKode, { required_error: "Obligatorisk" }),
});

const exstreamOrderLetterValidationSchema = baseOrderLetterValidationSchema.extend({
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
  brevtittel: z.string().optional(),
});

const exstreamWithTitleOrderLetterValidationSchema = exstreamOrderLetterValidationSchema.extend({
  brevtittel: z.string().min(1, "Du må ha tittel for dette brevet"),
});

const eblankettValidationSchema = z.object({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Vennligst fyll inn mottaker"),
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
});

function BrevmalForExstream({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId, idTSSEkstern } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderExstreamLetterRequest>({
    mutationFn: (payload) => orderExstreamLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const validationSchema = letterTemplate.redigerbarBrevtittel
    ? exstreamWithTitleOrderLetterValidationSchema
    : exstreamOrderLetterValidationSchema;

  const methods = useForm<z.infer<typeof validationSchema>>({
    defaultValues: {
      isSensitive: undefined,
      brevtittel: "",
    },
    resolver: zodResolver(validationSchema),
  });

  const { reset: resetMutation } = orderLetterMutation;
  const { reset: resetForm } = methods;
  useEffect(() => {
    resetForm();
    resetMutation();
  }, [templateId, resetMutation, resetForm]);

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              vedtaksId,
              idTSSEkstern,
              ...submittedValues,
            };
            return orderLetterMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="8">
            <Adresse />
            {letterTemplate.redigerbarBrevtittel ? (
              <TextField
                {...methods.register("brevtittel")}
                autoComplete="on"
                description="Gi brevet en kort og forklarende tittel."
                error={methods.formState.errors.brevtittel?.message}
                label="Endre tittel"
                size="medium"
              />
            ) : undefined}
            <SelectLanguage letterTemplate={letterTemplate} />
            <SelectSensitivity />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}

function BrevmalForDoksys({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, saksId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderDoksysLetterRequest>({
    mutationFn: (payload) => orderDoksysLetter(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const { reset } = orderLetterMutation;
  useEffect(() => {
    reset();
  }, [templateId, reset]);

  const methods = useForm<z.infer<typeof baseOrderLetterValidationSchema>>({
    resolver: zodResolver(baseOrderLetterValidationSchema),
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Divider />
      <Adresse />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              vedtaksId,
              ...submittedValues,
            };
            return orderLetterMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="4">
            <SelectLanguage letterTemplate={letterTemplate} />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}

function Eblankett({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { saksId } = Route.useParams();

  const { vedtaksId } = Route.useSearch();

  const methods = useForm<z.infer<typeof eblankettValidationSchema>>({
    defaultValues: {
      landkode: "",
      mottakerText: "",
    },
    resolver: zodResolver(eblankettValidationSchema),
  });

  const orderEblankettMutation = useMutation<string, AxiosError<Error> | Error, OrderEblankettRequest>({
    mutationFn: (payload) => orderEblankett(saksId, payload),
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
      <FormProvider {...methods}>
        <form
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              vedtaksId,
              ...submittedValues,
            };
            return orderEblankettMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="4">
            <SelectSensitivity />
            <SelectAvtaleland />
            <TextField
              {...methods.register("mottakerText")}
              autoComplete="off"
              error={methods.formState.errors.mottakerText?.message}
              label="Mottaker"
              size="small"
            />
          </VStack>
          <BestillOgRedigerButton orderMutation={orderEblankettMutation} />
        </form>
      </FormProvider>
    </>
  );
}

function BestillOgRedigerButton({
  orderMutation,
}: {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any -- request type is not relevant for this component
  orderMutation: UseMutationResult<string, AxiosError<Error> | Error, any>;
}) {
  return (
    <VStack gap="4">
      {orderMutation.error && <ApiError error={orderMutation.error} title="Bestilling feilet" />}
      {orderMutation.isSuccess ? (
        <Alert size="small" variant="success">
          <Heading level="3" size="xsmall">
            Brev bestilt
          </Heading>
          <span>
            Åpnet ikke brevet seg? <Link href={orderMutation.data}>Klikk her for å prøve igjen</Link>
          </span>
        </Alert>
      ) : (
        <Button
          css={css`
            width: fit-content;
          `}
          icon={<ArrowRightIcon />}
          iconPosition="right"
          loading={orderMutation.isPending}
          size="small"
          type="submit"
          variant="primary"
        >
          Åpne brev
        </Button>
      )}
    </VStack>
  );
}

function SelectSensitivity() {
  return (
    <Controller
      name="isSensitive"
      render={({ field, fieldState }) => (
        <RadioGroup
          legend="Er brevet sensitivt?"
          {...field}
          css={css`
            .navds-radio-buttons {
              margin: 0 !important;
            }

            .navds-radio__label {
              padding: var(--a-spacing-2) 0;
            }
          `}
          error={fieldState.error?.message}
          size="medium"
          value={field.value ?? null}
        >
          <Radio value>Ja</Radio>
          <Radio value={false}>Nei</Radio>
        </RadioGroup>
      )}
    />
  );
}

function SelectLanguage({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { saksId } = Route.useParams();
  const { register, setValue } = useFormContext();
  const preferredLanguage = usePreferredLanguage(saksId);

  // Update selected language if preferredLanguage was not loaded before form initialization.
  useEffect(() => {
    if (preferredLanguage && letterTemplate.spraak.includes(preferredLanguage)) {
      setValue("spraak", preferredLanguage);
    }
  }, [preferredLanguage, setValue, letterTemplate.spraak]);

  return (
    <Select {...register("spraak")} label="Språk" size="medium">
      {letterTemplate.spraak.map((spraak) => (
        <option key={spraak} value={spraak}>
          {SPRAAK_ENUM_TO_TEXT[spraak]} {preferredLanguage === spraak ? "(foretrukket språk)" : ""}
        </option>
      ))}
    </Select>
  );
}

function SelectAvtaleland() {
  const avtalelandQuery = useQuery(getAvtaleLand);
  const { register, formState } = useFormContext();

  const options = avtalelandQuery.data ?? [];

  return (
    <Select {...register("landkode")} error={formState.errors.landkode?.message?.toString()} label="Land" size="small">
      <option value={""}>Velg land</option>
      {options.map((option) => (
        <option key={option.kode} value={option.kode}>
          {option.navn}
        </option>
      ))}
    </Select>
  );
}

function LetterTemplateHeading({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      <Heading level="2" size="medium">
        {letterTemplate.name}
      </Heading>
      <div
        css={css`
          display: flex;
          align-items: center;
          gap: var(--a-spacing-3);
          margin-top: var(--a-spacing-2);
        `}
      >
        <LetterTemplateTags letterTemplate={letterTemplate} />
      </div>
    </div>
  );
}

function LetterTemplateTags({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <div>
      {(() => {
        switch (letterTemplate.brevsystem) {
          case BrevSystem.Brevbaker: {
            return (
              <Tag size="small" variant="alt2-moderate">
                Brevbaker
              </Tag>
            );
          }
          case BrevSystem.Exstream: {
            return (
              <Tag size="small" variant="alt1-moderate">
                Exstream
              </Tag>
            );
          }
          case BrevSystem.DokSys: {
            return (
              <Tag size="small" variant="alt3-moderate">
                Doksys
              </Tag>
            );
          }
        }
      })()}
    </div>
  );
}

function Adresse() {
  const { idTSSEkstern } = Route.useSearch();

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

function FavoriteButton() {
  const { templateId } = Route.useParams();
  const queryClient = useQueryClient();
  const isFavoritt = useQuery({
    ...getFavoritter,
    select: (favoritter) => favoritter.includes(templateId),
  }).data;

  const toggleFavoritesMutation = useMutation<unknown, unknown, string>({
    mutationFn: (id) => (isFavoritt ? deleteFavoritt(id) : addFavoritt(id)),
    onSettled: () => queryClient.invalidateQueries({ queryKey: getFavoritter.queryKey }),
  });

  if (isFavoritt) {
    return (
      <Button
        css={css`
          width: fit-content;
        `}
        icon={<StarFillIcon aria-hidden />}
        onClick={() => toggleFavoritesMutation.mutate(templateId)}
        size="small"
        variant="secondary"
      >
        Fjern som favoritt
      </Button>
    );
  }

  return (
    <Button
      css={css`
        width: fit-content;
      `}
      icon={<StarIcon aria-hidden />}
      onClick={() => toggleFavoritesMutation.mutate(templateId)}
      size="small"
      variant="secondary-neutral"
    >
      Legg til som favoritt
    </Button>
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
