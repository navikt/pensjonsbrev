package no.nav.brev.brevbaker.vedlegg

class PDFVedlegg(private val sider: Map<Side, Map<String, String?>>) {
    @JvmInline
    value class Side(val sidenummer: Int)

    init {
        require(sider.keys.size == sider.keys.map { it.sidenummer }.distinct().size)
        require(sider.keys.minOf { it.sidenummer } == 1)
        require(sider.keys.maxOf { it.sidenummer } == sider.keys.size + 1)
    }

    fun antallSider() = sider.keys.size

    override fun equals(other: Any?): Boolean {
        if (other !is PDFVedlegg) { return false }
        return this.sider == other.sider
    }
    override fun hashCode() = sider.hashCode()
    override fun toString() = "PDFVedlegg(sider=$sider)"
}