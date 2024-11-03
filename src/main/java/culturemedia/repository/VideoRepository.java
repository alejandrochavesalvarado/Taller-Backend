package culturemedia.repository;

import culturemedia.model.Video;

import java.util.List;

public interface VideoRepository {
    List<Video> findAll();
    Video save(Video video);
    List<Video> findByTitle(String title);
    List<Video> findByDuration(Double fromDuration, Double toDuration);
}
