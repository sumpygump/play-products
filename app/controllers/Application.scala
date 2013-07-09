package controllers

import play.api.mvc.{Action, Controller}

/**
 * Application controller
 */
object Application extends Controller
{
  /**
   * Index (home) action
   */
  def index = Action {
    Redirect(routes.Products.list())
  }
}
