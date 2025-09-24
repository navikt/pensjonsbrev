ALTER TABLE brevredigering
    DROP COLUMN "redigertBrev";

ALTER TABLE brevredigering
    DROP COLUMN "redigertBrevHash";

ALTER TABLE "Document"
    DROP COLUMN "brevpdf";