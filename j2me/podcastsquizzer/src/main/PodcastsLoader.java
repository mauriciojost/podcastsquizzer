package main;

public interface PodcastsLoader {
    public final String NEXT = "Next";
    public final String PREVIOUS = "Previous";
    public void loadPodcast(String command);
}
