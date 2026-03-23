#import "template.typ": template
#import "content/title.typ": title1, title2, title3
#import "content/paragraph.typ": paragraph
#import "content/state.typ": section-end
#import "content/list.typ": bulletlist
#import "content/table.typ": next-page-table
#import "attachment.typ" : attachment
#import "closing.typ" : closing
#show : template.with(
  lettertitle: "En fin tittel"
)

#{
  title1[Test 1]

  next-page-table(
    columns: (1fr, 1fr),
    [Tabell oversikt], [Heading 1], [Heading 2], [Data row 1], [Value 1],
    [Data row 2], [Value 2],
    [Data row 3], [Value 3],
    [Data row 4], [Value 4],[Tabell oversikt], [Heading 1], [Heading 2], [Data row 1], [Value 1],
    [Data row 2], [Value 2],
    [Data row 3], [Value 3],
    [Data row 4], [Value 4],[Tabell oversikt], [Heading 1], [Heading 2], [Data row 1], [Value 1],
    [Data row 2], [Value 2],
    [Data row 3], [Value 3],
    [Data row 4], [Value 4],[Tabell oversikt], [Heading 1], [Heading 2], [Data row 1], [Value 1],
    [Data row 2], [Value 2],
    [Data row 3], [Value 3],
    [Data row 4], [Value 4],
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
  attachment("en fin tittel 2", sectionNumber: 2, showCaseDetails: true)[
    #paragraph[This is the attachment content.]
    #paragraph[#lorem(50)]
  ]
  
  attachment("en fin tittel 3", sectionNumber: 3, showCaseDetails: false)[
    #paragraph[This is the attachment content.]
    #paragraph[#lorem(1000)]
  ]
  attachment("en fin tittel 4", sectionNumber: 4, showCaseDetails: false)[
    #paragraph[This is the attachment content.]
    #paragraph[#lorem(1000)]
  ]
  [#metadata("end") <endOfLetter>]
}