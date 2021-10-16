package utility

import model.Area

object AnimalUtils {

  /**
   * Check if a sequence of point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param points a set of [[Point]] whose positions are to be checked.
   * @return true if at least one point is not placeable because was in a non-walkable area, otherwise true.
   */
  def areNotPlaceable(areas: Seq[Area], points: Seq[Point]): Boolean = points.exists(point => isNotPlaceable(areas, point))

  /**
   * Check if a point is in a non-walkable area.
   *
   * @param areas a set [[Area]] in which the [[Point]] can be placed.
   * @param point the [[Point]] whose position is to be checked.
   * @return true if the point is not placeable because was in a non-walkable area, otherwise true.
   */
  def isNotPlaceable(areas: Seq[Area], point: Point): Boolean = {
    Constants.NonWalkableArea.contains(areas.find(a => a.area.contains(point)).getOrElse(return false).areaType)
  }

}
