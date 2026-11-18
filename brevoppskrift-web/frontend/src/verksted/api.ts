import axios, { type InternalAxiosRequestConfig, isAxiosError } from "axios";

import type * as G from "~/api/brevoppskrift-api.generated";

import { normalizeDraft } from "./normalize";
import {
  type ArchiveReason,
  type AttachmentUsage,
  type Draft,
  type DraftRecord,
  type DraftStatus,
  type DraftSummary,
  type GenerateResult,
  type Integrations,
  type Language,
  type Me,
  type PreviewResult,
  type ProblemBody,
  type PullRequestJob,
  type TemplateSource,
} from "./types";

export const WORKSHOP_API_BASE_PATH = "/api/v1";

const http = axios.create({ baseURL: WORKSHOP_API_BASE_PATH, withCredentials: true, timeout: 20_000 });
/** Eksponert for tester som bytter adapter. */
export const workshopHttp = http;

const CSRF_HEADER = "X-CSRF-Token";
const SAFE_METHODS = new Set(["get", "head", "options"]);
type RetryableConfig = InternalAxiosRequestConfig & { _csrfRetried?: boolean };

let csrfToken: Promise<string> | undefined;

/** Henter CSRF-token første gang en skriving trenger det (double-submit; BFF beskytter alle muterende metoder). */
function getCsrfToken(): Promise<string> {
  if (!csrfToken) {
    csrfToken = http.get<{ token: string }>("/csrf").then(
      (response) => response.data.token,
      (error: unknown) => {
        csrfToken = undefined;
        throw error;
      },
    );
  }
  return csrfToken;
}

/** Brukes av tester for å nullstille tokenbufferen. */
export function resetCsrfTokenForTests() {
  csrfToken = undefined;
}

http.interceptors.request.use(async (config) => {
  if (!SAFE_METHODS.has((config.method ?? "get").toLowerCase())) {
    config.headers.set(CSRF_HEADER, await getCsrfToken());
  }
  return config;
});

// Kun eksplisitt CSRF_REJECTED gir ny token og ett nytt forsøk. Aldri retry på generell 409/403.
http.interceptors.response.use(undefined, async (error: unknown) => {
  if (isAxiosError(error) && error.config) {
    const config = error.config as RetryableConfig;
    const code = (error.response?.data as ProblemBody | undefined)?.code;
    if (
      error.response?.status === 403 &&
      code === "CSRF_REJECTED" &&
      !config._csrfRetried &&
      !SAFE_METHODS.has((config.method ?? "get").toLowerCase())
    ) {
      config._csrfRetried = true;
      csrfToken = undefined;
      return http.request(config);
    }
  }
  throw error;
});

/** Feil fra API-et, normalisert fra RFC 9457. `unavailable` betyr at tjenesten ikke svarer som et API (ikke konfigurert, nede, eller SPA-HTML). */
export class ApiProblem extends Error {
  readonly status: number | undefined;
  readonly code: string | undefined;
  readonly detail: string | undefined;
  readonly errors: unknown;
  readonly currentRevision: number | undefined;
  readonly unavailable: boolean;

  constructor(init: {
    message: string;
    status?: number;
    code?: string;
    detail?: string;
    errors?: unknown;
    currentRevision?: number;
    unavailable?: boolean;
  }) {
    super(init.message);
    this.name = "ApiProblem";
    this.status = init.status;
    this.code = init.code;
    this.detail = init.detail;
    this.errors = init.errors;
    this.currentRevision = init.currentRevision;
    this.unavailable = init.unavailable ?? false;
  }

  get isConflict() {
    return this.status === 409;
  }
}

export function toApiProblem(error: unknown): ApiProblem {
  if (error instanceof ApiProblem) {
    return error;
  }
  if (isAxiosError(error)) {
    const response = error.response;
    if (!response) {
      return new ApiProblem({ message: "Får ikke kontakt med malverkstedet.", unavailable: true });
    }
    const contentType = String(response.headers?.["content-type"] ?? "");
    const body = response.data as ProblemBody | string | undefined;
    const isProblem = typeof body === "object" && body !== null;
    if (!contentType.includes("json") && !isProblem) {
      // HTML eller tom body fra BFF/SPA: API-et er ikke tilgjengelig.
      return new ApiProblem({
        message: "Malverkstedet er ikke tilgjengelig.",
        status: response.status,
        unavailable: true,
      });
    }
    const problem = isProblem ? body : {};
    const unavailable = [502, 503, 504].includes(response.status);
    return new ApiProblem({
      message: problem.detail ?? messageForStatus(response.status),
      status: response.status,
      code: problem.code,
      detail: problem.detail,
      errors: problem.errors,
      currentRevision: typeof problem.currentRevision === "number" ? problem.currentRevision : undefined,
      unavailable,
    });
  }
  return new ApiProblem({ message: error instanceof Error ? error.message : "Ukjent feil." });
}

