package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (breed == null || breed.isEmpty()) {
            throw new IllegalArgumentException("Breed must not be null or empty");
        }

        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new BreedNotFoundException("Empty response from API");
            }

            String body = response.body().string();
            JSONObject json = new JSONObject(body);
            String status = json.getString("status");

            if ("error".equals(status)) {
                throw new BreedNotFoundException(json.getString("message"));
            }

            JSONArray arr = json.getJSONArray("message");
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                subBreeds.add(arr.getString(i));
            }
            return subBreeds;

        } catch (IOException e) {
            throw new BreedNotFoundException("API request failed: " + e.getMessage());
        } catch (Exception e) {
            throw new BreedNotFoundException("Failed to process API response: " + e.getMessage());
        }
    }
}
