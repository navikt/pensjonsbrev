import { css } from "@emotion/react";
import { ArrowLeftIcon } from "@navikt/aksel-icons";
import { Box, Button, Heading, HGrid, Select, VStack } from "@navikt/ds-react";
import { createFileRoute, Link, notFound, redirect, useNavigate } from "@tanstack/react-router";
import { useEffect, useRef } from "react";

import { getTemplateDescription, getTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TemplateDocumentation } from "~/api/templateDocumentationTypes";
import { DataClassesPanel } from "~/components/DataClasses";
import { Document, KnownDataClassesProvider } from "~/components/TemplateDocumentationView";

export const Route = createFileRoute("/template/$malType/$templateId")({
  loaderDeps: ({ search: { language } }) => ({ language }),
  params: {
    parse: (raw: Record<string, string>) => ({
      templateId: raw.templateId,
      malType: raw.malType as MalType,
    }),
  },
  loader: async ({ context, deps, params, preload }) => {
    const description = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDescription.queryKey(params.malType, params.templateId),
      queryFn: () => getTemplateDescription.queryFn(params.malType, params.templateId),
    });

    const defaultLanguage = description.languages[0];
    if (!defaultLanguage) {
      throw notFound();
    }

    if (!deps.language && !preload) {
      throw redirect({
        to: "/template/$malType/$templateId",
        search: { language: defaultLanguage },
        params,
      });
    }

    const language = deps.language ?? defaultLanguage;

    const documentation = await context.queryClient.ensureQueryData({
      queryKey: getTemplateDocumentation.queryKey(params.malType, params.templateId, language),
      queryFn: () => getTemplateDocumentation.queryFn(params.malType, params.templateId, language),
    });

    return { documentation, description };
  },
  validateSearch: (
    search: Record<string, unknown>,
  ): {
    language?: string;
    highlightedDataClass?: string;
    highlightedDataField?: string;
    highlightedDataFieldOwner?: string;
    index?: number;
  } => {
    const indexRaw = search.index?.toString();
    const index = indexRaw !== undefined && /^\d+$/.test(indexRaw) ? Number(indexRaw) : undefined;
    return {
      language: search.language?.toString(),
      highlightedDataClass: search.highlightedDataClass?.toString(),
      highlightedDataField: search.highlightedDataField?.toString(),
      highlightedDataFieldOwner: search.highlightedDataFieldOwner?.toString(),
      index,
    };
  },
  component: TemplateExplorer,
});

function TemplateExplorer() {
  const { documentation } = Route.useLoaderData();
  const { templateId } = Route.useParams();
  const { index } = Route.useSearch();
  const previewRef = useRef<HTMLDivElement>(null);

  // biome-ignore lint/correctness/useExhaustiveDependencies: documentation is an intentional trigger so we re-highlight when navigating between templates with the same index.
  useEffect(() => {
    const container = previewRef.current;
    if (!container) {
      return;
    }

    for (const prev of container.querySelectorAll(".search-target")) {
      prev.classList.remove("search-target");
    }

    if (index === undefined) {
      return;
    }

    const raf = requestAnimationFrame(() => {
      const el = container.querySelector(`[data-block-index="${CSS.escape(String(index))}"]`);
      if (el) {
        el.classList.add("search-target");
        el.scrollIntoView({ behavior: "smooth", block: "center" });
      }
    });
    return () => cancelAnimationFrame(raf);
  }, [index, documentation]);

  return (
    <VStack height="100%" overflow="hidden">
      <Box asChild flexShrink="0" marginBlock="space-8" marginInline="space-16 auto">
        <Button as={Link} icon={<ArrowLeftIcon />} size="small" to="/templates" variant="secondary">
          Tilbake til mal-oversikten
        </Button>
      </Box>
      <HGrid columns="auto 1fr" flexGrow="1" minHeight="0" overflow="hidden">
        <DataClassesPanel templateModelSpecification={documentation.templateModelSpecification} />
        <VStack
          align="center"
          gap="space-16"
          overflow="auto"
          paddingBlock="space-16"
          paddingInline="space-16"
          width="100%"
        >
          <Heading size="medium" spacing>
            Oppskrift for {templateId}
          </Heading>
          <SelectLanguage />
          <div
            css={css`
              width: 100%;

              .search-target {
                outline: 3px solid var(--ax-warning-400);
                border-radius: var(--ax-radius-4);
                scroll-margin-top: var(--ax-space-64);
              }
            `}
            ref={previewRef}
          >
            <TemplateDocumentationSection documentation={documentation} />
          </div>
        </VStack>
      </HGrid>
    </VStack>
  );
}

function TemplateDocumentationSection({ documentation }: { documentation: TemplateDocumentation }) {
  return (
    <KnownDataClassesProvider templateModelSpecification={documentation.templateModelSpecification}>
      <Document templateDocumentation={documentation} />
      {documentation.attachments.map((attachment, index) => (
        <Document key={index} templateDocumentation={attachment} />
      ))}
    </KnownDataClassesProvider>
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
        margin-bottom: var(--ax-space-32);
      `}
      label="Språk"
      onChange={(event) => navigate({ replace: true, search: (s) => ({ ...s, language: event.target.value }) })}
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
