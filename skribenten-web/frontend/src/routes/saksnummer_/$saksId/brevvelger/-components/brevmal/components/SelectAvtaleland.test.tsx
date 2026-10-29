import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, cleanup, render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { FormProvider, useForm, useWatch } from "react-hook-form";
import { afterEach, expect, test } from "vitest";

import { getAvtaleLand } from "~/api/skribenten-api-endpoints";

import SelectAvtaleland from "./SelectAvtaleland";

afterEach(cleanup);

function CountryForm() {
  const form = useForm({ defaultValues: { landkode: "NOR" } });
  const country = useWatch({ control: form.control, name: "landkode" });
  return (
    <FormProvider {...form}>
      <SelectAvtaleland />
      <output data-testid="landkode">{country}</output>
    </FormProvider>
  );
}

test("beholder standardverdi og manuelt valg ved lasting og oppdatering uten å endre cache", async () => {
  const user = userEvent.setup();
  const queryClient = new QueryClient({ defaultOptions: { queries: { enabled: false } } });
  render(
    <QueryClientProvider client={queryClient}>
      <CountryForm />
    </QueryClientProvider>,
  );
  const select = screen.getByRole("combobox", { name: "Land" });
  expect(screen.getAllByRole("option").map((option) => option.textContent)).toEqual(["Velg land"]);
  expect(screen.getByTestId("landkode").textContent).toBe("NOR");

  const options = Object.freeze([
    { kode: "BEL", navn: "Belgia" },
    { kode: "NOR", navn: "Norge" },
    { kode: "GBR", navn: "Storbritannia" },
  ]);
  act(() => {
    queryClient.setQueryData(getAvtaleLand.queryKey, options);
  });
  await waitFor(() => expect(screen.getByDisplayValue("Norge")).toBe(select));
  expect(screen.getAllByRole("option").map((option) => option.textContent)).toEqual([
    "Velg land",
    "Norge",
    "Belgia",
    "Storbritannia",
  ]);
  expect(queryClient.getQueryData(getAvtaleLand.queryKey)).toBe(options);
  expect(options.map((option) => option.kode)).toEqual(["BEL", "NOR", "GBR"]);

  await user.selectOptions(select, "GBR");
  act(() => {
    queryClient.setQueryData(getAvtaleLand.queryKey, [...options, { kode: "SWE", navn: "Sverige" }]);
  });
  await screen.findByRole("option", { name: "Sverige" });
  expect(screen.getByDisplayValue("Storbritannia")).toBe(select);
  expect(screen.getByTestId("landkode").textContent).toBe("GBR");

  await user.selectOptions(select, "");
  expect(screen.getByTestId("landkode").textContent).toBe("");
  queryClient.clear();
});
