package controllers;

import controllers.security.AdminAuthontificator;
import io.ebean.Ebean;
import models.UpdateForm;
import models.Entities.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.collection.JavaConverters;

import javax.inject.Inject;

import static scala.collection.JavaConverters.asScalaBuffer;

import java.util.*;


/**
 * UserController is responsible for every action with user administration.
 * It shows users list, render all adding, updating and registration forms and handling all get/post requests from them.
 */
public class UsersController extends Controller {
    private static Logger.ALogger logger = Logger.of(UsersController.class);


    /**
     * Render table with all registered users.
     * @return Response with params to be displayed in table:
     * list of users and users fields names
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result usersList(){
        logger.info("Пользователь " + session().get("username") + " переходит на страницу списка пользователей");

        List<User> users = User.find.all();
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getId() <= o2.getId())
                    return -1;
                return 1;
            }
        });
        ArrayList<String> nameColomn = new ArrayList<>();
        User us = new User();
        nameColomn = us.getNameColomn();

        return ok(views.html.users.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(users)));
    }

    /**
     *
     * @param id identificator of user to delete
     * @return Response with updated list of users
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result deleteUser(Integer id){
        logger.info("Пользователь " + session().get("username") + " удаляет пользователя " + User.find.byId(id).getLogin());
        User.find.deleteById(id);
        return redirect(routes.UsersController.usersList());
    }

    @Inject
    FormFactory formFactory;


    /**
     * Render registration page
     * @return Response with registration form to registration page
     */
    public Result renderAddUserForm(){
        logger.info("Пользователь переходит на страницу регистрации");
        Form<User> form = formFactory.form(User.class);

        return ok(views.html.createUser.render(form));
    }

    /**
     * Render adding new user page for admin
     * @return Response with adding user form
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result renderAdminForm(){
        logger.info("Пользователь " + session().get("username") + " переходит на страницу добавления нового пользователя");
        Form<User> form = formFactory.form(User.class);

        return ok(views.html.createUserAdmin.render(form));
    }

    /**
     * Handle post request with form in it, parse it, and trying to add new user to database.
     * After it redirects to login page or stays on current page if there was some errors.
     * @return Redirects to other pages by calling theirs controllers.
     */
    public Result addingUser(){
        logger.info("Регистрация регистрации нового пользователя");
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if(userForm.hasErrors() || userForm.hasGlobalErrors()){
            return ok(views.html.createUser.render(userForm));
        }
        Map<String, String> rawdata = userForm.rawData();

        Boolean isAdmin = Boolean.valueOf(String.valueOf(rawdata.get("isAdmin")));
        User user = new User();
        //User user = userForm.get();
        user.setLogin(rawdata.get("login"));
        user.setPassword(rawdata.get("password"));
        user.setRegion(Integer.valueOf(rawdata.get("region")));
        user.setAdmin(isAdmin);
        List<User> users = Ebean.find(User.class).where().eq("login", user.getLogin()).findList();
        if(users.isEmpty()){
            try{
                Ebean.save(user);
            }catch (Exception ex){
                logger.info("При регистрации пользователя произошла ошибка");
               // System.out.println("Something went wrong");
                return redirect(routes.UsersController.renderAddUserForm());
            }
            logger.info("Пользователь " + user.getLogin() + " успешно зарегистрирован");
            return redirect(routes.LoginController.checkingLoginForm());

        }
        logger.info("Произошла какая-то неведома ошибка");
        return redirect(routes.UsersController.renderAddUserForm());
    }

    /**
     * Handle post request with form in it, parse it, and trying to add new user to database if user added by admin.
     * After it redirects to userslist page or stays on current page if there were some errors.
     * @return Redirects to other pages by calling theirs controllers.
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result addingUserAdmin(){
        logger.info("Пользователь " + session().get("username") + " добавляет нового пользователя");
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        if(userForm.hasErrors() || userForm.hasGlobalErrors()){
            return ok(views.html.createUserAdmin.render(userForm));
        }
        Map<String, String> rawdata = userForm.rawData();

        Boolean isAdmin = Boolean.valueOf(String.valueOf(rawdata.get("isAdmin")));
        User user = new User();
        //User user = userForm.get();
        user.setLogin(rawdata.get("login"));
        user.setPassword(rawdata.get("password"));
        user.setRegion(Integer.valueOf(rawdata.get("region")));
        user.setAdmin(isAdmin);
        List<User> users = Ebean.find(User.class).where().eq("login", user.getLogin()).findList();
        if(users.isEmpty()){
            try{
                Ebean.save(user);
            }catch (Exception ex){
                logger.info("Пользователь: " + session().get("username") + " -- при добавлении нового пользователя произошла ошибка");
                return redirect(routes.UsersController.renderAdminForm());
            }

            logger.info("Пользователь " + user.getLogin()+ " успешно добавлен пользователем " + session().get("username"));
            return redirect(routes.UsersController.usersList());

        }


        return redirect(routes.UsersController.renderAdminForm());
    }

    /**
     * Render update form for updating user`s info as admin
     * @param id identificator of user to be updated
     * @return Response with form with data of user fill in it
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result renderUpdateUserInfo(Integer id){
        User user = User.find.byId(id);
        logger.info("Пользователь " + session().get("username") + " обновляет информацию о пользователе " + user.getLogin());
        UpdateForm update = new UpdateForm(user.getPassword(), user.getRegion(), user.getAdmin());
        Form<UpdateForm> updateForm = formFactory.form(UpdateForm.class).fill(update);
        return ok(views.html.updateUser.render(updateForm, user));

    }


    /**
     * Handle form with data, parse it and try to update user`s info in database for admin.
     * @param id identificator of user to be updated
     * @return redirects to o userslist page if its ok and stays on current page if there were some errors
     */
    @Security.Authenticated(AdminAuthontificator.class)
    public Result updateUserInfo(Integer id){
        User user = User.find.byId(id);
        Form<UpdateForm> form = formFactory.form(UpdateForm.class).bindFromRequest();
        if(form.hasErrors() || form.hasGlobalErrors()){
            logger.info("Пользователь: " + session().get("username") + " при обновлении информации о " + user.getLogin() + " одно или несколько полей остались пустыми");
            return ok(views.html.updateUser.render(form, user));
        }
        Map<String, String> rawdata = form.rawData();

        Boolean isAdmin = Boolean.valueOf((rawdata.get("isAdmin")));
        user.setPassword(rawdata.get("password"));
        user.setRegion(Integer.valueOf(rawdata.get("region")));
        user.setAdmin(isAdmin);
        System.out.println("some errors " + isAdmin);
        Ebean.update(user);
        logger.info("Пользователь: " + session().get("username") + " данные о пользователе " + user.getLogin() + " успешно обновлены");
        return redirect(routes.UsersController.usersList());
    }
}


