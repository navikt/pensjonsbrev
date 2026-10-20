ALTER TABLE mottaker
    ALTER COLUMN "mottaker" SET NOT NULL;
ALTER TABLE mottaker
    ALTER COLUMN "type" DROP NOT NULL;
ALTER TABLE mottaker
    ALTER COLUMN "manueltAdressertTil" DROP NOT NULL;