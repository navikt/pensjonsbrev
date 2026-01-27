/**
 * Umami Analytics Configuration
 *
 * This module provides configuration for Umami analytics integration.
 * Environment variables are loaded from Vite's environment system.
 *
 * For production deployment, set these environment variables in NAIS config.
 * For local development, the defaults enable console logging for debugging.
 *
 * Contact #ResearchOps on Slack to get the actual tracking codes for your project.
 */

interface UmamiConfig {
  /** Whether Umami tracking is enabled */
  enabled: boolean;
  /** The Umami website ID for this project */
  websiteId: string;
  /** The Umami script URL */
  scriptUrl: string;
  /** Domains where tracking should be active */
  domains: string[];
  /** Whether we're in development mode */
  isDevelopment: boolean;
}

/**
 * Get Umami configuration from environment variables
 *
 * Environment variables (set in NAIS or .env.local):
 * - VITE_UMAMI_ENABLED: "true" or "false" to enable/disable tracking
 * - VITE_UMAMI_WEBSITE_ID: The website ID from Umami dashboard
 * - VITE_UMAMI_SCRIPT_URL: The Umami script URL
 * - VITE_UMAMI_DOMAINS: Comma-separated list of domains for tracking
 */
export const getUmamiConfig = (): UmamiConfig => {
  const isDevelopment = import.meta.env.DEV;

  return {
    enabled: import.meta.env.VITE_UMAMI_ENABLED !== "false",
    websiteId: import.meta.env.VITE_UMAMI_WEBSITE_ID || "REPLACE_WITH_ACTUAL_WEBSITE_ID",
    scriptUrl: import.meta.env.VITE_UMAMI_SCRIPT_URL || "https://umami.nav.no/script.js",
    domains: (import.meta.env.VITE_UMAMI_DOMAINS || "localhost,pensjonsbrev.intern.dev.nav.no")
      .split(",")
      .map((d: string) => d.trim()),
    isDevelopment,
  };
};

/**
 * Check if the current domain should have tracking enabled
 */
export const shouldTrackOnCurrentDomain = (): boolean => {
  const config = getUmamiConfig();
  const currentDomain = typeof globalThis.window !== "undefined" ? globalThis.location.hostname : "";
  return config.enabled && config.domains.some((domain) => currentDomain.includes(domain));
};
