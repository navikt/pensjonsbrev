import { useCallback, useEffect, useRef } from "react";

import {
  createComponentTracker,
  trackEvent,
  trackPageView,
  type UmamiEventData,
  type UmamiEventName,
} from "~/utils/umami";

/**
 * Hook for tracking Umami events in React components
 *
 * @param componentName - Optional component name for scoped tracking
 *
 * @example
 * // Basic usage
 * const { track } = useUmami();
 * track("knapp klikket", { handling: "lagre" });
 *
 * @example
 * // Scoped to component
 * const { track, trackAction } = useUmami("Vedlegg");
 * trackAction("lagt til", { type: "PE-001" }); // Will include komponent: "Vedlegg"
 */
export const useUmami = (componentName?: string) => {
  const tracker = componentName ? createComponentTracker(componentName) : null;

  const track = useCallback(
    (eventName: UmamiEventName | string, eventData?: UmamiEventData) => {
      if (tracker) {
        tracker.trackEvent(eventName, eventData);
      } else {
        trackEvent(eventName, eventData);
      }
    },
    [tracker],
  );

  const trackAction = useCallback(
    (action: string, data?: Omit<UmamiEventData, "komponent">) => {
      if (tracker) {
        tracker.trackAction(action, data);
      } else {
        trackEvent(action, data);
      }
    },
    [tracker],
  );

  return {
    track,
    trackAction,
    trackPageView,
  };
};

/**
 * Hook for tracking page views on route changes
 * Use this in your route components to track when users visit pages
 *
 * @param pageName - The name of the page for tracking purposes
 *
 * @example
 * // In a route component
 * useUmamiPageView("Saksoversikt");
 */
export const useUmamiPageView = (pageName: string) => {
  const hasTracked = useRef(false);

  useEffect(() => {
    // Only track once per mount to avoid duplicate events
    if (!hasTracked.current) {
      trackEvent("navigasjon side lastet", { type: pageName });
      trackPageView();
      hasTracked.current = true;
    }
  }, [pageName]);
};

/**
 * Hook for tracking time spent on a component/page
 * Useful for measuring engagement
 *
 * @param componentName - The name of the component/page
 * @param minTimeMs - Minimum time (ms) before tracking (default: 5000ms)
 *
 * @example
 * useUmamiTimeSpent("BrevRedigering", 10000); // Track after 10 seconds
 */
export const useUmamiTimeSpent = (componentName: string, minTimeMs: number = 5000) => {
  const startTime = useRef<number | null>(null);

  useEffect(() => {
    startTime.current = Date.now();

    return () => {
      if (startTime.current !== null) {
        const timeSpent = Date.now() - startTime.current;
        if (timeSpent >= minTimeMs) {
          trackEvent("tid brukt", {
            komponent: componentName,
            verdi: Math.round(timeSpent / 1000), // Convert to seconds
          });
        }
      }
    };
  }, [componentName, minTimeMs]);
};

/**
 * Hook for tracking modal interactions
 * Automatically tracks open/close events
 *
 * @param modalName - The name of the modal
 * @param isOpen - Whether the modal is open
 *
 * @example
 * const [isOpen, setIsOpen] = useState(false);
 * useUmamiModal("LeggTilVedlegg", isOpen);
 */
export const useUmamiModal = (modalName: string, isOpen: boolean) => {
  const wasOpen = useRef(false);

  useEffect(() => {
    if (isOpen && !wasOpen.current) {
      trackEvent("modal åpnet", { type: modalName });
    } else if (!isOpen && wasOpen.current) {
      trackEvent("modal lukket", { type: modalName });
    }
    wasOpen.current = isOpen;
  }, [isOpen, modalName]);
};
