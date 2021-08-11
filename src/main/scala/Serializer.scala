import com.google.gson.{Gson, JsonStreamParser}

import java.io.PrintWriter
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.reflect.io.File

sealed trait Serializer

  object Serializer {
    val gson = new Gson

    def serializeOne[U](obj: U): String = gson.toJson(obj)

    @tailrec
    def serializeMany[U](objs: Iterable[U], serializedObjs:String = ""): String = objs.toSeq match {
      case h::t  => serializeMany(t, serializedObjs + serializeOne(h))
      case _ => serializedObjs
    }

    @tailrec
    def serializeArray[U](objs: Iterable[U], serializedObjs:String = "["): String = objs.toSeq match {
      case h::t  => serializeArray(t, serializedObjs + serializeOne(h))
      case _ => serializedObjs + "]"
    }

    def serializeManyOnFile[U](objs:Iterable[U])(fileName:String): Unit =
      new PrintWriter("res"+File.separator+fileName) { write(serializeMany(objs)); close() }

    def deserializeOne[T](json: String)(classOfT: Class[T]): T = gson.fromJson(json,classOfT)

    def deserializeMany[T](json: String)(classOfT: Class[T]): Seq[T] = {
      val p = new JsonStreamParser(json)
      val tempList = new ListBuffer[T]
      while (p.hasNext) {
        val e = p.next()
        if (e.isJsonObject) {
          /* do something useful with JSON object .. */
          val obj = gson.fromJson(e, classOfT)
          tempList += obj
        }
        /* handle other JSON data structures */
      }
      tempList.toSeq
    }


    def deserializeManyFromFile[T](fileName: String): Seq[T] = ???

  }