function messageForStatus(status: number): string {
  switch (status) {
    case 400:
    case 422: {
      return "Forespørselen er ugyldig.";
    }
    case 401: {
      return "Du er ikke innlogget.";
    }
    case 403: {
      return "Du har ikke tilgang til å gjøre endringer.";
    }
    case 404: {
      return "Fant ikke ressursen.";
    }
    case 409: {
      return "Utkastet er endret av noen andre.";
    }
    case 413: {
      return "Dokumentet er for stort.";
    }
    case 429: {
      return "For mange forespørsler. Prøv igjen senere.";
    }
    default: {
      return `Noe gikk galt (${status}).`;
    }
  }
}

async function call<T>(request: Promise<{ data: T }>): Promise<T> {
  try {
    return (await request).data;
  } catch (error) {
    throw toApiProblem(error);
  }
}

export const workshopKeys = {
  all: ["VERKSTED"] as const,
  me: ["VERKSTED", "ME"] as const,
  integrations: ["VERKSTED", "INTEGRASJONER"] as const,
  catalog: ["VERKSTED", "KATALOG"] as const,
  drafts: ["VERKSTED", "UTKAST"] as const,
  draft: (id: string) => ["VERKSTED", "UTKAST", id] as const,
  pullRequest: (id: string) => ["VERKSTED", "UTKAST", id, "PR"] as const,
  usage: (id: string) => ["VERKSTED", "UTKAST", id, "BRUK"] as const,
};

type WireRecord = Omit<DraftRecord, "draft"> & { draft: G.Draft };
const toRecord = (record: WireRecord): DraftRecord => ({ ...record, draft: normalizeDraft(record.draft) });
const recordCall = async (request: Promise<{ data: WireRecord }>): Promise<DraftRecord> =>
  toRecord(await call(request));

export const workshopApi = {
  me: () => call(http.get<Me>("/meg")),
  integrations: () => call(http.get<Integrations>("/integrasjoner")),
  catalog: () => call(http.get<TemplateSource[]>("/katalog")),
  catalogDraft: async (id: string): Promise<Draft> =>
    normalizeDraft(await call(http.get<G.Draft>(`/katalog/${encodeURIComponent(id)}`))),
  drafts: () => call(http.get<DraftSummary[]>("/utkast")),
  draft: (id: string) => recordCall(http.get<WireRecord>(`/utkast/${id}`)),
  usage: (id: string) => call(http.get<AttachmentUsage[]>(`/utkast/${id}/bruk`)),
  create: (body: { draft?: Draft; sourceId?: string }) => recordCall(http.post<WireRecord>("/utkast", body)),
  save: (id: string, revision: number, draft: Draft) =>
    recordCall(http.put<WireRecord>(`/utkast/${id}`, { revision, draft })),
  setStatus: (id: string, revision: number, status: DraftStatus) =>
    recordCall(http.post<WireRecord>(`/utkast/${id}/status`, { revision, status })),
  approveBaseline: (id: string, revision: number) =>
    recordCall(http.post<WireRecord>(`/utkast/${id}/grunnlinje`, { revision })),
  archive: async (id: string, revision: number, reason: ArchiveReason) => {
    await call(http.post<void>(`/utkast/${id}/arkiver`, { revision, reason }));
  },
  generate: (draft: Draft) => call(http.post<GenerateResult>("/generer", { draft })),
  preview: async (draft: Draft, language: Language, scenarioId?: string): Promise<PreviewResult> => {
    const result = await call(http.post<G.PreviewResult>("/forhandsvisning", { draft, language, scenarioId }));
    return { ...result, warnings: result.warnings ?? [] };
  },
  aiNynorsk: (text: string, language: Language, instruction?: string) =>
    call(http.post<{ text: string }>("/ki/nynorsk", { text, language, instruction })),
  aiKlarsprak: (text: string, language: Language, instruction?: string) =>
    call(http.post<{ text: string }>("/ki/klarsprak", { text, language, instruction })),
  createPullRequest: (id: string, revision: number) =>
    call(http.post<PullRequestJob>(`/utkast/${id}/pr`, { revision })),
  /** `null` når det ikke finnes noen jobb (404). */
  pullRequest: async (id: string): Promise<PullRequestJob | null> => {
    try {
      return await call(http.get<PullRequestJob>(`/utkast/${id}/pr`));
    } catch (error) {
      if (error instanceof ApiProblem && error.status === 404 && !error.unavailable) {
        return null;
      }
      throw error;
    }
  },
};

/** Saksbehandlere får aldri et nettleservindu med skrivetilgang uten at API-et er tilgjengelig. */
export function isUnavailable(error: unknown): boolean {
  return error instanceof ApiProblem && error.unavailable;
}
