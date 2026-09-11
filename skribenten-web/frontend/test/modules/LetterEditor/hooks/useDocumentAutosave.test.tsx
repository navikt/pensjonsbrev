import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, renderHook, waitFor } from "@testing-library/react";
import { type ReactNode, useState } from "react";
import { describe, expect, it, vi } from "vitest";

import { type SaveStatus, useDocumentAutosave } from "~/Brevredigering/LetterEditor/hooks/useDocumentAutosave";

// Shortening the debounce lets the tests use real timers, which keeps the promise/queue
// interleaving in the hook realistic instead of driving it through a fake clock.
vi.mock("~/components/ManagedLetterEditor/autosave_timer", () => ({ AUTOSAVE_TIMER: 5 }));

// React only enables act() support when this flag is set; RTL sets it during render, but the hook's
// queued saves are driven from act() calls outside render.
(globalThis as { IS_REACT_ACT_ENVIRONMENT?: boolean }).IS_REACT_ACT_ENVIRONMENT = true;

const tick = (ms = 0) => new Promise((resolve) => setTimeout(resolve, ms));

type Doc = { text: string };

const wrapper = ({ children }: { children: ReactNode }) => {
  const queryClient = new QueryClient({ defaultOptions: { mutations: { retry: false }, queries: { retry: false } } });
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};

type Deferred<T> = { promise: Promise<T>; resolve: (value: T) => void; reject: (error: unknown) => void };

const deferred = <T,>(): Deferred<T> => {
  let resolve!: (value: T) => void;
  let reject!: (error: unknown) => void;
  const promise = new Promise<T>((res, rej) => {
    resolve = res;
    reject = rej;
  });
  return { promise: promise, resolve: resolve, reject: reject };
};

/**
 * A saver whose requests the test resolves by hand, so overlapping saves can be observed.
 * Every call is recorded and stays pending until the test resolves or rejects it.
 */
const controlledSaver = () => {
  const saved: Doc[] = [];
  const pending: Deferred<Doc>[] = [];
  const mutationFn = vi.fn((doc: Doc) => {
    saved.push(doc);
    const next = deferred<Doc>();
    pending.push(next);
    return next.promise;
  });
  return {
    saved: saved,
    mutationFn: mutationFn,
    settle: async (index: number, outcome: Doc | Error) => {
      await act(async () => {
        if (outcome instanceof Error) {
          pending[index].reject(outcome);
        } else {
          pending[index].resolve(outcome);
        }
        await Promise.resolve();
      });
    },
  };
};

/** Resolves every request immediately, optionally failing the first `failures` attempts. */
const immediateSaver = (options?: { failures?: number; delayed?: boolean }) => {
  const saved: Doc[] = [];
  let failuresLeft = options?.failures ?? 0;
  const mutationFn = vi.fn(async (doc: Doc) => {
    saved.push(doc);
    // Guard against the unbounded re-save loop: without a cap a broken hook would spin forever
    // and hang the suite instead of failing it.
    if (saved.length > 20) {
      throw new Error("Autosave kalte mutationFn for mange ganger");
    }
    // Resolving on a macrotask guarantees that React has re-rendered with whatever the caller
    // changed in onSaveStart before the response is handled.
    if (options?.delayed) await tick();
    if (failuresLeft > 0) {
      failuresLeft--;
      throw new Error("lagring feilet");
    }
    return doc;
  });
  return { saved: saved, mutationFn: mutationFn };
};

type SuccessBehaviour =
  /** Realistic caller: collapses to SAVED and keeps the document identity when content is unchanged. */
  | "collapse"
  /** Caller that never reports SAVED, e.g. because the response never compares equal. */
  | "staysDirty"
  /**
   * Caller that rewrites the document object when a save starts, the way ManagedLetterEditorContext
   * folds the cursor position into the state it saves. The content is unchanged, only the identity.
   */
  | "newDocumentOnSaveStart";

