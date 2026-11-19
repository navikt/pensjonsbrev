ALTER TABLE brevredigering
    ADD COLUMN "tekniskVersjon" INTEGER NOT NULL DEFAULT 1;

ALTER TABLE redigertVedlegg
    ADD COLUMN "tekniskVersjon" INTEGER NOT NULL DEFAULT 1;

CREATE FUNCTION brevredigering_teknisk_versjon() RETURNS INTEGER
    LANGUAGE sql STABLE AS
$$ SELECT COALESCE(MAX("tekniskVersjon"), 1) FROM brevredigering $$;

CREATE FUNCTION redigertvedlegg_teknisk_versjon() RETURNS INTEGER
    LANGUAGE sql STABLE AS
$$ SELECT COALESCE(MAX("tekniskVersjon"), 1) FROM redigertVedlegg $$;

ALTER TABLE brevredigering
    ALTER COLUMN "tekniskVersjon" SET DEFAULT brevredigering_teknisk_versjon();

ALTER TABLE redigertVedlegg
    ALTER COLUMN "tekniskVersjon" SET DEFAULT redigertvedlegg_teknisk_versjon();
