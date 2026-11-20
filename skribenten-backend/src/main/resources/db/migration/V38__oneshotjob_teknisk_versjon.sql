ALTER TABLE oneshotjob
    ADD COLUMN "tekniskVersjon" INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 2) UNIQUE,
    ALTER COLUMN "completedAt" DROP NOT NULL;

SELECT setval(
    pg_get_serial_sequence('oneshotjob', 'tekniskVersjon'),
    GREATEST(
        COALESCE((SELECT MAX("tekniskVersjon") FROM oneshotjob), 1),
        COALESCE((SELECT MAX("tekniskVersjon") FROM brevredigering), 1),
        COALESCE((SELECT MAX("tekniskVersjon") FROM redigertVedlegg), 1)
    ) + 1,
    false
);
