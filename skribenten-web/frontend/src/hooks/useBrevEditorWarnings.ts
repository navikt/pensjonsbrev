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
import { type EditedDocument, type PropertyUsage } from "~/types/brevbakerTypes";

interface UseBrevEditorWarningsParams<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }> {
  brevkode: string;
  form: UseFormReturn<FormSchema>;
  redigertBrev: EditedDocument;
  propertyUsage?: PropertyUsage[];
  warnAboutMissingFromTemplate?: boolean;
  missingFromTemplateVedleggCount?: number;
}

type WarningResult = { kind: WarnModalKind; count?: number } | null;

export function useBrevEditorWarnings<FormSchema extends { saksbehandlerValg: SaksbehandlerValg }>({
  brevkode,
  form,
  redigertBrev,
  propertyUsage,
  warnAboutMissingFromTemplate = true,
  missingFromTemplateVedleggCount = 0,
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
    const missingFromTemplate = numberOfMissingFromTemplateBlocks() + missingFromTemplateVedleggCount;

    if (unedited > 0 && missingRequired) {
      return { kind: "fritekstOgTekstValg", count: unedited };
    }
    if (unedited > 0) {
      return { kind: "fritekst", count: unedited };
    }
    if (missingRequired) {
      return { kind: "tekstValg" };
    }
    if (warnAboutMissingFromTemplate && missingFromTemplate > 0) {
      return { kind: "avsnittIkkeIMal", count: missingFromTemplate };
    }
    return null;
  }, [
    hasMissingRequiredSaksbehandlerValg,
    numberOfUneditedFritekstPlaceholders,
    numberOfMissingFromTemplateBlocks,
    warnAboutMissingFromTemplate,
    missingFromTemplateVedleggCount,
  ]);

  return { getWarning, hasMissingRequiredSaksbehandlerValg, numberOfUneditedFritekstPlaceholders };
}
