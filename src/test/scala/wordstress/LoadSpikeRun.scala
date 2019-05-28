package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._

class LoadSpikeRun extends CommonSimulation {

    val BACKGROUND_RATE = Math.ceil(0.4 * LOAD_FACTOR).asInstanceOf[Int];
	  val BACKGROUND_USERS = Math.ceil(1 * LOAD_FACTOR).asInstanceOf[Int];
    val SPIKE_RATE = Math.ceil(4 * LOAD_FACTOR).asInstanceOf[Int];

    println("BACKGROUND_RATE:" + BACKGROUND_RATE);
    println("BACKGROUND_USERS:" + BACKGROUND_USERS);
    println("SPIKE_RATE:" + SPIKE_RATE);

		setUp(
				scenario("Constant low load")
					.exec(Scripts.compositeWordpressLoad())

					.inject(rampConcurrentUsers(1).to(BACKGROUND_USERS).during(20 seconds), constantConcurrentUsers(BACKGROUND_USERS).during(5 minutes))

  			  //.throttle(reachRps(BACKGROUND_RATE) in (20 seconds), holdFor(5 minutes))

					.protocols(httpProtocol),

				scenario("Spike")
					.exec(Scripts.compositeWordpressLoad())

					.inject(
						constantUsersPerSec(0).during(2 minutes),
						rampUsersPerSec(1).to( SPIKE_RATE).during(1 minutes),
						constantUsersPerSec(0).during(2 minutes),
					)

					.protocols(httpProtocol),

		).maxDuration(5 minutes).pauses(uniformPausesPlusOrMinusDuration(0.5 second))

}