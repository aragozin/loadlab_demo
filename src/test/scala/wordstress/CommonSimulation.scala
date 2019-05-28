package wordstress

import io.gatling.core.Predef._
import io.gatling.core.filter.{Filters, WhiteList}
import io.gatling.http.Predef._

class CommonSimulation extends Simulation {

  val LOAD_FACTOR = IDEPathHelper.readLoadFactor();

  val httpProtocol = http
    //    .proxy(Proxy("127.0.0.1",8888))
//      .inferHtmlResources(WhiteList(Seq("http://wp\\.loadlab.*")))
      .baseUrl("http://wp.loadlab.ragozin.info")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0")

}
