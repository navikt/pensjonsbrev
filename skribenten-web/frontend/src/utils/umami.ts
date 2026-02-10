/**
 * Umami Analytics Utility Module
 *
 * This module provides utilities for tracking user behavior with Umami analytics.
 * It follows NAV's taxonomy and guidelines for event naming and data collection.
 *
 * Uses NAV's sporing.js script which exposes window.umami for tracking.
 *
 * @see https://aksel.nav.no/god-praksis/artikler/umami-maling
 * @see https://umami.is/docs/tracking
 *
 * IMPORTANT: Do not track:
 * - Personal identifiable information (PII) like fødselsnummer, email, phone
 * - Free-text field content that may contain PII
 * - Search field content that may contain PII
 */

// Umami tracking function type (exposed by sporing.js)
interface UmamiTracker {
  track: (eventName: string, eventData?: Record<string, string | number | boolean>) => void;
}

// Extend globalThis to include umami from sporing.js
declare global {
  var umami: UmamiTracker | undefined;
  var localAnalyticsEvents: Array<{
    timestamp: number;
    type: "event" | "pageview";
    name: string;
    data?: unknown;
  }>;
}

// Initialize local storage for demo purposes
if (typeof globalThis.window !== "undefined") {
  globalThis.localAnalyticsEvents = JSON.parse(sessionStorage.getItem("localAnalyticsEvents") || "[]");
}

const logToLocalDemo = (type: "event" | "pageview", name: string, data?: unknown) => {
  if (typeof globalThis.window === "undefined") {
    return;
  }

  // Ensure the array exists
  if (!globalThis.localAnalyticsEvents) {
    globalThis.localAnalyticsEvents = JSON.parse(sessionStorage.getItem("localAnalyticsEvents") || "[]");
  }

  const entry = {
    timestamp: Date.now(),
    type,
    name,
    data,
  };
  globalThis.localAnalyticsEvents.unshift(entry);
  sessionStorage.setItem("localAnalyticsEvents", JSON.stringify(globalThis.localAnalyticsEvents));
};

/**
 * NAV Taxonomy for event naming
 * Following the standard: {action} {object} {context?}
 */

export type UmamiEventName =
  // Navigation events
  | "navigasjon sak åpnet"
  | "navigasjon brev åpnet"
  | "navigasjon side lastet"
  // Vedlegg (attachment) events
  | "vedlegg modal åpnet"
  | "vedlegg modal lukket"
  | "vedlegg lagt til"
  | "vedlegg fjernet"
  | "vedlegg valgt"
  // P1 events
  | "p1 modal åpnet"
  | "p1 modal lukket"
  | "p1 redigert"
  // Brev (letter) events
  | "brev opprettet"
  | "brev redigert"
  | "brev ferdigstilt"
  | "brev forhåndsvist"
  | "brev sendt"
  // Brev status events (informasjonsbrev vs vedtaksbrev)
  | "brev klar status endret"
  | "brev distribusjonstype endret"
  // Pesys navigation events
  | "pesys inngang"
  | "pesys redirect"
  | "pesys feil"
  // Attestering events
  | "attestering blokkert"
  // Error events
  | "feil oppstått"
  // Generic events
  | "knapp klikket"
  | "modal åpnet"
  | "modal lukket";

export interface UmamiEventData {
  /** Component or feature name */
  komponent?: string;
  /** Action performed */
  handling?: string;
  /** Additional context */
  kontekst?: string;
  /** Value (e.g., count, selection) - avoid PII */
  verdi?: string | number;
  /** Status (success, error, etc.) */
  status?: "suksess" | "feil" | "avbrutt";
  /** Type identifier - avoid PII */
  type?: string;
  /** Generic metadata - avoid PII */
  [key: string]: string | number | boolean | undefined;
}

/**
 * Check if Umami is available (sporing.js loaded)
 */
export const isUmamiAvailable = (): boolean => {
  return (
    typeof globalThis.window !== "undefined" &&
    globalThis.umami !== undefined &&
    typeof globalThis.umami.track === "function"
  );
};

/**
 * Track a custom event with Umami
 *
 * @param eventName - The name of the event following NAV taxonomy
 * @param eventData - Optional event data (avoid PII)
 *
 * @example
 * trackEvent("vedlegg lagt til", { type: "PE-001", komponent: "Vedlegg" });
 */
export const trackEvent = (eventName: UmamiEventName | string, eventData?: UmamiEventData): void => {
  logToLocalDemo("event", eventName, eventData);

  if (!isUmamiAvailable()) {
    // In development without Umami, log to console for debugging
    if (import.meta.env.DEV) {
      // eslint-disable-next-line no-console
      console.log("[Umami POC] Event tracked:", eventName, eventData);
    }
    return;
  }

  try {
    // Filter out undefined values and ensure no PII
    const cleanedData = eventData
      ? (Object.fromEntries(Object.entries(eventData).filter(([, value]) => value !== undefined)) as Record<
          string,
          string | number | boolean
        >)
      : undefined;

    // Use umami.track() exposed by sporing.js
    globalThis.umami?.track(eventName, cleanedData);
  } catch {
    // Silently fail - analytics should not break the app
  }
};

/**
 * Track page view manually (usually automatic with Umami)
 *
 * @param url - Optional custom URL to track
 */
export const trackPageView = (url?: string): void => {
  logToLocalDemo("pageview", url ?? globalThis.location.pathname);

  if (!isUmamiAvailable()) {
    if (import.meta.env.DEV) {
      // eslint-disable-next-line no-console
      console.log("[Umami POC] Page view:", url ?? globalThis.location.pathname);
    }
    return;
  }

  try {
    // Track page view using umami.track()
    globalThis.umami?.track(url ?? globalThis.location.pathname);
  } catch {
    // Silently fail - analytics should not break the app
  }
};

/**
 * Create a scoped tracker for a specific component
 * This helps maintain consistent naming and reduces boilerplate
 *
 * @param componentName - The name of the component
 *
 * @example
 * const tracker = createComponentTracker("Vedlegg");
 * tracker.trackAction("åpnet", { type: "modal" });
 */
export const createComponentTracker = (componentName: string) => ({
  trackAction: (action: string, data?: Omit<UmamiEventData, "komponent">) => {
    trackEvent(`${action}`, {
      ...data,
      komponent: componentName,
    });
  },
  trackEvent: (eventName: UmamiEventName | string, data?: Omit<UmamiEventData, "komponent">) => {
    trackEvent(eventName, {
      ...data,
      komponent: componentName,
    });
  },
});

/**
 * Common event tracking helpers following NAV taxonomy
 */
export const umamiEvents = {
  // Button clicks
  buttonClick: (buttonName: string, context?: string) =>
    trackEvent("knapp klikket", { handling: buttonName, kontekst: context }),

  // Modal events
  modalOpened: (modalName: string) => trackEvent("modal åpnet", { type: modalName }),
  modalClosed: (modalName: string) => trackEvent("modal lukket", { type: modalName }),

  // Navigation
  pageLoaded: (pageName: string) => trackEvent("navigasjon side lastet", { type: pageName }),

  // Errors
  errorOccurred: (errorType: string, context?: string) =>
    trackEvent("feil oppstått", { type: errorType, kontekst: context }),
};
