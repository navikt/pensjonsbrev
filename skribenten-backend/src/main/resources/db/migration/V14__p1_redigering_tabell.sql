CREATE TABLE IF NOT EXISTS p1data
(
    "brevredigeringId" BIGINT PRIMARY KEY CONSTRAINT p1data_brevredigeringid_unique UNIQUE,
    p1data            bytea  NOT NULL,

    CONSTRAINT fk_p1_data_brevredigeringid__id FOREIGN KEY ("brevredigeringId") REFERENCES brevredigering (id) ON DELETE CASCADE ON UPDATE RESTRICT
)