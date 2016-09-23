package org.agoncal.application.conference.attendee.data;

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
     * create table AcceptedTalk (id varchar(255) not null, language varchar(255), title varchar(255), primary key (id))
     * create table Speaker (id varchar(255) not null, avatarUrl varchar(255), bio varchar(5000), blog varchar(255), company varchar(255), firstName varchar(255), language varchar(3), lastName varchar(255), twitter varchar(255), primary key (id))
     * create table Speaker_AcceptedTalk (Speaker_id varchar(255) not null, acceptedTalks_id varchar(255) not null)
     */

    private static String attendeeCreateSQLStatement;
    private static Map<String, String> talksAlreadyExist = new HashMap<>();

    public static void main(String[] args) throws IOException {

        File file = Paths.get("src/test/resources/attendees.json").toFile();
        JsonReader rdr = Json.createReader(new FileReader(file.getAbsoluteFile()));

        JsonArray results = rdr.readArray();
        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            attendeeCreateSQLStatement = "INSERT INTO Attendee (id, firstName, lastName, company, twitter, avatarUrl) values (";

            attendeeCreateSQLStatement += "'" + getSqlValue(result, "uuid").substring(1, 38).concat("att") + "', ";
            attendeeCreateSQLStatement += getSqlValue(result, "firstName") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "lastName") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "company") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "twitter") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "avatarURL") + ", ";

            attendeeCreateSQLStatement += ");";
            System.out.println(attendeeCreateSQLStatement);
        }
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
