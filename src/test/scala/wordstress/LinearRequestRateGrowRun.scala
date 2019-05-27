package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._

class LinearRequestRateGrowRun extends CommonSimulation {

	  val USER_CAP = Math.ceil(50 * LOAD_FACTOR).asInstanceOf[Int];
	  val TARGET_RPS = Math.ceil(1.3 * LOAD_FACTOR).asInstanceOf[Int];

		setUp(
				scenario("Quick benchmark")
					.exec(Scripts.compositeWordpressLoad())

					.inject(rampConcurrentUsers(2).to(USER_CAP).during (10 minutes))

					.throttle(jumpToRps(2), reachRps(TARGET_RPS) in (10 minutes))

					.protocols(httpProtocol)
		).maxDuration(10 minutes).disablePauses

}