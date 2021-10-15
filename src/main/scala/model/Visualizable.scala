package model

/**
 * Trait that represent an element viewable on the map.
 */
trait Visualizable {
  val icon: String
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
  def apply(icon: String): Visualizable = new VisualizableImpl(icon)

  private class VisualizableImpl(override val icon: String) extends Visualizable

}