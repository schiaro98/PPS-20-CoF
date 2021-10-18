package model

/**
 * Trait that represent an element that is positioned at a certain point on the map.
 */
trait Placeable {
  /**
   * The point on the map where the element is located
   */
  val position: Point
}
