from locust import FastHttpUser, task, between
import azuread
import os
import uuid

payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request.json'), "r").read()

class BrevbakerLoadTest(FastHttpUser):
    token = azuread.fetch_token()
    wait_time = between(0,0)
    #network_timeout = 300.0
    @task
    def load_test(self):
        #headers = {'Content-Type': 'application/json'} # local testing
        headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + self.access_token(), 'X-Request-ID': str(uuid.uuid4())}# dev environment testing
        self.client.post("/letter/autobrev/pdf", payload, headers=headers)

    def access_token(self):
        if self.token.is_valid():
            return self.token.token
        else:
            self.token = azuread.fetch_token()
            return self.token.token