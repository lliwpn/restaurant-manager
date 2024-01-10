package persistence;

/*
Description: template reference of writable interface
Author: CPSC210 Team / Paul Carter
Date: October 16th, 2021
URL: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
 */

import org.json.JSONObject;

public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();

}
