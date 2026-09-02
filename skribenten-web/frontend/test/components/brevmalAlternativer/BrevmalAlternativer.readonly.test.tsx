import { fireEvent, render, screen } from "@testing-library/react";
import { FormProvider, useForm, useWatch } from "react-hook-form";
import { describe, expect, test, vi } from "vitest";

import BrevmalAlternativer from "~/components/brevmalAlternativer/BrevmalAlternativer";
import { type LetterModelSpecification } from "~/types/brevbakerTypes";

const BREVKODE = "EN_BREVKODE";
const SAKSBEHANDLER_VALG_TYPE = "no.nav.pensjon.brev.EnDto.SaksbehandlerValg";

const specification = {
  letterModelTypeName: "no.nav.pensjon.brev.EnDto",
  types: {
    "no.nav.pensjon.brev.EnDto": {
      saksbehandlerValg: { type: "object", nullable: false, typeName: SAKSBEHANDLER_VALG_TYPE },
    },
    [SAKSBEHANDLER_VALG_TYPE]: {
      enTekst: { type: "scalar", nullable: true, kind: "STRING", displayText: "En tekst" },
      etFlagg: { type: "scalar", nullable: false, kind: "BOOLEAN", displayText: "Et flagg" },
    },
  },
} as unknown as LetterModelSpecification;

vi.mock("~/api/brev-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brev-queries")>()),
  useModelSpecification: <T,>(_brevkode: string, select: (data: LetterModelSpecification) => T) => ({
    status: "success" as const,
    specification: select(specification),
    error: null,
  }),
}));

const ValgSpion = () => {
  const valg = useWatch({ name: "saksbehandlerValg" });
  return <output data-testid="valg">{JSON.stringify(valg)}</output>;
};

const Wrapper = (props: { readOnly?: boolean }) => {
  const form = useForm({ defaultValues: { saksbehandlerValg: { enTekst: "En verdi", etFlagg: true } } });

  return (
    <FormProvider {...form}>
      <BrevmalAlternativer brevkode={BREVKODE} readOnly={props.readOnly} />
      <ValgSpion />
    </FormProvider>
  );
};

describe("<BrevmalAlternativer readOnly />", () => {
  test("attestanten ser verdiene, men kan ikke endre dem", () => {
    render(<Wrapper readOnly />);

    const tekstfelt = screen.getByDisplayValue("En verdi") as HTMLInputElement;
    expect(tekstfelt.readOnly).toBe(true);

    fireEvent.click(screen.getByRole("checkbox"));
    expect(screen.getByTestId("valg").textContent).toBe(JSON.stringify({ enTekst: "En verdi", etFlagg: true }));
  });

  test("saksbehandleren kan fortsatt endre valgene", () => {
    render(<Wrapper />);

    const tekstfelt = screen.getByDisplayValue("En verdi") as HTMLInputElement;
    expect(tekstfelt.readOnly).toBe(false);

    fireEvent.click(screen.getByRole("checkbox"));
    expect(screen.getByTestId("valg").textContent).toBe(JSON.stringify({ enTekst: "En verdi", etFlagg: false }));
  });
});
