package org.example;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        FileReader reader = new FileReader("gmap_distance_matrix_response.json");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(reader, map.getClass());

        Map<String, Object> rows = (Map<String, Object>) ((List<Map<String, Object>>) map.get("rows")).get(0);
        Map<String, Object> elements = (Map<String, Object>) ((List<Map<String, Object>>) rows.get("elements")).get(0);
        Map<String, Object> duration = (Map<String, Object>) elements.get("duration");

        System.out.println("Duration: " + duration.get("text"));
    }
}
