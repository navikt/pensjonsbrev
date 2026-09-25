ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "navnKryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "postnummerKryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "poststedKryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "adresselinje1Kryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "adresselinje2Kryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "adresselinje3Kryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "landkodeKryptert" bytea NULL;

ALTER TABLE mottaker
    ADD COLUMN IF NOT EXISTS "manueltAdressertTilKryptert" bytea NULL;