CREATE TABLE IF NOT EXISTS brevredigering
(
    id                       BIGSERIAL PRIMARY KEY,
    "saksId"                 BIGINT      NOT NULL,
    "vedtaksId"              BIGINT      NULL,
    brevkode                 VARCHAR(50) NOT NULL,
    spraak                   VARCHAR(50) NOT NULL,
    "avsenderEnhetId"        VARCHAR(50) NULL,
    "saksbehandlerValg"      JSON        NOT NULL,
    "redigertBrev"           JSON        NOT NULL,
    "redigertBrevHash"       bytea       NOT NULL,
    "laastForRedigering"     BOOLEAN     NOT NULL,
    distribusjonstype        VARCHAR(50) NOT NULL,
    "redigeresAvNavIdent"    VARCHAR(50) NULL,
    "sistRedigertAvNavIdent" VARCHAR(50) NOT NULL,
    "opprettetAvNavIdent"    VARCHAR(50) NOT NULL,
    opprettet                TIMESTAMP   NOT NULL,
    sistredigert             TIMESTAMP   NOT NULL,
    "sistReservert"          TIMESTAMP   NULL,
    "signaturSignerende"     VARCHAR(50) NOT NULL,
    "signaturAttestant"      VARCHAR(50) NULL,
    "journalpostId"          BIGINT      NULL,
    "attestertAvNavIdent"    VARCHAR(50) NULL
);
CREATE INDEX IF NOT EXISTS brevredigering_saksid ON brevredigering ("saksId");
CREATE INDEX IF NOT EXISTS brevredigering_opprettetavnavident ON brevredigering ("opprettetAvNavIdent");

CREATE TABLE IF NOT EXISTS "Document"
(
    id                 BIGSERIAL PRIMARY KEY,
    brevredigering     BIGINT NOT NULL CONSTRAINT document_brevredigering_unique UNIQUE,
    "dokumentDato"     DATE   NOT NULL,
    brevpdf            bytea  NOT NULL,
    "redigertBrevHash" bytea  NOT NULL,
    CONSTRAINT fk_document_brevredigering__id FOREIGN KEY (brevredigering) REFERENCES brevredigering (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS favourites
(
    id            SERIAL,
    "User Id"     VARCHAR(50) NOT NULL,
    "Letter Code" VARCHAR(50) NOT NULL,
    CONSTRAINT PK_Favourite_ID PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS mottaker
(
    "brevredigeringId" BIGINT PRIMARY KEY CONSTRAINT mottaker_brevredigeringid_unique UNIQUE,
    "type"             VARCHAR(50) NOT NULL,
    "tssId"            VARCHAR(50) NULL,
    navn               VARCHAR(50) NULL,
    postnummer         VARCHAR(50) NULL,
    poststed           TEXT        NULL,
    adresselinje1      TEXT        NULL,
    adresselinje2      TEXT        NULL,
    adresselinje3      TEXT        NULL,
    landkode           VARCHAR(2)  NULL,
    CONSTRAINT fk_mottaker_brevredigeringid__id FOREIGN KEY ("brevredigeringId") REFERENCES brevredigering (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS busy
(
    busy BOOLEAN NOT NULL CONSTRAINT busy_busy_unique UNIQUE
);
