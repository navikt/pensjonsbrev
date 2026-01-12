UPDATE brevredigering SET brevtype = 'VEDTAKSBREV' where "vedtaksId" is not null and brevtype is null;
UPDATE brevredigering SET brevtype = 'INFORMASJONSBREV' where "vedtaksId" is null and brevtype is null;

ALTER TABLE brevredigering ALTER COLUMN brevtype SET NOT NULL;