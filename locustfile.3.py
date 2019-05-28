import random
from locust import HttpLocust, TaskSet, task
from pyquery import PyQuery

LOGIN_CHANCE = 0.1

def maybeLogin(l):
    if (random.random() < LOGIN_CHANCE):
        l.client.get("/wp-login.php")
        l.client.post("/wp-login.php", {"log": "boss", "pwd": "boss"})

class WordPress(TaskSet):

    @task(2)
    def frontPageBounce(self):        
        self.client.get("/")

    @task(3)
    def frontPageNavigateRecentPost(self):
        r = self.client.get("/")
        maybeLogin(self)
        pq = PyQuery(r.content)
        l = random.choice(pq(".entry-title a")).attrib["href"]
        r = self.client.get(l, name="/(post)")

    @task(1)
    def pickPostFromArchive(self):
        r = self.client.get("/")
        maybeLogin(self)
        pq = PyQuery(r.content)
        l = random.choice(pq("section.widget_archive a")).attrib["href"]
        r = self.client.get(l, name="/(archive)")
        pq = PyQuery(r.content)
        l = random.choice(pq(".entry-title a")).attrib["href"]
        r = self.client.get(l, name="/(post)")

    @task(1)
    def pickPostFromTagCloud(self):
        r = self.client.get("/")
        maybeLogin(self)
        pq = PyQuery(r.content)
        l = random.choice(pq("a.tag-cloud-link")).attrib["href"]
        r = self.client.get(l, name="/(tag)")
        pq = PyQuery(r.content)
        l = random.choice(pq("h2.entry-title>a")).attrib["href"]
        r = self.client.get(l, name="/(post)")


class WordpressUser(HttpLocust):
    task_set = WordPress
    host = "http://wp.loadlab.ragozin.info"