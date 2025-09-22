ALTER TABLE brevredigering
    ALTER COLUMN "redigertBrev" DROP NOT NULL;

ALTER TABLE brevredigering
    ALTER COLUMN "redigertBrevHash" DROP NOT NULL;

ALTER TABLE "Document"
    ALTER COLUMN "brevpdf" DROP NOT NULL;