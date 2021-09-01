package controller

import com.google.gson._
import model.{Size, Species}

import java.io.PrintWriter
import java.lang.reflect.Type
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.reflect.io.File

sealed trait Serializer {
  def serializeOne[U](obj: U): String
  def serializeArray[U](objs: Iterable[U], serializedObjs:String): String
  def serializeManyToFile[U](objs:Iterable[U])(fileName:String): Unit
  def deserializeOne[T](json: String)(classOfT: Class[T]): T
  def deserializeMany[T](json: String)(classOfT: Class[T]): Seq[T]
  def deserializeManyFromFile[T](fileName: String)(classOfT: Class[T]): Seq[T]
}

object Serializer {
  val gson = new Gson

  def serializeOne[U](obj: U): String = gson.toJson(obj)

  @tailrec
  def serializeMany[U](objs: Iterable[U], serializedObjs:String = ""): String = objs.toSeq match {
    case h::t  => serializeMany(t, serializedObjs + serializeOne(h))
    case _ => serializedObjs
  }

  def serializeManyToFile[U](objs:Iterable[U])(fileName:String): Unit =
    new PrintWriter("res"+File.separator+"serialization"+File.separator+fileName) { write(serializeMany(objs)); close() }

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


  def deserializeManyFromFile[T](fileName: String)(classOfT: Class[T]): Seq[T] = {
    import java.nio.charset.StandardCharsets
    import java.nio.file.{Files, Path}

    val path = Path.of("res"+File.separator+"serialization"+File.separator+fileName)
    val json = Files.readString(path, StandardCharsets.UTF_8)
    deserializeMany(json)(classOfT)
  }
}

class SpeciesSerializer extends Serializer{

//  class InterfaceAdapter extends JsonSerializer[Size] with JsonDeserializer[Size] {
//    override def serialize(src: Size, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = new JsonPrimitive(src.toString)
//
//    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Size =  {
//      val res = json match {
//        case primitive: JsonPrimitive if primitive.isString =>
//          val size = primitive.getAsString
//          size match {
//            case "Big" => Size.Big
//            case "Medium" => Size.Medium
//            case "Small" => Size.Small
//            case _ => null
//          }
//        case _ => null
//      }
//      Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to State"))
//    }
//  }

  object SizeSerializer extends JsonSerializer[Size] {
    override def serialize(src: Size, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =  new JsonPrimitive(src.toString)
  }

  object SizeDeserializer extends JsonDeserializer[Species]{
    override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Species = {
      val res = json match {
        case obj: JsonObject if obj.has("size") =>
          val sizeAsString = obj.get("size").getAsString
          val size = sizeAsString match {
            case "Big" => Size.Big
            case "Medium" => Size.Medium
            case "Small" => Size.Small
            case _ => null
          }
          val name = obj.get("name").getAsString
          val strength = obj.get("strength").getAsInt
          val sight = obj.get("sight").getAsInt
          val icon = obj.get("icon").getAsString
          Species(icon, name, size, strength, sight)
        case _ => null
      }
      Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to State"))
    }
  }

  val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Size], SizeSerializer)
    .registerTypeHierarchyAdapter(classOf[Species], SizeDeserializer).setPrettyPrinting().create()

//  val gson: Gson = new GsonBuilder().registerTypeAdapter(classOf[Species], new InterfaceAdapter).create()

  override def serializeOne[U](obj: U): String = gson.toJson(obj)

  override def serializeArray[U](objs: Iterable[U], serializedObjs: String): String = ???

  override def serializeManyToFile[U](objs: Iterable[U])(fileName: String): Unit = ???

  override def deserializeOne[T](json: String)(classOfT: Class[T]): T = gson.fromJson(json,classOfT)

  override def deserializeMany[T](json: String)(classOfT: Class[T]): Seq[T] = ???

  override def deserializeManyFromFile[T](fileName: String)(classOfT: Class[T]): Seq[T] = ???
}