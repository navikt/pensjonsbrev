import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import {
  findMissingPdfjsFeatures,
  shortBrowserName,
  trackMissingPdfjsSupport,
} from "~/utils/pdfjsBrowserSupportTracking";

const trackMock = vi.fn();

const prototypeFeatures = ["toHex", "toBase64", "setFromBase64", "setFromHex"] as const;
const staticFeatures = ["fromBase64"] as const;

const originalDescriptors = {
  prototype: Object.fromEntries(
    prototypeFeatures.map((name) => [name, Object.getOwnPropertyDescriptor(Uint8Array.prototype, name)]),
  ),
  static: Object.fromEntries(staticFeatures.map((name) => [name, Object.getOwnPropertyDescriptor(Uint8Array, name)])),
};

const stubAllFeatures = () => {
  for (const name of prototypeFeatures) {
    Object.defineProperty(Uint8Array.prototype, name, { value: () => {}, configurable: true, writable: true });
  }
  for (const name of staticFeatures) {
    Object.defineProperty(Uint8Array, name, { value: () => {}, configurable: true, writable: true });
  }
};

const restoreFeatures = () => {
  for (const [name, descriptor] of Object.entries(originalDescriptors.prototype)) {
    if (descriptor) Object.defineProperty(Uint8Array.prototype, name, descriptor);
    else Reflect.deleteProperty(Uint8Array.prototype, name);
  }
  for (const [name, descriptor] of Object.entries(originalDescriptors.static)) {
    if (descriptor) Object.defineProperty(Uint8Array, name, descriptor);
    else Reflect.deleteProperty(Uint8Array, name);
  }
};

beforeEach(() => {
  trackMock.mockClear();
  globalThis.umami = { track: trackMock };
  sessionStorage.clear();
  stubAllFeatures();
});

afterEach(() => {
  restoreFeatures();
});

describe("findMissingPdfjsFeatures", () => {
  it("returnerer tom liste når alt støttes", () => {
    expect(findMissingPdfjsFeatures()).toEqual([]);
  });

  it("returnerer manglende funksjoner", () => {
    Reflect.deleteProperty(Uint8Array.prototype, "toHex");
    Reflect.deleteProperty(Uint8Array, "fromBase64");

    expect(findMissingPdfjsFeatures()).toEqual(["toHex", "fromBase64"]);
  });
});

describe("trackMissingPdfjsSupport", () => {
  it("sender ikke event når alt støttes", () => {
    trackMissingPdfjsSupport();

    expect(trackMock).not.toHaveBeenCalled();
  });

  it("sender event med manglende funksjoner", () => {
    Reflect.deleteProperty(Uint8Array.prototype, "toHex");
    Reflect.deleteProperty(Uint8Array.prototype, "toBase64");

    trackMissingPdfjsSupport();

    expect(trackMock).toHaveBeenCalledTimes(1);
    const [eventName, data] = trackMock.mock.calls[0] as [string, Record<string, unknown>];
    expect(eventName).toBe("pdfjs mangler nettleserstøtte");
    expect(data).toEqual({
      mangler_toHex: true,
      mangler_toBase64: true,
      mangler_fromBase64: false,
      mangler_setFromBase64: false,
      mangler_setFromHex: false,
      nettleser: expect.any(String),
    });
  });

  it("sender event bare én gang per sesjon", () => {
    Reflect.deleteProperty(Uint8Array.prototype, "toHex");

    trackMissingPdfjsSupport();
    trackMissingPdfjsSupport();

    expect(trackMock).toHaveBeenCalledTimes(1);
  });
});

describe("shortBrowserName", () => {
  it.each([
    [
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0",
      "Edge 130",
    ],
    [
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36",
      "Chrome 128",
    ],
    ["Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0", "Firefox 128"],
    [
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Safari/605.1.15",
      "Safari 17.4",
    ],
    ["noe helt annet", "unknown"],
  ])("%s -> %s", (userAgent, expected) => {
    expect(shortBrowserName(userAgent)).toBe(expected);
  });
});
