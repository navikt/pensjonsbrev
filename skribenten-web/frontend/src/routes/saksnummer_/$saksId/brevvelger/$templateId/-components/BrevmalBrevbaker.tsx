import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Button, Select, VStack } from "@navikt/ds-react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { useMemo } from "react";
import { Controller, useForm } from "react-hook-form";
import type { z } from "zod";

import { getEnheter } from "~/api/skribenten-api-endpoints";
import { ApiError } from "~/components/ApiError";
import { Divider } from "~/components/Divider";
import type { LetterMetadata } from "~/types/apiTypes";
import { SpraakKode } from "~/types/apiTypes";
import { SPRAAK_ENUM_TO_TEXT } from "~/types/nameMappings";

import { Route } from "../route";
import LetterTemplateHeading from "./LetterTemplate";
import type { brevmalBrevbakerFormSchema } from "./TemplateUtils";

const BrevmalBrevbaker = (properties: { letterTemplate: LetterMetadata; preferredLanguage: SpraakKode | null }) => {
  const navigate = useNavigate({ from: Route.fullPath });
  const enheterQuery = useQuery(getEnheter);
  const sorterteSpråk = useMemo(() => {
    return properties.letterTemplate.spraak.toSorted();
  }, [properties.letterTemplate.spraak]);

  const form = useForm<z.infer<typeof brevmalBrevbakerFormSchema>>({
    defaultValues: {
      spraak:
        properties.preferredLanguage && sorterteSpråk.includes(properties.preferredLanguage)
          ? properties.preferredLanguage
          : sorterteSpråk[0],
      enhetsId: "",
    },
  });

  return (
    <>
      <LetterTemplateHeading letterTemplate={properties.letterTemplate} />
      <Divider />
      <form
        onSubmit={form.handleSubmit((values) => {
          navigate({
            to: "/saksnummer/$saksId/brev",
            search: () => ({
              brevkode: properties.letterTemplate.id,
              spraak: values.spraak,
              enhetsId: values.enhetsId || null,
            }),
            replace: true,
          });
        })}
      >
        <VStack gap="8">
          <Controller
            control={form.control}
            name="spraak"
            render={({ field, fieldState }) => (
              <Select
                {...field}
                data-cy="språk-velger-select"
                error={fieldState.error?.message}
                label="Språk"
                size="medium"
              >
                {Object.entries(SpraakKode).map((spraak) => (
                  <option key={spraak[0]} value={spraak[1]}>
                    {SPRAAK_ENUM_TO_TEXT[spraak[1]]}{" "}
                    {properties.preferredLanguage === spraak[1] ? "(foretrukket språk)" : ""}
                  </option>
                ))}
              </Select>
            )}
          />
          <div>
            {enheterQuery.isSuccess && (
              <Controller
                control={form.control}
                name="enhetsId"
                render={({ field, fieldState }) => (
                  <Select {...field} error={fieldState.error?.message} label="Avsenderenhet" size="medium">
                    <option value={""}>Velg enhet</option>
                    {enheterQuery.data.map((option) => (
                      <option key={option.id} value={option.id}>
                        {option.navn}
                      </option>
                    ))}
                  </Select>
                )}
              />
            )}

            {enheterQuery.isPending && <>Henter enheter....</>}
            {enheterQuery.isError && <ApiError error={enheterQuery.error} title="Klarte ikke å hente enheter" />}
          </div>
        </VStack>
        <Button
          css={css`
            width: fit-content;
          `}
          data-cy="order-letter"
          icon={<ArrowRightIcon />}
          iconPosition="right"
          size="small"
        >
          Åpne brev
        </Button>
      </form>
    </>
  );
};

export default BrevmalBrevbaker;
