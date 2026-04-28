from locust import FastHttpUser, task, between, events
import os
import uuid

from azuread import ThreadSafeToken

@events.test_start.add_listener
def _(environment, **kwargs):
    global payload, token_provider
    payload = open(os.path.join(os.path.dirname(__file__), 'autobrev_request.json'), "r").read()
    token_provider = ThreadSafeToken()

class BrevbakerLoadTest(FastHttpUser):
    wait_time = between(0, 0)

    @task
    def load_test(self):
        headers = {
            'Content-Type': 'application/json',
            'X-Request-ID': str(uuid.uuid4()),
            'Authorization': f'Bearer {token_provider.get()}',
            'Connection': 'close'
        }
        self.client.post('/letter/autobrev/pdf', data=payload, headers=headers)
