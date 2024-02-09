import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon, StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import {
  Alert,
  BodyShort,
  Button,
  Heading,
  Link,
  Radio,
  RadioGroup,
  Select,
  Tag,
  TextField,
  VStack,
} from "@navikt/ds-react";
import type { UseMutationResult } from "@tanstack/react-query";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, notFound } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import React, { useEffect } from "react";
import { Controller, FormProvider, useForm, useFormContext } from "react-hook-form";
import { z } from "zod";

import {
  addFavoritt,
  deleteFavoritt,
  getAvtaleLand,
  getFavoritter,
  getKontaktAdresse,
  getLetterTemplate,
  orderLetter,
} from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import type { LetterMetadata, OrderEblankettRequest, OrderLetterRequest } from "~/types/apiTypes";
import { BrevSystem, SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger/$templateId")({
  component: SelectedTemplate,
  loaderDeps: ({ search: { vedtaksId } }) => ({ includeVedtak: !!vedtaksId }),
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
  }, [templateId, orderLetterMutation]);

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
      <Heading level="3" size="xsmall">
        Mottaker (TODO)
      </Heading>
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
                return navigate({ to: "/saksnummer/$sakId/redigering/$templateId", params: { sakId, templateId } });
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
