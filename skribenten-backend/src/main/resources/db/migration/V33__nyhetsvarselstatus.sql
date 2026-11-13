ALTER TABLE bekreftetNyhetsvarsel RENAME TO nyhetsvarselstatus;
ALTER TABLE nyhetsvarselstatus RENAME CONSTRAINT bekreftetnyhetsvarsel_pkey TO nyhetsvarselstatus_pkey;
ALTER TABLE nyhetsvarselstatus RENAME COLUMN bekreftet TO "gjennomfoertOmvisningen";
ALTER TABLE nyhetsvarselstatus ALTER COLUMN "gjennomfoertOmvisningen" DROP NOT NULL;
ALTER TABLE nyhetsvarselstatus ADD COLUMN "settAnnonsen" TIMESTAMP;
