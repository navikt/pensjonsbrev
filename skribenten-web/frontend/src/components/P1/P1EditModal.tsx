import "~/css/p1.css";

import { css } from "@emotion/react";
import { zodResolver } from "@hookform/resolvers/zod/dist/zod";
import { Alert, Button, Heading, Loader, Modal, Tabs } from "@navikt/ds-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { getBrev } from "~/api/brev-queries";
import { p1OverrideKeys, saveP1Override } from "~/api/brev-queries";
import type { P1Redigerbar } from "~/types/p1";
import type { P1RedigerbarForm } from "~/types/p1FormTypes";

import { emptyP1 } from "./emptyP1";
import { filledP1 } from "./filledP1";
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
  const [activeTab, setActiveTab] = useState<P1TabKey>("innehaver");
  const queryClient = useQueryClient();

  const formMethods = useForm<P1RedigerbarForm>({
    defaultValues: filledP1,
    resolver: zodResolver(p1RedigerbarFormSchema),
    mode: "onBlur", // Validate on blur for better UX
  });

  const { handleSubmit, reset } = formMethods;

  // Load or create P1 override when modal is open
  const { data: p1Override, isLoading: isP1Loading, isError: isP1Error } = useP1Override(saksId, brevId, open);

  // When P1 data arrives, reset form with mapped values
  // useEffect(() => {
  //   if (p1Override) {
  //     reset(mapP1DtoToForm(p1Override));
  //   }
  // }, [p1Override, reset]);

  const lagreMutation = useMutation({
    mutationFn: (formValues: P1RedigerbarForm) => {
      const dto: P1Redigerbar = mapP1FormToDto(formValues);
      return saveP1Override(saksId, brevId, dto);
    },
    onSuccess: () => {
      // Invalidate P1 override + brev
      queryClient.invalidateQueries({ queryKey: p1OverrideKeys.id(brevId) });
      queryClient.invalidateQueries({ queryKey: getBrev.queryKey(brevId) });
      onClose();
    },
  });

  const onSubmit = (values: P1RedigerbarForm) => {
    lagreMutation.mutate(values);
  };

  const handleCancel = () => {
    onClose();
  };

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
          onSubmit={handleSubmit(onSubmit)}
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
            ) : (
              <>
                <Tabs onChange={(v) => setActiveTab(v as P1TabKey)} value={activeTab}>
                  <Tabs.List>
                    <Tabs.Tab label="1. Personopplysninger om innehaveren" value="innehaver" />
                    <Tabs.Tab label="2. Personopplysninger om den forsikrede" value="forsikret" />
                    <Tabs.Tab label="3. Innvilget pensjon" value="innvilget" />
                    <Tabs.Tab label="4. Avslag på pensjon" value="avslag" />
                    <Tabs.Tab label="5. Institusjonen som har fylt ut skjemaet" value="institusjon" />
                  </Tabs.List>

                  <Tabs.Panel className="p1-tabs-panel" value="innehaver">
                    <P1InnehaverTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="forsikret">
                    <P1ForsikredeTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="innvilget">
                    <P1InnvilgetTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="avslag">
                    <P1AvslagTab />
                  </Tabs.Panel>

                  <Tabs.Panel className="p1-tabs-panel" value="institusjon">
                    <P1InstitusjonTab />
                  </Tabs.Panel>
                </Tabs>

                {(isP1Error || lagreMutation.isError) && (
                  <Alert
                    css={css`
                      margin-top: 1rem;
                    `}
                    size="small"
                    variant="error"
                  >
                    Noe gikk galt ved lasting eller lagring av P1.
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
              disabled={isInitialLoading}
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
