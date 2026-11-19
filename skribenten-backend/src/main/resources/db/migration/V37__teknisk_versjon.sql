ALTER TABLE brevredigering
    ADD COLUMN "tekniskVersjon" INTEGER NOT NULL DEFAULT 1;

ALTER TABLE redigertVedlegg
    ADD COLUMN "tekniskVersjon" INTEGER NOT NULL DEFAULT 1;
