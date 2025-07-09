/**
 * Converts today’s plain-text 403 messages from the backend
 * into an enum. Frontend code can switch on `error.forbidReason`
 */

export type AttestForbiddenReason = "MISSING_ATTESTANT_ROLE" | "SELF_ATTESTATION" | "ALREADY_ATTESTED" | "UNKNOWN_403";

/**
 * Map backend 403 text to enum value.
 * Extend or tweak the substrings as backend messages change.
 */
export function parseAttest403(msg?: string): AttestForbiddenReason {
  if (!msg) return "UNKNOWN_403";

  if (msg.includes("har ikke attestantrolle")) return "MISSING_ATTESTANT_ROLE";
  if (msg.includes("attestere sitt eget brev")) return "SELF_ATTESTATION";
  if (msg.includes("allerede attestert")) return "ALREADY_ATTESTED";

  return "UNKNOWN_403";
}
