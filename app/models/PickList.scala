package models

/**
 * Picklist case class
 */
case class PickList (
  warehouse: String
)

/**
 * Picklist companion class
 */
object PickList
{
  def find(warehouse: String): List[Preparation] = {
    // Other logic would happen here, this is a simple example
    Preparation.find(warehouse)
  }
}
