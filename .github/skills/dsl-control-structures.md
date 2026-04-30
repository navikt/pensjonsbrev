# Skill: Control structures (showIf, ifNotNull, forEach, predicates)

**Scope:** every way a brevmal branches or repeats — `showIf`, `ifElse`, `ifNotNull`, `forEach`, and the predicate operators (`and`, `or`, `not`, `equalTo`, …) that build the boolean expressions they take.

**Companion docs:** [`dsl/kontroll-struktur/index.adoc`](../../docs/modules/ROOT/pages/dsl/kontroll-struktur/index.adoc) and the per-function pages under that folder.

## Always use DSL conditionals — never Kotlin `if`

The template body is a **builder that runs once at application startup**. A Kotlin `if` there is evaluated against whatever values are in scope at startup (usually nothing useful) and bakes a single branch into the template — every subsequent letter order will then render that same branch.

DSL conditionals (`showIf`, `ifElse`, `ifNotNull`, `forEach`) instead capture the `Expression` and evaluate it per letter order.

```kotlin
// wrong — Kotlin `if` runs at template-construction time, not at letter-render time
if (data.harBarnetillegg) { paragraph { … } }

// right — showIf evaluates the predicate per letter
showIf(data.harBarnetillegg) { paragraph { … } }
```

A Kotlin `if` on the result of an `Expression` selector is therefore always wrong inside a DSL body. (Kotlin `if` on a config value that is genuinely fixed at boot — e.g. a feature toggle read once at app start — is a different and rare case.)

## `showIf` — the workhorse

```kotlin
showIf(harBarnetillegg) {
    paragraph { text(bokmal { +"Du får barnetillegg." }) }
}
```

### Else and else-if — extend the chain, don't open a new `showIf`

`showIf` returns a `ShowElseScope` with three extension methods:

| Method | Effect |
|---|---|
| `orShow { … }` | Runs when all preceding predicates were false. **Terminates** the chain. |
| `orShowIf(pred) { … }` | Else-if. Chainable. |
| `orIfNotNull(expr) { it -> … }` | Else-if-not-null — binds the non-null value for the else branch. Chainable. |

```kotlin
showIf(sivilstand.equalTo(GIFT)) {
    text(bokmal { +"Du er gift." })
}.orShowIf(sivilstand.equalTo(SAMBOER)) {
    text(bokmal { +"Du er samboer." })
}.orShow {
    text(bokmal { +"Du er enslig." })
}
```

Terminate chains with `orShow { }` unless the predicate is provably exhaustive.

### `ifElse` for inline word changes — not whole blocks

