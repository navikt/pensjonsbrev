# AGENTS.md - AI Coding Assistant Guide for Pensjonsbrev

## Architecture Overview

The **Pensjonsbrev** repository contains applications and libraries related to producing letters to users of Nav. 
The system consists of multiple Gradle/Kotlin modules and TypeScript/React frontends. The core architecture has the following main components:

1. **Brevbaker** (`brevbaker/pdf-bygger`, `brevbaker/core`, `brevbaker/dsl`, `brevbaker/template-model-generator`) - Template engine that renders letters to PDF/HTML via a custom DSL
2. **Pensjon-brevbaker** (`pensjon/brevbaker`) - Domain-specific application that combines templates for pension and disability benefit letters and serves them via API
3. **Skribenten** (`skribenten-backend`, `skribenten-web`) - Letter authoring system with editing UI for caseworkers

Letter templates live in domain-specific modules: `pensjon/maler`, `alder/maler`, `ufoere/maler`, `etterlattemaler`, `planlegge-pensjon-maler`.

Additionally, there is PoC application `brevoppskrift-web` used to view automatically generated documentation for the letter templates. 

### Key Data Flow
- **Auto letters**: Template DSL → LetterMarkup → PDF-bygger (LaTeX) → PDF
- **Editable letters**: Template DSL → LetterMarkup → Skribenten edits → Edit.Letter → converted to LetterMarkup → PDF
- ExStream/Doksys are **legacy** systems being migrated away from (see `docs/adr/MigreringsstrategiBrevlosninger.md`)

## Letter Template DSL Patterns

Templates use a Kotlin DSL with strict scoping. Key patterns found in `pensjon/maler/src/main/kotlin`:

```kotlin
@TemplateModelHelpers  // Triggers KSP to generate *Selectors for type-safe field access
object UngUfoerAuto : AutobrevTemplate<UngUfoerAutoDto> {
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(...)
    ) {
        title { text(bokmal { +"Title" }, nynorsk { +"Tittel" }) }
        outline {
            paragraph { text(bokmal { +"Content" }) }
            showIf(harBarnetillegg) { /* conditional content */ }
            includePhrase(ReusablePhrase)  // Reusable content blocks
        }
    }
}
```

- **Expressions**: Use generated selectors (e.g., `UngUfoerAutoDtoSelectors.totaltUfoerePerMnd`) for type-safe field access
- **String concatenation**: `+"Text " + expression.format() + " more"` (unary plus for literals)
- **Conditionals**: `showIf(predicate) { }.orShowIf(other) { }.orShow { }` or inline `ifElse(pred, "yes", "no")`
- **Phrases**: `OutlinePhrase`, `ParagraphPhrase`, `TextOnlyPhrase`, `PlainTextOnlyPhrase` for reusable content

## Golden Paths

These are the *officially blessed* recipes for common tasks. Follow them step-by-step. Each ends in a verifiable command. Deviations are allowed, but default to the path.

> Only one recipe is documented so far: **Add a new redigerbar brev**. More recipes will be added as they are validated against real tasks.

### Add a new redigerbar (caseworker-editable) brev

**When:** A caseworker needs to produce a letter and edit its text before sending.

#### 0. Pick the right module

Redigerbare brev live in several maler modules — pick the one matching the domain:

| Module | Brevkode registry | Fixtures file (examples of current location) |
|---|---|---|
| `pensjon/maler` | `pensjon/api-model/.../maler/Pesysbrevkoder.kt` | `pensjon/maler/src/test/kotlin/no/nav/pensjon/brev/Fixtures.kt` |
| `alder/maler` | `alder/api-model/.../model/Aldersbrevkoder.kt` | `alder/maler/src/test/kotlin/no/nav/pensjon/brev/alder/Fixtures.kt` |
| `ufoere/maler` | `ufoere/api-model/.../Ufoerebrevkoder.kt` | `ufoere/maler/src/test/kotlin/no/nav/pensjon/brev/ufore/Fixtures.kt` |
| `etterlattemaler` | domain-local registry under `etterlattemaler/…` | `etterlattemaler/src/test/kotlin/no/nav/pensjon/etterlatte/Fixtures.kt` |
| `planlegge-pensjon-maler` | domain-local registry | `planlegge-pensjon-maler/src/test/kotlin/no/nav/pensjon/brev/planleggepensjon/Fixtures.kt` |

New maler modules may appear — locate the module's brevkode registry (`*brevkoder*.kt`) and its Fixtures file before starting..

#### 1. Package layout is domain-local

Packages are organised by domain, not by a fixed `redigerbar/` subfolder. Mirror existing templates in the same module / domain area. Examples actually in use:

