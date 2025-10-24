import { useCallback, useMemo } from "react";
import type { UseFormReturn } from "react-hook-form";

import { countUnfilledFritekstPlaceholders } from "~/Brevredigering/LetterEditor/actions/common";
import type { WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";
import {
  extractRelevantSaksbehandlerValgFields,
  useModelSpecificationForm,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import type { SaksbehandlerValg } from "~/types/brev";
import type { EditedLetter, PropertyUsage } from "~/types/brevbakerTypes";

interface UseBrevEditorWarningsParams<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }> {
  brevkode: string;
  form: UseFormReturn<FormSchema>;
  redigertBrev: EditedLetter;
  propertyUsage?: PropertyUsage[];
}

type WarningResult = { kind: WarnModalKind; count?: number } | null;

export function useBrevEditorWarnings<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }>({
  brevkode,
  form,
  redigertBrev,
  propertyUsage,
}: UseBrevEditorWarningsParams<FormSchema>) {
  const { status, specification, saksbehandlerValgType } = useModelSpecificationForm(brevkode);

  const relevantFields = useMemo(
    () => extractRelevantSaksbehandlerValgFields(propertyUsage ?? [], saksbehandlerValgType),
    [propertyUsage, saksbehandlerValgType],
  );

  const filteredSpecification = useMemo(() => {
    if (!specification) return undefined;

    if (relevantFields.size === 0) return specification;

    return Object.fromEntries(Object.entries(specification).filter(([key]) => relevantFields.has(key)));
  }, [specification, relevantFields]);

  const hasMissingRequiredSaksbehandlerValg = useCallback((): boolean => {
    if (status !== "success" || !filteredSpecification) return false;

    const values = form.getValues()?.saksbehandlerValg ?? {};

    return Object.entries(filteredSpecification).some(([key, fieldType]) => {
      if (fieldType.type !== "enum") return false;

      if (!(key in values)) return false;

      const value = values[key];
      return value == null || (typeof value === "string" && value.trim().length === 0);
    });
  }, [form, filteredSpecification, status]);

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
