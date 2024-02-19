import { createFileRoute } from "@tanstack/react-router";

import { getTemplate } from "~/api/brevbaker-api-endpoints";
import type { Conditional, ContentOrControlStructure, Element, Expression } from "~/api/brevbakerTypes";
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
    <div>
      {templateDocumentation.title.map((cocs, index) => {
        return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
      })}
      {templateDocumentation.outline.map((cocs, index) => {
        return <ContentOrControlStructureComponent cocs={cocs} key={index} />;
      })}
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
    case ElementType.TITLE1:
    case ElementType.TITLE2: {
      return content.text.map((cocs, index) => <ContentOrControlStructureComponent cocs={cocs} key={index} />);
    }
    case ElementType.PARAGRAPH_TEXT_LITERAL: {
      return <span>{content.text}</span>;
    }
    case ElementType.PARAGRAPH_TEXT_EXPRESSION: {
      return <span>Expression TODO</span>;
    }
    case ElementType.PARAGRAPH: {
      return content.paragraph.map((cocs, index) => <ContentOrControlStructureComponent cocs={cocs} key={index} />);
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
    <div>
      <ExpressionComponent expression={conditional.predicate} />
      <ShowIf cocs={conditional.showIf} />
      <ShowElse cocs={conditional.showElse} />
    </div>
  );
}

function ShowIf<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  return (
    <div>
      <span>Show if: </span>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </div>
  );
}

function ShowElse<E extends Element>({ cocs }: { cocs: ContentOrControlStructure<E>[] }) {
  return (
    <div>
      <span>Show else: </span>
      {cocs.map((a, index) => (
        <ContentOrControlStructureComponent cocs={a} key={index} />
      ))}
    </div>
  );
}

function ExpressionComponent({ expression }: { expression: Expression }) {
  return <span>Expression TODO</span>;
}
