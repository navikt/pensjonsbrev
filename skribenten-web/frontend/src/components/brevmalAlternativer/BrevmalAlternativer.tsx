import { css } from "@emotion/react";
import { Heading, Tabs, VStack } from "@navikt/ds-react";

import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { type PropertyUsage } from "~/types/brevbakerTypes";

import { ApiError } from "../ApiError";
import { BrevAlternativTab } from "./BrevmalAlternativerUtils";

const BrevmalAlternativer = (props: {
  brevkode: string;
  propertyUsage?: PropertyUsage[];
  submitOnChange?: () => void;

  /**
   * Kan velge hvilke felter som skal vises. Default er at begge vises (dersom dem finnes, ellers bare den som finnes)
   */
  onlyShowRequired?: boolean;
}) => {
  const specificationFormElements = usePartitionedModelSpecification(props.brevkode, props.propertyUsage);

  switch (specificationFormElements.status) {
    case "error": {
      return (
        <ApiError
          error={specificationFormElements.error}
          title="Feil oppstod ved henting av alternativer for brevmal"
        />
      );
    }
    case "pending": {
      return null;
    }
    case "success": {
      const { optionalFields, requiredFields } = specificationFormElements;
      const hasOptional = optionalFields.length > 0;
      const hasRequired = requiredFields.length > 0;

      if (!hasOptional && !hasRequired) {
        return null;
      }

      if (props.onlyShowRequired) {
        // Boolean felter er optional med default value false, så de må registreres slik i form.
        // Dermed må SaksbehandlerValgModelEditor også rendres om det finnes optional felter (ingenting blir synlig).
        return (
          <VStack gap="space-12">
            <SaksbehandlerValgModelEditor
              brevkode={props.brevkode}
              fieldsToRender="required"
              specificationFormElements={specificationFormElements}
              submitOnChange={props.submitOnChange}
            />
          </VStack>
        );
      } else {
        if (!hasOptional) {
          return (
            <VStack gap="space-12">
              <Heading size="xsmall">Overstyring</Heading>
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender="required"
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        if (!hasRequired) {
          return (
            <VStack gap="space-12">
              <Heading size="xsmall">Tekstvalg</Heading>
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender="optional"
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        return (
          <VStack gap="space-12">
            <Tabs
              css={css`
                width: 100%;
                display: flex;
                flex-direction: column;
                gap: var(--ax-space-20);
              `}
              defaultValue={BrevAlternativTab.TEKSTER}
              fill
              size="small"
            >
              <Tabs.List>
                <Tabs.Tab label="Tekstvalg" value={BrevAlternativTab.TEKSTER} />
                <Tabs.Tab label="Overstyring" value={BrevAlternativTab.OVERSTYRING} />
              </Tabs.List>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--ax-space-20);
                `}
                value={BrevAlternativTab.TEKSTER}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender="optional"
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--ax-space-20);
                `}
                value={BrevAlternativTab.OVERSTYRING}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender="required"
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
            </Tabs>
          </VStack>
        );
      }
    }
    default: {
      return null;
    }
  }
};

export default BrevmalAlternativer;
