CREATE TABLE draft (
    id UUID PRIMARY KEY,
    revision BIGINT NOT NULL DEFAULT 1 CHECK (revision > 0),
    status TEXT NOT NULL DEFAULT 'WORK' CHECK (status IN ('WORK', 'READY')),
    source_key TEXT,
    document JSONB NOT NULL,
    metadata JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by TEXT NOT NULL,
    updated_by TEXT NOT NULL,
    archived_at TIMESTAMPTZ,
    archive_reason TEXT CHECK (archive_reason IN ('DONE', 'RESET', 'DISCARDED'))
);

CREATE UNIQUE INDEX draft_active_source ON draft (source_key)
    WHERE source_key IS NOT NULL AND archived_at IS NULL;

CREATE TABLE pr_job (
    id UUID PRIMARY KEY,
    draft_id UUID NOT NULL REFERENCES draft(id),
    revision BIGINT NOT NULL,
    document JSONB NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'RUNNING', 'SUCCEEDED', 'FAILED')),
    attempts INTEGER NOT NULL DEFAULT 0,
    lease_token UUID,
    lease_until TIMESTAMPTZ,
    pr_url TEXT,
    error_code TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (draft_id, revision)
);
