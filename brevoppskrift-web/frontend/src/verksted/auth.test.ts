import { QueryClient } from "@tanstack/react-query";
import { isRedirect } from "@tanstack/react-router";
import { afterEach, describe, expect, it, vi } from "vitest";

import { Route as EditorRoute } from "~/routes/arbeidsutkast.$draftId";
import { Route as ListRoute } from "~/routes/arbeidsutkast.index";
import { Route as CreateRoute } from "~/routes/opprett";

import { ApiProblem, workshopApi, workshopKeys } from "./api";
import { requireWorkshopLogin, workshopLoginUrl } from "./auth";

afterEach(() => vi.restoreAllMocks());

describe("workshop login", () => {
  function options(href: string, preload = false) {
    return { context: { queryClient: new QueryClient() }, location: { href }, preload };
  }

  it("protects each workshop entry route", () => {
    for (const route of [ListRoute, CreateRoute, EditorRoute]) {
      expect(route.options.beforeLoad).toBe(requireWorkshopLogin);
    }
  });

  it.each(["/arbeidsutkast", "/arbeidsutkast/draft-1?language=NN#innhold", "/opprett"])(
    "starts Wonderwall login on 401 and returns to %s",
    async (href) => {
      vi.spyOn(workshopApi, "me").mockRejectedValue(new ApiProblem({ message: "Unauthorized", status: 401 }));
      const result = await requireWorkshopLogin(options(href)).catch((error: unknown) => error);
      expect(isRedirect(result)).toBe(true);
      if (isRedirect(result)) {
        expect(result.options.href).toBe(`/oauth2/login?redirect=${encodeURIComponent(href)}`);
        expect(result.options.reloadDocument).toBe(true);
      }
    },
  );

  it("does not start login or call the API when preloading a link", async () => {
    const me = vi.spyOn(workshopApi, "me");
    await requireWorkshopLogin(options("/arbeidsutkast", true));
    expect(me).not.toHaveBeenCalled();
  });

  it("allows authenticated readers and refreshes stale identity before entry", async () => {
    const me = { navIdent: "Z000000", name: "Test", canEdit: false };
    const fetch = vi.spyOn(workshopApi, "me").mockResolvedValue(me);
    const input = options("/arbeidsutkast");
    input.context.queryClient.setQueryData(workshopKeys.me, { ...me, canEdit: true });
    await requireWorkshopLogin(input);
    expect(fetch).toHaveBeenCalledOnce();
    expect(input.context.queryClient.getQueryData(workshopKeys.me)).toEqual(me);
  });

  it.each([403, 503])("keeps %s as a recoverable error rather than a login loop", async (status) => {
    vi.spyOn(workshopApi, "me").mockRejectedValue(new ApiProblem({ message: "Failed", status }));
    await expect(requireWorkshopLogin(options("/opprett"))).resolves.toBeUndefined();
  });

  it.each(["https://other.invalid/arbeidsutkast", "//other.invalid/opprett", "/templates"])(
    "never redirects login back to %s",
    (href) => {
      expect(workshopLoginUrl(href)).toBe("/oauth2/login?redirect=%2Farbeidsutkast");
    },
  );
});
