import json
import os
import threading
import urllib.request
from dataclasses import dataclass
from datetime import datetime, timedelta

azure_config_path = os.path.join(os.path.dirname(__file__), 'secrets/azuread.json')
azure_config = json.loads(open(azure_config_path, "r").read())

token_request_data = {
    'tenant': azure_config['AZURE_APP_TENANT_ID'],
    'grant_type': 'client_credentials',
    'client_id': azure_config['AZURE_APP_CLIENT_ID'],
    'client_secret': azure_config['AZURE_APP_CLIENT_SECRET'],
    'scope': 'api://dev-gcp.pensjonsbrev.pensjon-brevbaker/.default'
}

@dataclass
class AccessToken:
    token: str
    expires_in: int
    issued_at: datetime = datetime.now()

    def is_valid(self):
        return (datetime.now() + timedelta(seconds = 60)) < (self.issued_at + timedelta(seconds = self.expires_in))

def fetch_token():
    request = urllib.request.Request(azure_config['AZURE_OPENID_CONFIG_TOKEN_ENDPOINT'], data=urllib.parse.urlencode(token_request_data).encode('utf-8'))
    request.add_header('Content-Type', 'application/x-www-form-urlencoded')

    response = urllib.request.urlopen(request)
    token = json.loads(response.read().decode('utf-8'))
    response.close()

    return AccessToken(token['access_token'], int(token['expires_in']))

class ThreadSafeToken:
    def __init__(self):
        self._lock = threading.Lock()
        self._token = fetch_token()

    def get(self):
        if self._token.is_valid():
            return self._token.token
        else:
            with self._lock:
                if not self._token.is_valid():
                    self._token = fetch_token()
            return self._token.token