import { useRef, useState } from "react";
import { type FieldValues, type UseFormReturn } from "react-hook-form";

import { type WarnModalKind } from "~/Brevredigering/LetterEditor/components/warnModal";

type Warning = { kind: WarnModalKind; count?: number } | null;

/**
 * Shared warn-modal-guarded submit flow: validates the form, checks `getWarning()` for a
 * blocking warning (e.g. unfilled fritekst / missing tekstvalg), and if present opens the warn
 * modal and stashes the pending values instead of submitting immediately. Confirming in the modal
 * ("Fortsett") replays the stashed submit; the actual submit action (autosave vs. attestering, and
 * where to navigate afterwards) stays fully at the call site via `onConfirmedSubmit`.
 */
export function useGuardedFormSubmit<TFormData extends FieldValues>({
  form,
  getWarning,
  onConfirmedSubmit,
}: {
  form: UseFormReturn<TFormData>;
  getWarning: () => Warning;
  onConfirmedSubmit: (values: TFormData) => void;
}) {
  const [warnOpen, setWarnOpen] = useState(false);
  const [warn, setWarn] = useState<Warning>(null);
  const pendingSubmitValuesRef = useRef<TFormData | null>(null);

  const guardedSubmit = form.handleSubmit((values) => {
    const warning = getWarning();
    if (warning) {
      pendingSubmitValuesRef.current = values;
      setWarn(warning);
      setWarnOpen(true);
      return;
    }
    pendingSubmitValuesRef.current = null;
    onConfirmedSubmit(values);
  });

  const warnModalProps = {
    open: warnOpen,
    kind: warn?.kind ?? ("fritekst" as WarnModalKind),
    count: warn?.count ?? 0,
    onClose: () => {
      pendingSubmitValuesRef.current = null;
      setWarnOpen(false);
      setWarn(null);
    },
    onFortsett: () => {
      const values = pendingSubmitValuesRef.current;
      pendingSubmitValuesRef.current = null;
      setWarnOpen(false);
      setWarn(null);
      if (!values) return;
      onConfirmedSubmit(values);
    },
  };

  return { guardedSubmit, warnModalProps };
}
