// Collision matrix for spacing between element types
// Format: "from-to": (space, sticky)
// - space: vertical space to add above the element
// - sticky: if true, prevents page break between elements
#let spacingMatrix = (
  // From "start" (beginning of document/section)
  "start-title": (28pt, false),
  "start-paragraph": (33pt, false),
  "start-list": (17.5pt, false),
  "start-table": (25pt, false),
  "start-form": (23pt, false),

  // From "title"
  "title-title": (5pt, true),
  "title-paragraph": (12pt, true),
  "title-list": (10.5pt, true),
  "title-table": (11pt, true),
  "title-form": (12pt, true),

  // From "paragraph"
  "paragraph-title": (29pt, false),
  "paragraph-paragraph": (25pt, false),
  "paragraph-list": (9pt, false),
  "paragraph-table": (18pt, false),
  "paragraph-form": (16pt, false),

  // From "list"
  "list-title": (23pt, false),
  "list-paragraph": (26.5pt, false),
  "list-list": (26pt, false),
  "list-table": (20pt, false),
  "list-form": (25pt, false),

  // From "table"
  "table-title": (26pt, false),
  "table-paragraph": (27pt, false),
  "table-list": (18pt, false),
  "table-table": (25pt, false),
  "table-form": (16pt, false),

  // From "form"
  "form-title": (26pt, false),
  "form-paragraph": (25pt, false),
  "form-list": (26pt, false),
  "form-table": (16pt, false),
  "form-form": (28pt, false),
)

// Get spacing info for transitioning from previous element to current element
// Returns: (space, sticky)
#let getSpacing(fromType, toType) = {
  let key = fromType + "-" + toType
  spacingMatrix.at(key)
}

