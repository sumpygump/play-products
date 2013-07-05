package controllers

import play.api.mvc.{Action, Controller}
import models.Product

import play.api.data.Form
import play.api.data.Forms.{mapping, longNumber, nonEmptyText}
import play.api.i18n.Messages

/**
 * Products controller
 */
object Products extends Controller
{
  /**
   * List action
   */
  def list = Action { implicit request =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }

  /**
   * Show action
   *
   * @param Product EAN
   */
  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map ( product =>
      Ok(views.html.products.details(product))
    ).getOrElse(NotFound)
  }

  /**
   * Product form
   */
  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )
}
