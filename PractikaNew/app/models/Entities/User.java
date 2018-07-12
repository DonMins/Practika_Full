package models.Entities;


import io.ebean.Ebean;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;

import java.util.List;

import java.util.ArrayList;

@Constraints.Validate
@Entity
@Table(name = "users", schema = "dictionary")
/**
 * Class c user parameters (login, password, region, id, admin rights)
 */
public class User implements Constraints.Validatable<String> {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "dictionary.user_id_seq")
    private Integer id;

    @Constraints.Required
    private String login;
    @Constraints.Required
    private String password;
    @Constraints.Required
    private Integer region;

    @Column(name = "is_admin")
    private boolean isAdmin;
    public User(){}


    public User(Integer id, String login, String password, Integer region, Boolean isAdmin){
        this.id = id;
        this.login = login;
        this.password = password;
        this.region = region;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", region=" + region +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public ArrayList<String> getNameColomn(){
        ArrayList<String > nameColomn = new ArrayList<>();
        nameColomn.add("Id");
        nameColomn.add("Логин");
        nameColomn.add("Пароль");
        nameColomn.add("Регион");
        nameColomn.add("Учетная запись админа");

        return nameColomn;
    }

    public static Finder<Integer, User> find = new Finder<>(User.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String validate() {
        List<User> users = Ebean.find(User.class).where().eq("login", login).findList();
        if(users.isEmpty()){
            return null;
        }
        return "Пользователь с таким логином уже существует";
    }
}
