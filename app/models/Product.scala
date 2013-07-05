package models

/**
 * Product case class
 */
case class Product (
  ean: Long,
  name: String,
  description: String
)

/**
 * Product companion object
 */
object Product {

  /**
   * Mock product data
   */
  var products = Set(
    Product(5010255079763L, "Paperclips Large",
      "Large Plain Pack of 1000"),
    Product(5018206244666L, "Giant Paperclips",
      "Giant Plain 51mm 100 pack"),
    Product(5018306332812L, "Paperclip Giant Plain",
      "Giant Plain Pack of 10000"),
    Product(5018306312913L, "No Tear Paper Clip",
      "No Tear Extra Large Pack of 1000"),
    Product(5018206244611L, "Zebra Paperclips",
      "Zebra Length 28mm Assorted 150 Pack")
  )

  /**
   * Find all products sorted by EAN
   */
  def findAll = this.products.toList.sortBy(_.ean)

  /**
   * Find product by EAN
   *
   * @param Product EAN
   */
  def findByEan(ean: Long) = this.products.find(_.ean == ean)
}
