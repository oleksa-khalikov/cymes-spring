package pl.com.bottega.cymes.showscheduler.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowTest {

    @Mock
    private ShowSchedulerConfiguration configuration;

    @Test
    public void createsShow() {
        // given
        var cmd = new ScheduleShowCommandExample().toCommand();
        var movie = new MovieExample().toMovie();
        when(configuration.showReservationBuffer()).thenReturn(Duration.ofMinutes(45));

        // when
        var show = new Show(cmd, movie, configuration);

        // then
        assertThat(show.getId()).isEqualTo(cmd.getShowId());
        assertThat(show.getStart()).isEqualTo(cmd.getWhen());
        assertThat(show.getEnd()).isEqualTo(cmd.getWhen().plus(configuration.showReservationBuffer()).plus(movie.getDurationMinutes(), MINUTES));
        assertThat(show.getCinemaHallId()).isEqualTo(cmd.getCinemaHallId());
        assertThat(show.getCinemaId()).isEqualTo(cmd.getCinemaId());
        assertThat(show.getMovieId()).isEqualTo(movie.getId());
    }
}