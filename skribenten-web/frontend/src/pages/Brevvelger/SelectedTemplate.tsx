import { css } from "@emotion/react";
import { ArrowRightIcon, StarFillIcon, StarIcon } from "@navikt/aksel-icons";
import { BodyShort, Button, Heading, Select, Tag } from "@navikt/ds-react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useNavigate, useParams, useRouteContext, useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect } from "react";
import { FormProvider, useForm, useFormContext } from "react-hook-form";

import {
  addFavoritt,
  deleteFavoritt,
  getFavoritter,
  getLetterTemplate,
  orderLetter,
} from "~/api/skribenten-api-endpoints";
import { Divider } from "~/components/Divider";
import { usePreferredLanguage } from "~/hooks/usePreferredLanguage";
import { redigeringRoute, selectedTemplateRoute } from "~/tanStackRoutes";
import type { LetterMetadata, OrderLetterRequest } from "~/types/apiTypes";
import { BrevSystem } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import { BrevvelgerTabOptions } from "./BrevvelgerPage";

export function SelectedTemplate() {
  const { fane } = useSearch({ from: selectedTemplateRoute.id });

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
      {fane === BrevvelgerTabOptions.BREVMALER ? <Brevmal /> : <Eblankett />}
    </div>
  );
}

function Brevmal() {
  const { templateId, sakId } = useParams({ from: selectedTemplateRoute.id });
  const navigate = useNavigate({ from: selectedTemplateRoute.id });
  const { getSakQueryOptions } = useRouteContext({ from: selectedTemplateRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette??
  const letterTemplate = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    select: (letterTemplates) =>
      letterTemplates.kategorier
        .flatMap((kategori) => kategori.templates)
        .find((letterMetadata) => letterMetadata.id === templateId),
    enabled: !!sak,
  }).data;

  const orderLetterMutation = useMutation<unknown, AxiosError<Error>, OrderLetterRequest>({
    mutationFn: orderLetter,
    onSuccess: () => {},
  });

  const methods = useForm();

  if (!letterTemplate) {
    return <></>;
  }

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
            if (letterTemplate.brevsystem === BrevSystem.Brevbaker) {
              navigate({ to: redigeringRoute.id, params: { sakId, templateId } });
            } else {
              const orderLetterRequest = {
                brevkode: letterTemplate.id,
                spraak: submittedValues.spraak,
                sakId: Number(sakId),
                gjelderPid: sak?.foedselsnr ?? "TODO",
              };
              orderLetterMutation.mutate(orderLetterRequest);
            }
          })}
        >
          <SelectLanguage letterTemplate={letterTemplate} />

          <Button
            css={css`
              width: fit-content;
            `}
            icon={<ArrowRightIcon />}
            iconPosition="right"
            size="small"
            type="submit"
            variant="primary"
          >
            Rediger brev
          </Button>
        </form>
      </FormProvider>
    </>
  );
}

function SelectLanguage({ letterTemplate }: { letterTemplate: LetterMetadata }) {
  const { sakId } = useParams({ from: selectedTemplateRoute.id });
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

function Eblankett() {
  const { templateId } = useParams({ from: selectedTemplateRoute.id });

  const { getSakQueryOptions } = useRouteContext({ from: selectedTemplateRoute.id });
  const sak = useQuery(getSakQueryOptions).data;

  // TODO: deling av data mellom routes må kunne gjøres enklere enn dette??
  const letterTemplate = useQuery({
    queryKey: getLetterTemplate.queryKey(sak?.sakType as string),
    queryFn: () => getLetterTemplate.queryFn(sak?.sakType as string),
    select: (letterTemplates) => letterTemplates.eblanketter.find((letterMetadata) => letterMetadata.id === templateId),
    enabled: !!sak,
  }).data;

  if (!letterTemplate) {
    return <></>;
  }

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
  if (letterTemplate.isEblankett) {
    return <></>;
  }

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
  const { templateId } = useParams({ from: selectedTemplateRoute.id });
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
