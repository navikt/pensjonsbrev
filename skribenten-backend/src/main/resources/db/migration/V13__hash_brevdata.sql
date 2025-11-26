-- Step 1: Add column
ALTER TABLE "Document" ADD COLUMN IF NOT EXISTS "brevdataHash" bytea NULL;

-- Step 2: Initialize with default value (e.g. empty bytea)
UPDATE "Document" SET "brevdataHash" = decode('', 'hex') WHERE "brevdataHash" IS NULL;

-- Step 3: Make column non-nullable
ALTER TABLE "Document" ALTER COLUMN "brevdataHash" SET NOT NULL;