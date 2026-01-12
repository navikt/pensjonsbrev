ALTER TABLE brevredigering ADD COLUMN brevtype VARCHAR(50) NULL;

UPDATE brevredigering SET brevtype = 'VEDTAKSBREV' where "vedtaksId" is not null;
UPDATE brevredigering SET brevtype = 'INFORMASJONSBREV' where "vedtaksId" is null;