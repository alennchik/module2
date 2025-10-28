package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        if (fetcher == null) throw new IllegalArgumentException("Fetcher cannot be null");
        this.delegate = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        // Count every delegate call (success or exception)
        callsMade++;

        List<String> result = delegate.getSubBreeds(breed); // may throw
        cache.put(breed, result);                            // cache only successes
        return result;
    }

    public int getCallsMade() { return callsMade; }
}
