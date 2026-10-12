ALTER TABLE brevredigering
    ALTER COLUMN "saksbehandlerValgKryptert" SET NOT NULL;
ALTER TABLE brevredigering
    ALTER COLUMN "saksbehandlerValg" DROP NOT NULL;