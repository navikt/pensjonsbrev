// Collision matrix for spacing between element types
// Format: "from-to": (space, sticky)
// - space: vertical space to add above the element
// - sticky: if true, prevents page break between elements
#let spacingMatrix = (
  // From "start" (beginning of document/section)
  "start-title": (1pt, false),
  "start-paragraph": (2pt, false),
  "start-list": (3pt, false),
  "start-table": (28pt, false),
  "start-form": (5pt, false),

  // From "title"
  "title-title": (3pt, true),
  "title-paragraph": (3pt, true),
  "title-list": (3pt, true),
  "title-table": (3pt, true),
  "title-form": (3pt, true),

  // From "paragraph"
  "paragraph-title": (25pt, false),
  "paragraph-paragraph": (25pt, false),
  "paragraph-list": (9pt, false),
  "paragraph-table": (16pt, false),
  "paragraph-form": (16pt, false),

  // From "list"
  "list-title": (25pt, false),
  "list-paragraph": (16pt, false),
  "list-list": (16pt, false),
  "list-table": (16pt, false),
  "list-form": (16pt, false),

  // From "table"
  "table-title": (25pt, false),
  "table-paragraph": (16pt, false),
  "table-list": (16pt, false),
  "table-table": (16pt, false),
  "table-form": (16pt, false),

  // From "form"
  "form-title": (25pt, false),
  "form-paragraph": (16pt, false),
  "form-list": (16pt, false),
  "form-table": (16pt, false),
  "form-form": (16pt, false),
)

// Get spacing info for transitioning from previous element to current element
// Returns: (space, sticky)
#let getSpacing(fromType, toType) = {
  let key = fromType + "-" + toType
  spacingMatrix.at(key)
}

