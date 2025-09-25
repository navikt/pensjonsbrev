ALTER TABLE brevredigering
    ALTER COLUMN "redigertBrevKryptert" SET NOT NULL;

ALTER TABLE brevredigering
    ALTER COLUMN "redigertBrevKryptertHash" SET NOT NULL;

ALTER TABLE "Document"
    ALTER COLUMN "pdfKryptert" SET NOT NULL;