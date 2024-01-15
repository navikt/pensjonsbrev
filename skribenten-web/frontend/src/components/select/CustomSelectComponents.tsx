import { css } from "@emotion/react";
import { BodyShort, ErrorMessage, HelpText, Label } from "@navikt/ds-react";
import type { ReactNode } from "react";
import type { FieldError } from "react-hook-form";
import type { CSSObjectWithLabel, GroupBase, MenuListProps, Props } from "react-select";
import { components } from "react-select";
import Select from "react-select";
import type { AsyncProps } from "react-select/async";
import AsyncSelect from "react-select/async";

const commonSelectCss = css`
  border-radius: var(--a-border-radius-medium);
  border: 1px solid var(--a-border-strong);

  width: 100%;
  height: 48px;

  color: var(--a-gray-900);

  & > div {
    cursor: text;
    height: 100%;
  }

  &:focus-within {
    outline: none;
    box-shadow: var(--a-shadow-focus);
  }
`;

const commonSelectOverwriteStyles = {
  clearIndicator: (base: CSSObjectWithLabel) => ({
    ...base,
    cursor: "pointer",
  }),
  // Removes default focus-border, so it can be replaced with focus from DesignSystem
  control: (base: CSSObjectWithLabel) => ({
    ...base,
    boxShadow: "none",
    border: 0,
  }),
  // Give a high zIndex so that a long result list will overflow from inside a Modal
  menuPortal: (base: CSSObjectWithLabel) => ({ ...base, zIndex: 9999 }),
  // Make border and size of input box to be identical with those from DesignSystem
  valueContainer: (base: CSSObjectWithLabel) => ({
    ...base,
    padding: "4px",
    color: "black",
  }),
  // Remove separator
  indicatorSeparator: (base: CSSObjectWithLabel) => ({
    ...base,
    display: "none",
  }),
  dropdownIndicator: (base: CSSObjectWithLabel) => ({
    ...base,
    color: "var(--a-gray-900)",
  }),
  placeholder: (base: CSSObjectWithLabel) => ({
    ...base,
    color: "var(--a-gray-900)",
  }),
  noOptionsMessage: (base: CSSObjectWithLabel) => ({
    ...base,
    color: "var(--a-gray-900)",
  }),
};

export function AsyncSearch<
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
>(properties: AsyncProps<Option, IsMulti, Group>) {
  return (
    <AsyncSelect
      cacheOptions
      css={commonSelectCss}
      isClearable
      loadingMessage={() => "Søker..."}
      menuPortalTarget={document.body}
      styles={{
        ...commonSelectOverwriteStyles,
        dropdownIndicator: (base) => ({ ...base, display: "none" }),
      }}
      {...properties}
      components={{ MenuList: CustomMenuList, ...properties.components }}
    />
  );
}

/**
 * Limit the number of items in the displayed list to 30 to help performance.
 * It is much more efficient to type a more specific search rather than look through a dropdown list of more than 100 items.
 */
function CustomMenuList<Option, IsMulti extends boolean = false, Group extends GroupBase<Option> = GroupBase<Option>>(
  properties: MenuListProps<Option, IsMulti, Group>,
) {
  const { children } = properties;
  const filteredChildren = Array.isArray(children) ? children.slice(0, 30) : children;
  return <components.MenuList {...properties}>{filteredChildren}</components.MenuList>;
}

export function SelectLayoutWrapper({
  label,
  children,
  htmlFor,
  helpText,
  error,
  description,
}: {
  label: string;
  children: ReactNode;
  htmlFor: string;
  helpText?: string;
  error?: FieldError;
  description?: string;
}) {
  return (
    <div
      className="navds-form-field"
      css={[
        css`
          display: flex;
          flex-direction: column;
          gap: var(--a-spacing-2);
        `,
        error && errorCss,
      ]}
    >
      <Label
        css={css`
          display: flex;
          gap: var(--a-spacing-2);
        `}
        htmlFor={htmlFor}
      >
        {label}
        {helpText ? (
          <HelpText strategy="fixed" title="Mer informasjon">
            {helpText}
          </HelpText>
        ) : undefined}
      </Label>
      {description && <BodyShort className="navds-form-field__description">{description}</BodyShort>}
      {children}
      {error && <ErrorMessage>{error.message}</ErrorMessage>}
    </div>
  );
}

export function BasicSelect<
  Option,
  IsMulti extends boolean = false,
  Group extends GroupBase<Option> = GroupBase<Option>,
>(properties: Props<Option, IsMulti, Group>) {
  return (
    <Select
      {...properties}
      components={{ MenuList: CustomMenuList, ...properties.components }}
      css={commonSelectCss}
      escapeClearsValue
      isClearable
      isSearchable
      menuPosition="fixed"
      placeholder={properties.placeholder ?? ""}
      styles={commonSelectOverwriteStyles}
    />
  );
}

const errorCss = css`
  > div {
    border-color: var(--a-border-danger);
    box-shadow: 0 0 0 1px var(--a-border-danger);
  }
`;
