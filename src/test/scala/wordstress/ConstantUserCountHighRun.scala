package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._;

class ConstantUserCountHighRun extends CommonSimulation {

    val USER_COUNT = Math.ceil(1.5 * LOAD_FACTOR).asInstanceOf[Int];

		setUp(
				scenario("Constants user low")
					.exec(Scripts.compositeWordpressLoad())

					.inject(rampConcurrentUsers(1).to(USER_COUNT).during(20 seconds), constantConcurrentUsers(USER_COUNT).during(5 minutes))

					.protocols(httpProtocol)

		).maxDuration(5 minutes).disablePauses

}