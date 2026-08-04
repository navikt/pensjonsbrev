import { type Express, type NextFunction, type Request, type Response } from "express";
import { collectDefaultMetrics, Histogram, Registry } from "prom-client";

const register = new Registry();

// Gir blant annet nodejs_eventloop_lag_seconds, nodejs_heap_size_used_bytes og
// nodejs_gc_duration_seconds. Event loop lag er hovedgrunnen til at vi samler disse: en blokkert
// event loop gjør appen treg eller helt uresponsiv uten at container-CPU ser unormal ut, så
// signalet finnes verken i plattformmetrikkene fra nais eller i de trace-utledede metrikkene.
collectDefaultMetrics({ register });

// BFF-en proxyer mot skribenten-backend, som har 60s requestTimeout mot brevbaker. Grensene går
// derfor til 120s, slik at et kall som går til timeout havner innenfor histogrammet og ikke i
// +Inf. Det er også grunnen til at vi ikke kan nøye oss med de trace-utledede metrikkene fra
// auto-instrumenteringen: Tempo sine bucket-grenser stopper på 5s og kan ikke konfigureres herfra.
const svartid = new Histogram({
  name: "http_server_request_duration_seconds",
  help: "Svartid for forespørsler til BFF-en, i sekunder",
  labelNames: ["method", "route", "status_code"],
  buckets: [0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 1, 2, 5, 10, 30, 60, 120],
  registers: [register],
});

/**
 * Grupperer forespørsler i et fast, lite sett med verdier. Rå sti kan ikke brukes som label -
 * `/bff/skribenten-backend/sak/12345/brev/678` ville gitt en ny tidsserie per saks- og brev-id.
 *
 * Oppdelingen er bevisst grov: skribenten-backend eksponerer allerede latens per rute, så det er
 * der man finner ut *hvilket* endepunkt som er tregt. BFF-ens oppgave er å vise om proxylaget som
 * helhet er tregt eller feiler, inkludert OBO-veksling og nettverk.
 */
export function klassifiserRute(sti: string): string {
  if (sti.startsWith("/bff/skribenten-backend")) {
    return "proxy";
  }
  if (sti.startsWith("/bff/api")) {
    return "bff-api";
  }
  if (sti.startsWith("/internal")) {
    return "probe";
  }
  return "static";
}

const maalSvartid = (request: Request, response: Response, next: NextFunction): void => {
  const stopp = svartid.startTimer();
  // "close" fyres både når svaret er ferdig sendt og når klienten kobler fra underveis. "finish"
  // ville mistet avbrutte forespørsler, og det er nettopp de trege vi er mest interessert i.
  response.once("close", () => {
    // Ved avbrudd står response.statusCode igjen på standardverdien 200, siden vi aldri rakk å
    // sette den. Uten dette ville trege kall brukeren ga opp sett ut som vellykkede 2xx i
    // histogrammet. 499 er samme konvensjon som nginx bruker for "client closed request".
    const avbrutt = !response.writableEnded;
    stopp({
      method: request.method,
      route: klassifiserRute(request.path),
      status_code: avbrutt ? 499 : response.statusCode,
    });
  });
  next();
};

export function setupMetrics(server: Express) {
  server.use(maalSvartid);

  server.get("/internal/metrics", async (_request, response) => {
    response.set("Content-Type", register.contentType);
    response.end(await register.metrics());
  });
}
