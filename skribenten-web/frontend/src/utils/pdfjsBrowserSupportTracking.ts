import { trackEvent } from "~/utils/umami";

// pdfjs-dist 6 calls these without a fallback (only the legacy build polyfills them).
const requiredFeatures: { name: string; isSupported: () => boolean }[] = [
  { name: "toHex", isSupported: () => typeof Uint8Array.prototype.toHex === "function" },
  { name: "toBase64", isSupported: () => typeof Uint8Array.prototype.toBase64 === "function" },
  { name: "fromBase64", isSupported: () => typeof Uint8Array.fromBase64 === "function" },
  { name: "setFromBase64", isSupported: () => typeof Uint8Array.prototype.setFromBase64 === "function" },
  { name: "setFromHex", isSupported: () => typeof Uint8Array.prototype.setFromHex === "function" },
];

const SESSION_KEY = "pdfjs-browser-support-tracked";

export const findMissingPdfjsFeatures = (): string[] =>
  requiredFeatures.filter((feature) => !feature.isSupported()).map((feature) => feature.name);

export const shortBrowserName = (userAgent: string): string => {
  const patterns: [RegExp, string][] = [
    [/Edg\/(\d+)/, "Edge"],
    [/Firefox\/(\d+)/, "Firefox"],
    [/Chrome\/(\d+)/, "Chrome"],
    [/Version\/(\d+(?:\.\d+)?).*Safari/, "Safari"],
  ];
  for (const [pattern, name] of patterns) {
    const match = pattern.exec(userAgent);
    if (match) return `${name} ${match[1]}`;
  }
  return "unknown";
};

const isAlreadyTracked = (): boolean => {
  try {
    return globalThis.sessionStorage.getItem(SESSION_KEY) === "true";
  } catch {
    return false;
  }
};

const markAsTracked = () => {
  try {
    globalThis.sessionStorage.setItem(SESSION_KEY, "true");
  } catch {
    // sessionStorage may be unavailable; tracking more than once is acceptable then.
  }
};

export const trackMissingPdfjsSupport = (): void => {
  const missing = findMissingPdfjsFeatures();
  if (missing.length === 0 || isAlreadyTracked()) return;

  markAsTracked();
  trackEvent("pdfjs mangler nettleserstøtte", {
    mangler: missing.join(","),
    nettleser: shortBrowserName(globalThis.navigator?.userAgent ?? ""),
  });
};
