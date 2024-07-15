// "When accessing a member from an await expression, the await expression has to be parenthesized, which is not readable."
// For the purpose of this file it is convenient to be able to access the data property of axios response as a one-liners.
/* eslint-disable unicorn/no-await-expression-member*/

import axios from "axios";

import type { BrevInfo } from "~/types/brev";

import { SKRIBENTEN_API_BASE_PATH } from "./skribenten-api-endpoints";

export const hentAlleBrevForSak = {
  queryKey: ["hentAlleBrevForSak"],
  queryFn: async (saksId: string) => hentAlleBrevForSakFunction(saksId),
};

const hentAlleBrevForSakFunction = async (saksId: string) =>
  (await axios.get<BrevInfo[]>(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev`)).data;

export const lagPdfForBrev = async (saksId: string, brevId: string) => {
  //return generateRandomPDFContent();
  /*return `%PDF-1.0
                1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj 2 0 obj<</Type/Pages/Kids[3 0 R]/Count 1>>endobj 3 0 obj<</Type/Page/MediaBox[0 0 3 3]>>endobj
                xref
                0 4
                0000000000 65535 f
                0000000010 00000 n
                0000000053 00000 n
                0000000102 00000 n
                trailer<</Size 4/Root 1 0 R>>
                startxref
                149
                %EOF`;*/
  return (await axios.post(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`)).data;
};

export const hentPdfForBrev = {
  queryKey: ["hentAlleBrevForSak"],
  queryFn: async (saksId: string, brevId: string) => hentPdfForBrevFunction(saksId, brevId),
};

export const hentPdfForBrevFunction = async (saksId: string, brevId: string) =>
  (await axios.get(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}/pdf`)).data;

export const slettBrev = async (saksId: string, brevId: string) =>
  (await axios.delete(`${SKRIBENTEN_API_BASE_PATH}/sak/${saksId}/brev/${brevId}`)).data;

function generateRandomText() {
  const loremIpsum = `Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                        Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris 
                        nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in 
                        reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla 
                        pariatur. Excepteur sint occaecat cupidatat non proident, sunt in 
                        culpa qui officia deserunt mollit anim id est laborum.`;

  // Generate random text based on the lorem ipsum template
  const randomText = loremIpsum
    .split(" ")
    .sort(() => Math.random() - 0.5) // Shuffle words randomly
    .slice(0, Math.floor(Math.random() * 50) + 50) // Randomly choose a portion of text
    .join(" ");

  return randomText;
}

function generateRandomPdfContent() {
  const randomText = generateRandomText();

  // Construct the PDF content
  const pdfContent = `
    %PDF-1.4
    %����
    1 0 obj
    << /Type /Catalog
       /Pages 2 0 R
    >>
    endobj
    2 0 obj
    << /Type /Pages
       /Kids [3 0 R]
       /Count 1
    >>
    endobj
    3 0 obj
    <<  /Type /Page
        /Parent 2 0 R
        /Resources << /Font << /F1 4 0 R >> >>
        /Contents 5 0 R
    >>
    endobj
    4 0 obj
    << /Type /Font
       /Subtype /Type1
       /BaseFont /Helvetica
    >>
    endobj
    5 0 obj
    << /Length ${randomText.length} >>
    stream
    ${randomText}
    endstream
    endobj
    trailer
    << /Root 1 0 R
       /Size 6
    >>
    startxref
    12345
    %%EOF
    `;

  return pdfContent;
}
