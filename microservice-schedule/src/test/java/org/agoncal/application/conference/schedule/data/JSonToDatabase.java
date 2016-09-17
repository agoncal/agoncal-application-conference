package org.agoncal.application.conference.schedule.data;

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
     * create table Room (id varchar(255) not null, primary key (id))
     * create table Schedule (id varchar(255) not null, day varchar(255), fromTime varchar(255), fromTimeMillis bigint, isaBreak boolean, notAllocated boolean, toTime varchar(255), toTimeMillis bigint, room_id varchar(255), talk_id varchar(255), primary key (id))
     * create table Speaker (id varchar(255) not null, name varchar(255), primary key (id))
     * create table Talk (id varchar(255) not null, talkType varchar(255), title varchar(255), track varchar(255), primary key (id))
     * create table Talk_Speaker (Talk_id varchar(255) not null, speakers_id varchar(255) not null)
     */

    private static String talkCreateSQLStatement;
    private static String speakerCreateSQLStatement;
    private static String joinTableCreateSQLStatement;
    private static Map<String, String> speakersAlreadyExist = new HashMap<>();

    public static void main(String[] args) throws IOException {

        parse(Paths.get("src/test/resources/monday.json").toFile());
        parse(Paths.get("src/test/resources/tuesday.json").toFile());
        parse(Paths.get("src/test/resources/wednesday.json").toFile());
        parse(Paths.get("src/test/resources/thursday.json").toFile());
        parse(Paths.get("src/test/resources/friday.json").toFile());
    }

    public static void parse(File file) throws IOException {

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
                joinTableCreateSQLStatement += "'" + getId(speaker.getJsonObject("link").getString("href") + "'");
                joinTableCreateSQLStatement += ");";

                System.out.println(joinTableCreateSQLStatement);
            }
            System.out.println("");
        }
    }

    private static String getId(String href) {
        return href.substring(href.lastIndexOf('/') + 1, href.length());
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
