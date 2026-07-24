import { useCallback, useMemo } from "react";
import { type UseFormReturn } from "react-hook-form";

import {
  countMissingFromTemplateBlocks,
  countUneditedFritekstPlaceholders,
} from "~/Brevredigering/LetterEditor/actions/common";
import { type WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";
import {
  filterModelSpecificationByPropertyUsage,
  useModelSpecificationForm,
} from "~/Brevredigering/ModelEditor/ModelEditor";
import { type SaksbehandlerValg } from "~/types/brev";
import { type EditedLetter, type PropertyUsage } from "~/types/brevbakerTypes";

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

  const filteredSpecification = useMemo(
    () => filterModelSpecificationByPropertyUsage(specification, propertyUsage, saksbehandlerValgType),
    [specification, propertyUsage, saksbehandlerValgType],
  );

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

  const numberOfUneditedFritekstPlaceholders = useCallback(
    () => countUneditedFritekstPlaceholders(redigertBrev),
    [redigertBrev],
  );

  const numberOfMissingFromTemplateBlocks = useCallback(
    () => countMissingFromTemplateBlocks(redigertBrev),
    [redigertBrev],
  );

  const getWarning = useCallback((): WarningResult => {
    const unedited = numberOfUneditedFritekstPlaceholders();
    const missingRequired = hasMissingRequiredSaksbehandlerValg();
    const missingFromTemplate = numberOfMissingFromTemplateBlocks();

    if (unedited > 0 && missingRequired) {
      return { kind: "fritekstOgTekstValg", count: unedited };
    }
    if (unedited > 0) {
      return { kind: "fritekst", count: unedited };
    }
    if (missingRequired) {
      return { kind: "tekstValg" };
    }
    if (missingFromTemplate > 0) {
      return { kind: "avsnittIkkeIMal", count: missingFromTemplate };
    }
    return null;
  }, [hasMissingRequiredSaksbehandlerValg, numberOfUneditedFritekstPlaceholders, numberOfMissingFromTemplateBlocks]);

  return { getWarning, hasMissingRequiredSaksbehandlerValg, numberOfUneditedFritekstPlaceholders };
}
