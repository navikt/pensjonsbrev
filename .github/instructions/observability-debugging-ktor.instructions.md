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

### `k8s_cluster_name`-verdier i Mimir

Skillens eksempler antar cluster-navn som `dev-gcp` / `prod-gcp`. For `ktor_http_server_requests_*`
i Mimir er den faktiske `k8s_cluster_name`-verdien kortformen **`dev`** / **`prod`** (ikke
`dev-gcp`/`prod-gcp`). En spørring med `-gcp`-suffiks gir et tomt resultat (`"result":[]`) uten
feilmelding, så sjekk alltid label-verdiene først, f.eks.:

```bash
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=count(ktor_http_server_requests_seconds_count%7Bapp=%22$APP%22%7D)by(k8s_cluster_name)" | jq .
```

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

## Connection pool: `db_client_connections_*`, ikke `hikaricp_*`

Skillens `query-library.md` foreslår `hikaricp_connections_active / hikaricp_connections_max` for
metning i tilkoblingspoolen. Den serien **finnes ikke** for appene i dette repoet — OTel-javaagenten
instrumenterer HikariCP etter OpenTelemetry semconv i stedet. Bruk disse navnene:

| Skillens antakelse | Faktisk i dette repoet |
|---|---|
| `hikaricp_connections_active` | `db_client_connections_usage{state="used"}` |
| `hikaricp_connections_idle` | `db_client_connections_usage{state="idle"}` |
| `hikaricp_connections_max` | `db_client_connections_max` |
| `hikaricp_connections_pending` | `db_client_connections_pending_requests` |
| `hikaricp_connections_timeout_total` | `db_client_connections_timeouts_total` |

```bash
# Metning i tilkoblingspoolen (%)
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=db_client_connections_usage{k8s_cluster_name=\"$CLUSTER\",app=\"$APP\",state=\"used\"}/db_client_connections_max{k8s_cluster_name=\"$CLUSTER\",app=\"$APP\"}*100" | jq .
```

Merk at appene kjører **dobbel instrumentering** — både Micrometer og OTel-javaagenten. Derfor finnes
flere metrikkpar som måler det samme under ulike navn (`ktor_http_server_requests_seconds_*` +
`http_server_request_duration_seconds_*`, `jvm_memory_used_bytes` med både Micrometer-taggen `area`
og OTel-taggen `jvm_memory_type`). Velg ett sett per spørring — ikke summer på tvers, da teller du dobbelt.

## Container-metrikker (cAdvisor): ingen `app`-tag, ingen CFS-serier

Skillens ressursspørringer (`Pattern 3`, «Saturation» i `query-library.md`) treffer ikke som skrevet:

| Skillens antakelse | Faktisk i dette repoet |
|---|---|
| `container_*{app="$APP"}` | cAdvisor-seriene har **ingen `app`-tag** — filtrer på `namespace="pensjonsbrev"` + `container="$APP"` eller `pod=~"$APP-.*"` |
| `container_spec_memory_limit_bytes` | finnes **ikke** i Mimir — bruk `kube_pod_container_resource_limits{resource="memory"}` |
| `container_cpu_cfs_throttled_periods_total` / `container_cpu_cfs_periods_total` | finnes **ikke** for våre containere, fordi Nais ikke setter `limits.cpu` — uten CPU-limit er det ingen CFS-kvote å strupe mot. Bruk `container_pressure_cpu_waiting_seconds_total` som mål på CPU-kø |

```bash
# Minnebruk i % av grensen (erstatter skillens container_spec_memory_limit_bytes-variant)
curl -s -H "User-Agent: nav-pilot/observability-debugging" -H "X-Scope-OrgID: tenant" \
  "https://mimir.nav.cloud.nais.io/prometheus/api/v1/query?query=container_memory_working_set_bytes{k8s_cluster_name=\"$CLUSTER\",namespace=\"pensjonsbrev\",container=\"$APP\"}/on(pod,namespace,container)kube_pod_container_resource_limits{k8s_cluster_name=\"$CLUSTER\",namespace=\"pensjonsbrev\",container=\"$APP\",resource=\"memory\"}*100" | jq .
```

## Når hele poden er stille: skill app-feil fra node-feil

Både `ktor_*`, `jvm_*` og `db_client_connections_*` kommer fra appens eget `/metrics`-endepunkt. Er
appen frosset, mangler de per definisjon — **«ingen data» betyr ikke «ingenting galt»**. Bruk kilder
utenfor prosessen til å skille årsakene:

- `container_cpu_usage_seconds_total` mot null (ikke i taket) ⇒ prosessen får ikke CPU — se etter
  node-feil, ikke GC. Er den i taket, er det appen selv.
- Fryser **alle** containerne i poden samtidig (`cloudsql-proxy`, `texas`, `elector`), er det ikke appen.
- Slutter cAdvisor å oppdatere `container_memory_working_set_bytes` for hele poden, har kubelet stanset.
- `kube_node_status_condition{condition="Ready",status="unknown"} == 1` for pod-ens node bekrefter
  node-feil. Finn noden med `kube_pod_info{pod="$POD"}` og sjekk om alle poder på noden mistet Ready
  samtidig — da er appen kollateral skade.
- I loggen er `HikariPool-1 - Thread starvation or clock leap detected (housekeeper delta=...)` en
  presis måling av hvor lenge JVM-en faktisk stod stille.

## Loki: `k8s_cluster_name` er `prod`/`dev`, også her

Skillen bruker `prod-gcp`/`dev-gcp` i LogQL-eksemplene. I Loki er de gyldige verdiene `dev`,
`dev-fss`, `prod` og `prod-fss` — samme kortform som i Mimir. Feil verdi gir et tomt resultat uten
feilmelding.

## Boundaries

### ✅ Always

- Oversett `uri` → `route`, `exception` → `throwable`, og dropp `outcome`-baserte spørringer til
  fordel for `status=~"..."` når du bruker `observability-debugging`-skillen mot
  `pensjon/brevbaker`, `brevbaker/pdf-bygger` eller `skribenten-backend`.
- Bruk `k8s_cluster_name="dev"` / `"prod"` (ikke `-gcp`-suffiks) i Mimir- **og** Loki-spørringer for
  disse appene, og verifiser label-verdien først dersom en spørring uventet gir tomt resultat.
- Filtrer cAdvisor-serier (`container_*`) på `namespace` + `container`/`pod` — de har ingen `app`-tag.
- Skill app-feil fra node-feil før du konkluderer når metrikkene forsvinner: sjekk
  `kube_node_status_condition` for pod-ens node.

### 🚫 Never

- Ikke anta at `address`-taggen finnes i Mimir for disse appene — den er eksplisitt fjernet.
- Ikke anta at frontend/BFF-appene (`skribenten-web`, `brevoppskrift-web`) eksponerer disse
  Ktor/Micrometer-metrikkene.
- Ikke bruk `hikaricp_*`, `container_spec_memory_limit_bytes` eller `container_cpu_cfs_*` — ingen av
  dem finnes for disse appene.
- Ikke summer Micrometer- og OTel-varianten av samme måling — det gir dobbelttelling.
