import isEqual from "lodash/isEqual";
import { useEffect, useMemo, useRef } from "react";
import { useForm } from "react-hook-form";

import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type SaksbehandlerValg } from "~/types/brev";

export interface SaksbehandlerValgFormData {
  saksbehandlerValg: SaksbehandlerValg;
}

/**
 * Keeps the form for tekstvalg and overstyringer in sync with the letter editor. Valid changes are handed to
 * the editor, which debounces and merges them into the letter. Choices applied by the editor (merge, undo/redo,
 * reset) are written back to the form without overwriting values the user has changed since.
 */
export function useSaksbehandlerValgForm({
  isLocked,
  onBeforeChange,
}: {
  /** While locked, e.g. during the final submit, form changes are not handed to the editor. */
  isLocked: () => boolean;
  onBeforeChange: (valg: SaksbehandlerValg) => void;
}) {
  const { editorState, updateSaksbehandlerValg, registerLetterResetHandler } = useManagedLetterEditorContext();
  const editorValg = editorState.saksbehandlerValg;
  const defaultValues = useMemo(() => ({ saksbehandlerValg: { ...editorValg } }), [editorValg]);
  const form = useForm<SaksbehandlerValgFormData>({ defaultValues });

  // Only the latest validation may update the editor; an older one could otherwise apply outdated values.
  const validationRevision = useRef(0);
  const previousEditorValg = useRef(editorValg);

  const onValgChange = () => {
    if (isLocked()) return;
    const revision = ++validationRevision.current;
    const updatedValg = structuredClone(form.getValues().saksbehandlerValg);
    form.trigger().then((isValid) => {
      if (
        !isLocked() &&
        isValid &&
        revision === validationRevision.current &&
        isEqual(updatedValg, form.getValues().saksbehandlerValg) &&
        !isEqual(updatedValg, editorValg)
      ) {
        onBeforeChange(updatedValg);
        updateSaksbehandlerValg(updatedValg);
      }
    });
  };

  const cancelPendingChange = () => {
    validationRevision.current++;
  };

  useEffect(() => {
    const currentValg = form.getValues().saksbehandlerValg;
    const hasNewerFormValues =
      !isEqual(currentValg, previousEditorValg.current) && !isEqual(currentValg, defaultValues.saksbehandlerValg);
    form.reset(defaultValues, { keepDirtyValues: hasNewerFormValues });
    previousEditorValg.current = defaultValues.saksbehandlerValg;
  }, [defaultValues, form]);

  useEffect(() => {
    // Resetting to the template discards choices that are still only in the form, e.g. awaiting a field debounce.
    registerLetterResetHandler((response) => {
      validationRevision.current++;
      previousEditorValg.current = response.saksbehandlerValg;
      form.reset({ saksbehandlerValg: { ...response.saksbehandlerValg } });
    });
    return () => registerLetterResetHandler(null);
  }, [form, registerLetterResetHandler]);

  return { form, onValgChange, cancelPendingChange };
}
