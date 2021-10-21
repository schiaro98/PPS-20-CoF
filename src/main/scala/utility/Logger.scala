package utility

object Logger {

  var history : Seq[String] = Seq.empty

  def info(message: String, debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"INFO: $message")
  }

  def debug(message: String,debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"DEBUG: $message")
  }

  def error(message: String, debug: Boolean = false): Unit = {
    history = history :+ message
    if(debug)println(s"ERROR: $message")
  }
}
