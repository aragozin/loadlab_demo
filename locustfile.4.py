import random, sys, csv
from locust import HttpLocust, TaskSet, task
from pyquery import PyQuery
from itertools import cycle

LOGIN_CHANCE = 0.1
USER_CREDENTIALS = None

def maybeLogin(l):
    if (random.random() < LOGIN_CHANCE):
        l.client.get("/wp-login.php")
        user = USER_CREDENTIALS.__next__()
        print("Login as " + user[0])
        l.client.post("/wp-login.php", {"log": user[0], "pwd": user[4]})

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

    def __init__(self):
        super(WordpressUser, self).__init__()
        global USER_CREDENTIALS
        if (USER_CREDENTIALS == None):
            with open('data/users_100.csv', 'r') as f:
                reader = csv.reader(f)
                USER_CREDENTIALS = cycle(list(reader))