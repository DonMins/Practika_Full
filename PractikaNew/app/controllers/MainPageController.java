package controllers;

import controllers.security.ActionAuthontificator;
import controllers.security.AdminAuthontificator;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
/**
 The controller shows main page for administrator */
public class MainPageController extends Controller {
    private static Logger.ALogger logger = Logger.of(MainPageController.class);
/**
 * @return - it is link to the main page for administrator*/
    @Security.Authenticated(AdminAuthontificator.class)
        public Result renderMainPage(){
        logger.info("Пользователь " + session().get("username") + " переходит на главную страницу");
        return ok(views.html.mainPage.render());
    }
}
