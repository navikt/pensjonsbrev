import { DownloadIcon } from "@navikt/aksel-icons";
import { Box, Button, HStack, InlineMessage, Loader, Select, VStack } from "@navikt/ds-react";
import { useState } from "react";

import { workshopApi } from "../api";
import { type Draft } from "../types";
import { useServerResult } from "../useServerResult";
import { ProblemMessage } from "./Problems";

function download(path: string, content: string) {
  const url = URL.createObjectURL(new Blob([content], { type: "text/plain;charset=utf-8" }));
  const a = document.createElement("a");
  a.href = url;
  a.download = path.split("/").at(-1) ?? "fil.kt";
  a.click();
  URL.revokeObjectURL(url);
}

/** Kode genereres alltid av serveren. Klienten viser bare resultatet og feilene. */
export function CodePanel({ draft }: { draft: Draft }) {
  const result = useServerResult(draft, () => workshopApi.generate(draft), true, 900);
  const [selected, setSelected] = useState<string>();
  const files = result.data?.files ?? [];
  const file = files.find((f) => f.path === selected) ?? files[0];
  return (
    <VStack gap="space-8">
      <HStack align="center" gap="space-8" justify="space-between">
        <strong>Generert kode</strong>
        <HStack align="center" gap="space-8">
          {result.loading ? <Loader size="xsmall" title="Genererer" /> : null}
          <Button onClick={result.refresh} size="xsmall" variant="tertiary">
            Generer på nytt
          </Button>
        </HStack>
      </HStack>
      {result.error ? <ProblemMessage error={result.error}>Generering feilet</ProblemMessage> : null}
      {result.stale && result.data ? (
        <InlineMessage status="info">Koden under er fra forrige versjon.</InlineMessage>
      ) : null}
      {result.data?.issues.map((issue) => (
        <InlineMessage key={`${issue.path}:${issue.code}:${issue.message}`} status="error">
          {issue.path ? `${issue.path}: ` : ""}
          {issue.message} ({issue.code})
        </InlineMessage>
      ))}
      {file ? (
        <>
          <HStack align="end" gap="space-8">
            <Select label="Fil" onChange={(e) => setSelected(e.target.value)} size="small" value={file.path}>
              {files.map((f) => (
                <option key={f.path} value={f.path}>
                  {f.path}
                </option>
              ))}
            </Select>
            <Button
              icon={<DownloadIcon aria-hidden />}
              onClick={() => download(file.path, file.content)}
              size="small"
              variant="secondary"
            >
              Last ned
            </Button>
            <Button onClick={() => void navigator.clipboard?.writeText(file.content)} size="small" variant="tertiary">
              Kopier
            </Button>
          </HStack>
          <Box background="neutral-soft" borderRadius="4" overflow="auto" padding="space-8">
            <pre style={{ margin: 0, whiteSpace: "pre" }}>
              <code>{file.content}</code>
            </pre>
          </Box>
        </>
      ) : result.data || result.error ? (
        <span>Ingen filer generert.</span>
      ) : (
        <span>Genererer…</span>
      )}
    </VStack>
  );
}
