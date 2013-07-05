package controllers

import java.util.Date

import play.api.mvc.{Action, Controller, Flash}
import play.api.libs.concurrent.Akka
import play.api.templates.Html

import models.PickList

/**
 * Picklists controller
 */
object PickLists extends Controller
{
  /**
   * Listing of picklist targets
   */
  def list() = Action { implicit request =>
    Ok(views.html.picklists.list())
  }

  /**
   * Actions for a given warehouse
   */
  def warehouse(warehouse: String) = Action { implicit request =>
    Ok(views.html.picklists.warehouse(warehouse))
  }

  /**
   * Action to preview a picklist
   */
  def preview(warehouse: String) = Action { implicit request =>
    val pickList = PickList.find(warehouse)
    val timestamp = new java.util.Date
    Ok(views.html.picklists.pickList(warehouse, pickList, timestamp))
  }

  /**
   * Asynchronous sending of pick list
   */
  def sendAsync(warehouse: String) = Action { implicit request =>
    import play.api.Play.current
    Akka.future {
      val pickList = PickList.find(warehouse)
      send(views.html.picklists.pickList(warehouse, pickList, new Date))
    }
    Redirect(routes.PickLists.list())
  }

  def send(html: Html) {
    // todo
  }
}
