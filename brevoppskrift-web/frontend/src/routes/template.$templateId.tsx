import { css } from "@emotion/react";
import { BodyLong, Heading, Select, Table, VStack } from "@navikt/ds-react";
import { createFileRoute, Link, notFound, useNavigate } from "@tanstack/react-router";

import { getAllBrevkoder, getTemplateDescription, getTemplateDocumentation } from "~/api/brevbaker-api-endpoints";
import type {
  Attachment,
  Conditional,
  ContentOrControlStructure,
  Element,
  Expression,
  TemplateDocumentation,
} from "~/api/brevbakerTypes";
import { ContentOrControlStructureType, ElementType } from "~/api/brevbakerTypes";
import { DataClasses, InspectedDataClass } from "~/components/DataClasses";

export const Route = createFileRoute("/template/$templateId")({
  loaderDeps: ({ search: { language } }) => ({ language }),
  loader: async ({ context, navigate, deps, params, preload }) => {
    await context.queryClient.ensureQueryData({
      queryKey: getAllBrevkoder.queryKey,
      queryFn: () => getAllBrevkoder.queryFn(),
    });

    const description = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDescription.queryKey(params.templateId),
      queryFn: () => getTemplateDescription.queryFn(params.templateId),
    });

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    if (!deps.language && !preload) {
      navigate({
        replace: true,
        search: () => ({
          language: defaultLanguage,
        }),
      });
    }

    const language = deps.language ?? defaultLanguage;

    const documentation = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDocumentation.queryKey(params.templateId, language),
      queryFn: () => getTemplateDocumentation.queryFn(params.templateId, language),
    });

    return { documentation, description };
  },
  validateSearch: (search: Record<string, unknown>): { language?: string; inspectedModel?: string } => ({
    language: search.language?.toString(),
    inspectedModel: search.inspectedModel?.toString(),
  }),
  component: TemplateExplorer,
});

function TemplateExplorer() {
  const { documentation } = Route.useLoaderData();
  const { templateId } = Route.useParams();

  return (
    <>
      <Heading size="medium" spacing>
        Oppskrift for {templateId}
      </Heading>
      <InspectedDataClass />
      <SelectLanguage />
      <DataClasses templateModelSpecification={documentation.templateModelSpecification} />
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
        width: 200px;
        margin-bottom: var(--a-spacing-8);
      `}
      label="Språk"
      onChange={(event) => navigate({ search: { language: event.target.value }, replace: true })}
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
      return (
        <span className="expression">
          <ExpressionToText expression={content.expression} />
        </span>
      );
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
        <div className="expression">
          <code>If </code>
          <ExpressionToText expression={conditional.predicate} />
        </div>
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

function ExpressionToText({ expression }: { expression: Expression }) {
  if ("scopeName" in expression) return expression.scopeName;
  if ("value" in expression) return expression.value;

  const firstExpressionResolved = <ExpressionToText expression={expression.first} />;
  const secondExpressionResolved = expression.second ? <ExpressionToText expression={expression.second} /> : "";
  switch (expression.operator.syntax) {
    case "FUNCTION": {
      return (
        <span>
          {expression.operator.text}
          <span
            css={css`
              color: purple;
            `}
          >
            (
          </span>
          {firstExpressionResolved}
          {secondExpressionResolved}
          <span
            css={css`
              color: purple;
            `}
          >
            )
          </span>
        </span>
      );
    }
    case "POSTFIX": {
      return (
        <Link
          from={Route.fullPath}
          preload={false}
          replace
          search={(s) => ({ ...s, inspectedModel: expression.type?.replace("?", "") })}
        >
          {firstExpressionResolved}
          {expression.operator.text}
        </Link>
      );
    }
    case "INFIX": {
      return (
        <span>
          {firstExpressionResolved}{" "}
          <span
            css={css`
              color: red;
            `}
          >
            {expression.operator.text}
          </span>
          {expression.operator.text === "and" || expression.operator.text === "or" ? <br /> : ""}
          {secondExpressionResolved}
        </span>
      );
    }
    case "PREFIX": {
      return (
        <span>
          {expression.operator.text}
          {firstExpressionResolved}
          {secondExpressionResolved}
        </span>
      );
    }
  }
}
