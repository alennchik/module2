package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String breed = "hound";
        BreedFetcher fetcher = new CachingBreedFetcher(new BreedFetcherForLocalTesting());

        int result = getNumberOfSubBreeds(breed, fetcher);
        System.out.println(breed + " has " + result + " sub breeds");

        breed = "cat";
        result = getNumberOfSubBreeds(breed, fetcher);
        System.out.println(breed + " has " + result + " sub breeds");
    }

    /** Returns number of sub-breeds; 0 if list is null or breed not found. */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher breedFetcher) {
        try {
            List<String> subs = breedFetcher.getSubBreeds(breed);
            return (subs == null) ? 0 : subs.size();
        } catch (BreedFetcher.BreedNotFoundException e) {
            return 0;
        }
    }
}

