package wordstress

import java.io.{File, FileReader}
import java.nio.file.Path
import java.util.Properties

import io.gatling.commons.util.PathHelper._

object IDEPathHelper {

  val gatlingConfUrl: Path = getClass.getClassLoader.getResource("gatling.conf")
  val projectRootDir = gatlingConfUrl.ancestor(4)

  val mavenSourcesDirectory = projectRootDir / "src" / "test" / "scala"
  val mavenResourcesDirectory = projectRootDir / "src" / "test" / "resources"
  val mavenTargetDirectory = projectRootDir / "target"
  val mavenBinariesDirectory = mavenTargetDirectory / "test-classes"

  val resourcesDirectory = mavenResourcesDirectory
  val recorderSimulationsDirectory = mavenSourcesDirectory
  val resultsDirectory = mavenTargetDirectory / "gatling"
  val recorderConfigFile = mavenResourcesDirectory / "recorder.conf"

  def readLoadFactor(): Double = {
    val prop = new Properties();
    prop.load(new FileReader(new File("load.prop")));
    val lf = prop.getProperty("load.factor", "1");
    System.out.println("Load factor: " + lf);
    return java.lang.Double.valueOf(lf);
  }
}