`ifElse(pred, "ja", "nei")` is for **inline single-word/short-phrase variants** inside a sentence (singular/plural, gendered noun). For a whole sentence or block that is structurally identical across languages, prefer `showIf` so each language branch stays self-contained. See [`dsl-text-and-formatting.md`](dsl-text-and-formatting.md#inline-word-level-conditionals--ifelse).

### Factor shared predicates into an outer `showIf`

When two or more adjacent `showIf` blocks share an `and`-conjunct, lift it into an outer `showIf` and nest the variants inside. Rendered output is identical (nested `showIf` is logical AND), but the shared precondition appears once:

```kotlin
// wrong — `data.gate` repeated in every branch
showIf(data.gate and data.kategori.equalTo(A)) { … }
    .orShowIf(data.gate and data.kategori.equalTo(B)) { … }
    .orShowIf(data.gate and data.kategori.equalTo(C)) { … }

// right — outer showIf gates the whole block
showIf(data.gate) {
    showIf(data.kategori.equalTo(A)) { … }
        .orShowIf(data.kategori.equalTo(B)) { … }
        .orShowIf(data.kategori.equalTo(C)) { … }
}
```

Same refactor when two separate `showIf` blocks share an AND-tail and differ only by one atom — lift the shared part. Don't try to be clever about *semantically* equivalent predicates that happen to be written differently; this is a textual-dedup pattern.

## `ifNotNull` — null-safe access with binding

```kotlin
ifNotNull(data.ektefelle) { person ->
    paragraph { text(bokmal { +"Ektefellen din: " + person.navn }) }
}
```

- Multi-arg overload: `ifNotNull(a, b) { aNonNull, bNonNull -> … }` runs only when **both** are non-null. Use this instead of nested `ifNotNull`.
- Else: `.orShow { … }` (terminator) or `.orIfNotNull(other) { it -> … }` (chainable, tries the next nullable).

```kotlin
ifNotNull(primaerAdresse) { adresse ->
    text(bokmal { +"Adressen din: " + adresse })
}.orIfNotNull(sekundaerAdresse) { adresse ->
    text(bokmal { +"Alternativ adresse: " + adresse })
}.orShow {
    text(bokmal { +"Vi har ingen registrert adresse." })
}
```

If you find yourself nesting `ifNotNull` heavily, that's usually a Dto smell — group fields that are required together into a single nested data class, so the impossible state is unrepresentable. See [`write-template.md`](write-template.md#information-model-design-the-dto).

## `forEach` — iterate over an `Expression<Collection<T>>`

```kotlin
list {
    forEach(data.barn) { barn ->
        item { text(bokmal { +barn.navn + ", født " + barn.fodselsdato.format() }) }
    }
}
```

- Empty list → no output. If surrounding prose only makes sense when the list is non-empty, gate the whole block:
  ```kotlin
  showIf(barn.isNotEmpty()) {
      paragraph { text(bokmal { +"Vi har registrert at du har barn:" }) }
      list { forEach(barn) { … } }
  }.orShow {
      paragraph { text(bokmal { +"Du har ingen registrerte barn." }) }
  }
  ```
- Works in `outline`, `paragraph`, `list`, and `table` body — see [`dsl-tables-and-lists.md`](dsl-tables-and-lists.md).
- Nesting is allowed: `forEach(beregninger) { b -> forEach(b.poster) { p -> … } }`.
- `showIf` is fine *inside* `forEach`. If you find yourself filtering a collection with `forEach + showIf`, that's a smell — push the filter upstream into the fagsystem so the Dto only carries items the brev actually shows.

## Predicate operators — build `Expression<Boolean>` from selectors

The predicate passed to `showIf` / `ifElse` / `ifNotNull` is an `Expression`. Build it from Dto selectors by chaining these operators — never call Kotlin `==` or `in` on the selector directly.

### Comparisons

| Operator | Use |
|---|---|
| `.equalTo(x)` / `.notEqualTo(x)` | Equality |
| `.greaterThan(n)` / `.lessThan(n)` / `.greaterThanOrEqual(n)` / `.lessThanOrEqual(n)` | Numeric |
| `.isOneOf(a, b, …)` | Membership in enum-set / vararg list |

### Logical combinators

| Operator | Use |
|---|---|
| `a and b` (infix) | Both true |
| `a or b` (infix) | At least one true |
| `not(expr)` (prefix) / `expr.not()` (method) | Negation. Use the **prefix** form to wrap a composite (`not(a or b)`) — Kotlin operator precedence makes `!(a or b)` awkward on infix expressions. |

```kotlin
showIf((harBarn and borINorge) or harSaerligTillegg) { … }
```

### Null and collection predicates

| Operator | Use |
|---|---|
| `.notNull()` / `.isNull()` | Boolean nullability test (without binding — use `ifNotNull` if you also want the value) |
| `.isEmpty()` / `.isNotEmpty()` | On `Expression<Collection<T>>` |
| `.size()` | Collection length as `Expression<Int>` |
| `.ifNull(fallback)` | Nullable `Expression<T?>` → non-nullable `Expression<T>`. Fallback must match `T`. |

### Naming complex predicates

For lesbarhet, give long predicates a name. Better still, ask whether the logic should live upstream in the fagsystem — letter templates should carry presentation logic, not rules.

```kotlin
val harRettTilBarnetillegg = harBarn and borINorge and not(harSaerkullavtale)
showIf(harRettTilBarnetillegg) { includePhrase(BarnetilleggInfo) }
```

## Common mistakes

- Kotlin `if` on Dto values inside the DSL body — bakes one branch into the template at startup; use `showIf`.
- Using `==` or `in` on a selector — these are Kotlin operators on the bare receiver; the DSL needs `Expression`-aware operators (`.equalTo`, `.isOneOf`).
- Opening a new `showIf` for the else case instead of chaining `.orShow` / `.orShowIf` — duplicates the predicate.
- Long `ifElse` chains for content that's whole sentences or blocks — switch to `showIf`/`orShow`.
- `forEach` over a list, then `showIf` inside it to filter — the filter belongs upstream in the fagsystem.
- Forgetting `orShow { }` on a non-exhaustive chain — silently emits nothing for the unhandled cases.
- Nested `ifNotNull` instead of the multi-arg overload `ifNotNull(a, b) { aN, bN -> … }`.
- Importing predicate operators via wildcard or quick-fix — they live one symbol per import under `no.nav.pensjon.brev.template.dsl.expression.…`. Wildcards don't pull them, and the IDE often picks the wrong package.
