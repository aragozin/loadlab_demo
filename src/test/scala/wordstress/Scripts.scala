package wordstress

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import scala.util.Random

object Scripts {

    val LOGIN_CHANCE = 0.05
  
    def frontPageBounce(): ChainBuilder = {
      return exec(
            http("Front page")
              .get("/")
              .check(status.is(200))
          )
          .exitHereIfFailed
    }
    
    def frontPageNavigateRecentPost(): ChainBuilder = {
      return exec(Requests.mayBeLogin(LOGIN_CHANCE))
            .exec(Requests.selectRandomPostFromFrontPage())
            .pause(1, 3)
    		    .exec(Requests.navigateToPost())      
    }

    def pickPostFromArchive():  ChainBuilder = {
      return exec(Requests.mayBeLogin(LOGIN_CHANCE))
            .exec(Requests.selectRandomPostFromArchive())
            .pause(1, 3)
    		    .exec(Requests.navigateToPost())
    }
    
    def directPostTraffic(): ChainBuilder = {
      return feed(Feeders.hotLinks)
             .exec(session => {session.set("POST_URI", session.attributes("link").asInstanceOf[String])})
             .exec(Requests.navigateToPost())
    }

    def searchNavigation(): ChainBuilder = {
      return exec(Requests.mayBeLogin(LOGIN_CHANCE))
            .feed(Feeders.searches.random())
            .exec(Requests.selectRandomFromSearchResults())
            .pause(1, 3)
    		    .exec(Requests.navigateToPost())      
    }
    
    def tagCloudNavigation(): ChainBuilder = {
      return exec(Requests.mayBeLogin(LOGIN_CHANCE))
            .exec(Requests.selectRandomPostFromTagClould())
            .pause(1, 3)
    		    .exec(Requests.navigateToPost())      
    }
    
    def rssBounce(): ChainBuilder = {
      return exec(
            http("RSS feed")
              .get("/feed")
              .check(status.is(200))
          )
    }
    
    def rssRecentNavigation(): ChainBuilder = {
      return exec(Requests.selectRandomPostFromRssFeed())
            .pause(1, 3)
            .exec(Requests.navigateToPost())
    }

    def compositeWordpressLoad(): ChainBuilder = {
      return randomSwitch(
        5.0 -> Scripts.frontPageBounce(),
        20.0 -> Scripts.frontPageNavigateRecentPost(),
        15.0 -> Scripts.tagCloudNavigation(),
        20.0 -> Scripts.pickPostFromArchive(),
        20.0 -> Scripts.searchNavigation(),
        10.0 -> Scripts.rssRecentNavigation(),
        10.0 -> Scripts.directPostTraffic(),
      )
    }
}