package org.agoncal.application.conference.talk.data;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class JSonToDatabase {

    /**
     * create table Talk (id varchar(255) not null, language varchar(255), summary varchar(255), talkType varchar(255), title varchar(255), track varchar(255), primary key (id))
     * create table Speaker (id varchar(255) not null, name varchar(255), primary key (id))
     * create table Talk_Speaker (Talk_id varchar(255) not null, speakers_id varchar(255) not null)
     */

    private static String talkCreateSQLStatement;
    private static String speakerCreateSQLStatement;
    private static String joinTableCreateSQLStatement;
    private static Map<String, String> speakersAlreadyExist = new HashMap<>();

    public static void main(String[] args) throws IOException {

        File file = Paths.get("src/test/resources/talks.json").toFile();
        JsonReader rdr = Json.createReader(new FileReader(file.getAbsoluteFile()));

        JsonArray results = rdr.readArray();
        for (JsonObject talk : results.getValuesAs(JsonObject.class)) {

            talkCreateSQLStatement = "INSERT INTO Talk (id, title, language, talkType, track, summary) values (";
            talkCreateSQLStatement += "'" + talk.getString("id") + "', ";
            talkCreateSQLStatement += getSqlValue(talk, "title") + ", ";
            talkCreateSQLStatement += getSqlValue(talk, "lang") + ", ";
            talkCreateSQLStatement += getSqlValue(talk, "talkType") + ", ";
            talkCreateSQLStatement += getSqlValue(talk, "track") + ", ";
            talkCreateSQLStatement += getSqlValue(talk, "summary") + ", ";
            talkCreateSQLStatement += ");";

            System.out.println(talkCreateSQLStatement);

            JsonArray speakers = talk.getJsonArray("speakers");
            for (JsonObject speaker : speakers.getValuesAs(JsonObject.class)) {

                if (!speakersAlreadyExist.containsKey(speaker.getJsonObject("link").getString("href"))) {

                    speakersAlreadyExist.put(speaker.getJsonObject("link").getString("href"), "exists");
                    speakerCreateSQLStatement = "INSERT INTO Speaker (id, name) values (";
                    speakerCreateSQLStatement += "'" + getId(speaker.getJsonObject("link").getString("href")) + "', ";
                    speakerCreateSQLStatement += getSqlValue(speaker, "name");
                    speakerCreateSQLStatement += ");";

                    System.out.println(speakerCreateSQLStatement);
                }

                joinTableCreateSQLStatement = "INSERT INTO Talk_Speaker (Talk_id, speakers_id) values (";
                joinTableCreateSQLStatement += "'" + talk.getString("id") + "', ";
                joinTableCreateSQLStatement += "'" + getId(speaker.getJsonObject("link").getString("href")+ "'");
                joinTableCreateSQLStatement += ");";

                System.out.println(joinTableCreateSQLStatement);
            }
            System.out.println("");
        }
    }

    private static String getId(String href) {
        return href.substring(href.lastIndexOf('/')+1, href.length());
    }

    private static String getSqlValue(JsonObject jsonObject, String key) {
        try {
            String value = jsonObject.getString(key);
            if (value == null) {
                return "null";
            } else {
                return "'" + value.replaceAll("[\\t\\r\\n\\-\\+\\.\\^:,'“”]", " ").trim() + "'";
            }
        } catch (Exception e) {
            return "null";
        }
    }
}
