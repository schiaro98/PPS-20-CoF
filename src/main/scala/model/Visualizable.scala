package model

import java.awt.image.BufferedImage

/**
 * Trait that represent an element viewable on the map.
 */
trait Visualizable {
  val icon: BufferedImage
}
/**
 * Object that represent an element viewable on the map.
 */
object Visualizable {
  /**
   * Apply method for a Visualizable.
   *
   * @param icon the image to draw in the map.
   * @return an implementation of Visualizable.
   */
  def apply(icon: BufferedImage): Visualizable = new VisualizableImpl(icon)

  private class VisualizableImpl(override val icon: BufferedImage) extends Visualizable
}