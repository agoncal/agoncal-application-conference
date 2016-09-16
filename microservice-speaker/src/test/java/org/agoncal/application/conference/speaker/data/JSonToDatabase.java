package org.agoncal.application.conference.speaker.data;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class JSonToDatabase {

    /**
     * create table acceptedTalks (Speaker_id varchar(255) not null, acceptedTalkId varchar(255) not null)
     * create table Speaker (id varchar(255) not null, avatarUrl varchar(255), bio varchar(5000), blog varchar(255), company varchar(255), email varchar(255), firstName varchar(255), language varchar(3), name varchar(255), twitter varchar(255), primary key (id))
     */

    public static void main(String[] args) throws IOException {

        File file = Paths.get("src/test/resources/speakers.json").toFile();
        JsonReader rdr = Json.createReader(new FileReader(file.getAbsoluteFile()));

        JsonArray results = rdr.readArray();
        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            System.out.print("INSERT INTO Speaker (id, firstName, lastName, company, twitter, avatarUrl, language, blog, bio) values (");

            System.out.print(getSqlValue(result, "uuid") + ", ");
            System.out.print(getSqlValue(result, "firstName") + ", ");
            System.out.print(getSqlValue(result, "lastName") + ", ");
            System.out.print(getSqlValue(result, "company") + ", ");
            System.out.print(getSqlValue(result, "twitter") + ", ");
            System.out.print(getSqlValue(result, "avatarURL") + ", ");

            JsonArray links = result.getJsonArray("links");
            for (JsonObject link : links.getValuesAs(JsonObject.class)) {
                getSpeaker(link.getString("href"));
            }

            System.out.println(");");
        }
    }

    private static String getSqlValue(JsonObject jsonObject, String key) {
        try {
            String value = jsonObject.getString(key);
            if (value == null) {
                return "null";
            } else {
                return "'" + value.replaceAll("[\\t\\r\\n\\-\\+\\.\\^:,']"," ") + "'";
            }
        } catch (Exception e) {
            return "null";
        }
    }

    private static void getSpeaker(String href) throws IOException {
        URL url = new URL(href);
        try (InputStream is = url.openStream();
             JsonReader rdr = Json.createReader(is)) {

            JsonObject result = rdr.readObject();
            System.out.print(getSqlValue(result, "lang") + ", ");
            System.out.print(getSqlValue(result, "blog") + ", ");
            System.out.print(getSqlValue(result, "bio"));
        }
    }
}
