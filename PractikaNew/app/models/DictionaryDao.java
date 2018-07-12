package models;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.w3c.dom.Document;
import play.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import java.io.*;
import java.util.List;
import java.util.Set;
/**The class for generating XML structure from table*/
/**
 * Class to retrieve information from the database and package it into an xml file
 */
public class DictionaryDao {

    private static Logger.ALogger logger = Logger.of(DictionaryDao.class);

    /**
     * Forming and sending the structure of the directory in .xml format
     * @param tablename it is tablename
     * @return string result
     */
    public static String getTableStructureXML(String tablename) {
        logger.info("Формирование и отправка структуры справочника " + tablename + " в формате .xml");
        String result = "";
        Document doc = null;
        try {
            StringWriter stringWriter = new StringWriter();

            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);

//            String q = "SELECT c.column_name, c.data_type, pgd.description " +
//                    "from pg_catalog.pg_statio_all_tables as st " +
//                    "inner join pg_catalog.pg_description pgd on (pgd.objoid=st.relid) " +
//                    "right outer join information_schema.columns c on (pgd.objsubid=c.ordinal_position and  c.table_schema=st.schemaname and c.table_name=st.relname) " +
//                    "where table_schema = 'dictionary' and table_name = '" + tablename+ "'";
            String q = " SELECT upper(c.table_schema::text) AS owner, " +
                    "    upper(c.table_name::text) AS table_name, " +
                    "    upper(c.column_name::text) AS column_name, " +
                    "    pgd.description, " +
                    "    c.data_type, " +
                    "    c.character_maximum_length AS char_length, " +
                    "    c.numeric_precision AS data_precision, " +
                    "    c.numeric_scale AS data_scale " +
                    "   FROM pg_statio_all_tables st " +
                    "     JOIN pg_description pgd ON pgd.objoid = st.relid " +
                    "     JOIN information_schema.columns c ON pgd.objsubid = c.ordinal_position::integer AND c.table_schema::name = st.schemaname AND c.table_name::name = st.relname " +
                    "     where table_schema = 'dictionary' and table_name = '" + tablename +"'";
            SqlQuery query = Ebean.createSqlQuery(q);
            List<SqlRow> rows = query.findList();
            if(rows.isEmpty()){
                return "table doesnt exist";
            }
            xMLStreamWriter.writeStartDocument();

            xMLStreamWriter.writeStartElement("Structure");


            for (SqlRow row : rows) {
                xMLStreamWriter.writeStartElement("column");
                Set<String> keyset = row.keySet();
                for (String s : keyset) {
                    xMLStreamWriter.writeStartElement(s);
                    if(row.getString(s) == null) {
                        xMLStreamWriter.writeCharacters(" ");
                    }else {
                        xMLStreamWriter.writeCharacters(row.getString(s));
                    }
                    xMLStreamWriter.writeEndElement();
                }
                xMLStreamWriter.writeEndElement();

            }
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();

            result = stringWriter.getBuffer().toString();

            stringWriter.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }

    /**
     * Forming and sending the contents of the directory in xml format
     * @param tablename it is tablename
     * @return string result
     */
    public static String getTableValues(String tablename) {
        logger.info("Формирование и отправка содержимого справочника " + tablename + " в формате .xml");
        String result = "";
        try {
            StringWriter stringWriter = new StringWriter();
            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xMLStreamWriter =
                    xMLOutputFactory.createXMLStreamWriter(stringWriter);


            SqlQuery query = Ebean.createSqlQuery("select * from dictionary." + tablename);
            List<SqlRow> rows = query.findList();
            System.out.println(rows);
            if(rows.isEmpty()){
                return "table is empty or doesnt exist";
            }
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement(tablename);


            for (SqlRow row : rows) {
                xMLStreamWriter.writeStartElement("row");
                Set<String> keyset = row.keySet();
                for (String s : keyset) {
                    xMLStreamWriter.writeStartElement(s);
                    if(row.getString(s) == null) {
                        xMLStreamWriter.writeCharacters(" ");
                    }else {
                        xMLStreamWriter.writeCharacters(row.getString(s));
                    }
                    xMLStreamWriter.writeEndElement();
                }
                xMLStreamWriter.writeEndElement();
            }
            xMLStreamWriter.writeEndDocument();
            xMLStreamWriter.flush();
            xMLStreamWriter.close();

            result = stringWriter.getBuffer().toString();
            stringWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "table is empty or doesnt exist";
        }
        return result;
    }

}
