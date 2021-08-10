import com.google.gson.{GsonBuilder, JsonElement, JsonStreamParser}

import java.io.{ByteArrayInputStream, InputStream, InputStreamReader, Reader}
import java.nio.charset.StandardCharsets

case class Car(brand: String, doors:String)

object ReadWrite extends App {
  val initialString = "{\"brand\":\"poop\", \"doors\": 105}{\"brand\":\"Panda\", \"doors\": 5}"
  val is = new ByteArrayInputStream(initialString.getBytes(StandardCharsets.UTF_8));
  val r:Reader = new InputStreamReader(is, "UTF-8")
  val gson = new GsonBuilder().create()
  val p = new JsonStreamParser(r)

  while (p.hasNext()) {
    val e = p.next()
    if (e.isJsonObject()) {
       val car = gson.fromJson(e, classOf[Car])
      println(car)
      /* do something useful with JSON object .. */
    }
    /* handle other JSON data structures */
  }
}
