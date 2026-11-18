CREATE TABLE source_catalogue_entry (
    source_id TEXT NOT NULL,
    commit_sha TEXT NOT NULL,
    document JSONB NOT NULL,
    PRIMARY KEY (source_id, commit_sha)
);
