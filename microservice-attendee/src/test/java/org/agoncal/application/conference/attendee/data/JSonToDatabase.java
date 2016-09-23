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

    private static String attendeeCreateSQLStatement;

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
