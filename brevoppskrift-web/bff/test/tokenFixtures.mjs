import { generateKeyPairSync, randomUUID, sign } from "node:crypto";
import { createServer } from "node:http";

const { privateKey, publicKey } = generateKeyPairSync("rsa", { modulusLength: 2048 });
const key = { ...publicKey.export({ format: "jwk" }), kid: "test-key", alg: "RS256", use: "sig" };

export async function listening(app) {
  const server = createServer(app);
  await new Promise((resolve) => server.listen(0, "127.0.0.1", resolve));
  return { server, url: `http://127.0.0.1:${server.address().port}` };
}

export async function close({ server }) {
  server.closeAllConnections();
  await new Promise((resolve) => server.close(resolve));
}

export function json(response, body) {
  response.setHeader("Content-Type", "application/json");
  response.end(JSON.stringify(body));
}

export async function identityProvider(exchange) {
  const requests = [];
  const identity = await listening(async (request, response) => {
    if (request.url === "/jwks") {
      json(response, { keys: [key] });
      return;
    }
    const chunks = [];
    for await (const chunk of request) chunks.push(chunk);
    const body = JSON.parse(Buffer.concat(chunks).toString());
    requests.push({ path: request.url, body });
    if (exchange) exchange(request, response, body);
    else json(response, { access_token: exchangedToken });
  });
  function token(claims = {}) {
    const header = Buffer.from(JSON.stringify({ alg: "RS256", kid: key.kid })).toString("base64url");
    const payload = Buffer.from(
      JSON.stringify({
        iss: identity.url,
        aud: "test-bff",
        sub: "test-user",
        NAVident: "Z000000",
        exp: Math.floor(Date.now() / 1000) + 3600,
        jti: randomUUID(),
        ...claims,
      }),
    ).toString("base64url");
    const input = `${header}.${payload}`;
    return `${input}.${sign("sha256", Buffer.from(input), privateKey).toString("base64url")}`;
  }
  const exchangedToken = token({ aud: "test-backend" });
  Object.assign(process.env, {
    AZURE_OPENID_CONFIG_ISSUER: identity.url,
    AZURE_OPENID_CONFIG_JWKS_URI: `${identity.url}/jwks`,
    AZURE_APP_CLIENT_ID: "test-bff",
    NAIS_TOKEN_ENDPOINT: `${identity.url}/token`,
    NAIS_TOKEN_INTROSPECTION_ENDPOINT: `${identity.url}/introspect`,
    NAIS_TOKEN_EXCHANGE_ENDPOINT: `${identity.url}/exchange`,
  });
  return { ...identity, token, exchangedToken, requests };
}
