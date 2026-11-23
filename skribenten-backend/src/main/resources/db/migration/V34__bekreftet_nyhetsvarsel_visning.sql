CREATE VIEW bekreftetNyhetsvarsel AS
SELECT "navIdent", "nyhetsvarselId", "gjennomfoertOmvisningen" AS bekreftet
FROM nyhetsvarselstatus
WHERE "gjennomfoertOmvisningen" IS NOT NULL;
