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
     * create table Room (id)
     * create table Talk (id, talkType, title, track)
     * create table Speaker (id, name)
     * create table Talk_Speaker (Talk_id, speakers_id)
     * create table Schedule (id, day, fromTime, fromTimeMillis, isaBreak, notAllocated, toTime, toTimeMillis, room_id, talk_id)
     */

    private static String roomCreateSQLStatement;
    private static String talkCreateSQLStatement;
    private static String speakerCreateSQLStatement;
    private static String joinTableCreateSQLStatement;
    private static String scheduleTableCreateSQLStatement;

    private static Map<String, String> roomAlreadyExist = new HashMap<>();
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

        JsonArray slots = rdr.readObject().getJsonArray("slots");
        for (JsonObject slot : slots.getValuesAs(JsonObject.class)) {

            if (!roomAlreadyExist.containsKey(slot.getString("roomId"))) {

                roomAlreadyExist.put(slot.getString("roomId"), "exists");
                roomCreateSQLStatement = "INSERT INTO Room (id) values (";
                roomCreateSQLStatement += "'" + slot.getString("roomId") + "'";
                roomCreateSQLStatement += ");";

                System.out.println(roomCreateSQLStatement);
            }

            if (slot.containsKey("talk") && !slot.get("talk").toString().equals("null")) {
                JsonObject talk = slot.getJsonObject("talk");

                talkCreateSQLStatement = "INSERT INTO Talk (id, talkType, title, track) values (";
                talkCreateSQLStatement += "'" + talk.getString("id") + "', ";
                talkCreateSQLStatement += getSqlValue(slot, "talkType") + ", ";
                talkCreateSQLStatement += getSqlValue(slot, "title") + ", ";
                talkCreateSQLStatement += getSqlValue(slot, "track");
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
            }

            scheduleTableCreateSQLStatement = "INSERT INTO Schedule (id, day, fromTime, fromTimeMillis, isaBreak, talk_id, notAllocated, toTime, toTimeMillis, room_id) values (";
            scheduleTableCreateSQLStatement += "'" + slot.getString("slotId") + "', ";
            scheduleTableCreateSQLStatement += getSqlValue(slot, "day") + ", ";
            scheduleTableCreateSQLStatement += "'" + slot.getString("fromTime") + "', ";
            scheduleTableCreateSQLStatement += slot.getInt("fromTimeMillis") + ", ";

            if (slot.containsKey("break") && slot.get("break").toString().equals("null"))
                scheduleTableCreateSQLStatement += "false, ";
            else
                scheduleTableCreateSQLStatement += "true, ";

            if (slot.containsKey("talk") && slot.get("talk").toString().equals("null"))
                scheduleTableCreateSQLStatement += "null, ";
            else
                scheduleTableCreateSQLStatement += "'" + slot.getJsonObject("talk").getString("id") + "', ";

            scheduleTableCreateSQLStatement += slot.getBoolean("notAllocated") + ", ";
            scheduleTableCreateSQLStatement += "'" + slot.getString("toTime") + "', ";
            scheduleTableCreateSQLStatement += slot.getInt("toTimeMillis") + ", ";
            scheduleTableCreateSQLStatement += "'" + slot.getString("roomId") + "'";
            scheduleTableCreateSQLStatement += ");";

            System.out.println(scheduleTableCreateSQLStatement);
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