const useHarness = (options: { mutationFn: (doc: Doc) => Promise<Doc>; behaviour?: SuccessBehaviour }) => {
  const behaviour = options.behaviour ?? "collapse";
  const [state, setState] = useState<{ document: Doc; saveStatus: SaveStatus }>({
    document: { text: "start" },
    saveStatus: "SAVED",
  });

  const saver = useDocumentAutosave<Doc, Doc>({
    document: state.document,
    saveStatus: state.saveStatus,
    mutationFn: options.mutationFn,
    onSaveStart: () =>
      setState((s) => ({
        document: behaviour === "newDocumentOnSaveStart" ? { ...s.document } : s.document,
        saveStatus: "SAVE_PENDING",
      })),
    onSaveSuccess: (response) => {
      setState((s) => {
        if (behaviour === "staysDirty") return { ...s, saveStatus: "DIRTY" };
        // Mirrors ManagedAttachmentEditor: edits made while the save was in flight win.
        if (s.saveStatus === "DIRTY") return s;
        if (s.document.text === response.text) return { ...s, saveStatus: "SAVED" };
        return { document: response, saveStatus: "SAVED" };
      });
    },
    onSaveError: () => setState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  return {
    saver: saver,
    document: state.document,
    saveStatus: state.saveStatus,
    edit: (text: string) => setState({ document: { text: text }, saveStatus: "DIRTY" }),
  };
};

const renderAutosave = (options: { mutationFn: (doc: Doc) => Promise<Doc>; behaviour?: SuccessBehaviour }) =>
  renderHook(() => useHarness(options), { wrapper: wrapper });

describe("useDocumentAutosave", () => {
  it("lagrer det siste dokumentet når autosave-timeren løper ut", async () => {
    const saver = immediateSaver();
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("endret"));

    await waitFor(() => expect(saver.saved).toEqual([{ text: "endret" }]));
    await waitFor(() => expect(result.current.saveStatus).toBe("SAVED"));
  });

  it("lagrer ikke et dokument som allerede er lagret", async () => {
    const saver = immediateSaver();
    renderAutosave({ mutationFn: saver.mutationFn });

    await new Promise((resolve) => setTimeout(resolve, 30));

    expect(saver.mutationFn).not.toHaveBeenCalled();
  });

  it("saveNow lagrer umiddelbart uten å vente på timeren", async () => {
    const saver = immediateSaver();
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("umiddelbar"));
    await act(async () => {
      await result.current.saver.saveNow();
    });

    expect(saver.saved).toEqual([{ text: "umiddelbar" }]);
    expect(result.current.saveStatus).toBe("SAVED");
  });

  it("serialiserer saveNow mot en autosave som allerede er underveis", async () => {
    const saver = controlledSaver();
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("først"));
    await waitFor(() => expect(saver.saved).toHaveLength(1));

    act(() => result.current.edit("sist"));
    let saveNowSettled = false;
    const saveNowPromise = act(async () => {
      await result.current.saver.saveNow();
      saveNowSettled = true;
    });

    // The second request must not be sent while the first one is still in flight.
    await new Promise((resolve) => setTimeout(resolve, 20));
    expect(saver.saved).toHaveLength(1);
    expect(saveNowSettled).toBe(false);

    await saver.settle(0, { text: "først" });
    await waitFor(() => expect(saver.saved).toHaveLength(2));
    await saver.settle(1, { text: "sist" });
    await saveNowPromise;

    expect(saver.saved).toEqual([{ text: "først" }, { text: "sist" }]);
    expect(result.current.saveStatus).toBe("SAVED");
  });

  it("saveNow resolver selv om kalleren aldri rapporterer SAVED", async () => {
    const saver = immediateSaver();
    const { result } = renderAutosave({ mutationFn: saver.mutationFn, behaviour: "staysDirty" });

    act(() => result.current.edit("aldri lagret"));
    await act(async () => {
      await result.current.saver.saveNow();
    });

    // The document never changed between attempts, so a single save is all that is warranted.
    expect(saver.saved).toEqual([{ text: "aldri lagret" }]);
  });

  it("gjør et begrenset antall forsøk når kalleren bytter ut dokumentobjektet ved hver lagring", async () => {
    const saver = immediateSaver({ delayed: true });
    const { result } = renderAutosave({ mutationFn: saver.mutationFn, behaviour: "newDocumentOnSaveStart" });

    act(() => result.current.edit("nytt objekt hver gang"));
    await act(async () => {
      await result.current.saver.saveNow();
    });

    expect(saver.saved.length).toBeGreaterThan(0);
    expect(saver.saved.length).toBeLessThanOrEqual(5);
  });

  it("prøver ikke automatisk på nytt med det samme dokumentet etter en feilet lagring", async () => {
    const saver = immediateSaver({ failures: 1 });
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("feiler"));
    await waitFor(() => expect(saver.saved).toHaveLength(1));
    await waitFor(() => expect(result.current.saver.saveFailed).toBe(true));

    await new Promise((resolve) => setTimeout(resolve, 30));
    expect(saver.saved).toHaveLength(1);

    // A new edit is a new document, so autosave picks up again.
    act(() => result.current.edit("nytt forsøk"));
    await waitFor(() => expect(saver.saved).toEqual([{ text: "feiler" }, { text: "nytt forsøk" }]));

    // The error state must clear again, otherwise the editor keeps showing a save error forever.
    await waitFor(() => expect(result.current.saver.saveFailed).toBe(false));
  });

  it("saveNow propagerer feil til kalleren", async () => {
    const saver = immediateSaver({ failures: 1 });
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("feiler"));
    await act(async () => {
      await expect(result.current.saver.saveNow()).rejects.toThrow("lagring feilet");
    });
    expect(result.current.saveStatus).toBe("DIRTY");
  });

  it("withSavingPaused venter på aktiv lagring og hindrer nye lagringer mens operasjonen kjører", async () => {
    const saver = controlledSaver();
    const { result } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("under lagring"));
    await waitFor(() => expect(saver.saved).toHaveLength(1));

    let operationStarted = false;
    const operation = deferred<string>();
    const paused = act(async () => {
      await result.current.saver.withSavingPaused(async () => {
        operationStarted = true;
        return operation.promise;
      });
    });

    // The operation must wait for the in-flight save to finish.
    await new Promise((resolve) => setTimeout(resolve, 20));
    expect(operationStarted).toBe(false);

    await saver.settle(0, { text: "under lagring" });
    await waitFor(() => expect(operationStarted).toBe(true));

    // Edits made while paused must not trigger a save.
    act(() => result.current.edit("mens pauset"));
    await new Promise((resolve) => setTimeout(resolve, 30));
    expect(saver.saved).toHaveLength(1);

    await act(async () => {
      operation.resolve("ferdig");
      await operation.promise;
    });
    await paused;

    await waitFor(() => expect(saver.saved).toHaveLength(2));
  });

  it("lagrer et ulagret dokument når editoren avmonteres", async () => {
    const saver = immediateSaver();
    const { result, unmount } = renderAutosave({ mutationFn: saver.mutationFn });

    act(() => result.current.edit("ved unmount"));
    unmount();

    await waitFor(() => expect(saver.saved).toEqual([{ text: "ved unmount" }]));
  });
});
