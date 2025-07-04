import { css } from "@emotion/react";
import { Heading, Tabs, VStack } from "@navikt/ds-react";

import {
  SaksbehandlerValgModelEditor,
  usePartitionedModelSpecification,
} from "~/Brevredigering/ModelEditor/ModelEditor";

import { ApiError } from "../ApiError";
import { BrevAlternativTab } from "./BrevmalAlternativerUtils";

const BrevmalAlternativer = (props: {
  brevkode: string;
  submitOnChange?: () => void;

  /**
   * Kan velge hvilke felter som skal vises. Default er at begge vises (dersom dem finnes, ellers bare den som finnes)
   */
  onlyShowRequired?: boolean;
  withTitle?: boolean;
}) => {
  const specificationFormElements = usePartitionedModelSpecification(props.brevkode);

  switch (specificationFormElements.status) {
    case "error": {
      return (
        <ApiError
          error={specificationFormElements.error}
          title={"Feil oppstod ved henting av alternativer for brevmal"}
        />
      );
    }
    case "pending": {
      return null;
    }
    case "success": {
      if (
        specificationFormElements.optionalFields.length === 0 &&
        specificationFormElements.requiredfields.length === 0
      ) {
        return null;
      }

      if (props.onlyShowRequired) {
        if (
          specificationFormElements.requiredfields.length > 0 ||
          specificationFormElements.optionalFields.length > 0
        ) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"required"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        } else {
          return null;
        }
      } else {
        if (specificationFormElements.optionalFields.length === 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"required"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        if (specificationFormElements.requiredfields.length === 0) {
          return (
            <VStack gap="3">
              {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
              <SaksbehandlerValgModelEditor
                brevkode={props.brevkode}
                fieldsToRender={"optional"}
                specificationFormElements={specificationFormElements}
                submitOnChange={props.submitOnChange}
              />
            </VStack>
          );
        }

        return (
          <VStack gap="3">
            {props.withTitle && <Heading size="xsmall">Brevmal alternativer</Heading>}
            <Tabs
              css={css`
                width: 100%;

                display: flex;
                flex-direction: column;
                gap: var(--a-spacing-5);

                .navds-tabs__scroll-button {
                  /* vi har bare 2 tabs, så det gir ikke mening tab listen skal være scrollbar. Den tar i tillegg mye ekstra plass når skjermen er <1024px */
                  display: none;
                }
              `}
              defaultValue={BrevAlternativTab.TEKSTER}
              fill
              size="small"
            >
              <Tabs.List
                css={css`
                  display: grid;
                  grid-template-columns: repeat(2, 1fr);
                `}
              >
                <Tabs.Tab label="Tekster" value={BrevAlternativTab.TEKSTER} />
                <Tabs.Tab label="Overstyring" value={BrevAlternativTab.OVERSTYRING} />
              </Tabs.List>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--a-spacing-5);
                `}
                value={BrevAlternativTab.TEKSTER}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender={"optional"}
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
              <Tabs.Panel
                css={css`
                  display: flex;
                  flex-direction: column;
                  gap: var(--a-spacing-5);
                `}
                value={BrevAlternativTab.OVERSTYRING}
              >
                <SaksbehandlerValgModelEditor
                  brevkode={props.brevkode}
                  fieldsToRender={"required"}
                  specificationFormElements={specificationFormElements}
                  submitOnChange={props.submitOnChange}
                />
              </Tabs.Panel>
            </Tabs>
          </VStack>
        );
      }
    }
  }
};

export default BrevmalAlternativer;
