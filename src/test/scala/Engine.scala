import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import wordstress.{ConstantUserCountLowRun, IDEPathHelper}

object Engine extends App {

  val props = new GatlingPropertiesBuilder()
    .resourcesDirectory(IDEPathHelper.resourcesDirectory.toString)
    .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
    .simulationClass("wordstress.LoadSpikeRun")

  Gatling.fromMap(props.build)
}
