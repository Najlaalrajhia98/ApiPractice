package org.example;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

  /**
  The code is a basic implementation
   of the Google Maps Distance Matrix API that calculates the duration of travel between two locations.
    The code makes use of the Gson library to parse JSON data and the okhttp3 library to make HTTP requests.
  */
public class Main {
    private static final String FILE_NAME = "gmap_distance_matrix_response.json";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter pickup point (address or coordinates): ");
        String pickup = sc.nextLine();
        System.out.print("Enter dropoff point (address or coordinates): ");
        String dropoff = sc.nextLine();

            // A class that is used to create the URL for the API request
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/distancematrix/json").newBuilder();

            // The base URl and various query parameters added to it
            urlBuilder.addQueryParameter("origins", pickup);
            urlBuilder.addQueryParameter("destinations", dropoff);
            urlBuilder.addQueryParameter("units", "imperial");

            // The API KEY is accessing the Google Maps Distance Matrix API
            urlBuilder.addQueryParameter("key", "THE API KEY");

            // An instance of OkHttpClient is created to handle the HTTP request.
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()

                    // url method of the request is to set the url for the api request
                    .url(urlBuilder.build())
                    .get() // to set the HTTP method of the request to GET
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println(responseBody);
                    writeToFile(responseBody);
                    fileReader();
                } else {
                    System.out.println("An error occurred while fetching the response. Error code: " + response.code());
                }
            } catch (IOException e) {
                System.out.println("An error occurred while fetching the response: " + e.getMessage());
            }
            // fileReader();
        }

      /**
       * @fileReader() This method reads the file that was written by the writeToFile method,
       * parses the JSON data using Gson and extracts the duration of travel from the response
       */
    public static void fileReader() {
        try {
            FileReader reader = new FileReader(FILE_NAME);
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<>();
            map = (Map<String, Object>) gson.fromJson(reader, map.getClass());

            // casting the row/elements keys which considered as a  list of hashmap
            Map<String, Object> rows = (Map<String, Object>) ((List<Map<String, Object>>) map.get("rows")).get(0);
            Map<String, Object> elements = (Map<String, Object>) ((List<Map<String, Object>>) rows.get("elements")).get(0);
            Map<String, Object> duration = (Map<String, Object>) elements.get("duration");

            System.out.println("Duration: " + duration.get("text"));

            // exception handling for potential errors that may occur during the HTTP request and file reading.
        } catch (FileNotFoundException e) {
            System.out.println("The file " + FILE_NAME + " could not be found: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("No data found in the response file.");
        }
    }
      /**
       *
       * @param response This method writes the response from the Google Maps API to a file in JSON format.
       * @throws IOException
       */
    public static void writeToFile(String response) throws IOException {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map = gson.fromJson(response, map.getClass());
        FileWriter writer = new FileWriter(FILE_NAME);
        gson.toJson(map, writer);
        writer.flush();
        writer.close();
    }
}