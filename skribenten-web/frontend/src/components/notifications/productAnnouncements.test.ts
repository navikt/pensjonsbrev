import { describe, expect, test } from "vitest";

import { automaticTourUntil, listedUntil, type ProductAnnouncement } from "./productAnnouncements";

const announcement = (publishedAt: string): ProductAnnouncement => ({
  id: "test",
  publishedAt,
  title: "Tittel",
  description: "Beskrivelse",
  documentationUrl: "https://dokumentasjon.example",
});

describe("datoer for produktnyheter", () => {
  test("gjennomgangen starter automatisk i 30 dager etter publisering", () => {
    expect(automaticTourUntil(announcement("2026-11-10"))).toBe("2026-12-10");
  });

  test("nyheten vises i klokken i tre måneder etter publisering", () => {
    expect(listedUntil(announcement("2026-11-10"))).toBe("2027-02-10");
  });

  test("håndterer måneder med færre dager", () => {
    expect(listedUntil(announcement("2026-11-30"))).toBe("2027-02-28");
  });
});
