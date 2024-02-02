import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { ArrowRightIcon, StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { Alert, BodyShort, Button, Heading, Radio, RadioGroup, Select, Tag, VStack } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, notFound } from "@tanstack/react-router";
import { useNavigate } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { Controller, FormProvider, useForm, useFormContext } from "react-hook-form";
import { z } from "zod";

import {
  addFavoritt,
  deleteFavoritt,
  getEblanketter,
  getFavoritter,
  getLetterTemplate,
  orderLetter,
} from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import { BrevvelgerTabOptions } from "~/routes/saksnummer_.$sakId.brevvelger";
import type { LetterMetadata, OrderLetterRequest } from "~/types/apiTypes";
import { BrevSystem, SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

export const Route = createFileRoute("/saksnummer/$sakId/brevvelger/$templateId")({
  component: SelectedTemplate,
  loader: async ({ context: { queryClient, getSakQueryOptions }, params: { templateId } }) => {
    const sak = await queryClient.ensureQueryData(getSakQueryOptions);

    const letterTemplates = await queryClient.ensureQueryData({
      queryKey: getLetterTemplate.queryKey(sak.sakType),
      queryFn: () => getLetterTemplate.queryFn(sak.sakType),
    });

    const eblanketter = await queryClient.ensureQueryData(getEblanketter);

    const letterTemplate = [...letterTemplates, ...eblanketter].find(
      (letterMetadata) => letterMetadata.id === templateId,
    );

    if (!letterTemplate) {
      throw notFound();
    }

    return { letterTemplate, sak };
  },
  notFoundComponent: () => {
    // eslint-disable-next-line react-hooks/rules-of-hooks -- this works and is used as an example in the documentation: https://tanstack.com/router/latest/docs/framework/react/guide/not-found-errors#data-loading-inside-notfoundcomponent
    const { templateId } = Route.useParams();
    return <Alert variant="info">Fant ikke brevmal med id {templateId}</Alert>;
  },
});

const formValidationSchema = z.object({
  spraak: z.nativeEnum(SpraakKode, { required_error: "Obligatorisk" }),
  enhetsId: z.string({ required_error: "Obligatorisk" }).length(4, "Obligatorisk"),
  isSensitive: z.boolean({ required_error: "Obligatorisk" }),
});

export function SelectedTemplate() {
  const { fane } = Route.useSearch();
  const { letterTemplate } = Route.useLoaderData();

  if (!letterTemplate) {
    return <></>;
  }

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
      {fane === BrevvelgerTabOptions.BREVMALER ? (
        <Brevmal letterTemplate={letterTemplate} />
      ) : (
        <Eblankett letterTemplate={letterTemplate} />
      )}
    </div>
  );
}

function Brevmal({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { templateId, sakId } = Route.useParams();
  const { enhetsId } = Route.useSearch();
  const { sak } = Route.useLoaderData();
  const navigate = useNavigate({ from: Route.fullPath });

  const orderLetterMutation = useMutation<string, AxiosError<Error> | Error, OrderLetterRequest>({
    mutationFn: orderLetter,
    onSuccess: (callbackUrl) => {
      window.open(callbackUrl);
    },
  });

  const methods = useForm<z.infer<typeof formValidationSchema>>({
    defaultValues: {
      enhetsId,
      isSensitive: letterTemplate?.brevsystem === BrevSystem.Extream ? undefined : false, // Supply default value to pass validation if Brev is not Doksys
    },
    resolver: zodResolver(formValidationSchema),
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
              case BrevSystem.Extream:
              case BrevSystem.DokSys: {
                const orderLetterRequest = {
                  brevkode: letterTemplate.id,
                  sakId: Number(sakId),
                  gjelderPid: sak.foedselsnr,
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

          <VStack gap="4">
            {orderLetterMutation.error && <Alert variant="error">{orderLetterMutation.error.message}</Alert>}
            <Button
              css={css`
                width: fit-content;
              `}
              icon={<ArrowRightIcon />}
              iconPosition="right"
              loading={orderLetterMutation.isPending}
              size="small"
              type="submit"
              variant="primary"
            >
              Rediger brev
            </Button>
          </VStack>
        </form>
      </FormProvider>
    </>
  );
}

function SelectSensitivity({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  if (letterTemplate.brevsystem !== BrevSystem.Extream) {
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

function Eblankett({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  return (
    <>
      <LetterTemplateHeading letterTemplate={letterTemplate} />
      <Heading level="3" size="xsmall">
        Formål og målgruppe
      </Heading>
      <BodyShort size="small">E-blankett</BodyShort>
      <Divider />
    </>
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
          case BrevSystem.Extream: {
            return (
              <Tag size="small" variant="alt1-moderate">
                Extream
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
