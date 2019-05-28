package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._

class QuickmarkRun extends CommonSimulation {

	  val time = 10 minutes

		setUp(
				scenario("Quick benchmark")
					.exec(Scripts.compositeWordpressLoad())

					.inject(constantConcurrentUsers(1).during(20 seconds), rampConcurrentUsers(2).to(100).during (time))

					.protocols(httpProtocol)
		).maxDuration(time).disablePauses

}