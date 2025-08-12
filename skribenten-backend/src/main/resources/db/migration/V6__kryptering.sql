ALTER TABLE brevredigering
ADD COLUMN IF NOT EXISTS "redigertBrevKryptert" bytea NULL;

ALTER TABLE document
ADD COLUMN IF NOT EXISTS "pdfKryptert" bytea NULL;