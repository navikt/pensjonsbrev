

#let columnheadercolor = rgb("#E6F0FF")
#let linecolor = rgb("#7C8695")
#let headersepcolor = rgb("#000000")
#let row1color = rgb("#F2F3F5")
#let row2color = rgb("#FFFFFF")

#let next-page-table(head: [], ..table-args) = context {
  let columns = table-args.named().at("columns", default: 1)
  let column-amount = if type(columns) == int {
    columns
  } else if type(columns) == array {
    columns.len()
  } else {
    1
  }

  
    set table(
    column-gutter: 0pt,
    row-gutter: 0pt,
    inset: (x: 8pt, y: 6.4pt),
    stroke: (x, y) => (
      left: none,
      right: none,
      top: if y == 0 { none } else if y == 1 { 1pt + headersepcolor } else { 1pt + linecolor },
      bottom: 1pt + linecolor,
    ),
    fill: (x, y) => {
      if calc.odd(y) {
        row1color
      } else {
        row2color
      }
    },
  )
  set table.header(repeat: true)

  // Counter of tables so we can create a unique table-part-counter for each table
  let table-counter = counter("table")
  table-counter.step()

  // Counter for the amount of pages in the table
  // It is increased by one for each footer repetition
  let table-part-counter = counter("table-part" + str(table-counter.get().first()))
  show <table-footer>: footer => {
    table-part-counter.step()
    context {
      if table-part-counter.get() != table-part-counter.final() {
      // if table-part-counter.get().first() > 1 {
        // Display the footer only if we aren't at the last page
        footer
      }
    }
  }
  show <table-header>: header => {
    table-part-counter.step()
    context {
      if table-part-counter.get().first() != 1 {
        // Display the header only if we aren't at the first page
        header
        v(-0.7em)
        head
      } else {
        head
      }
    }
  }

  table(
    table.header(
        // The 'next page' content spans all columns and has no stroke
        // Must be selectable by the show rule above which hides it at the last page
        table.cell(inset: 0pt, colspan: column-amount, stroke: none, align(left)[Continuation of Previous Table <table-header> ] ),
      ),
    ..table-args,
    table.footer(
      // The 'next page' content spans all columns and has no stroke
      // Must be selectable by the show rule above which hides it at the last page
      table.cell(inset: (x:0pt), colspan: column-amount, stroke: none, align(right)[See next page for continuation <table-footer> ])
    )
  )

  // Compensate for the empty footer at the last page of the table
  v(-1em)
}