CREATE TABLE IF NOT EXISTS settNyhetsvarsel
(
    "navIdent"       VARCHAR(50)  NOT NULL,
    "nyhetsvarselId" VARCHAR(100) NOT NULL,
    sett             TIMESTAMP    NOT NULL,

    CONSTRAINT settnyhetsvarsel_pkey PRIMARY KEY ("navIdent", "nyhetsvarselId")
);
