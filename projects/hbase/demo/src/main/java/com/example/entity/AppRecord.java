package com.example.entity;
import lombok.Getter;
import lombok.Setter;
import java.lang.reflect.Field;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import java.io.IOException;
@Getter
@Setter
public class AppRecord {
    private String app_name;
    private String app_id;
    private String category;
    private String rating;
    private String rating_count;
    private String installs;
    private String minimum_installs;
    private String maximum_installs;
    private String free;
    private String price;
    private String currency;
    private String size;
    private String minimum_android;
    private String developer_id;
    private String developer_website;
    private String developer_email;
    private String released;
    private String privacy_policy;
    private String last_updated;
    private String content_rating;
    private String ad_supported;
    private String in_app_purchases;
    private String editor_choice;

    public static AppRecord parse(String line) throws IOException {
        CSVParser parser = new CSVParserBuilder()
        .withSeparator(',')
        .withQuoteChar('"')
        .withIgnoreQuotations(false)
        .build();
        AppRecord appRecord = new AppRecord();
        String[] fields = parser.parseLine(line);
        appRecord.setApp_name(fields[0]);
        appRecord.setApp_id(fields[1]);
        appRecord.setCategory(fields[2]);
        appRecord.setRating(fields[3]);
        appRecord.setRating_count(fields[4]);
        appRecord.setInstalls(fields[5]);
        appRecord.setMinimum_installs(fields[6]);
        appRecord.setMaximum_installs(fields[7]);
        appRecord.setFree(fields[8]);
        appRecord.setPrice(fields[9]);
        appRecord.setCurrency(fields[10]);
        appRecord.setSize(fields[11]);
        appRecord.setMinimum_android(fields[12]);
        appRecord.setDeveloper_id(fields[13]);
        appRecord.setDeveloper_website(fields[14]);
        appRecord.setDeveloper_email(fields[15]);
        appRecord.setReleased(fields[16]);
        appRecord.setLast_updated(fields[17]);
        appRecord.setContent_rating(fields[18]);
        appRecord.setPrivacy_policy(fields[19]);
        appRecord.setAd_supported(fields[20]);
        appRecord.setIn_app_purchases(fields[21]);
        appRecord.setEditor_choice(fields[22]);
        return appRecord;
    }
    public static void main(String[] args) {
        String csvLine = "Gakondo,com.ishakwe.gakondo,Adventure,0.0,0,10+,10,15,True,0,USD,10M,7.1 and up,Jean Confident Irénée NIYIZIBYOSE,https://beniyizibyose.tk/#/,jean21101999@gmail.com,\"Feb 26, 2020\",\"Feb 26, 2020\",Everyone,https://beniyizibyose.tk/projects/,False,False,False,2021-06-15 20:19:35";
        try {
            AppRecord appRecord = AppRecord.parse(csvLine);
            printFields(appRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void printFields(Object obj) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println(field.getName() + ": " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}