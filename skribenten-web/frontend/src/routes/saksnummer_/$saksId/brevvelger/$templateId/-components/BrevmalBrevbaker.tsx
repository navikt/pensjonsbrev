import { css } from "@emotion/react";
import { ArrowRightIcon } from "@navikt/aksel-icons";
import { Button, VStack } from "@navikt/ds-react";
import { useNavigate } from "@tanstack/react-router";
import { useMemo } from "react";
import { FormProvider, useForm } from "react-hook-form";
import type { z } from "zod";

import { Divider } from "~/components/Divider";
import type { LetterMetadata } from "~/types/apiTypes";
import type { SpraakKode } from "~/types/apiTypes";

import { Route } from "../route";
import { PersonAdresse } from "./Adresse";
import LetterTemplateHeading from "./LetterTemplate";
import SelectEnhet from "./SelectEnhet";
import SelectLanguage from "./SelectLanguage";
import type { brevmalBrevbakerFormSchema } from "./TemplateUtils";

const BrevmalBrevbaker = (properties: { letterTemplate: LetterMetadata; preferredLanguage: SpraakKode | null }) => {
  const { saksId } = Route.useParams();
  const navigate = useNavigate({ from: Route.fullPath });

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
      <FormProvider {...form}>
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
            <PersonAdresse kanEndreAndresse={false} sakId={saksId} />
            <SelectLanguage preferredLanguage={properties.preferredLanguage} sorterteSpråk={sorterteSpråk} />
            <SelectEnhet />
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
      </FormProvider>
    </>
  );
};

export default BrevmalBrevbaker;
