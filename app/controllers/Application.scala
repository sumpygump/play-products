package controllers

import play.api.mvc.{Action, Controller}

/**
 * Application controller
 */
object Application extends Controller
{
  def index = Action {
    Redirect(routes.Products.list)
  }
}
