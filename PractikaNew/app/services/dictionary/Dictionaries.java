package services.dictionary;

import javax.jws.WebService;

@WebService
public interface Dictionaries {
    String getTableStructure(String tablename);
    String getTableValues(String tablename);
//    String getReestrStructure();
//    String getReestr();
//    String getDoctorsStructure();
//    String getDoctors();
//    String getF002Structure();
//    String getF002();
//    String getIntervStructure();
//    String getInterv();
//    String getIntervKhStructure();
//    String getIntervKh();


}
