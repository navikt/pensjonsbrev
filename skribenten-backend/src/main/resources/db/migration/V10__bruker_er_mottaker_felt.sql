ALTER TABLE mottaker ADD COLUMN IF NOT EXISTS "manueltAdressertTil" VARCHAR(50) NULL;
UPDATE mottaker SET "manueltAdressertTil" = 'IKKE_RELEVANT' WHERE "manueltAdressertTil" IS NULL AND "type" = 'SAMHANDLER';
UPDATE mottaker SET "manueltAdressertTil" = 'BRUKER' WHERE "manueltAdressertTil" IS NULL AND "type" != 'SAMHANDLER';
ALTER TABLE mottaker ALTER COLUMN "manueltAdressertTil" SET NOT NULL;