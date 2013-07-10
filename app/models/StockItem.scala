package models

import play.api.Play.current
import play.api.db.DB

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.ResultSetParser

/**
 * StockItem case class
 */
case class StockItem (
  id: Long,
  productId: Long,
  warehouseId: Long,
  quantity: Long
)

object StockItem
{
  val stockItemParser: RowParser[StockItem] = {
    import anorm.SqlParser._
    import anorm.~
    long("id") ~
    long("product_id") ~
    long("warehouse_id") ~
    long("quantity") map {
      case id ~ productId ~ warehouseId ~ quantity =>
        StockItem(id, productId, warehouseId, quantity)
    }
  }
}
