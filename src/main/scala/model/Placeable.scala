package model

/**
 * Trait that represent an element that is positioned at a certain point on the map.
 */
trait Placeable {
  val position: (Int, Int)
}