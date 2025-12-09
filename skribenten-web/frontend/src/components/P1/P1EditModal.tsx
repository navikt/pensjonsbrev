import "~/css/p1.css";

import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod/dist/zod";
import { Alert, Button, Heading, Loader, Modal, Tabs } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { type FieldErrors, FormProvider, useForm } from "react-hook-form";

import { getBrev, p1OverrideKeys, saveP1Override } from "~/api/brev-queries";
import type { P1Redigerbar } from "~/types/p1";
import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { P1AvslagTab } from "./P1AvslagTab";
import { P1ForsikredeTab } from "./P1ForsikredeTab";
import { P1InnehaverTab } from "./P1InnehaverTab";
import { P1InnvilgetTab } from "./P1InnvilgetTab";
import { P1InstitusjonTab } from "./P1InstitusjonTab";
import { mapP1DtoToForm, mapP1FormToDto } from "./p1Mappings";
import { p1RedigerbarFormSchema } from "./p1ValidationSchema";
import { useP1Override } from "./useP1Override";

type P1TabKey = "innehaver" | "forsikret" | "innvilget" | "avslag" | "institusjon";

type P1EditingModalProps = {
  brevId: number;
  saksId: string;
  open: boolean;
  onClose: () => void;
};

export const P1EditModal = ({ brevId, saksId, open, onClose }: P1EditingModalProps) => {
  const [activeTab, setActiveTab] = useState<P1TabKey>("innvilget");
  const [validationError, setValidationError] = useState<string | null>(null);
  const queryClient = useQueryClient();

  const formMethods = useForm<P1RedigerbarForm>({
    resolver: zodResolver(p1RedigerbarFormSchema),
    mode: "onBlur",
  });

  const {
    handleSubmit,
    reset,
    formState: { errors },
  } = formMethods;

  // Load P1 data when modal is open
  const { data: p1Override, isLoading: isP1Loading, isError: isP1Error } = useP1Override(saksId, brevId, open);

  // When P1 data arrives, reset form with mapped values
  useEffect(() => {
    if (p1Override) {
      reset(mapP1DtoToForm(p1Override));
    }
  }, [p1Override, reset]);

  const lagreMutation = useMutation({
    mutationFn: (formValues: P1RedigerbarForm) => {
      const dto: P1Redigerbar = mapP1FormToDto(formValues);
      return saveP1Override(saksId, brevId, dto);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: p1OverrideKeys.id(brevId) });
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(brevId) });
      onClose();
    },
  });

  const onSubmit = (values: P1RedigerbarForm) => {
    setValidationError(null);
    lagreMutation.mutate(values);
  };

  const onValidationError = (fieldErrors: FieldErrors<P1RedigerbarForm>) => {
    // Find tabs with errors and navigate to first one
    const tabsWithErrors: { tab: P1TabKey; label: string }[] = [];

    if (fieldErrors.innehaver) tabsWithErrors.push({ tab: "innehaver", label: "1. Personopplysninger om innehaveren" });
    if (fieldErrors.forsikrede)
      tabsWithErrors.push({ tab: "forsikret", label: "2. Personopplysninger om den forsikrede" });
    if (fieldErrors.innvilgedePensjoner) tabsWithErrors.push({ tab: "innvilget", label: "3. Innvilget pensjon" });
    if (fieldErrors.avslaattePensjoner) tabsWithErrors.push({ tab: "avslag", label: "4. Avslag på pensjon" });
    if (fieldErrors.utfyllendeInstitusjon)
      tabsWithErrors.push({ tab: "institusjon", label: "5. Institusjonen som har fylt ut skjemaet" });

    if (tabsWithErrors.length > 0) {
      // Navigate to first tab with errors
      setActiveTab(tabsWithErrors[0].tab);

      // Show which tabs have errors
      const errorLabels = tabsWithErrors.map((t) => t.label).join(", ");
      setValidationError(`Skjemaet har feil som må rettes: ${errorLabels}`);
    }
  };

  const handleCancel = () => {
    setValidationError(null);
    onClose();
  };

  // Check which tabs have errors for visual indicator
  const hasInnehaverError = !!errors.innehaver;
  const hasForsikredeError = !!errors.forsikrede;
  const hasInnvilgetError = !!errors.innvilgedePensjoner;
  const hasAvslagError = !!errors.avslaattePensjoner;
  const hasInstitusjonError = !!errors.utfyllendeInstitusjon;

  const isInitialLoading = isP1Loading && !p1Override;

  return (
    <Modal aria-label="Rediger vedlegg P1" className="p1-modal" onClose={handleCancel} open={open} size="medium">
      <Modal.Header>
        <Heading size="medium">Overstyring av vedlegg – P1 samlet melding om pensjonsvedtak</Heading>
      </Modal.Header>

      <FormProvider {...formMethods}>
        <form
          css={css`
            display: flex;
            flex-direction: column;
            flex: 1;
            min-height: 0;
          `}
          onSubmit={handleSubmit(onSubmit, onValidationError)}
        >
          <Modal.Body
            css={css`
              flex: 1 1 auto;
              overflow: auto;
            `}
          >
            {isInitialLoading ? (
              <div className="p1-loader-container">
                <Loader size="3xlarge" title="Laster data for P1..." />
              </div>
            ) : isP1Error ? (
              <Alert variant="error">Kunne ikke laste P1-data. Prøv igjen senere.</Alert>
            ) : (
              <>
                {validationError && (
                  <Alert
                    closeButton
                    css={css`
                      margin-bottom: 1rem;
                    `}
                    onClose={() => setValidationError(null)}
                    size="small"
                    variant="error"
                  >
                    {validationError}
                  </Alert>
                )}

                <Tabs onChange={(v) => setActiveTab(v as P1TabKey)} value={activeTab}>
                  <Tabs.List>
                    <Tabs.Tab
                      label={<TabLabel hasError={hasInnehaverError} label="1. Personopplysninger om innehaveren" />}
                      value="innehaver"
                    />
                    <Tabs.Tab
                      label={<TabLabel hasError={hasForsikredeError} label="2. Personopplysninger om den forsikrede" />}
                      value="forsikret"
                    />
                    <Tabs.Tab
                      label={<TabLabel hasError={hasInnvilgetError} label="3. Innvilget pensjon" />}
                      value="innvilget"
                    />
                    <Tabs.Tab
                      label={<TabLabel hasError={hasAvslagError} label="4. Avslag på pensjon" />}
                      value="avslag"
                    />
                    <Tabs.Tab
                      label={
                        <TabLabel hasError={hasInstitusjonError} label="5. Institusjonen som har fylt ut skjemaet" />
                      }
                      value="institusjon"
                    />
                  </Tabs.List>

                  <Tabs.Panel className="p1-tabs-panel" value="innehaver">
                    <P1InnehaverTab disabled />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="forsikret">
                    <P1ForsikredeTab disabled />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="innvilget">
                    <P1InnvilgetTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="avslag">
                    <P1AvslagTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="institusjon">
                    <P1InstitusjonTab disabled />
                  </Tabs.Panel>
                </Tabs>

                {lagreMutation.isError && (
                  <Alert
                    css={css`
                      margin-top: 1rem;
                    `}
                    size="small"
                    variant="error"
                  >
                    Noe gikk galt ved lagring av P1. Prøv igjen.
                  </Alert>
                )}
              </>
            )}
          </Modal.Body>

          <Modal.Footer
            css={css`
              justify-content: space-between;
              flex-shrink: 0;
            `}
          >
            <Button
              disabled={isInitialLoading || isP1Error}
              loading={lagreMutation.isPending}
              size="medium"
              type="submit"
              variant="primary"
            >
              Lagre
            </Button>
            <Button
              css={css`
                && {
                  margin-left: 0;
                }
              `}
              disabled={lagreMutation.isPending}
              onClick={handleCancel}
              type="button"
              variant="tertiary"
            >
              Avbryt
            </Button>
          </Modal.Footer>
        </form>
      </FormProvider>
    </Modal>
  );
};

// Helper component for tab labels with error indicator
const TabLabel = ({ label, hasError }: { label: string; hasError: boolean }) => (
  <span className={hasError ? "p1-tab-error" : ""}>
    {label}
    {hasError && <span aria-label="Har feil" className="p1-tab-error-dot" />}
  </span>
);
