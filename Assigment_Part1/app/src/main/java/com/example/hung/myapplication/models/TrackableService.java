package com.example.hung.myapplication.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.example.hung.myapplication.R;
import com.example.hung.myapplication.database.TrackableTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Nguyen My
 */

public class TrackableService {

    private static final String LOG_TAG = TrackingService.class.getName();
    private static Context context;
    private List<TrackableInfo> TrackableList = new ArrayList<>();

    private TrackableService(){

    }

    public static class TrackableInfo {

        public int id;
        public String name;
        public String description;
        public String url;
        public String category;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ContentValues toValues() {
            ContentValues values = new ContentValues(5);

            values.put(TrackableTable.COLUMN_ID,id);
            values.put(TrackableTable.COLUMN_NAME,name);
            values.put(TrackableTable.COLUMN_DESCRITION,description);
            values.put(TrackableTable.COLUMN_URL,url);
            values.put(TrackableTable.COLUMN_CATEGORY,category);
            return values;
        }


    }

    private static class LazyHolder {

        static final TrackableService INSTANCE = new TrackableService();

    }

    // PUBLIC METHODS

    // singleton
    // thread safe lazy initialisation: see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    public static TrackableService getSingletonInstance(Context context) {

        TrackableService.context = context;
        return TrackableService.LazyHolder.INSTANCE;

    }

    private void parseFile(Context context) {

        TrackableList.clear();
        // resource reference to tracking_data.txt in res/raw/ folder of your project
        // supports trailing comments with //
        try (Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.food_truck_data)))
        {
            // match comma and 0 or more whitespace OR trailing space and newline
            scanner.useDelimiter(",\"|\",\"|\"\\n+");
            while (scanner.hasNext())
            {
                //create new trackableinfo object and add all element which are read from file.
                TrackableInfo newtrackablelist = new TrackableInfo();
                newtrackablelist.id = scanner.nextInt();
                newtrackablelist.name = String.valueOf(scanner.next());
                newtrackablelist.description = String.valueOf(scanner.next());
                newtrackablelist.url = String.valueOf(scanner.next());
                newtrackablelist.category = String.valueOf(scanner.next());
                TrackableList.add(newtrackablelist);
            }

        }
        catch (Resources.NotFoundException e)
        {
            Log.i(LOG_TAG, "File Not Found Exception Caught");
        }

    }

    public void logAll() {

        parseFile(context);

    }


    public List<TrackableInfo> getTrackableList() {
        return TrackableList;
    }
}
