/**
 * Lager en minimal, gyldig pdf med én side per tekst, base64-kodet slik pdf-endepunktene returnerer den.
 * Tekstene må være ASCII, siden xref-offsetene regnes i bytes.
 */
export function lagPdfBase64(sider: string[]): string {
  const fontId = 3;
  const sideId = (index: number) => 4 + index * 2;
  const innholdId = (index: number) => 5 + index * 2;

  const objekter: string[] = [
    "<< /Type /Catalog /Pages 2 0 R >>",
    `<< /Type /Pages /Kids [${sider.map((_, index) => `${sideId(index)} 0 R`).join(" ")}] /Count ${sider.length} /MediaBox [0 0 300 144] >>`,
    "<< /Type /Font /Subtype /Type1 /BaseFont /Times-Roman >>",
  ];
  for (const [index, tekst] of sider.entries()) {
    const innhold = `BT /F1 18 Tf 0 0 Td (${tekst}) Tj ET`;
    objekter.push(
      `<< /Type /Page /Parent 2 0 R /Resources << /Font << /F1 ${fontId} 0 R >> >> /Contents ${innholdId(index)} 0 R >>`,
      `<< /Length ${innhold.length} >>\nstream\n${innhold}\nendstream`,
    );
  }

  let pdf = "%PDF-1.1\n";
  const offsets = objekter.map((objekt, index) => {
    const offset = pdf.length;
    pdf += `${index + 1} 0 obj\n${objekt}\nendobj\n`;
    return offset;
  });
  const xrefOffset = pdf.length;
  pdf += `xref\n0 ${objekter.length + 1}\n0000000000 65535 f \n`;
  pdf += offsets.map((offset) => `${String(offset).padStart(10, "0")} 00000 n \n`).join("");
  pdf += `trailer\n<< /Size ${objekter.length + 1} /Root 1 0 R >>\nstartxref\n${xrefOffset}\n%%EOF\n`;

  return Buffer.from(pdf, "ascii").toString("base64");
}
