import { describe, expect } from "vitest";

import { convertFieldToReadableLabel } from "~/Brevredigering/ModelEditor/components/utils";

describe("convertFieldToReadableLabel", () => {
  it("should split words by capital letters", () => {
    expect(convertFieldToReadableLabel("mottattFraUtland").toLowerCase()).toEqual("mottatt fra utland");
  });

  it("should capitalize first letter", () => {
    expect(convertFieldToReadableLabel("denGangDa")).toEqual("Den gang da");
  });

  it("should keep casing of acronyms", () => {
    expect(convertFieldToReadableLabel("harAFPOgUFO")).toEqual("Har AFP og UFO");
  });

  it("should replace æ", () => {
    expect(convertFieldToReadableLabel("aerekjaerFaerrest")).toEqual("Ærekjær færrest");
  });

  it("should replace ø", () => {
    expect(convertFieldToReadableLabel("oevrigeFoelelser")).toEqual("Øvrige følelser");
  });

  it("should replace å", () => {
    expect(convertFieldToReadableLabel("aaremaalPaaGang")).toEqual("Åremål på gang");
  });
});
