package controllers.security;

import controllers.routes;
import io.ebean.Ebean;
import models.Entities.User;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
/** The controller, which checks user's rights*/
public class ActionAuthontificator extends play.mvc.Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        String s = ctx.session().get("username");
        List<User> users = Ebean.find(User.class).where().eq("login", s).findList();
        if(users.isEmpty()){
            return null;
        }
        User user = users.get(0);
        if(user != null){
            return user.getLogin();
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.LoginController.renderLoginForm());
    }
}
