package models

import scala.language.postfixOps

import play.api.Play.current
import play.api.db.DB

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.ResultSetParser

/**
 * Product case class
 */
case class Product (
  id: Long,
  ean: Long,
  name: String,
  description: String
)

/**
 * Product companion object
 */
object Product
{
  /**
   * Mock product data
   */
  var products = Set(
    Product(1, 5010255079763L, "Paperclips Large",
      "Large Plain Pack of 1000"),
    Product(2, 5018206244666L, "Giant Paperclips",
      "Giant Plain 51mm 100 pack"),
    Product(3, 5018306332812L, "Paperclip Giant Plain",
      "Giant Plain Pack of 10000"),
    Product(4, 5018306312913L, "No Tear Paper Clip",
      "No Tear Extra Large Pack of 1000"),
    Product(5, 5018206244611L, "Zebra Paperclips",
      "Zebra Length 28mm Assorted 150 Pack")
  )

  /**
   * Get all products from database
   *
   * @return List[Product]
   */
  def getAll: List[Product] = DB.withConnection
  {
    val sql: SqlQuery = SQL("SELECT * FROM products ORDER BY name ASC")

    implicit connection =>
    sql().map ( row =>
      Product(
        row[Long]("id"),
        row[Long]("ean"),
        row[String]("name"),
        row[String]("description")
      )
    ).toList
  }

  /**
   * Get all products from database
   *
   * Illustrates pattern matching results
   *
   * @return List[Product]
   */
  def getAllWithPatterns: List[Product] = DB.withConnection
  {
    val sql: SqlQuery = SQL("SELECT * FROM products ORDER BY name ASC")

    implicit connection =>
    import anorm.Row
    sql().collect {
      case Row(
        Some(id: Long),
        Some(ean: Long),
        Some(name: String),
        Some(description: String)
      ) => Product(id, ean, name, description)
    }.toList
  }

  /**
   * Get all products from database
   *
   * Illustrates using a parser
   */
  def getAllWithParser: List[Product] = DB.withConnection
  {
    val sql: SqlQuery = SQL("SELECT * FROM products ORDER BY name ASC")

    implicit connection =>
    sql.as(productsParser)
  }

  val productParser: RowParser[Product] = {
    import anorm.~
    import anorm.SqlParser._
    long("id") ~
    long("ean") ~
    str("name") ~
    str("description") map {
      case id ~ ean ~ name ~ description =>
        Product(id, ean, name, description)
    }
  }

  val productsParser: ResultSetParser[List[Product]] = {
    productParser *
  }

  def productStockItemParser: RowParser[(Product, StockItem)] = {
    import anorm.SqlParser._
    productParser ~ StockItem.stockItemParser map (flatten)
  }

  def getAllProductsWithStockItems: Map[Product, List[StockItem]] = {
    DB.withConnection { implicit connection =>
    val sql = SQL("""SELECT p.*, s.*
      FROM products p
      INNER JOIN stock_items s ON (p.id = s.product_id)""")

    val results: List[(Product, StockItem)] =
      sql.as(productStockItemParser *)
    results.groupBy { _._1 }.mapValues { _.map { _._2 } }
    }
  }

  def insert(product: Product): Boolean = {
    DB.withConnection { implicit connection =>

    SQL("""INSERT INTO products
      VALUES ({id}, {ean}, {name}, {description})""").on(
        "id" -> product.id,
        "ean" -> product.ean,
        "name" -> product.name,
        "description" -> product.description
      ).executeUpdate() == 1
    }
  }

  def update(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""UPDATE products
        SET
          name = {name},
          ean = {ean},
          description = {description}
        WHERE id = {id}
        """)
        .on(
          "id" -> product.id,
          "name" -> product.name,
          "ean" -> product.ean,
          "description" -> product.description
        )
        .executeUpdate() == 1
    }
  }

  def delete(product: Product): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("DELETE FROM products WHERE id = {id}")
        .on("id" -> product.id)
        .executeUpdate() == 0
    }
  }

  /**
   * Find all products sorted by EAN
   */
  def findAll = this.products.toList.sortBy(_.ean)

  /**
   * Find product by EAN
   *
   * @param Product EAN
   */
  def findByEan(ean: Long) = {
    DB.withConnection { implicit connection =>
      //this.products.find(_.ean == ean)
      val sql: SqlQuery = SQL("SELECT * FROM products WHERE ean = {ean}")

      val products = sql.on("ean" -> ean)
        .as(productsParser)

      products.headOption
    }
  }

  /**
   * Add new product
   *
   * @param Product object
   */
  def add(product: Product) {
    this.products = this.products + product
  }
}
