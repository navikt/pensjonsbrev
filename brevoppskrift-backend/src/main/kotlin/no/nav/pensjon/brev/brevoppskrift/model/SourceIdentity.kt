package no.nav.pensjon.brev.brevoppskrift.model

@JvmInline
value class SourceId(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class GitCommit(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class Sha256Digest(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class RepositoryPath(val value: String) {
    override fun toString(): String = value
}
