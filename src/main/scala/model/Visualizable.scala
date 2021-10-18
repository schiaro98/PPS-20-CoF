package model

import java.awt.Color

/**
 * Trait that represent an element which has characteristics that allow it to be drawn on the map.
 */
trait Visualizable {

  /**
   * The color that should be used to draw the element.
   */
  val color: Color
}