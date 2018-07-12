package models;

import play.data.validation.Constraints;

/**
 * Class for updating user data (password, region, administrator rights)
 */
public class UpdateForm {
    @Constraints.Required
    private String password;

    @Constraints.Required
    private Integer region;

    @Override
    public String toString() {
        return "UpdateForm{" +
                "password='" + password + '\'' +
                ", region=" + region +
                ", isAdmin=" + isAdmin +
                '}';
    }
    
    private boolean isAdmin;

    public UpdateForm(@Constraints.Required String password, @Constraints.Required Integer region, @Constraints.Required Boolean isAdmin) {
        this.password = password;
        this.region = region;
        this.isAdmin = isAdmin;
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

    public UpdateForm(){}
}
