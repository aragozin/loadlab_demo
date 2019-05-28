import random
from locust import HttpLocust, TaskSet, task
from pyquery import PyQuery

class WordPress(TaskSet):

    @task
    def frontPageBounce(self):        
        self.client.get("/")

    @task
    def frontPageNavigateRecentPost(self):
        r = self.client.get("/")
        pq = PyQuery(r.content)
        l = random.choice(pq(".entry-title a")).attrib["href"]
        r = self.client.get(l)

    @task
    def pickPostFromArchive(self):
        r = self.client.get("/")
        pq = PyQuery(r.content)
        l = random.choice(pq("section.widget_archive a")).attrib["href"]
        r = self.client.get(l)
        pq = PyQuery(r.content)
        l = random.choice(pq(".entry-title a")).attrib["href"]
        r = self.client.get(l)

    @task
    def pickPostFromTagCloud(self):
        r = self.client.get("/")
        pq = PyQuery(r.content)
        l = random.choice(pq("a.tag-cloud-link")).attrib["href"]
        r = self.client.get(l)
        pq = PyQuery(r.content)
        l = random.choice(pq("h2.entry-title>a")).attrib["href"]
        r = self.client.get(l)


class WordpressUser(HttpLocust):
    task_set = WordPress
    host = "http://wp.loadlab.ragozin.info"