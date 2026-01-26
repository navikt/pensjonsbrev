import "~/css/p1.css";

import { zodResolver } from "@hookform/resolvers/zod/dist/zod";
import { Alert, BoxNew, Button, Heading, HStack, Loader, Modal, Tabs, VStack } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useRef, useState } from "react";
import { type FieldErrors, FormProvider, useForm } from "react-hook-form";

import { getBrev, getP1Override, saveP1Override } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { useLandData, useLandDataP1 } from "~/hooks/useLandData";
import type { P1Redigerbar } from "~/types/p1";
import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { ApiError } from "../ApiError";
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
  const [saveSuccess, setSaveSuccess] = useState(false);
  const successTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const queryClient = useQueryClient();

  // Cleanup timeout on unmount
  useEffect(() => {
    return () => {
      if (successTimeoutRef.current) {
        clearTimeout(successTimeoutRef.current);
      }
    };
  }, []);

  const formMethods = useForm<P1RedigerbarForm>({
    resolver: zodResolver(p1RedigerbarFormSchema),
    mode: "onBlur",
    reValidateMode: "onBlur",
  });

  const {
    handleSubmit,
    reset,
    formState: { errors },
  } = formMethods;

  // Track if form has been initialized with data
  const formInitializedRef = useRef(false);

  // Load P1 data when modal is open
  const {
    data: p1Data,
    isLoading: isP1Loading,
    isError: isP1Error,
    error: p1Error,
  } = useP1Override(saksId, brevId, open);

  // When P1 data arrives, reset form with mapped values (only once)
  useEffect(() => {
    if (p1Data && !formInitializedRef.current) {
      reset(mapP1DtoToForm(p1Data));
      formInitializedRef.current = true;
    }
  }, [p1Data, reset]);

  // Reset the ref when modal closes
  useEffect(() => {
    if (!open) {
      formInitializedRef.current = false;
    }
  }, [open]);

  const { data: landListe } = useLandDataP1();

  const lagreMutation = useMutation({
    mutationFn: (formValues: P1RedigerbarForm) => {
      const dto: P1Redigerbar = mapP1FormToDto(formValues);
      return saveP1Override(saksId, brevId, dto);
    },
    onSuccess: (_, savedFormValues) => {
      // Reset form with the values we just saved to mark it as clean (not dirty)
      // ie makes the isDirty false again
      reset(savedFormValues);

      queryClient.invalidateQueries({ queryKey: hentPdfForBrev.queryKey(brevId) });
      queryClient.invalidateQueries({ queryKey: getP1Override.queryKey(brevId) });
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(brevId) });
      setSaveSuccess(true);
      // Clear any existing timeout before setting a new one
      if (successTimeoutRef.current) {
        clearTimeout(successTimeoutRef.current);
      }
      // Auto-hide success message after 3 seconds
      successTimeoutRef.current = setTimeout(() => setSaveSuccess(false), 3000);
    },
  });

  const onSubmit = (values: P1RedigerbarForm) => {
    setValidationError(null);
    setSaveSuccess(false);
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
    } else if (Object.keys(fieldErrors).length > 0) {
      // Fallback: If we have errors but couldn't map them to a tab (e.g. sakstype, root errors)
      setValidationError(`Skjemaet kan ikke lagres pga. ugyldig data (felt: ${Object.keys(fieldErrors).join(", ")})`);
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

  const isInitialLoading = isP1Loading && !p1Data;

  return (
    <VStack asChild height="85vh" maxHeight="85vh" maxWidth="85vw" width="85vw">
      <Modal
        aria-label="Rediger vedlegg P1"
        className="p1-modal"
        data-cy="p1-edit-modal"
        onClose={handleCancel}
        open={open}
        size="medium"
      >
        <Modal.Header>
          <Heading size="small">Overstyring av vedlegg - P1 samlet melding om pensjonsvedtak</Heading>
        </Modal.Header>

        <FormProvider {...formMethods}>
          <VStack asChild flexGrow="1" minHeight="0">
            <form onSubmit={handleSubmit(onSubmit, onValidationError)}>
              <VStack asChild flexGrow="1" overflow="hidden" padding="0">
                <Modal.Body>
                  {isInitialLoading ? (
                    <VStack align="center" flexGrow="1" justify="center">
                      <Loader size="3xlarge" title="Laster data for P1..." />
                    </VStack>
                  ) : isP1Error ? (
                    <VStack align="center" flexGrow="1" justify="center">
                      <ApiError error={p1Error} title="Kunne ikke laste P1-data" />
                    </VStack>
                  ) : (
                    <>
                      {saveSuccess && (
                        <BoxNew asChild marginBlock="0 space-8" marginInline="space-20">
                          <Alert size="small" variant="success">
                            Endringene ble lagret
                          </Alert>
                        </BoxNew>
                      )}
                      {validationError && (
                        <BoxNew asChild marginBlock="0 space-8" marginInline="space-20">
                          <Alert closeButton onClose={() => setValidationError(null)} size="small" variant="error">
                            {validationError}
                          </Alert>
                        </BoxNew>
                      )}

                      <VStack asChild overflow="hidden">
                        <Tabs onChange={(v) => setActiveTab(v as P1TabKey)} size="small" value={activeTab}>
                          <BoxNew asChild paddingInline="space-20">
                            <Tabs.List>
                              <Tabs.Tab
                                label={
                                  <TabLabel hasError={hasInnehaverError} label="1. Personopplysninger om innehaveren" />
                                }
                                value="innehaver"
                              />
                              <Tabs.Tab
                                label={
                                  <TabLabel
                                    hasError={hasForsikredeError}
                                    label="2. Personopplysninger om den forsikrede"
                                  />
                                }
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
                                  <TabLabel
                                    hasError={hasInstitusjonError}
                                    label="5. Institusjonen som har fylt ut skjemaet"
                                  />
                                }
                                value="institusjon"
                              />
                            </Tabs.List>
                          </BoxNew>

                          <Tabs.Panel className="p1-tabs-panel" value="innehaver">
                            <P1InnehaverTab disabled />
                          </Tabs.Panel>

                          <Tabs.Panel className="p1-tabs-panel" value="forsikret">
                            <P1ForsikredeTab disabled />
                          </Tabs.Panel>

                          <Tabs.Panel className="p1-tabs-panel" value="innvilget">
                            <P1InnvilgetTab landListe={landListe || []} />
                          </Tabs.Panel>

                          <Tabs.Panel className="p1-tabs-panel" value="avslag">
                            <P1AvslagTab landListe={landListe || []} />
                          </Tabs.Panel>

                          <Tabs.Panel className="p1-tabs-panel" value="institusjon">
                            <P1InstitusjonTab disabled />
                          </Tabs.Panel>
                        </Tabs>
                      </VStack>

                      {lagreMutation.isError && (
                        <BoxNew asChild marginBlock="space-16 0" marginInline="space-20">
                          <Alert size="small" variant="error">
                            Noe gikk galt ved lagring av P1. Prøv igjen.
                          </Alert>
                        </BoxNew>
                      )}
                    </>
                  )}
                </Modal.Body>
              </VStack>

              <HStack asChild justify="space-between">
                <Modal.Footer>
                  <Button disabled={lagreMutation.isPending} onClick={handleCancel} type="button" variant="tertiary">
                    Avbryt
                  </Button>
                  <SubmitButton isInitialLoading={isInitialLoading} isLoading={lagreMutation.isPending} />
                </Modal.Footer>
              </HStack>
            </form>
          </VStack>
        </FormProvider>
      </Modal>
    </VStack>
  );
};

// Helper component for tab labels with error indicator
const TabLabel = ({ label, hasError }: { label: string; hasError: boolean }) => (
  <>
    <BoxNew as="span" width={hasError ? "calc(100% - 8px)" : "100%"}>
      {label}
    </BoxNew>
    {hasError && (
      <BoxNew aria-label="Har feil" as="div" background="danger-strong" borderRadius="full" height="8px" width="8px" />
    )}
  </>
);

// Submit button - always enabled after initial load to avoid UX confusion
interface SubmitButtonProps {
  isLoading: boolean;
  isInitialLoading: boolean;
}

const SubmitButton = ({ isLoading, isInitialLoading }: SubmitButtonProps) => {
  return (
    <Button disabled={isInitialLoading} loading={isLoading} size="medium" type="submit" variant="primary">
      Lagre
    </Button>
  );
};
