from locust import HttpLocust, TaskSet, task
from pyquery import PyQuery

class WordPress(TaskSet):

    @task
    def frontPageBounce(self):        
        self.client.get("/")


class WordpressUser(HttpLocust):
    task_set = WordPress
    host = "http://wp.loadlab.ragozin.info"
