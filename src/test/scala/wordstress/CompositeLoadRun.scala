package wordstress

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class CompositeLoadRun extends CommonSimulation {
  
    if (false) {
      setUp(
      		scenario("Composite load")
      		.randomSwitch(
      		     5.0 -> Scripts.frontPageBounce(),
      		    20.0 -> Scripts.frontPageNavigateRecentPost(),
      		    15.0 -> Scripts.tagCloudNavigation(),
      		    20.0 -> Scripts.pickPostFromArchive(),
      		    20.0 -> Scripts.searchNavigation(),
      		    10.0 -> Scripts.rssRecentNavigation(),
      		    10.0 -> Scripts.directPostTraffic(),
      		 )
  //    		.inject(atOnceUsers(1))
          .inject(rampConcurrentUsers(1).to(20).during (10 minutes))
          .protocols(httpProtocol)
      ).maxDuration(10 minutes).disablePauses
    }

    if (true) {
      setUp(
      		scenario("Composite load")
  					.exec(Scripts.compositeWordpressLoad())
						.inject(rampConcurrentUsers(2).to(500).during (10 minutes))

						.throttle(jumpToRps(5), reachRps(15) in (10 minutes))

						.protocols(httpProtocol)
      ).maxDuration(10 minutes).disablePauses
    }
}