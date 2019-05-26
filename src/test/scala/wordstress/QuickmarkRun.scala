package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._

class QuickmarkRun extends CommonSimulation {

		setUp(
				scenario("Quick benchmark")
					.exec(Scripts.compositeWordpressLoad())

					.inject(rampConcurrentUsers(1).to(100).during (10 minutes))

					.protocols(httpProtocol)
		).maxDuration(10 minutes).disablePauses

}