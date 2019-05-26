package wordstress

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object Feeders {
  
  val users = csv("data/users_100.csv").circular; 

  val posts = csv("data/posts_mini.csv").circular; 
  
  val searches = csv("data/search.csv").circular;
  
  val hotLinks = csv("data/hot_list.csv").circular;
  
  def ff(feed: Iterator[Map[String, Any]], skip: Int): Iterator[Map[String, Any]] = {
    for(i <- 1 to skip) {
      feed.next();
    };
    return feed;
  }  
}