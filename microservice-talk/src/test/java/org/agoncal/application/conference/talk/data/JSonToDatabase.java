package org.agoncal.application.conference.talk.data;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static List<String> talkCreateSQLStatements;
    private static String speakerCreateSQLStatement;
    private static Map<String, String> speakersAlreadyExist = new HashMap<>();

    public static void main(String[] args) throws IOException {

        File file = Paths.get("src/test/resources/talks.json").toFile();
        JsonReader rdr = Json.createReader(new FileReader(file.getAbsoluteFile()));

        JsonArray results = rdr.readArray();
        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            talkCreateSQLStatements = new ArrayList<>();
            speakerCreateSQLStatement = "INSERT INTO Talk (id, title, language, talkType, track, summary) values (";

            speakerCreateSQLStatement += "'" + result.getString("id") + "', ";
            speakerCreateSQLStatement += getSqlValue(result, "title") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "lang") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "talkType") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "track") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "summary") + ", ";

            // JsonArray speakers = result.getJsonArray("speakers");
            // for (JsonObject link : speakers.getValuesAs(JsonObject.class)) {
            //     getSpeaker(result.getString("id"), link.getString("href"));
            // }

            speakerCreateSQLStatement += ");";
            System.out.println(speakerCreateSQLStatement);
            // for (String acceptedTalkCreateSQLStatement : talkCreateSQLStatements) {
            //     System.out.println(acceptedTalkCreateSQLStatement);
            // }
            // System.out.println("");
        }
    }

    private static void getSpeaker(String speakerUUID, String hrefSpeaker) throws IOException {
        URL url = new URL(hrefSpeaker);
        try (InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)) {

            JsonObject result = rdr.readObject();
            speakerCreateSQLStatement += getSqlValue(result, "lang") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "blog") + ", ";
            speakerCreateSQLStatement += getSqlValue(result, "bio");

            JsonArray acceptedTalks = result.getJsonArray("acceptedTalks");
            for (int i = 0; i < acceptedTalks.size(); i++) {
                JsonArray links = acceptedTalks.getJsonObject(i).getJsonArray("links");

                for (JsonObject link : links.getValuesAs(JsonObject.class)) {
                    String hrefTalk = link.getString("href");
                    if (hrefTalk.contains("/talks"))
                        talkCreateSQLStatements.addAll(getTalk(speakerUUID, hrefTalk));
                }
            }
        }
    }

    private static List<String> getTalk(String speakerUUID, String hrefTalk) throws IOException {
        List<String> sqlStatements = new ArrayList<>();
        URL url = new URL(hrefTalk);
        String acceptedTalkCreateSQLStatement;
        String joinTableCreateSQLStatement;
        try (InputStream is = url.openStream(); JsonReader rdr = Json.createReader(is)) {
            JsonObject result = rdr.readObject();

            if (!speakersAlreadyExist.containsKey(result.getString("id"))) {

                System.out.println(result.toString());

                speakersAlreadyExist.put(result.getString("id"), "exists");
                acceptedTalkCreateSQLStatement = "INSERT INTO AcceptedTalk (id, title, language) values (";
                acceptedTalkCreateSQLStatement += "'" + result.getString("id") + "', ";
                acceptedTalkCreateSQLStatement += getSqlValue(result, "title") + ", ";
                acceptedTalkCreateSQLStatement += getSqlValue(result, "lang");
                acceptedTalkCreateSQLStatement += ");";
                sqlStatements.add(acceptedTalkCreateSQLStatement);
            }

            joinTableCreateSQLStatement = "INSERT INTO Speaker_AcceptedTalk (Speaker_id, acceptedTalks_id) values (";
            joinTableCreateSQLStatement += "'" + speakerUUID + "', ";
            joinTableCreateSQLStatement += "'" + result.getString("id") + "', ";
            joinTableCreateSQLStatement += ");";
        }
        sqlStatements.add(joinTableCreateSQLStatement);
        return sqlStatements;
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
