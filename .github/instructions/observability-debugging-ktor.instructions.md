---
applyTo: ".github/skills/observability-debugging/**"
---

# Ktor-korrigering for observability-debugging-skillen

Skillen [`observability-debugging`](../skills/observability-debugging/SKILL.md) er skrevet med
Spring Boot Actuator-metrikker som utgangspunkt. Alle Kotlin-backends i **dette** repoet
(`pensjon/brevbaker`, `brevbaker/pdf-bygger`, `skribenten-backend`) bruker i stedet
`io.ktor:ktor-server-metrics-micrometer` (`install(MicrometerMetrics) { ... }` i hver tjenestes
`Metrics.kt`, uten overstyrt `metricName`). Det gir andre metrikk- og tag-navn enn skillens
PromQL-eksempler antar. Bruk tabellen under til å oversette enhver spørring fra skillen før den
kjøres mot Mimir for disse appene.

`skribenten-web` og `brevoppskrift-web` er frontend/BFF, ikke JVM-backends med disse metrikkene —
denne korrigeringen gjelder ikke dem.

## Metrikk- og tag-oversettelse

| Spring Boot (skillens antakelse) | Ktor (faktisk i dette repoet) |
|---|---|
| `http_server_requests_seconds_count` / `_sum` / `_bucket` | `ktor_http_server_requests_seconds_count` / `_sum` / `_bucket` |
| tag `uri` | tag `route` (Ktors rutemal, f.eks. `/api/letter/{brevkode}`) |
| tag `exception` | tag `throwable` (fullt kvalifisert klassenavn, eller `"n/a"`) |
| tag `outcome` (`SUCCESS`/`CLIENT_ERROR`/`SERVER_ERROR`) | *finnes ikke* — bruk `status=~"5.."` / `status=~"4.."` i stedet |
| `http_server_requests_active_seconds_count` | `ktor_http_server_requests_active` (rent gauge, ingen `_seconds`- eller `_count`-suffiks) |
| tag `address` | finnes som default i Ktor-pluginen, men fjernes eksplisitt i alle tre tjenesters `Metrics.kt` via `MeterFilter.ignoreTags("address")` — ikke forvent den i Mimir |

Kilde: `io.ktor:ktor-server-metrics-micrometer-jvm` (verifisert mot versjonen brukt i
`gradle/libs.versions.toml`) — default `metricName = "ktor.http.server.requests"`, med
default-tags `address`, `method`, `route`, `status`, `throwable` satt i `addDefaultTags(...)`.
Alle tre tjenester setter `distributionStatisticConfig` med `percentilesHistogram(true)` og egne
`serviceLevelObjectives(...)`, så `_bucket`-serien finnes og `histogram_quantile()` kan brukes som
i skillen.

## Korrigerte eksempler

Erstatt skillens Mimir-spørringer med disse (samme `$CLUSTER`/`$APP`-plassholdere og headere som
resten av skillen):

```bash
# Feilrate per rute (erstatter skillens http_server_requests_seconds_count + uri)
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=sum(rate(ktor_http_server_requests_seconds_count{k8s_cluster_name=\"$CLUSTER\",app=\"$APP\",status=~\"5..\"}[5m]))by(route)" | jq .

# p95-latens per rute (erstatter skillens http_server_requests_seconds_bucket + uri)
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=histogram_quantile(0.95,sum(rate(ktor_http_server_requests_seconds_bucket{k8s_cluster_name=\"$CLUSTER\",app=\"$APP\"}[5m]))by(le,route))" | jq '.data.result[] | {endpoint: .metric.route, p95_seconds: .value[1]}'

# Antall aktive requests (erstatter skillens http_server_requests_active_seconds_count)
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=ktor_http_server_requests_active{k8s_cluster_name=\"$CLUSTER\",app=\"$APP\"}" | jq .
```

## Boundaries

### ✅ Always

- Oversett `uri` → `route`, `exception` → `throwable`, og dropp `outcome`-baserte spørringer til
  fordel for `status=~"..."` når du bruker `observability-debugging`-skillen mot
  `pensjon/brevbaker`, `brevbaker/pdf-bygger` eller `skribenten-backend`.

### 🚫 Never

- Ikke anta at `address`-taggen finnes i Mimir for disse appene — den er eksplisitt fjernet.
- Ikke anta at frontend/BFF-appene (`skribenten-web`, `brevoppskrift-web`) eksponerer disse
  Ktor/Micrometer-metrikkene.
