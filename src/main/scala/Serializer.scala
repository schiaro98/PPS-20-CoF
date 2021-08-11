import com.google.gson.Gson

import java.io.PrintWriter
import scala.annotation.tailrec
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

    def serializeManyOnFile[U](objs:Iterable[U])(fileName:String): Unit =
      new PrintWriter("res"+File.separator+fileName) { write(serializeMany(objs)); close() }

    def deserializeOne[T](json: String)(classOfT: Class[T]): T = gson.fromJson(json,classOfT)

  }





