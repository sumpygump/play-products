package models

/**
 * Preparation case class
 */
case class Preparation (
  warehouse: String,
  orderNumber: Long,
  productEan: Long,
  quantity: Int,
  location: String
) {

  // Hydrate product objects immediately
  // Wouldn't scale well with a DB backend
  // I am not sure how to do this more efficiently, probably something other than using a case class?
  val product: Product = Product.findByEan(productEan).get
}

object Preparation
{
  // Sample data
  // This is not really how the data would be stored
  var orders = Set(
    Preparation("W35215", 3141592, 5010255079763L, 200, "Aisle 42 bin 7"),
    Preparation("W35215", 6535897, 5010255079763L, 500, "Aisle 42 bin 7"),
    Preparation("W35215", 93, 5010255079763L, 100, "Aisle 42 bin 7")
  )

  /**
   * Find all preps
   */
  def findAll = this.orders.toList

  /**
   * Find by warehouse code
   */
  def find(warehouse: String): List[Preparation] = findAll
}
