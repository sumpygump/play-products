package controllers

import play.api.mvc.{Action, Controller, Flash}
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
  def list(page: Int = 1) = Action { implicit request =>
    val products = Product.getAll
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
   * New product action
   */
  def newProduct = Action { implicit request =>
    val form = if (flash.get("error").isDefined)
      this.productForm.bind(flash.data)
    else
      this.productForm

    Ok(views.html.products.editProduct(form))
  }

  /**
   * Save action
   */
  def save = Action { implicit request =>
    val newProductForm = this.productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(
          routes.Products.newProduct()).
          flashing(Flash(form.data) + ("error" -> Messages("validation.errors"))
        )
      },
      
      success = { newProduct =>
        //Product.add(newProduct)
        Product.insert(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      }
    )
  }

  /**
   * Product form
   */
  private val productForm: Form[Product] = Form(
    mapping(
      "id" -> longNumber,
      "ean" -> longNumber.verifying(
        "validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )
}
