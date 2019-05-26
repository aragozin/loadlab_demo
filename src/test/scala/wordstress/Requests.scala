package wordstress

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import scala.util.Random

object Requests {

    def loginAs(user: String, password: String): ChainBuilder = {
      return loginAs(Iterator.continually(Map("login" -> user, "password" -> password)))
    }
  
    def loginAs(users: Iterator[Map[String, Any]]): ChainBuilder = {
      return exec(
		      http("Login")
			      .get("/wp-login.php")
			      .check(status.is(200))	      
        )
        .feed(users).exec(
          http("Post credentials")
            .post("/wp-login.php")
            .formParam("log", "${login}")
            .formParam("pwd", "${password}")
            .check(status.is(200))
        )
    }
    
    def addUser(userInfo: Iterator[Map[String, Any]]): ChainBuilder = {
      return exec(
            http("Open users")
              .get("/wp-admin/users.php")
              .check(status.is(200))
          )
          .exec(
            http("Add user form")
              .get("/wp-admin/user-new.php")
              .check(status.is(200))
              .check(css("#_wpnonce_create-user", "value").saveAs("nonce"))
          )
          .feed(userInfo).exec(
            http("Add user submit")
              .post("/wp-admin/user-new.php")
              .formParam("action", "createuser")
              .formParam("_wpnonce_create-user", "${nonce}")
              .formParam("user_login","${login}")
              .formParam("email","${email}")
              .formParam("first_name","${first_name}")
              .formParam("last_name","${last_name}")
              .formParam("role","author")
              .formParam("url","")
              .formParam("pass1","${password}")
              .formParam("pass1-text","${password}")
              .formParam("pass2","${password}")
              .check(status.is(200))
          )
    }
    
    def addPost(postData: Iterator[Map[String, Any]]): ChainBuilder = {
      return exec(
            http("Open posts list")
              .get("/wp-admin/edit.php")
              .check(status.is(200))
          )
          .exitHereIfFailed
          .exec(
            http("New post form")
              .get("/wp-admin/post-new.php")
              .check(css("#_wpnonce", "value").saveAs("nonce"))
              .check(css("#post_ID", "value").saveAs("post_ID"))
              .check(css("#post_author", "value").saveAs("post_author"))
              .check(css("input[name='user_ID'", "value").saveAs("user_ID"))
              .check(status.is(200))
          )
          .exitHereIfFailed
          .feed(postData).exec(
            http("New post submit")
              .post("/wp-admin/post.php")
              .formParam("action", "editpost")
              .formParam("originalaction", "editpost")
              .formParam("_wpnonce", "${nonce}")
              .formParam("_wp_http_referer", "/wp-admin/post-new.php")
              .formParam("comment_status", "open")
              .formParam("content", "${Body}")
              .formParam("post_ID", "${post_ID}")
              .formParam("post_title", "${Title}")
              .formParam("post_type", "post")
              .formParam("post_author", "${post_author}")
              .formParam("user_ID", "${user_ID}	")
              .formParam("publish", "Publish")
              .formParam("tax_input[post_tag]", "${Tags}")
              .formParam("visibility", "public")
              .formParam("post_format", "0")
              .formParam("post_category[]", "0")
              .formParam("advanced_view", "1")
              .formParam("comment_status", "open")
              .formParam("ping_status", "open")
              .formParam("aa", session => session.attributes("Date").asInstanceOf[String].substring(0, 4))
              .formParam("mm", session => session.attributes("Date").asInstanceOf[String].substring(5, 7))
              .formParam("jj", session => session.attributes("Date").asInstanceOf[String].substring(8, 10))
              .formParam("hh", session => session.attributes("Date").asInstanceOf[String].substring(11, 13))
              .formParam("mn", session => session.attributes("Date").asInstanceOf[String].substring(14, 16))
              .formParam("ss", session => session.attributes("Date").asInstanceOf[String].substring(17, 19))
              .formParam("edit_date", "1")
              .formParam("hidden_aa", "0")
              .formParam("hidden_mm", "0")
              .formParam("hidden_jj", "0")
              .formParam("hidden_hh", "0")
              .formParam("hidden_mn", "0")
              .formParam("hidden_ss", "0")
              .check(status.is(200))              
          ).exitHereIfFailed
    }
    
    def mayBeLogin(chance :Double): ChainBuilder = {
        return doIf(_ => Random.nextDouble() < chance) {
            exec(Requests.loginAs(Feeders.users.random()))
            .exitHereIfFailed
         }
    }
    
    def selectRandomPostFromArchive(): ChainBuilder = {
      return exec(
            http("Front page")
              .get("/")
              .check(css("section.widget_archive a", "href").findRandom.saveAs("ARCHIVE_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
          .exec(
            http("Archive listing")
              .get("${ARCHIVE_URI}")
              .check(css("article.post h2>a", "href").findRandom.saveAs("POST_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
    }

    def selectRandomPostFromFrontPage(): ChainBuilder = {
      return exec(
            http("Front page")
              .get("/")
              .check(css(".entry-title a", "href").findRandom.saveAs("POST_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
    }

    def selectRandomPostFromTagClould(): ChainBuilder = {
      return exec(
            http("Front page")
              .get("/")
              .check(css("a.tag-cloud-link", "href").findRandom.saveAs("TAG_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
          .pause(1, 3)
          .exec(
            http("Tag listing")
              .get("${TAG_URI}")
              .check(css("h2.entry-title>a", "href").findRandom.saveAs("POST_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
    }

    def selectRandomFromSearchResults(): ChainBuilder = {
      return exec(
            http("Search")
              .get("/")
              .queryParam("s", "${query}")
              .check(css("h2.entry-title>a", "href").findRandom.saveAs("POST_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
    }
    
    def selectRandomPostFromRssFeed(): ChainBuilder = {
      return exec(
            http("RSS feed")
              .get("/feed/")
              .check(xpath("//item/link").findRandom.saveAs("POST_URI"))
              .check(status.is(200))
          )
          .exitHereIfFailed
    }
    
    def navigateToPost(): ChainBuilder = {
      return exec(
            http("Navigate to post")
              .get("${POST_URI}")
              .check(status.is(200))
          )
          .exitHereIfFailed
    }
}