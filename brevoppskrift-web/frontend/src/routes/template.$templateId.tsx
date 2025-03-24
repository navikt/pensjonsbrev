import { css } from "@emotion/react";
import { BodyLong, Heading, Select, VStack } from "@navikt/ds-react";
import { createFileRoute, Link, notFound, useNavigate } from "@tanstack/react-router";

import { getAllBrevkoder, getTemplateDescription, getTemplateDocumentation } from "~/api/brevbaker-api-endpoints";
import type {
  Attachment,
  Conditional,
  ContentOrControlStructure,
  Element,
  ElseIf,
  Expression,
  ForEach,
  TemplateDescription,
  TemplateDocumentation,
} from "~/api/brevbakerTypes";
import { ContentOrControlStructureType, ElementType } from "~/api/brevbakerTypes";
import { DataClasses, trimClassName } from "~/components/DataClasses";

interface TemplateLoaderData {
  documentation: TemplateDocumentation;
  description: TemplateDescription;
}

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
  validateSearch: (
    search: Record<string, unknown>,
  ): { language?: string; highlightedDataClass?: string; highlightedDataField?: string } => ({
    language: search.language?.toString(),
    highlightedDataClass: search.highlightedDataClass?.toString(),
    highlightedDataField: search.highlightedDataField?.toString(),
  }),
  component: TemplateExplorer,
});

function TemplateExplorer() {
  const { documentation } = Route.useLoaderData() as TemplateLoaderData;
  const { templateId } = Route.useParams();

  return (
    <>
      <DataClasses templateModelSpecification={documentation.templateModelSpecification} />
      <VStack align="center" gap="4">
        <Heading size="medium" spacing>
          Oppskrift for {templateId}
        </Heading>
        <SelectLanguage />
        <Document templateDocumentation={documentation} />
        {documentation.attachments.map((attachment, index) => (
          <Document key={index} templateDocumentation={attachment} />
        ))}
      </VStack>
    </>
  );
}

function SelectLanguage() {
  const { description } = Route.useLoaderData() as TemplateLoaderData;
  const { language } = Route.useSearch();
  const navigate = useNavigate({ from: Route.fullPath });

  return (
    <Select
      css={css`
        width: 200px;
        margin-bottom: var(--a-spacing-8);
      `}
      data-cy="select-language"
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
      return <ForEachComponent content={cocs} />;
    }
  }
}

function ForEachComponent({ content }: { content: ForEach<Element> }) {
  return (
    <>
      <div className="expression">
        <code>For hver X i:</code> <ExpressionToText expression={content.items} />
      </div>
      <>
        {content.body.map((b, index) => (
          <ContentOrControlStructureComponent cocs={b} key={index} />
        ))}
      </>
    </>
  );
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
        <div
          css={css`
            display: grid;
            gap: 1px;
            grid-template-columns: repeat(${content.header.cells.length}, 1fr);
            border: 1px solid black;
            background: black;
            overflow: auto;

            .cell {
              background: white;
            }

            /* Indent cells that are conditional to an expression */
            .expression + .cell {
              padding-left: var(--a-spacing-4);
            }

            .conditional,
            .show-if,
            .show-else {
              display: contents;
            }

            .expression {
              grid-column: span ${content.header.cells.length};
            }
          `}
        >
          {content.header.cells.map((cell, index) => (
            <b className="cell" key={index}>
              {cell.text.map((t, index) => (
                <ContentOrControlStructureComponent cocs={t} key={index} />
              ))}
            </b>
          ))}
          {content.rows.map((r, index) => (
            <ContentOrControlStructureComponent cocs={r} key={index} />
          ))}
        </div>
      );
    }
    case ElementType.PARAGRAPH_TABLE_ROW: {
      return content.cells.map((cell, index) => (
        <span className="cell" key={index}>
          {cell.text.map((t, index) => (
            <ContentOrControlStructureComponent cocs={t} key={index} />
          ))}
        </span>
      ));
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
      {conditional.elseIf.map((elseIf, index) => (
        <ShowElseIf elseIf={elseIf} key={index} />
      ))}
      <ShowElse cocs={conditional.showElse} />
    </div>
  );
}

function ShowElseIf<E extends Element>({ elseIf }: { elseIf: ElseIf<E> }) {
  return (
    <div className="show-if">
      <div className="expression">
        <code>Else If </code>
        <ExpressionToText expression={elseIf.predicate} />
      </div>
      {elseIf.showIf.map((cocs, index) => (
        <ContentOrControlStructureComponent cocs={cocs} key={index} />
      ))}
    </div>
  );
}

function ShowIf<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  return (
    <>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </>
  );
}

function ShowElse<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  if (cocs.length === 0) {
    return <></>;
  }
  return (
    <div className="show-else">
      <div className="expression">
        <code>Else</code>
      </div>
      <>
        {cocs.map((a, index) => (
          <ContentOrControlStructureComponent cocs={a} key={index} />
        ))}
      </>
    </div>
  );
}

function ExpressionToText({ expression }: { expression: Expression }) {
  if ("scopeName" in expression) {
    switch (expression.scopeName) {
      case "forEach_item": {
        return "X"; // TODO: forskjellig navn for nøstede forEach
      }
      case "argument": {
        return "";
      }
      default: {
        return expression.scopeName;
      }
    }
  }
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
      const isDeepestPostFix = "scopeName" in expression.first && expression.first?.scopeName === "argument";
      const content = (
        <>
          {firstExpressionResolved}
          {expression.operator.text}
        </>
      );

      if (isDeepestPostFix) {
        const isPrimitive = expression.type?.includes("kotlin") || expression.type?.includes("java"); // TODO: too basic?
        return (
          <Link
            from={Route.fullPath}
            preload={false}
            replace
            search={(s) => ({
              ...s,
              highlightedDataClass: isPrimitive ? undefined : trimClassName(expression.type ?? "").replace("?", ""),
              highlightedDataField: isPrimitive ? expression.operator.text.replace(".", "") : undefined,
            })}
          >
            {content}
          </Link>
        );
      }

      return <span>{content}</span>;
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
          </span>{" "}
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
