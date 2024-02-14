import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon, PencilIcon, PlusIcon, StarFillIcon, StarIcon, XMarkIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  Button,
  Heading,
  HStack,
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
import { createFileRoute, notFound } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React, { useEffect, useRef } from "react";
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
  hentSamhandler,
  hentSamhandlerAdresse,
  orderLetter,
} from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import { SamhandlerTypeSelectFormPart } from "~/components/select/SamhandlerSelect";
import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import type {
  FinnSamhandlerRequestDto,
  FinnSamhandlerResponseDto,
  LetterMetadata,
  OrderEblankettRequest,
  OrderLetterRequest,
  SamhandlerPostadresse,
} from "~/types/apiTypes";
import { SamhandlerTypeCode } from "~/types/apiTypes";
import { BrevSystem, SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger/$templateId")({
  component: SelectedTemplate,
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
  validateSearch: (search: Record<string, unknown>): { idTSSEkstern?: string } => ({
    idTSSEkstern: search.idTSSEkstern?.toString(),
  }),
  loader: async ({ context: { queryClient, getSakQueryOptions }, params: { templateId }, deps: { includeVedtak } }) => {
    const sak = await queryClient.ensureQueryData(getSakQueryOptions);

    const letterTemplates = await queryClient.ensureQueryData({
      queryKey: getLetterTemplate.queryKey({ sakType: sak.sakType, includeVedtak }),
      queryFn: () => getLetterTemplate.queryFn(sak.sakType, { includeVedtak }),
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
    <div
      css={css`
        display: flex;
        padding: var(--a-spacing-6) var(--a-spacing-4);
        flex-direction: column;
        align-items: flex-start;
        gap: var(--a-spacing-5);
        border-right: 1px solid var(--a-gray-400);
      `}
    >
      <FavoriteButton />
      {letterTemplate.dokumentkategoriCode === "E_BLANKETT" ? (
        <Eblankett letterTemplate={letterTemplate} />
      ) : (
        <Brevmal letterTemplate={letterTemplate} />
      )}
    </div>
  );
}

const brevmalValidationSchema = z.object({
  spraak: z.nativeEnum(SpraakKode, { required_error: "Obligatorisk" }),
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
});

function Brevmal({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, sakId } = Route.useParams();
  const { vedtaksId } = Route.useSearch();
  const { sak } = Route.useLoaderData();
  const navigate = useNavigate({ from: Route.fullPath });

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderLetterRequest>({
    mutationFn: orderLetter,
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  useEffect(() => {
    orderLetterMutation.reset();
  }, [templateId, orderLetterMutation.reset]);

  const methods = useForm<z.infer<typeof brevmalValidationSchema>>({
    defaultValues: {
      isSensitive: letterTemplate?.brevsystem === BrevSystem.Exstream ? undefined : false, // Supply default value to pass validation if Brev is not Doksys
    },
    resolver: zodResolver(brevmalValidationSchema),
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">TODO</BodyShort>
      <Divider />
      <VelgSamhandlerModal />
      <Adresse />
      <FormProvider {...methods}>
        <form
          css={css`
            display: flex;
            flex-direction: column;
            height: 100%;
            justify-content: space-between;
          `}
          onSubmit={methods.handleSubmit((submittedValues) => {
            switch (letterTemplate.brevsystem) {
              case BrevSystem.Brevbaker: {
                return navigate({ to: "/saksnummer/$sakId/redigering/$templateId", params: { templateId } });
              }
              case BrevSystem.Exstream:
              case BrevSystem.DokSys: {
                const orderLetterRequest = {
                  brevkode: letterTemplate.id,
                  sakId: Number(sakId),
                  gjelderPid: sak.foedselsnr,
                  vedtaksId,
                  ...submittedValues,
                };
                return orderLetterMutation.mutate(orderLetterRequest);
              }
            }
          })}
        >
          <VStack gap="4">
            <SelectLanguage letterTemplate={letterTemplate} />
            <SelectSensitivity letterTemplate={letterTemplate} />
          </VStack>

          <BestillOgRedigerButton orderMutation={orderLetterMutation} />
        </form>
      </FormProvider>
    </>
  );
}

const eblankettValidationSchema = brevmalValidationSchema.extend({
  landkode: z.string().min(1, "Obligatorisk"),
  mottakerText: z.string().min(1, "Obligatorisk"),
});

function Eblankett({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { sakId } = Route.useParams();
  const { sak } = Route.useLoaderData();

  const { vedtaksId } = Route.useSearch();

  const methods = useForm<z.infer<typeof eblankettValidationSchema>>({
    defaultValues: {
      landkode: "",
      mottakerText: "",
    },
    resolver: zodResolver(eblankettValidationSchema),
  });

  const orderEblankettMutation = useMutation<string, AxiosError<Error> | Error, OrderEblankettRequest>({
    mutationFn: orderLetter,
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
      <FormProvider {...methods}>
        <form
          css={css`
            display: flex;
            flex-direction: column;
            height: 100%;
            justify-content: space-between;
          `}
          onSubmit={methods.handleSubmit((submittedValues) => {
            const orderLetterRequest = {
              brevkode: letterTemplate.id,
              sakId: Number(sakId),
              gjelderPid: sak.foedselsnr,
              vedtaksId,
              ...submittedValues,
            };
            return orderEblankettMutation.mutate(orderLetterRequest);
          })}
        >
          <VStack gap="4">
            <SelectLanguage letterTemplate={letterTemplate} />
            <SelectSensitivity letterTemplate={letterTemplate} />
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
      {orderMutation.error && <ApiError error={orderMutation.error} text="Bestilling feilet" />}
      {orderMutation.isSuccess ? (
        <Alert variant="success">
          <Heading level="3" size="xsmall">
            Brev bestilt
          </Heading>
          <span>
            Redigering skal åpne seg selv, hvis ikke er popup blokkert av nettleseren din.{" "}
            <Link href={orderMutation.data}>Klikk her for å prøve åpne på nytt</Link>
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
          Bestill og rediger brev
        </Button>
      )}
    </VStack>
  );
}

function SelectSensitivity({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  if (letterTemplate.brevsystem !== BrevSystem.Exstream) {
    return <></>;
  }

  return (
    <Controller
      name="isSensitive"
      render={({ field, fieldState }) => (
        <RadioGroup
          legend="Er brevet sensitivt?"
          {...field}
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

function SelectLanguage({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { sakId } = Route.useParams();
  const { register, setValue } = useFormContext();
  const preferredLanguage = usePreferredLanguage(sakId);

  // Update selected language if preferredLanguage was not loaded before form initialization.
  useEffect(() => {
    if (preferredLanguage && letterTemplate.spraak.includes(preferredLanguage)) {
      setValue("spraak", preferredLanguage);
    }
  }, [preferredLanguage, setValue, letterTemplate.spraak]);

  return (
    <Select {...register("spraak")} label="Språk" size="small">
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
    queryKey: getKontaktAdresse.queryKey(sak.foedselsnr),
    queryFn: () => getKontaktAdresse.queryFn(sak.foedselsnr),
  });
  return (
    <>
      <Heading level="3" size="xsmall">
        Adresse
      </Heading>
      {adresseQuery.data && <BodyShort>{adresseQuery.data.adresseString}</BodyShort>}
      {adresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
      {adresseQuery.error && <ApiError error={adresseQuery.error} text="Fant ikke adresse" />}
      <Divider />
    </>
  );
}
function SamhandlerAdresse() {
  const { idTSSEkstern } = Route.useSearch();

  const hentSamhandlerAdresseQuery = useQuery({
    queryKey: hentSamhandlerAdresse.queryKey(idTSSEkstern as string),
    queryFn: () => hentSamhandlerAdresse.queryFn({ idTSSEkstern: idTSSEkstern as string }),
    enabled: !!idTSSEkstern,
  });

  return (
    <>
      <Heading level="3" size="xsmall">
        Adresse
      </Heading>
      {hentSamhandlerAdresseQuery.data && (
        <BodyShort>{formatSamhandlerAdresse(hentSamhandlerAdresseQuery.data)}</BodyShort>
      )}
      {hentSamhandlerAdresseQuery.isPending && <BodyShort>Henter...</BodyShort>}
      {hentSamhandlerAdresseQuery.error && (
        <ApiError error={hentSamhandlerAdresseQuery.error} text="Fant ikke adresse" />
      )}
      <Divider />
    </>
  );
}

function formatSamhandlerAdresse(adresse: SamhandlerPostadresse) {
  const { land, linje1, postnr, poststed } = adresse;

  const defaultAddressLine = `${linje1}, ${postnr} ${poststed}`;

  if (land !== "NOR") {
    return `${defaultAddressLine} ${land}`;
  }

  return defaultAddressLine;
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
      variant="secondary"
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
  const { idTSSEkstern } = Route.useSearch();

  const methods = useForm<z.infer<typeof samhandlerSearchValidationSchema>>({
    defaultValues: {
      samhandlerType: undefined,
      navn: undefined,
    },
    resolver: zodResolver(samhandlerSearchValidationSchema),
  });

  const hentSamhandlerQuery = useQuery({
    queryKey: hentSamhandler.queryKey(idTSSEkstern as string),
    queryFn: () => hentSamhandler.queryFn({ hentDetaljert: false, idTSSEkstern: idTSSEkstern as string }),
    enabled: !!idTSSEkstern,
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

  return (
    <>
      <Heading level="3" size="xsmall">
        Samhandler
      </Heading>

      <HStack align="center" gap="4">
        {idTSSEkstern && <span>{hentSamhandlerQuery.data?.navn}</span>}
        <Button
          icon={idTSSEkstern ? <PencilIcon /> : <PlusIcon />}
          onClick={() => reference.current?.showModal()}
          size="small"
          variant="secondary"
        >
          {idTSSEkstern ? "Endre" : "Finn samhandler"}
        </Button>
        {idTSSEkstern && (
          <Button
            icon={<XMarkIcon />}
            onClick={() =>
              navigate({
                search: (s) => ({ ...s, idTSSEkstern: undefined }),
                replace: true,
              })
            }
            size="small"
            variant="danger"
          >
            Fjern
          </Button>
        )}
      </HStack>

      <Modal header={{ heading: "Søk etter samhandler" }} ref={reference} width={600}>
        <Modal.Body>
          <FormProvider {...methods}>
            <VStack
              as="form"
              gap="4"
              id="skjema"
              method="dialog"
              onSubmit={methods.handleSubmit((values) => finnSamhandlerMutation.mutate(values))}
            >
              <TextField
                autoComplete="off"
                error={methods.formState.errors.navn?.message}
                label="Søk"
                {...methods.register("navn")}
              />
              <SamhandlerTypeSelectFormPart />
              {finnSamhandlerMutation.data?.samhandlere.length === 0 && <Alert variant="info">Ingen treff</Alert>}
              {finnSamhandlerMutation.error && (
                <ApiError error={finnSamhandlerMutation.error} text="Kunne ikke hente samhandlere." />
              )}
              {(finnSamhandlerMutation.data?.samhandlere.length ?? 0) > 0 && (
                <>
                  <div>Fant {finnSamhandlerMutation.data?.samhandlere.length} treff</div>
                  <Table>
                    <Table.Header>
                      <Table.Row>
                        <Table.HeaderCell scope="col">Navn</Table.HeaderCell>
                        <Table.HeaderCell scope="col"></Table.HeaderCell>
                      </Table.Row>
                    </Table.Header>
                    <Table.Body>
                      {finnSamhandlerMutation.data?.samhandlere.map((samhandler) => (
                        <Table.Row key={samhandler.idTSSEkstern}>
                          <Table.HeaderCell scope="row">{samhandler.navn}</Table.HeaderCell>
                          <Table.DataCell>
                            <Button
                              onClick={() => {
                                reference.current?.close();
                                navigate({
                                  search: (s) => ({ ...s, idTSSEkstern: samhandler.idTSSEkstern }),
                                  replace: true,
                                });
                              }}
                              size="small"
                              type="button"
                              variant="secondary"
                            >
                              Velg
                            </Button>
                          </Table.DataCell>
                        </Table.Row>
                      ))}
                    </Table.Body>
                  </Table>
                </>
              )}
            </VStack>
          </FormProvider>
        </Modal.Body>
        <Modal.Footer>
          <Button form="skjema" loading={finnSamhandlerMutation.isPending}>
            Søk
          </Button>
          <Button onClick={() => reference.current?.close()} type="button" variant="secondary">
            Avbryt
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
