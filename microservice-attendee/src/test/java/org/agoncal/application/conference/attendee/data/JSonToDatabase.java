package org.agoncal.application.conference.attendee.data;

import org.agoncal.application.conference.commons.jwt.PasswordUtils;

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
            attendeeCreateSQLStatement = "INSERT INTO A_Attendee (id, login, firstName, lastName, password, company, twitter, avatarUrl) values (";
            String login = getLogin(result);

            attendeeCreateSQLStatement += "'" + getFormattedValue(result, "uuid").substring(1, 38).concat("att") + "', ";
            attendeeCreateSQLStatement += "'" + login + "', ";
            attendeeCreateSQLStatement += getFormattedValue(result, "firstName") + ", ";
            attendeeCreateSQLStatement += getFormattedValue(result, "lastName") + ", ";
            attendeeCreateSQLStatement += "'" + PasswordUtils.digestPassword(login) + "', ";
            attendeeCreateSQLStatement += getFormattedValue(result, "company") + ", ";
            attendeeCreateSQLStatement += getFormattedValue(result, "twitter") + ", ";
            attendeeCreateSQLStatement += getValue(result, "avatarURL");

            attendeeCreateSQLStatement += ");";
            System.out.println(attendeeCreateSQLStatement);
        }
    }

    private static String getLogin(JsonObject result) {
        String firstName = result.getString("firstName");
        String lastName = result.getString("lastName");

        switch (firstName.concat(lastName)) {
            case "AntonioGoncalves":
                return "agoncal";
            case "SebastienPertus":
                return "spertus";
            default:
                String name = firstName.trim().toLowerCase().concat(lastName.trim().toLowerCase()).replaceAll("[ \\t\\r\\n\\-\\+\\.\\^:,'“”šäöéá]", "");
                return name.substring(0, name.length() > 10 ? 10 : name.length());
        }
    }

    private static String getFormattedValue(JsonObject jsonObject, String key) {
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

    private static String getValue(JsonObject jsonObject, String key) {
        try {
            String value = jsonObject.getString(key);
            if (value == null) {
                return "null";
            } else {
                return "'" + value.trim() + "'";
            }
        } catch (Exception e) {
            return "null";
        }
    }
}
