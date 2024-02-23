import { css } from "@emotion/react";
import { BodyLong, Heading, Select, Table, VStack } from "@navikt/ds-react";
import { createFileRoute, notFound, useNavigate } from "@tanstack/react-router";

import { getTemplateDescription, getTemplateDocumentation } from "~/api/brevbaker-api-endpoints";
import type {
  Attachment,
  Conditional,
  ContentOrControlStructure,
  Element,
  Expression,
  TemplateDocumentation,
} from "~/api/brevbakerTypes";
import { ContentOrControlStructureType, ElementType } from "~/api/brevbakerTypes";

export const Route = createFileRoute("/template/$templateId")({
  loaderDeps: ({ search: { language } }) => ({ language }),
  loader: async ({ context, navigate, deps }) => {
    const description = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDescription.queryKey(templateId),
      queryFn: () => getTemplateDescription.queryFn(templateId),
    });

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    if (!deps.language) {
      navigate({
        search: () => ({
          language: defaultLanguage,
        }),
      });
    }

    const language = deps.language ?? defaultLanguage;

    const documentation = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDocumentation.queryKey(templateId, language),
      queryFn: () => getTemplateDocumentation.queryFn(templateId, language),
    });

    return { documentation, description };
  },
  validateSearch: (search: Record<string, unknown>): { language?: string } => ({
    language: search.language?.toString(),
  }),
  component: TemplateExplorer,
});

const templateId = "UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO";
function TemplateExplorer() {
  const { documentation } = Route.useLoaderData();

  return (
    <>
      <Heading size="medium" spacing>
        Oppskrift for {templateId}
      </Heading>
      <SelectLanguage />
      <VStack gap="4">
        <Document templateDocumentation={documentation} />
        {documentation.attachments.map((attachment, index) => (
          <Document key={index} templateDocumentation={attachment} />
        ))}
      </VStack>
    </>
  );
}

function SelectLanguage() {
  const { description } = Route.useLoaderData();
  const { language } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <Select
      css={css`
        margin-bottom: var(--a-spacing-8);
      `}
      label="Språk"
      onChange={(event) => navigate({ search: { language: event.target.value } })}
      size="medium"
      value={language}
    >
      {description.languages.map((language) => (
        <option key={language} value={language}>
          {language}
        </option>
      ))}
    </Select>
  );
}

function Document({ templateDocumentation }: { templateDocumentation: TemplateDocumentation | Attachment }) {
  return (
    <div className="preview">
      <div>
        {templateDocumentation.title.map((cocs, index) => {
          return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
        })}
      </div>
      <div>
        {templateDocumentation.outline.map((cocs, index) => {
          return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
        })}
      </div>
    </div>
  );
}

function ContentOrControlStructureComponent<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E> }) {
  switch (cocs.controlStructureType) {
    case ContentOrControlStructureType.CONDITIONAL: {
      return <ConditionalComponent conditional={cocs} />;
    }
    case ContentOrControlStructureType.CONTENT: {
      return <ContentComponent content={cocs.content} />;
    }
    case ContentOrControlStructureType.FOR_EACH: {
      return <ForEachComponent />;
    }
  }
}

function ForEachComponent() {
  // TODO
  return <></>;
  // return <span>ForEachComponent: TODO</span>;
}

function ContentComponent({ content }: { content: Element }) {
  switch (content.elementType) {
    case ElementType.TITLE1: {
      return (
        <Heading size="small" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementType.TITLE2: {
      return (
        <Heading size="xsmall" spacing>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </Heading>
      );
    }
    case ElementType.PARAGRAPH_TEXT_LITERAL: {
      return <span className="paragraph-text-literal">{content.text}</span>;
    }
    case ElementType.PARAGRAPH_TEXT_EXPRESSION: {
      return <ExpressionComponent expression={content.expression} />;
    }
    case ElementType.PARAGRAPH: {
      return (
        <BodyLong as="div" spacing>
          {content.paragraph.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </BodyLong>
      );
    }
    case ElementType.PARAGRAPH_TABLE: {
      return (
        <Table>
          <Table.Header>
            <Table.Row>
              {content.header.cells.map((cell, index) => (
                <Table.HeaderCell key={index} scope="col">
                  {cell.text.map((t, index) => (
                    <ContentOrControlStructureComponent cocs={t} key={index} />
                  ))}
                </Table.HeaderCell>
              ))}
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {content.rows.map((r, index) => (
              <ContentOrControlStructureComponent cocs={r} key={index} />
            ))}
          </Table.Body>
        </Table>
      );
    }
    case ElementType.PARAGRAPH_TABLE_ROW: {
      return (
        <Table.Row>
          {content.cells.map((cell, index) => (
            <Table.DataCell key={index}>
              {cell.text.map((t, index) => (
                <ContentOrControlStructureComponent cocs={t} key={index} />
              ))}
            </Table.DataCell>
          ))}
        </Table.Row>
      );
    }
    case ElementType.PARAGRAPH_ITEMLIST: {
      return (
        <ul>
          {content.items.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </ul>
      );
    }
    case ElementType.PARAGRAPH_ITEMLIST_ITEM: {
      return (
        <li>
          {content.text.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </li>
      );
    }
  }
}

function ConditionalComponent<E extends Element>({ conditional }: { conditional: Conditional<E> }) {
  return (
    <div className="conditional">
      <div className="show-if">
        <ExpressionComponent expression={conditional.predicate} />
        <ShowIf cocs={conditional.showIf} />
      </div>
      <ShowElse cocs={conditional.showElse} />
    </div>
  );
}

function ShowIf<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  return (
    <div>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </div>
  );
}

function ShowElse<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  if (cocs.length === 0) {
    return <></>;
  }
  return (
    <div className="show-else">
      <code>Else</code>
      <div>
        {cocs.map((a, index) => (
          <ContentOrControlStructureComponent cocs={a} key={index} />
        ))}
      </div>
    </div>
  );
}

function ExpressionComponent({ expression }: { expression: Expression }) {
  return <code className="expression">{expressionToText(expression)}</code>;
}

function expressionToText(expression: Expression): string {
  if ("scopeName" in expression) return expression.scopeName;
  if ("value" in expression) return expression.value;

  const firstExpressionResolved = expressionToText(expression.first);
  const secondExpressionResolved = expression.second ? `, ${expressionToText(expression.second)}` : "";

  switch (expression.operator.syntax) {
    case "FUNCTION": {
      return `${expression.operator.text}(${firstExpressionResolved}${secondExpressionResolved})`;
    }
    case "POSTFIX": {
      return `${stripPackageNameFromType(expression.type)}${expression.operator.text}`;
    }
    case "INFIX": {
      return `${firstExpressionResolved} ${expression.operator.text} ${secondExpressionResolved}`;
    }
    case "PREFIX": {
      return `${expression.operator.text}${firstExpressionResolved}${secondExpressionResolved}`;
    }
  }
}

function stripPackageNameFromType(type?: string) {
  return type?.replace("no.nav.pensjon.brevbaker.api.model.", "") ?? "";
}
