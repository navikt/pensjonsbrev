import { useCallback } from "react";
import type { UseFormReturn } from "react-hook-form";

import { countUnfilledFritekstPlaceholders } from "~/Brevredigering/LetterEditor/actions/common";
import type { WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";
import { useModelSpecificationForm } from "~/Brevredigering/ModelEditor/ModelEditor";
import type { SaksbehandlerValg } from "~/types/brev";
import type { EditedLetter } from "~/types/brevbakerTypes";

interface UseBrevEditorWarningsParams<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }> {
  brevkode: string;
  form: UseFormReturn<FormSchema>;
  redigertBrev: EditedLetter;
}

type WarningResult = { kind: WarnModalKind; count?: number } | null;

export function useBrevEditorWarnings<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }>({
  brevkode,
  form,
  redigertBrev,
}: UseBrevEditorWarningsParams<FormSchema>) {
  const { status, specification } = useModelSpecificationForm(brevkode);

  const hasMissingRequiredSaksbehandlerValg = useCallback((): boolean => {
    if (status !== "success" || !specification) return false;

    const values = form.getValues()?.saksbehandlerValg ?? {};

    return Object.entries(specification).some(([key, fieldType]) => {
      if (fieldType.type === "enum") {
        const value = values[key];
        return value == null || (typeof value === "string" && value.trim().length === 0);
      }
      return false;
    });
  }, [form, specification, status]);

  const numberOfUnfilledFritekstPlaceholders = useCallback(
    () => countUnfilledFritekstPlaceholders(redigertBrev),
    [redigertBrev],
  );

  const getWarning = useCallback((): WarningResult => {
    const unfilled = numberOfUnfilledFritekstPlaceholders();
    const missingRequired = hasMissingRequiredSaksbehandlerValg();

    if (unfilled > 0 && missingRequired) {
      return { kind: "fritekstOgTekstValg", count: unfilled };
    }
    if (unfilled > 0) {
      return { kind: "fritekst", count: unfilled };
    }
    if (missingRequired) {
      return { kind: "tekstValg" };
    }
    return null;
  }, [hasMissingRequiredSaksbehandlerValg, numberOfUnfilledFritekstPlaceholders]);

  return { getWarning, hasMissingRequiredSaksbehandlerValg, numberOfUnfilledFritekstPlaceholders };
}
