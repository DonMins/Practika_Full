package controllers;

import io.ebean.Ebean;
import models.Entities.User;
import models.LoginForm;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.*;
import java.util.List;


import javax.inject.Inject;

/**
 * Controller that render login form and hanlde all request for login and logout.
 */
public class LoginController extends Controller {
    @Inject
    FormFactory formFactory;
    private static Logger.ALogger logger = Logger.of(LoginController.class);
    /**
     * Render login form
     * @return Response with form in it
     */
    public Result renderLoginForm(){
        logger.info("Кто-то пришел на страницу логина");
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        return ok(views.html.login.render(form));
    }
    /**
     * Handle form with data, parse it and serching for such user in database.
     * If success it redirects to page depends on user`s role and put username in session.
     * If there were errors it redirects to loginPage again.
     * @return Responses with redirects to pages by calling their controllers.
     */
    public Result checkingLoginForm(){
        logger.info("Проверка формы логина");
        Form<LoginForm> bookForm = formFactory.form(LoginForm.class).bindFromRequest();
        if(bookForm.hasErrors()){
            logger.info("Пользователь оставил одно из полей пустым");
            return ok(views.html.login.render(bookForm));
        }
        if(bookForm.hasGlobalErrors()){
            logger.info("Введены некорректные данные");
            return ok(views.html.login.render(bookForm));
        }
        LoginForm book = bookForm.get();
        System.out.println(book.toString());
        List<User> users = Ebean.find(User.class).where().eq("login", book.getLogin()).findList();

        User user = users.get(0);
        if( user.getPassword().equals(book.getPassword())){
            logger.info("Пользователь " + user.getLogin() + " успешно авторизовался");
            session().put("username", book.getLogin());
            if (user.getAdmin()) {
                logger.info("Пользователь " + user.getLogin() + " авторизовался в роли администратора");
                return redirect(routes.MainPageController.renderMainPage());
            }
            else {
                logger.info("Пользователь " + user.getLogin() + " авторизовался в роли пользователя");
                return redirect(routes.XmlController.xmlFunction("reestr",0,book.getLogin(),user.getAdmin()));
            }
        }

        session().remove("username");
        return redirect(routes.LoginController.renderLoginForm());
    }

    /**
     * Handle logout request and remove user`s name form session.
     * @return Response with redirect to login page.
     */
    public Result logout(){
        logger.info("Пользователь " + session().get("username") + " разлогинился");
        session().remove("username");
        return redirect(routes.LoginController.renderLoginForm());
    }

}
