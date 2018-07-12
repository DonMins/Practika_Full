package controllers;


import controllers.security.ActionAuthontificator;
import io.ebean.Ebean;
import models.DictionaryDao;
import models.Entities.User;
import models.XMLMY;
import org.xml.sax.SAXException;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.indexXml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scala.collection.JavaConverters;

import javax.xml.parsers.ParserConfigurationException;
/** The Controller for showing table in the html page*/

public class XmlController extends Controller {
    private String logiN;
    private static Logger.ALogger logger = Logger.of(XmlController.class);

    @Security.Authenticated(ActionAuthontificator.class)
    /**
     * @param tablename - it is a tablename
     * @return - it is link to the page with xml structure
     * */

    public Result xmlStructura(String tablename) {
        String s = session().get("username");
        List<User>users = Ebean.find(User.class).where().eq("login", s).findList();

        logger.info("Пользователь: " + session().get("username") + " получает информацию о структруе таблицы " + tablename);
        ArrayList<ArrayList<String>> stek = XMLMY.outputStuctureOrValues(tablename);

        return ok(indexXml.render(tablename.toUpperCase(),
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList(),
                null,
                0,
                tablename,
                logiN,users.get(0).getAdmin()));
    }

    @Security.Authenticated(ActionAuthontificator.class)
    /**
     * @param tablename - it is a tablename
     * @return - The XML file
     * */
    public Result xmlSave(String tablename) throws ParserConfigurationException, SAXException, IOException {
        logger.info("Пользователь: " + session().get("username") + " скачивает справочник " + tablename);
        DictionaryDao dao = new DictionaryDao();
        String res = DictionaryDao.getTableValues(tablename);
        return ok(res);

    }


    @Security.Authenticated(ActionAuthontificator.class)
    /**
     * @param tablename - it is a tablename
     * @param position - it is a position for loading pages
     * @param login - it is a login of the user
     * @param admin - it is the boolean place for understending who is this user: admin or not
     *
     * @return - The link to the page with reestr
     * */
    public Result xmlFunction(String tablename, int position, String login, boolean admin) {

        logger.info("Пользователь: " + session().get("username") + " получает значения " + tablename);
        logiN = login;
        ArrayList<ArrayList<String>> stek = XMLMY.outPutAll(tablename);


        if (tablename.equals("reestr")) {

            return ok(indexXml.render("reestr",
                    JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                    JavaConverters.asScalaBuffer(stek.get(1)).toList(),
                    JavaConverters.asScalaBuffer(stek.get(2)).toList(),
                    position,
                    tablename,
                    login,admin
            ));
        } else {

            return ok(indexXml.render(tablename.toUpperCase(),
                    JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                    JavaConverters.asScalaBuffer(stek.get(1)).toList(),
                    null,
                    position,
                    tablename, login,admin));
        }

    }

}

