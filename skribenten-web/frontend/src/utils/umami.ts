/**
 * Umami Analytics Utility Module
 *
 * This module provides utilities for tracking user behavior with Umami analytics.
 * It follows NAV's taxonomy and guidelines for event naming and data collection.
 *
 * @see https://aksel.nav.no/god-praksis/artikler/umami-maling
 * @see https://umami.is/docs/tracking
 *
 * IMPORTANT: Do not track:
 * - Personal identifiable information (PII) like fødselsnummer, email, phone
 * - Free-text field content that may contain PII
 * - Search field content that may contain PII
 */

// Umami tracking function type
interface UmamiTracker {
  track: (eventName: string, eventData?: Record<string, string | number | boolean>) => void;
}

// Extend globalThis to include umami
declare global {
  var umami: UmamiTracker | undefined;
}

/**
 * NAV Taxonomy for event naming
 * Following the standard: {action} {object} {context?}
 *
 * Common actions:
 * - klikk (click)
 * - vis (view/show)
 * - åpne (open)
 * - lukke (close)
 * - endre (change)
 * - laste (load)
 * - sende (submit)
 * - velge (select)
 * - fjerne (remove)
 * - legge til (add)
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
 * Check if Umami is available and tracking is enabled
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
  if (!isUmamiAvailable()) {
    if (import.meta.env.DEV) {
      // eslint-disable-next-line no-console
      console.log("[Umami POC] Page view:", url ?? globalThis.location.pathname);
    }
    return;
  }

  try {
    // Umami tracks page views automatically, but we can manually trigger if needed
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
