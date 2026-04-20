/**
 * Returns the SHA-256 digest of the input string as a lowercase 64-character hex string.
 * Uses the Web Crypto API (`crypto.subtle`) and is safe for client-side use.
 */
export async function sha256Hash(input: string): Promise<string> {
  const encoded = new TextEncoder().encode(input);
  const hashBuffer = await crypto.subtle.digest("SHA-256", encoded);
  return Array.from(new Uint8Array(hashBuffer))
    .map((b) => b.toString(16).padStart(2, "0"))
    .join("");
}

/**
 * Returns the first `length` characters of the SHA-256 hex digest of the input string.
 *
 * ⚠️ NOT cryptographically safe and NOT guaranteed to be unique.
 * Truncation reduces collision resistance compared to the full 256-bit hash.
 * Suitable for analytics / non-security purposes only.
 *
 * Default length is 16 (representing 64 bits), which gives negligible collision
 * probability across millions of distinct inputs.
 *
 * @param input - The string to hash.
 * @param length - Number of hex characters to return (1–64). Defaults to 16.
 */
export async function truncatedSha256Hash(input: string, length = 16): Promise<string> {
  const hash = await sha256Hash(input);
  return hash.slice(0, length);
}
