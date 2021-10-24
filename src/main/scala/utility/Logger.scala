package utility

/**
 * Singleton object used to mantain a single Logger during the application life cycle
 */
object Logger {
  /**
   * Used to mantain every logging info
   */
  var history : Seq[String] = Seq.empty

  /**
   * Write a info message on [[history]]
   * @param message the message to be print
   * @param debug if true, print also on System Out
   */
  def info(message: String, debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"INFO: $message")
  }

  /**
   * Write a debug message on [[history]]
   * @param message the message to be print
   * @param debug if true, print also on System Out
   */
  def debug(message: String,debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"DEBUG: $message")
  }

  /**
   * Write a error message on [[history]]
   * @param message the message to be print
   * @param debug if true, print also on System Out
   */
  def error(message: String, debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"ERROR: $message")
  }
}
