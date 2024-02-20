import { BodyLong, Heading, VStack } from "@navikt/ds-react";
import { createFileRoute } from "@tanstack/react-router";

import { getTemplate } from "~/api/brevbaker-api-endpoints";
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
  loader: ({ context }) => {
    return context.queryClient.ensureQueryData({
      queryKey: getTemplate.queryKey(templateId),
      queryFn: () => getTemplate.queryFn(templateId),
    });
  },
  component: TemplateExplorer,
});

const templateId = "UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO";
function TemplateExplorer() {
  const templateDocumentation = Route.useLoaderData();

  return (
    <>
      <Heading size="medium" spacing>
        Oppskrift for {templateId}
      </Heading>
      <VStack gap="4">
        <Document templateDocumentation={templateDocumentation} />
        {templateDocumentation.attachments.map((attachment, index) => (
          <Document key={index} templateDocumentation={attachment} />
        ))}
      </VStack>
    </>
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
  return <span>ForEachComponent: TODO</span>;
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
        <BodyLong spacing>
          {content.paragraph.map((cocs, index) => (
            <ContentOrControlStructureComponent cocs={cocs} key={index} />
          ))}
        </BodyLong>
      );
    }
    case ElementType.PARAGRAPH_TABLE: {
      return <span>TABLE TODO</span>;
    }
    case ElementType.PARAGRAPH_ITEMLIST: {
      return <span>ITEMLIST TODO</span>;
    }
  }
}

function ConditionalComponent<E extends Element>({ conditional }: { conditional: Conditional<E> }) {
  return (
    <div className="conditional">
      <span>Hvis: </span>
      <ExpressionComponent expression={conditional.predicate} />
      <ShowIf cocs={conditional.showIf} />
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
    <div>
      <span className="expression">Ellers:</span>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </div>
  );
}

function ExpressionComponent({ expression }: { expression: Expression }) {
  return <span className="expression">{expressionToText(expression)}</span>;
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
