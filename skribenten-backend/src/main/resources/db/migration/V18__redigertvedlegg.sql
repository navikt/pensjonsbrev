CREATE TABLE IF NOT EXISTS redigertVedlegg
(
    "brevredigeringId"            BIGINT       NOT NULL,
    "vedleggId"                   VARCHAR(50)  NOT NULL,
    "redigertVedleggKryptert"     bytea        NOT NULL,
    "redigertVedleggKryptertHash" bytea        NOT NULL,

    CONSTRAINT redigertvedlegg_pkey PRIMARY KEY ("brevredigeringId", "vedleggId"),
    CONSTRAINT fk_redigert_vedlegg_brevredigeringid__id FOREIGN KEY ("brevredigeringId") REFERENCES brevredigering (id) ON DELETE CASCADE ON UPDATE RESTRICT
)
