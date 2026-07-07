-- vedleggHash sentraliserer cache-invalidering for valgte og redigerte vedlegg.
-- Kolonnen legges til som NOT NULL med DEFAULT (tom bytea) i én operasjon. DEFAULT-en gjør at:
--   1. Eksisterende rader fylles automatisk (regnes som utdaterte og rendres på nytt én gang).
--   2. Under rullerende deploy kan gamle pod-er fortsatt sette inn Document-rader uten å kjenne
--      kolonnen – de får DEFAULT-verdien i stedet for å feile på NOT NULL.
-- TODO fjern defaulting og påkrev i neste iterasjon (som i siste steg i V13).
ALTER TABLE "Document" ADD COLUMN IF NOT EXISTS "vedleggHash" bytea NOT NULL DEFAULT decode('', 'hex');
