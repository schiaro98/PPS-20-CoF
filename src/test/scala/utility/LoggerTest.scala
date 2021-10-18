package utility

import org.scalatest.funsuite.AnyFunSuite

class LoggerTest extends AnyFunSuite{
  private val logger = Logger
  private val anotherLogger = Logger

  test("All methods should be callable"){
    logger.info("info")
    logger.debug("debug")
    logger.error("error")
    assert(logger.history.contains("info"))
    for(i <- 1 to 100){
      logger.info(s"$i")
    }
    assert(logger.history.last contains "100")
    anotherLogger.info("Ciao")
    assert(logger.history.contains("Ciao"))
  }
}
