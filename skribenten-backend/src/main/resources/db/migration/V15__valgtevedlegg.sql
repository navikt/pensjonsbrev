CREATE TABLE IF NOT EXISTS valgteVedlegg
(
    "brevredigeringId" BIGINT PRIMARY KEY CONSTRAINT valgteVedlegg_brevredigeringid_unique UNIQUE,
    valgtevedlegg            JSON  NOT NULL,

    CONSTRAINT fk_valgte_vedlegg_brevredigeringid__id FOREIGN KEY ("brevredigeringId") REFERENCES brevredigering (id) ON DELETE CASCADE ON UPDATE RESTRICT
)