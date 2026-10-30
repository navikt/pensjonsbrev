CREATE TABLE IF NOT EXISTS bekreftetNyhetsvarsel
(
    "navIdent"       VARCHAR(50)  NOT NULL,
    "nyhetsvarselId" VARCHAR(100) NOT NULL,
    bekreftet        TIMESTAMP    NOT NULL,

    CONSTRAINT bekreftetnyhetsvarsel_pkey PRIMARY KEY ("navIdent", "nyhetsvarselId")
);
