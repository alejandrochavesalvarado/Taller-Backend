package culturemedia.service;

import culturemedia.exception.VideoNotFoundException;
import culturemedia.model.Video;
import culturemedia.model.View;
import culturemedia.repository.VideoRepository;
import culturemedia.repository.ViewsRepository;
import culturemedia.service.CultureMediaService;
import culturemedia.service.impl.CultureMediaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CultureMediaServiceTest {
    private CultureMediaService cultureMediaService;
    private VideoRepository videoRepository;
    private ViewsRepository viewsRepository;
    @BeforeEach
    void init() {
        videoRepository = Mockito.mock(VideoRepository.class);
        viewsRepository = Mockito.mock(ViewsRepository.class);
        cultureMediaService = new CultureMediaServiceImpl(videoRepository, viewsRepository);
    }
    @Test
    void when_FindAll_all_videos_should_be_returned_successfully() throws VideoNotFoundException {
        List<Video> videos = List.of(
                new Video("01", "Título 1", "Descripción 1", 1.5),
                new Video("02", "Título 2", "Descripción 2", 5.5)
        );
        when(videoRepository.findAll()).thenReturn(videos);
        List<Video> result = cultureMediaService.findAll();
        assertEquals(2, result.size());
        assertEquals("Título 1", result.get(0).title());
    }
    @Test
    void when_FindAll_does_not_find_any_video_an_VideoNotFoundException_should_be_thrown_successfully() throws VideoNotFoundException {
        when(videoRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(VideoNotFoundException.class, () -> cultureMediaService.findAll());
    }
    @Test
    void when_findByTitle_returns_the_videos_successfully() throws VideoNotFoundException {
        String title = "Título 3";
        List<Video> videos = List.of(new Video("03", title, "Descripción 3", 4.4));
        when(videoRepository.findByTitle(title)).thenReturn(videos);
        List<Video> result = cultureMediaService.findByTitle(title);
        assertEquals(1, result.size());
        assertEquals("03", result.get(0).code());
    }
    @Test
    void when_findByTitle_throws_the_exception_successfully() {
        when(videoRepository.findByTitle("non_existent_title")).thenReturn(Collections.emptyList());
        assertThrows(VideoNotFoundException.class, () -> cultureMediaService.findByTitle("non_existent_title"));
    }
    @Test
    void when_findByDuration_returns_the_videos_successfully() throws VideoNotFoundException {
        List<Video> videos = List.of(
                new Video("01", "Título 1", "Descripción 1", 1.5),
                new Video("03", "Título 3", "Descripción 3", 4.4)
        );
        when(videoRepository.findByDuration(1.0, 4.5)).thenReturn(videos);
        List<Video> result = cultureMediaService.findByDuration(1.0, 4.5);
        assertEquals(2, result.size());
        assertEquals("Título 1", result.get(0).title());
    }
    @Test
    void when_findByDuration_throws_the_exception_successfully() {
        when(videoRepository.findByDuration(0.0, 0.5)).thenReturn(Collections.emptyList());
        assertThrows(VideoNotFoundException.class, () -> cultureMediaService.findByDuration(0.0, 0.5));
    }
    @Test
    void when_saveVideo_should_return_saved_video() {
        Video video = new Video("07", "Nuevo Título", "Nueva Descripción", 2.5);
        when(videoRepository.save(video)).thenReturn(video);
        Video savedVideo = cultureMediaService.save(video);
        assertEquals("Nuevo Título", savedVideo.title());
        verify(videoRepository, times(1)).save(video);
    }
    @Test
    void when_saveView_should_return_saved_view() {
        Video video = new Video("08", "Otro Título", "Otra Descripción", 3.0);
        View view = new View("Usuario Prueba", LocalDateTime.now(), 25, video);
        when(viewsRepository.save(view)).thenReturn(view);
        View savedView = cultureMediaService.save(view);
        assertEquals("Usuario Prueba", savedView.userFullName());
        verify(viewsRepository, times(1)).save(view);
    }
}