- `ufoere/maler/src/main/kotlin/no/nav/pensjon/brev/ufore/maler/uforeavslag/UforeAvslagYrkesskadeGodkjent.kt`
- `pensjon/maler/src/main/kotlin/no/nav/pensjon/brev/maler/redigerbar/VedtakEndringAvAlderspensjonInstitusjonsopphold.kt`
- `pensjon/maler/src/main/kotlin/no/nav/pensjon/brev/maler/legacy/redigerbar/OpphoerGjenlevendepensjon.kt`

Put the new template next to siblings it is conceptually related to.

#### 2. Add the brevkode

Add the constant to the module's brevkode registry (e.g. `Pesysbrevkoder.Redigerbar`, `Ufoerebrevkoder.Redigerbar`, `Aldersbrevkoder.Redigerbar`).

Rules — **read these before picking a code**:

- **Unique across all modules.** Enforced by `AllTemplatesTest."alle maler skal bruke en unik brevkode"` — CI fails on collisions.
- **≤ 50 characters.** It is the letter's ID.
- **Immutable once in production.** A brevkode in prod is a stable identifier external systems may reference — never rename, never reuse. If the letter is replaced, allocate a new code.

#### 3. Add the Dto

In the module's `api-model` (e.g. `pensjon/api-model`, `ufoere/api-model`, `alder/api-model`). Plain `data class`; required fields have **no defaults**. Nest a `SaksbehandlerValg` / `Saksbehandlervalg` class if the brev has caseworker choices. Mirror the naming the surrounding module already uses.

#### 4. Add the template

```kotlin
@TemplateModelHelpers
object X : RedigerbarTemplate<XDto> {
    override val kode = <Module>brevkoder.Redigerbar.X   // the constant from step 2
    override val kategori = Brevkategori.<...>           // module-local enum
    override val brevkontekst = TemplateDescription.Brevkontekst.<...>
    override val sakstyper = setOf(Sakstype.<...>)       // module-local Sakstype
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),          // pick the actual subset — see step 5
        letterMetadata = LetterMetadata(
            displayTitle = "...",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title { text(bokmal { +"..." }, nynorsk { +"..." }) }
        outline { paragraph { text(bokmal { +"..." }, nynorsk { +"..." }) } }
    }
}
```

`Brevkategori`, `Sakstype` and similar enums are **module-local** — use the ones imported by neighbouring templates in the same module, not `pensjon`'s versions by default.

#### 5. Pick the language combination (type-safe)

`languages(...)` is overloaded and its argument list *is the type contract* for every `text { }` block in the template:

| Call | Type alias | Required DSL branches inside `text { }` |
|---|---|---|
| `languages(Bokmal)` | `LangBokmal` | `bokmal { }` |
| `languages(Bokmal, Nynorsk)` | `LangBokmalNynorsk` | `bokmal { }`, `nynorsk { }` |
| `languages(Bokmal, English)` | `LangBokmalEnglish` | `bokmal { }`, `english { }` |
| `languages(Bokmal, Nynorsk, English)` | `LangBokmalNynorskEnglish` / `BaseLanguages` | all three |

Pick the subset the brev actually supports — do not pad with placeholder translations. Missing or extra branches are compile errors, not runtime.

#### 6. Wire the fixture

If `XDto` introduces new nested types, extend the module's `Fixtures.kt` so `Fixtures.create<XDto>()` resolves via the reflection-based factory. Location varies per module — see the table in step 0. Use existing fixture helpers in the same file as your template (search neighbours for `Fixtures.create<`).

#### 7. Add the integration test

Templates are auto-discovered via each module's `ProductionTemplates`; no manual registration. Still write an integration test so the brev has fixture coverage and renders.

