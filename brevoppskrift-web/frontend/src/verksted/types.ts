/**
 * Klientens modelltyper for malverkstedet.
 *
 * Dokumentmodellen kommer fra den genererte filen `src/api/brevoppskrift-api.generated.ts`
 * (`:brevoppskrift-backend:generateAuthoringApi`). Generatoren markerer felter med standardverdier som valgfrie.
 * Her normaliseres de til påkrevde lister/verdier (`Norm`), og `normalize.ts` fyller dem inn ved innlasting,
 * slik at editoren slipper å håndtere `undefined` overalt. Normaliserte typer kan sendes tilbake uendret
 * fordi de er tilordnbare til de genererte typene.
 *
 * Konvoluttene rundt dokumentet (utkastpost, utkastoppsummering, /meg, PR-jobb, integrasjonsstatus) er ikke
 * med i generert fil og beskrives her etter den avtalte API-kontrakten.
 */
import type * as G from "~/api/brevoppskrift-api.generated";

export type { JsonValue } from "~/api/brevoppskrift-api.generated";

export type Language = G.DraftLanguage;
export const LANGUAGES: readonly Language[] = ["NB", "NN", "EN"];
export const LANGUAGE_LABELS: Record<Language, string> = { NB: "Bokmål", NN: "Nynorsk", EN: "Engelsk" };

/** Felter generatoren markerer som valgfrie fordi de har standardverdi; editoren regner dem alltid som satt. */
type Defaulted =
  | "nullable"
  | "children"
  | "enumValues"
  | "ifFalse"
  | "formatter"
  | "fontType"
  | "replies"
  | "states"
  | "choices"
  | "warnings"
  | "fields"
  | "outline"
  | "scenarios"
  | "comments"
  | "attachments";

type Id<T> = { [K in keyof T]: T[K] };

export type Norm<T> = T extends G.JsonValue
  ? T
  : T extends readonly (infer U)[]
    ? Norm<U>[]
    : T extends object
      ? Id<
          { [K in keyof T as K extends Defaulted ? never : K]: Norm<T[K]> } & {
            [K in keyof T as K extends Defaulted ? K : never]-?: Norm<T[K]>;
          }
        >
      : T;

/** Tekst per språk. Språk uten tekst mangler, og vises som tomme felt. */
export type Translations = Partial<Record<Language, string>>;

export type FieldKind = G.FieldKind;
export const FIELD_KINDS: readonly FieldKind[] = [
  "STRING",
  "NUMBER",
  "DOUBLE",
  "BOOLEAN",
  "DATE",
  "YEAR",
  "MONTH",
  "PERCENT",
  "ENUM",
  "OBJECT",
  "LIST",
];

export type Field = Norm<G.DataField>;
export type Choice = Norm<G.CaseworkerChoice>;

export type FontType = G.FontType;
export type Formatter = G.Formatter;
export type Operator = G.Comparison;
export type Literal = Norm<G.Literal>;
export type Predicate = Norm<G.Predicate>;
export type Inline = Norm<G.Inline>;
export type Block = Norm<G.Block>;
export type BlockType = Block["type"];

export type DraftType = G.DocumentType;
export type TargetModule = G.TargetModule;
export type DraftMetadata = G.DraftMetadata;
export type SourceReference = G.SourceReference;
export type Scenario = Norm<G.TestScenario>;

export type CommentAnchor = G.CommentAnchor;
export type CommentReply = G.CommentReply;
export type CommentState = G.CommentState;
export type DraftComment = Norm<G.DraftComment>;

export type Draft = Norm<G.Draft>;
export type Attachment = Norm<G.AttachmentCopy>;

export type DraftStatus = G.DraftStatus;
export type ArchiveReason = G.ArchiveReason;

export type Me = G.DraftUser;
export type Integrations = G.Capabilities;

export type TemplateSource = G.TemplateSource;

export type DraftSummary = G.DraftSummary;

/** Serverens `DraftRecord` med utkastet normalisert (se `normalize.ts`). */
export type DraftRecord = Omit<G.DraftRecord, "draft"> & { draft: Draft };

export type GenerateIssue = G.ValidationIssue;
export type GenerateResult = G.GenerationResult;
export type PreviewResult = Norm<G.PreviewResult>;
/** Aktivt eierbrev som refererer en kopi av dette vedlegget (GET /utkast/{id}/bruk). */
export type AttachmentUsage = G.AttachmentUse;

export type PullRequestJob = G.PrJob;

export type ProblemBody = Partial<G.Problem>;
