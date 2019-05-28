package wordstress

import io.gatling.core.Predef._

import scala.concurrent.duration._;

class ConstantUserCountHighRun extends CommonSimulation {

	val BACKGROUND_RATE = Math.ceil(0.4 * LOAD_FACTOR).asInstanceOf[Int];
	val USER_COUNT = Math.ceil(3.5 * LOAD_FACTOR).asInstanceOf[Int];

	println("BACKGROUND_RATE:" + BACKGROUND_RATE);
	println("USER_COUNT:" + USER_COUNT);

	setUp(
		scenario("Constant low load")
			.exec(Scripts.compositeWordpressLoad())

			.inject(rampConcurrentUsers(1).to(USER_COUNT).during(20 seconds), constantConcurrentUsers(USER_COUNT).during(5 minutes))

			.protocols(httpProtocol)

	).maxDuration(5 minutes).pauses(uniformPausesPlusOrMinusDuration(0.5 second))
}