Follow the integration-test pattern already used in the target module (search for `@Tag(TestTags.INTEGRATION_TEST)` and `renderTestPDF` / `renderTestHtml` inside that module's `src/test`). Integration tests spin up pdf-bygger via **Testcontainers** — Docker must be running, but you do not need `docker compose up` yourself.

#### 8. Verify

Run the module's integrationTest task — not `manualTest`. Integration tests start pdf-bygger via Testcontainers, so only a running Docker daemon is required.

```bash
# for the module you edited:
./gradlew :pensjon:maler:integrationTest
./gradlew :alder:maler:integrationTest
./gradlew :ufoere:maler:integrationTest
./gradlew :planlegge-pensjon-maler:integrationTest
./gradlew :etterlattemaler:integrationTest
```

Also run the cross-module uniqueness / documentation test:

```bash
./gradlew :pensjon:brevbaker:test --tests "no.nav.pensjon.brev.AllTemplatesTest"
```

This enforces the unique-brevkode rule and renders the template-documentation snapshot for every language the brev declares.

## Do NOT Edit By Hand

These files are generated or authoritative and must never be hand-edited by a human or agent:

- `**/routeTree.gen.ts` — regenerated by TanStack Router Vite plugin.
- `**/*.api` files — regenerate via `./gradlew apiDump` and commit the diff.
- `**/build/generated/**` — KSP output (e.g. `*DtoSelectors`). Change the annotated source and rebuild.
- `build/`, `*/build/`, `gradle-user-home/` — all build output.
- `package-lock.json`, `gradle.lockfile` — only touch as part of a deliberate dependency bump.
- `secrets/`, `*.env`, anything produced by `fetch-secrets.sh` — never commit, never paste into AI prompts.

If the agent finds itself about to write to one of these, stop and change the upstream source instead.

## Pitfalls & Conventions (frequent AI mistakes)

### Letter DSL (Kotlin)
- **Literals need unary `+`**: `bokmal { +"Tekst" + expr.format() }` — a bare `"Tekst"` is a compile error inside `text { }`.
- **Language parity is compile-time**: `languages(Bokmal, Nynorsk)` gives every `text { }` a `LangBokmalNynorsk` type — exactly a `bokmal { }` and a `nynorsk { }` branch are required, adding `english { }` is a compile error. Pick the actual subset the brev supports (see type aliases in `brevbaker/dsl/.../LanguageSupport.kt`: `LangBokmal`, `LangBokmalNynorsk`, `LangBokmalEnglish`, `LangBokmalNynorskEnglish`).
- **Prefer `showIf(...).orShowIf(...).orShow { }`** over nested Kotlin `if`. Terminate chains with `orShow { }` unless the predicate is provably exhaustive.
- **Use generated selectors** (e.g. `...DtoSelectors.pesysData.krav.virkDatoFom`) — never call `.toString()` on an `Expression`. Chain with `.format()`, `.equalTo(...)`, `.isOneOf(...)`, `.and(...)`, `.not()`, `.greaterThan(...)`, `.ifNull(...)`.
- **`fritekst("...")` vs `+"..."`**: `fritekst` produces caseworker-editable text in Skribenten; `+"..."` is fixed. Pick deliberately — do not mix defaults in.
- **Every new `Dto` field needs a fixture value.** `Fixtures.create<T>()` uses reflection and will fail without a registered value for new nested types.
- **`@TemplateModelHelpers`** on the template object triggers KSP to generate selectors. Missing this annotation = no selectors = red editor.

### Testing
- **Test tags**: `@Tag(TestTags.INTEGRATION_TEST)` is the verification path for maler modules and runs via `./gradlew :<module>:integrationTest` (starts pdf-bygger via Testcontainers — requires a running Docker daemon). `@Tag(TestTags.MANUAL_TEST)` is for ad-hoc visual inspection only — **do not** rely on it as a Golden Path verification step.
- Output paths: `build/test_html/` and `build/test_pdf/` — per module. Open them to inspect visual output.

### API & Binary Compatibility
- Any change to a public declaration in `brevbaker:api-model-common`, `brevbaker:dsl`, or `brevbaker:core` requires `./gradlew apiDump` and a committed `.api` diff. CI will fail otherwise.

## Project Structure & Build

**Multi-module Gradle project** with version catalog (`gradle/libs.versions.toml`). Each module has domain separation:
- `pensjon/brevbaker` - Entry point app combining all templates
- `brevbaker/dsl` - Template DSL library
- `brevbaker/core` - Rendering engine
- `brevbaker/api-model-common` - Shared API models (published artifact)

### Critical Build Commands
```bash
./gradlew build                    # Build all
./gradlew apiDump                  # Update .api files when public API changes
./gradlew integrationTest          # Tests tagged @Tag("integration-test")
./gradlew manualTest               # Visual tests tagged @Tag("manual-test")
```

**Binary compatibility validator**: Public API changes in `brevbaker:api-model-common`, `brevbaker:dsl`, `brevbaker:core` require running `./gradlew apiDump` to update `.api` files or build fails.

## Code Generation (KSP)

`@TemplateModelHelpers` annotation triggers KSP (`brevbaker/template-model-generator`) to generate type-safe selectors:
```kotlin
@TemplateModelHelpers
object MyTemplate : AutobrevTemplate<MyDto> { ... }
// Generates: MyDtoSelectors.field1, MyDtoSelectors.field2, etc.
```
Selectors enable expression chains: `data.person.address.postnummer`.

## Testing Conventions

**Test tags**: `@Tag(TestTags.INTEGRATION_TEST)` requires pdf-bygger service, `@Tag(TestTags.MANUAL_TEST)` for visual inspection.

**Test helpers** (`brevbaker/core/src/testFixtures`):
```kotlin
LetterTestImpl(template, Fixtures.create<DtoType>(), Language.Bokmal, Fixtures.fellesAuto)
    .renderTestPDF("OUTPUT_NAME")  // Writes to build/test_html/ or build/test_pdf/
```

**Fixtures**: Domain-specific fixture factories in `*/maler/src/test/.../Fixtures.kt` use `inline fun <reified T> create(): T` pattern.

**Parallel execution**: Tests run concurrently by default (configured in `build.gradle.kts`).

## Local Development Workflow

### Prerequisites
```bash
# Setup GitHub packages.read token in ~/.gradle/gradle.properties:
gpr.user=<username>
gpr.token=<pat token>

# For npm packages:
npm login --registry=https://npm.pkg.github.com --auth-type=legacy
```

### Running Services
```bash
./gradlew build

# Brevbaker + PDF-bygger only:
docker-compose up -d --build

# Full stack (Skribenten):
./fetch-secrets.sh  # Requires kubectl, naisdevice, gcloud auth
docker compose --profile skribenten up -d --build
npm run dev --prefix skribenten-web/frontend  # Frontend on :5173, access via :8083/vite-on
```

**Debug ports** (in docker-compose.yml): brevbaker :5015, pdf-bygger :5016, skribenten-backend :5017

### Iterating on LaTeX
For fast LaTeX updates during visual tests, use this before-launch command:
```bash
docker exec -u 0 -it pensjonsbrev-pdf-bygger-1 rm -rf /app/pensjonsbrev_latex && \
docker cp ./brevbaker/pdf-bygger/containerFiles/latex pensjonsbrev-pdf-bygger-1:/app/pensjonsbrev_latex/
```

## TypeScript/React Frontend Stack

- **Router**: TanStack Router with file-based routing (generates `routeTree.gen.ts` - DO NOT edit)
- **State**: TanStack Query for server state, Immer for complex local state (letter editor)
- **Styling**: NAV Aksel design system (`@navikt/ds-react`), Emotion for CSS-in-JS
- **Linting**: Biome (replaces ESLint/Prettier) with auto-format in `lint-staged.config.ts`
- **Testing**: Vitest for unit tests, Cypress for E2E and component tests

### Frontend File Conventions
- `routes/` - File-based routing (e.g., `saksnummer_/$saksId/brevvelger/route.tsx`)
- `*-queries.ts` - TanStack Query definitions with queryKey factories
- `*.cy.tsx` or `*.cy.ts` - Cypress tests

## Skribenten Letter Editing Model

Letters use a dual-model approach:
1. **LetterMarkup** (immutable, rendered from template) - structure with IDs
2. **Edit.Letter** (mutable, user edits) - tracks changes with `editedText`, `deletedContent`, `parentId`, `id`

Conversion: `letterMarkup.toEdit()` → edit → `editLetter.toMarkup()` → render PDF

**Reservation system**: Letters are locked by a user with reservation requests during editing. Reservations expire, managed by `BrevreservasjonPolicy`.

## API Compatibility Strategy

When changing required fields in published API models (e.g., `brevbaker:api-model-common`):
1. Add new field alongside old field (both required) and publish new version of API model
2. Update consumers to send both fields (Brevbaker ignores unknown fields, so this is safe)
3. Remove the old field from API model and publish new version
4. Update Brevbaker to use new field only (Brevbaker requires new field, and ignores the old unknown field, so this is safe)
5. Update consumers to use new field only

See README "Endring av obligatoriske felter i API-model" section.

## Authentication & Secrets

- **AzureAD JWT** for service-to-service auth
- **Wonderwall** handles user authentication (local docker setup)
- **On-behalf-of flow** in skribenten-backend for downstream calls

**Local secrets**: `./fetch-secrets.sh` pulls from Kubernetes dev-gcp namespace (requires naisdevice, kubectl, gcloud).

## Feature Toggles

**Unleash** integration for feature flags. Define using an enum (per template module) for type safety and easy access:
```kotlin
enum class FeatureToggles(private val key: String) {
    myFeature("myFeature");
    val toggle = FeatureToggle(key)
}
```
Use in templates: `override val featureToggle = FeatureToggles.myFeature.toggle`

## Database (Skribenten only)

- **Exposed ORM** with Flyway migrations (`skribenten-backend/src/main/resources/db/migration/`)
- **Encrypted columns**: `redigertBrevKryptert` uses `KrypteringService`
- **JSON columns**: Custom transformers in `DatabaseSchema.kt` with Jackson serialization
- **One-shot jobs**: Idempotent migrations via `OneShotJobTable` with leader election

## Key Abbreviations
- **PE**: Pensjon (pension domain)
- **UT**: Uføretrygd (disability benefits)
- **AP**: Alderspensjon (retirement pension)
- **PEN/PESYS**: Pension and disability benefits systems
- **TSS**: Tjenestebasert Samhandler Service (external recipients)

