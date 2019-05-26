package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._

class LoadSpikeRun extends CommonSimulation {

    val USER_COUNT = Math.ceil(0.4 * LOAD_FACTOR).asInstanceOf[Int];
    val SPIKE_RATE = Math.ceil(1 * LOAD_FACTOR).asInstanceOf[Int];


		setUp(
				scenario("Constant low load")
					.exec(Scripts.compositeWordpressLoad())

					.inject(rampConcurrentUsers(1).to(USER_COUNT).during(20 seconds), constantConcurrentUsers(USER_COUNT).during(5 minutes))

					.protocols(httpProtocol),

				scenario("Spike")
					.exec(Scripts.compositeWordpressLoad())

					.inject(
						constantUsersPerSec(0).during(2 minutes),
						rampUsersPerSec(1).to( SPIKE_RATE).during(1 minutes),
						constantUsersPerSec(0).during(2 minutes),
					)

					.protocols(httpProtocol),

		).maxDuration(10 minutes).disablePauses

}