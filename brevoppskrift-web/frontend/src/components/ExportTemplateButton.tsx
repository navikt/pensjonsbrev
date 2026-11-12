import { FloppydiskIcon } from "@navikt/aksel-icons";
import { Button, InlineMessage, VStack } from "@navikt/ds-react";
import { useState } from "react";

import { type TemplateExport } from "~/word/exportTemplate";

export function ExportTemplateButton(props: TemplateExport) {
  const [exporting, setExporting] = useState(false);
  const [error, setError] = useState<unknown>();

  async function download() {
    setExporting(true);
    setError(undefined);
    try {
      const { createTemplateWordDocument } = await import("~/word/exportTemplate");
      const blob = await createTemplateWordDocument(props);
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `${props.templateId}-${props.language}.docx`.replace(/[/\\:*?"<>|]/g, "_");
      document.body.append(link);
      link.click();
      link.remove();
      // The browser must have time to start reading the download before the URL is released.
      setTimeout(() => URL.revokeObjectURL(url), 60_000);
    } catch (error) {
      setError(error);
    } finally {
      setExporting(false);
    }
  }

  return (
    <VStack align="end" gap="space-4">
      <Button
        icon={<FloppydiskIcon aria-hidden />}
        loading={exporting}
        onClick={download}
        size="small"
        title="Last ned brevoppskrift som Word"
        type="button"
        variant="secondary"
      >
        Last ned
      </Button>
      {error !== undefined && (
        <InlineMessage role="alert" size="small" status="error">
          Kunne ikke eksportere brevoppskriften til Word. Prøv igjen.
        </InlineMessage>
      )}
    </VStack>
  );
}
