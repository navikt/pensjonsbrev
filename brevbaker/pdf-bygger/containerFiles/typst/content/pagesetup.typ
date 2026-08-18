// Felles side- og tekstoppsett. Deles av brev-roten (template.typ) og dokument-roten (document.typ),
// som kun skiller seg i hvilken footer de sender inn og hva de legger på første side.

#let pageSetup(title: "", footer: none, doc) = {
  set text(
    font: "Source Sans 3",
    fallback: true, // Falls back to Noto fonts for unsupported glyphs
    size: 11pt,
  )

  set par(
    leading: 8.7pt,  // Line spacing within paragraphs
    spacing: 24pt, // Space between paragraphs
  )

  set page(margin: (
      x: 64pt,
      y: 64pt,
      bottom: 74pt,
    ),
    footer: footer,
    footer-descent: 30% + 4pt,
  )
  set document(
    title: title,
  )

  doc
}
