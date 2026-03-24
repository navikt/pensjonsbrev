
#import "../input.typ": languageSettings

#let columnheadercolor = rgb("#E4EEFF")
#let linecolor = rgb("#6F7785")
#let headersepcolor = rgb("#000000")

#let row1color = rgb("#FFFFFF")
#let row2color = rgb("#F5F6F7")

#let next-page-table(column-align: (), ..table-args) = context {
  let columns = table-args.named().at("columns", default: ())
  let column-amount = columns.len()


  // Extract header cells from the positional arguments
  // The first `column-amount` positional args are header cells
  let pos-args = table-args.pos()
  let header-cells = pos-args.slice(0, calc.min(column-amount, pos-args.len()))
  let body-cells = pos-args.slice(calc.min(column-amount, pos-args.len()))

  // Counter of tables so we can create a unique table-part-counter for each table
  let table-counter = counter("table")
  table-counter.step()

  // Create unique label for this table's end marker
  let table-id = str(table-counter.get().first())
  let table-start-label = label("table-start-" + table-id)
  let table-end-label = label("table-end-" + table-id)

  // Show rules that check against stable markers rather than counting instances
  show <table-footer>: footer => {
    context {
      // Check if we're on the same page as the table end marker
      let end-markers = query(table-end-label)
      let current-page = here().page()
      let is-last-page = end-markers.len() > 0 and current-page == end-markers.first().location().page()
      // Use hide() to maintain stable layout - content takes space but is invisible
      v(-measure(footer).height)
      if is-last-page {
        hide(footer)
      } else {
        footer
      }
    }
  }

  show <table-header>: header => {
    context {
      // Check if we're on the same page as the table start marker
      let start-markers = query(table-start-label)
      let current-page = here().page()
      let is-first-page = start-markers.len() > 0 and current-page == start-markers.first().location().page()

      // Subtract the height of the header to keep the space above a table stable. This also means the heading will go into the margin a little bit when visible, but that is an ok compromize for a stable layout.
      v(-measure(header).height)
      if is-first-page {
        // Use hide() to maintain stable layout - content takes space but is invisible
        hide(header)
      } else {
        header
      }
    }
  }

  // Build styled header cells
  let styled-header-cells = header-cells.map(cell =>
    table.cell(
      inset: (x: 8pt, y: 11pt),
      fill: columnheadercolor,
      stroke: (bottom: 1pt + headersepcolor, top: none, left: none, right: none)
    )[#text(weight: "semibold", tracking: 0.2pt, cell)]
  )

  table(
    columns: columns,
    align: column-align,
    column-gutter: 0pt,
    row-gutter: 0pt,
    inset: (x: 8pt, y: 6.4pt),
    stroke: (x, y) => (
      left: none,
      right: none,
      top: if y <= 2 { none } else { 1pt + linecolor },
      bottom: 1pt + linecolor,
    ),
    fill: (x, y) => {
      if y <= 1 {
        // Header rows (continuation message + actual headers)
        none  // Fill is set per-cell for headers
      } else if calc.even(y) {
        row1color
      } else {
        row2color
      }
    },
    table.header(
      table.cell(
        inset: 0pt,
        colspan: column-amount,
        stroke: none,
        fill: none,
      )[#metadata(none) #table-start-label],
      // Start marker for the table (invisible, zero-size)

      table.cell(
        inset: 0pt,
        colspan: column-amount,
        stroke: none,
        fill: white,
      )[#languageSettings.tablecontinuedfrompreviouspage <table-header>#v(10pt)],
      // ^ Continuation message - shown only on subsequent pages via the show rule

      ..styled-header-cells,
    ),
    ..body-cells,
    table.cell(
      inset: 0pt,
      colspan: column-amount,
      stroke: none,
      fill: none,

    // End marker for the table body - placed as last body row, not in footer
    // This ensures the marker position is stable regardless of footer placement

    )[#metadata(none) #table-end-label],

    table.footer(
      // The 'next page' content spans all columns and has no stroke
      // Must be selectable by the show rule above which hides it at the last page
      table.cell(
        inset: 0pt,
        colspan: column-amount,
        stroke: none,
        fill: white,
        align(right)[#v(2em)#languageSettings.tablenextpagecontinuation <table-footer>],
      ),
    ),
  )
}