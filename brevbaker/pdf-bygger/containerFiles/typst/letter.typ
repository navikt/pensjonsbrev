#import "template.typ": template
#import "content/title.typ": title1, title2, title3
#import "content/paragraph.typ": paragraph
#import "content/state.typ": section-end
#import "content/list.typ": bulletlist
#import "content/table.typ": next-page-table
#import "attachment.typ" : startAttachment, endAttachment
#import "closing.typ" : closing
#show : template.with(
  lettertitle: "En fin tittel"
)

#{
  title1[Test 1]

  next-page-table(
    columns: (1fr, 1fr),
    [Heading 1], [Heading 2],
    [Data row 1], [Value 1],
    [Data row 2], [Value 2],
    [Data row 3], [Value 3],
    [Data row 4], [Value 4],
    [Data row 5], [Value 5],
    [Data row 6], [Value 6],
    [Data row 7], [Value 7],
    [Data row 8], [Value 8],
    [Data row 9], [Value 9],
    [Data row 10], [Value 10],
    [Data row 11], [Value 11],
    [Data row 12], [Value 12],
    [Data row 13], [Value 13],
    [Data row 14], [Value 14],
    [Data row 15], [Value 15],
    [Data row 16], [Value 16],
    [Data row 17], [Value 17],
    [Data row 18], [Value 18],
    [Data row 19], [Value 19],
    [Data row 20], [Value 20],
  )

  paragraph[#lorem(50)]
  title2[Test 2]
  bulletlist([test], [test])
  title3[Test 3]
  paragraph[#lorem(100)]
  title1[asdf]
  paragraph[#lorem(30)]
    title1[asdf]
  paragraph[#lorem(50)]
  paragraph[#lorem(540)]

  paragraph[Nulla nec velit nulla. Quisque metus mauris]
  title1[Test 1]
  bulletlist([test],[test],[test],[testffffff],[test],[test],[test])
  
  paragraph[#lorem(200)]
  closing
  startAttachment("en fin tittel 2", sectionNumber: 2, showCaseDetails: true)
  paragraph[This is the attachment content.]
  paragraph[#lorem(50)]
  endAttachment(sectionNumber: 2)

  startAttachment("en fin tittel 3", sectionNumber: 3, showCaseDetails: false)
  paragraph[This is the attachment content.]
  paragraph[#lorem(1000)]
  endAttachment(sectionNumber: 3)

  startAttachment("en fin tittel 4", sectionNumber: 4, showCaseDetails: false)
  paragraph[This is the attachment content.]
  paragraph[#lorem(1000)]
  endAttachment(sectionNumber: 4)

  [#metadata("end") <endOfLetter>]
}