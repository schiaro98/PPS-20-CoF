package controller

import com.google.gson.{JsonDeserializer, _}
import model.{Food, Size, Species}

import java.io.PrintWriter
import java.lang.reflect.Type
import scala.collection.mutable.ListBuffer
import scala.reflect.io.File

sealed trait SerializerType
case object DefaultSerializer extends SerializerType
case object OfSpecies extends SerializerType
case object OfFood extends SerializerType



sealed trait Serializer {
  def serializeOne[U](obj: U): String
  def serializeMany[U](objs: Iterable[U], serializedObjs:String = ""): String
  def serializeManyToFile[U](objs:Iterable[U])(fileName:String): Unit
  def deserializeOne[T](json: String)(classOfT: Class[T]): T
  def deserializeMany[T](json: String)(classOfT: Class[T]): Seq[T]
  def deserializeManyFromFile[T](fileName: String)(classOfT: Class[T]): Seq[T]
}

object Serializer {

  def apply(serializerType: SerializerType): Serializer = serializerType match {
    case DefaultSerializer => new SerializerImpl
    case OfSpecies => new SpeciesSerializer
    case OfFood => new FoodsSerializer
  }

  private class SerializerImpl() extends Serializer {
    val gson = new Gson

    def serializeOne[U](obj: U): String = gson.toJson(obj)

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

  private class SpeciesSerializer extends SerializerImpl {
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
          Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Species"))
        }
      }

      override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Size], SizeSerializer)
        .registerTypeHierarchyAdapter(classOf[Species], SizeDeserializer).setPrettyPrinting().create()
  }

  private class FoodsSerializer extends SerializerImpl{

    object FoodDeserializer extends JsonDeserializer[Food] {
      override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Food = {
        val res = json match {
          case obj: JsonObject if obj.has("icon") && obj.has("energy") =>
            val icon = obj.get("icon").getAsString
            val energy = obj.get("energy").getAsInt
            Food(icon, energy)
          case _ => null
        }
        Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Food"))
      }
    }

    override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Food], FoodDeserializer).create()
  }
}

