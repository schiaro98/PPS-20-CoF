package utility

import com.google.gson._
import model._

import java.awt.Color
import java.io.PrintWriter
import java.lang.reflect.Type
import scala.collection.mutable.ListBuffer
import scala.reflect.io.File

sealed trait SerializerType
case object DefaultSerializer extends SerializerType
case object OfSpecies extends SerializerType
case object OfFood extends SerializerType
case object OfArea extends SerializerType
case object OfProbability extends SerializerType

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
    case OfArea => new AreasSerializer
    case OfProbability => new ProbabilitySerializer
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
      if (json == "") Seq.empty[T] else deserializeMany(json)(classOfT)
    }
  }

  private class SpeciesSerializer extends SerializerImpl {
      object SizeSerializer extends JsonSerializer[Size] {
        override def serialize(src: Size, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =  new JsonPrimitive(src.toString)
      }

    object TypeSerializer extends JsonSerializer[model.Type] {
      override def serialize(src: model.Type, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =  new JsonPrimitive(src.toString)
    }

      object SpeciesDeserializer extends JsonDeserializer[Species]{
        override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Species = {
          val res = json match {
            case obj: JsonObject if obj.has("size") =>
              val sizeAsString = obj.get("size").getAsString
              val alimentationAsString = obj.get("alimentationType").getAsString
              val size = StringConverter.getSize(sizeAsString)
              val alimentationType = StringConverter.getAlimentationType(alimentationAsString)
              val name = obj.get("name").getAsString
              val strength = obj.get("strength").getAsInt
              val sight = obj.get("sight").getAsInt
              val color = deserializeOne(obj.get("color").toString)(classOf[Color])
              Species(name, size, strength, sight, alimentationType, color)
            case _ => null
          }
          Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Species"))
        }
      }

      override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Size], SizeSerializer)
        .registerTypeHierarchyAdapter(classOf[Species], SpeciesDeserializer)
        .registerTypeHierarchyAdapter(classOf[model.Type], TypeSerializer).setPrettyPrinting().create()
  }

  private class FoodsSerializer extends SerializerImpl {

    object FoodTypeSerializer extends JsonSerializer[model.FoodType] {
      override def serialize(src: FoodType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = new JsonPrimitive(src.toString)
    }

    object FoodDeserializer extends JsonDeserializer[Food] {
      override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Food = {
        val res = json match {
          case obj: JsonObject if obj.has("color") && obj.has("energy")
            && obj.has("foodType") =>
            val color = deserializeOne(obj.get("color").toString)(classOf[Color])
            val energy = obj.get("energy").getAsInt
            val foodType = obj.get("foodType").getAsString
            Food(color, energy, StringConverter.getFoodType(foodType))
          case _ => null
        }
        Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Food"))
      }
    }

    override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Food], FoodDeserializer)
    .registerTypeHierarchyAdapter(classOf[FoodType], FoodTypeSerializer).create()
  }

  private class ProbabilitySerializer extends SerializerImpl {

    object ProbabilityDeserializer extends JsonDeserializer[Probability] {
      override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Probability = {
        val res = json match {
          case obj: JsonObject if obj.has("probability") =>
            val probability = obj.get("probability").getAsInt
            Probability(probability)
          case _ => null
        }
        Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Probability"))
      }
    }

    override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[Probability], ProbabilityDeserializer).create()
  }

  private class AreasSerializer extends SerializerImpl{
    val probabilitySerializer: Serializer = Serializer(OfProbability)

    object AreaTypeSerializer extends JsonSerializer[AreaType] {
      override def serialize(src: AreaType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =  new JsonPrimitive(src.toString)
    }

    object AreasDeserializer extends JsonDeserializer[Area] {
      override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Area = {
        val res = json match {
          case obj: JsonObject if obj.has("areaType") =>
            val areaTypeAsString = obj.get("areaType").getAsString
            val areaType = areaTypeAsString match {
              case "Fertile" => Fertile
              case "Water" => Water
              case "Rock" => Rock
              case "Volcano" => Volcano
              case _ => null
            }
            val area = deserializeOne(obj.get("area").toString)(classOf[RectangleArea])
            if (obj.has("fertility")) {
              val fertility = probabilitySerializer.deserializeOne(obj.get("fertility").toString)(classOf[Probability])
              Area(areaType, area, fertility)
            } else {
              Area(areaType, area)
            }
          case _ => null
        }
        Option(res).getOrElse(throw new JsonParseException(s"$json can't be parsed to Area"))
      }
    }

    override val gson: Gson = new GsonBuilder().registerTypeHierarchyAdapter(classOf[AreaType], AreaTypeSerializer)
      .registerTypeHierarchyAdapter(classOf[Area], AreasDeserializer).setPrettyPrinting().create()
  }
}

