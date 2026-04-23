from locust import FastHttpUser, task, between, events
import os
import uuid

@events.test_start.add_listener
def _(environment, **kwargs):
    global payload
    payload = open(os.path.join(os.path.dirname(__file__), 'pdf_request_typst.json'), "r").read()

class PdfByggerLoadTest(FastHttpUser):
    wait_time = between(0, 0)

    @task
    def load_test(self):
        headers = {
            'Content-Type': 'application/json',
            'X-Request-ID': str(uuid.uuid4())
        }
        self.client.post('/produserBrev', data=payload, headers=headers)
