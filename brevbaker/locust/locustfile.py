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
            'X-Request-ID': str(uuid.uuid4()),
            # Tving ny TCP-forbindelse per request slik at ingress (ingress-nginx)
            # round-robin'er hver request over alle pdf-bygger-pods. Uten dette
            # holder FastHttpUser én persistent HTTP/1.1-forbindelse per virtuell
            # bruker, og siden ingress kun load-balancer ved connect-tidspunkt
            # blir trafikken effektivt "sticky" til de podene som var oppe da
            # forbindelsen ble etablert. HPA-skalerte pods får da null trafikk,
            # mens de opprinnelige står og koker på 100% CPU.
            'Connection': 'close'
        }
        self.client.post('/produserBrev', data=payload, headers=headers)
