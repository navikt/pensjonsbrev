export type CapabilityState = { enabled: boolean; label: string };

const ON = new Set(["ENABLED", "ENABLE", "AVAILABLE", "ON", "OK", "READY", "ACTIVE", "CONFIGURED", "TRUE"]);

/**
 * Backendens capability-form er ikke låst ennå. Tolker boolean, streng eller objekt
 * med `enabled`/`available`/`status`. Ukjent form regnes som AV, aldri som på.
 */
export function capabilityState(value: unknown): CapabilityState {
  if (typeof value === "boolean") {
    return { enabled: value, label: value ? "Aktiv" : "Ikke aktiv" };
  }
  if (typeof value === "string") {
    const enabled = ON.has(value.toUpperCase());
    return { enabled, label: enabled ? "Aktiv" : value };
  }
  if (value && typeof value === "object") {
    const record = value as Record<string, unknown>;
    for (const key of ["enabled", "available", "configured"]) {
      if (typeof record[key] === "boolean") {
        return { enabled: record[key], label: record[key] ? "Aktiv" : "Ikke aktiv" };
      }
    }
    if (typeof record.status === "string") {
      return capabilityState(record.status);
    }
  }
  return { enabled: false, label: "Ukjent" };
}
