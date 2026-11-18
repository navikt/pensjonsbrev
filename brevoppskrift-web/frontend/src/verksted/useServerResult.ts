import { useCallback, useEffect, useRef, useState } from "react";

import { type ApiProblem, toApiProblem } from "./api";

export type ServerResult<T> = {
  data: T | undefined;
  error: ApiProblem | undefined;
  loading: boolean;
  /** `true` når data stammer fra en eldre versjon av inndata enn den som vises nå. */
  stale: boolean;
  refresh: () => void;
};

/**
 * Kaller serveren (generering/forhåndsvisning) med debounce når inndata endres.
 * Svar fra utdaterte kall forkastes, så et sent svar aldri overskriver et nyere.
 */
export function useServerResult<T>(
  input: unknown,
  fetcher: () => Promise<T>,
  enabled: boolean,
  delayMs = 700,
): ServerResult<T> {
  const [state, setState] = useState<{ data?: T; error?: ApiProblem; loading: boolean; forInput?: unknown }>({
    loading: false,
  });
  const counter = useRef(0);
  const fetcherRef = useRef(fetcher);
  useEffect(() => {
    fetcherRef.current = fetcher;
  });
  const [tick, setTick] = useState(0);

  // biome-ignore lint/correctness/useExhaustiveDependencies: tick utløser eksplisitt ny henting
  useEffect(() => {
    if (!enabled) {
      return;
    }
    const id = ++counter.current;
    const timer = setTimeout(() => {
      setState((s) => ({ ...s, loading: true }));
      fetcherRef
        .current()
        .then((data) => {
          if (id === counter.current) {
            setState({ data, loading: false, forInput: input });
          }
        })
        .catch((error: unknown) => {
          if (id === counter.current) {
            setState((s) => ({ ...s, error: toApiProblem(error), loading: false, forInput: input }));
          }
        });
    }, delayMs);
    return () => clearTimeout(timer);
  }, [input, enabled, delayMs, tick]);

  const refresh = useCallback(() => setTick((t) => t + 1), []);
  return {
    data: state.data,
    error: state.error && state.forInput === input ? state.error : undefined,
    loading: state.loading,
    stale: state.forInput !== input,
    refresh,
  };
}
