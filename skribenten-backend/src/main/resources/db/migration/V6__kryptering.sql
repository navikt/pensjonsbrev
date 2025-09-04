ALTER TABLE brevredigering
ADD COLUMN IF NOT EXISTS "redigertBrevKryptert" bytea NULL;

ALTER TABLE brevredigering
ADD COLUMN IF NOT EXISTS "redigertBrevKryptertHash" bytea NULL;

ALTER TABLE "Document"
ADD COLUMN IF NOT EXISTS "pdfKryptert" bytea NULL;