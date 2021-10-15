package utility

object Logger {

  var history : Seq[String] = Seq.empty

  def info(message: String): Unit = {
    history = history :+ message
    println(s"INFO: $message")
  }

  def debug(message: String): Unit = {
    history = history :+ message
    println(s"DEBUG: $message")
  }

  def error(message: String): Unit = {
    history = history :+ message
    println(s"ERROR: $message")
  }
}
