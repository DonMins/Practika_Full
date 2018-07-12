package models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *Class for getting information from xml file
 * */

public class XMLMY {

    private static play.Logger.ALogger logger = play.Logger.of(XMLMY.class);
/**
 * Method for obtaining the table structure
 * @param tablename it is tablename
 * @return ArrayList<ArrayList<String>>
 *     The first ArrayList contains the name of the columns, the second data for these columns
 * */
    public static ArrayList<ArrayList<String>> outputStuctureOrValues(String tablename) {
        logger.info("Формирование структуры таблицы " + tablename + " из xml ");

        ArrayList<ArrayList<String>> stekAll = new ArrayList<>();
        ArrayList<String> stekColomn = new ArrayList<>();
        ArrayList<String> stekData = new ArrayList<>();
        String resStruc = DictionaryDao.getTableStructureXML(tablename);
        String coloun;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(resStruc.getBytes()));
            doc.getDocumentElement().normalize();

            Node root = doc.getDocumentElement();

            NodeList books = root.getChildNodes();

            for (int i = 0; i < books.getLength(); i++) {

                Node book = books.item(i);

                // Если нода не текст, то это кн
                // ига - заходим внутрь
                if (book.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = book.getChildNodes();
                    for (int j = 0; j < bookProps.getLength(); j++) {
                        Node bookProp = bookProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            stekData.add(bookProp.getChildNodes().item(0).getTextContent());

                            if (i < 1) {
                                coloun = bookProp.getNodeName();
                                stekColomn.add(coloun);

                            }

                        }
                    }
                }
            }
            stekAll.add(stekColomn);
            stekAll.add(stekData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return stekAll;

    }
    /**
     * Method for obtaining data by table name
     * @param tablename it is tablename
     * @return ArrayList<ArrayList<String>>
     *
     * */
    public  static ArrayList<ArrayList<String>> outPutAll(String tablename) {
        ArrayList<ArrayList<String>> stekAll = new ArrayList<>();
        ArrayList<String> stekColomn = new ArrayList<>();
        ArrayList<String> stekData = new ArrayList<>();
        ArrayList<String> stekCode= new ArrayList<>();

        String resStruc = DictionaryDao.getTableStructureXML(tablename);

        String resVal = DictionaryDao.getTableValues(tablename);

        String coloun = "";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new ByteArrayInputStream(resVal.getBytes()));
            Document doc2 = dBuilder.parse(new ByteArrayInputStream(resStruc.getBytes()));

            doc.getDocumentElement().normalize();
            doc2.getDocumentElement().normalize();

            Node root = doc.getDocumentElement();

            NodeList books = root.getChildNodes();
            boolean flag = true;
            boolean flag2 = true;
            int count =0;
            int count2 =0;

            for (int i = 0; i < books.getLength(); i++) {

                Node book = books.item(i);

                // Если нода не текст, то это книга - заходим внутрь
                if (book.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = book.getChildNodes();
                    if (tablename.equals("reestr")){
                        count = bookProps.getLength()-1;
                    }
                    else {
                        count = bookProps.getLength();
                    }
                    for (int j = 0; j < count; j++) {
                        Node bookProp = bookProps.item(j);
                        // Если нода не текст, то это один из параметров книги - печатаем
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            stekData.add(bookProp.getChildNodes().item(0).getTextContent());


                            if (flag) {
                                NodeList nList = doc2.getElementsByTagName("column");
                                if (tablename.equals("reestr")){
                                    count2 = nList.getLength()-1;
                                }
                                else {
                                    count2 = nList.getLength();
                                }

                                for (int k = 0; k < count2; k++) {
                                    Node book2 = nList.item(k);
                                    if (book2.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eElement = (Element) book2;
                                        coloun = eElement
                                                .getElementsByTagName("description")
                                                .item(0)
                                                .getTextContent();

                                        stekColomn.add(coloun);
                                    }
                                    flag = false;
                                }

                            }

                            if ((flag2)&&(tablename.equals("reestr"))) {
                                NodeList nList = doc.getElementsByTagName("row");
                                if (nList!=null) {
                                    for (int k = 0; k < nList.getLength(); k++) {
                                        Node book2 = nList.item(k);
                                        if (book2.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement = (Element) book2;
                                            coloun = eElement
                                                    .getElementsByTagName("code")
                                                    .item(0)
                                                    .getTextContent();

                                            stekCode.add(coloun.toLowerCase());
                                        }
                                        flag2 = false;
                                    }
                                }


                            }
                            else {stekCode.add(" ");}

                        }
                    }
                }
            }
            for (int i = 0;i<stekColomn.size();i++){
                System.out.println(stekColomn.get(i));
            }
            stekAll.add(stekColomn);
            stekAll.add(stekData);
            stekAll.add(stekCode);

            return stekAll;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}

