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

**Unleash** integration for feature flags. Define in using an enum (per template module) for type safety and easy access:
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

