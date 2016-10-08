package org.agoncal.application.conference.attendee.data;

import org.agoncal.application.conference.commons.security.PasswordUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

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
            attendeeCreateSQLStatement = "INSERT INTO Attendee (id, login, firstName, lastName, password, company, twitter, avatarUrl) values (";
            String login = getLogin(result);

            attendeeCreateSQLStatement += "'" + getSqlValue(result, "uuid").substring(1, 38).concat("att") + "', ";
            attendeeCreateSQLStatement += "'" + login + "', ";
            attendeeCreateSQLStatement += getSqlValue(result, "firstName") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "lastName") + ", ";
            attendeeCreateSQLStatement += "'" + PasswordUtils.digestPassword(login) + "', ";
            attendeeCreateSQLStatement += getSqlValue(result, "company") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "twitter") + ", ";
            attendeeCreateSQLStatement += getSqlValue(result, "avatarURL") + ", ";

            attendeeCreateSQLStatement += ");";
            System.out.println(attendeeCreateSQLStatement);
        }
    }

    private static String getLogin(JsonObject result) {
        String firstName = result.getString("firstName");
        String lastName = result.getString("lastName");
        String name = firstName.trim().toLowerCase().concat(lastName.trim().toLowerCase()).replaceAll("[ \\t\\r\\n\\-\\+\\.\\^:,'“”šäöéá]", "");
        return name.substring(0, name.length() > 10 ? 10 : name.length());
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
