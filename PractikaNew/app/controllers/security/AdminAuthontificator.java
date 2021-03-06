package controllers.security;

import controllers.routes;
import io.ebean.Ebean;
import models.Entities.User;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
/**The controller for the authentication*/
public class AdminAuthontificator extends play.mvc.Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        String s = ctx.session().get("username");
        List<User> users = Ebean.find(User.class).where().eq("login", s).findList();
        if(users.isEmpty()){
            return null;
        }
        User user = users.get(0);
        if(user != null && user.getAdmin()){
            return user.getLogin();
        }
        return null;
    }
/**@return - the link to the reestr for user*/
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.XmlController.xmlFunction("reestr",0,null,false));
    }
}
