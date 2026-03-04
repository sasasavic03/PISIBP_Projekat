package org.instagram.follow;

import org.instagram.follow.model.Follow;
import org.instagram.follow.model.FollowStatus;
import org.instagram.follow.repository.FollowRepository;
import org.instagram.follow.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class FollowServiceApplicationTests {

    @Mock
    private FollowRepository repository;

    @InjectMocks
    private FollowService service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }


    //TEST JAVNI PROFIL
    @Test
    void shouldCreateAcceptedFollowForPublicProfile(){
        when(repository.findByFollowerIdAndFollowingId(1L,2L))
                .thenReturn(Optional.empty());

        service.follow(1L,2L,false);

        verify(repository,times(1)).save(any(Follow.class));
    }

    //TEST PRIVATNI PROFIL
    @Test
    void shouldCreatePendingFollowForPrivvateProfile(){
        when(repository.findByFollowerIdAndFollowingId(1L, 2L))
                .thenReturn(Optional.empty());

        service.follow(1L,2L,true);

        ArgumentCaptor<Follow> captor = ArgumentCaptor.forClass(Follow.class);
        verify(repository).save(captor.capture());

        assertEquals(FollowStatus.PENDING, captor.getValue().getStatus());

    }

    //TEST ZA POSTOJECU RELACIJU
    @Test
    void shouldThrowExpcetionIfFollowAlreadyExists(){
        when(repository.findByFollowerIdAndFollowingId(1L,2L))
                .thenReturn(Optional.of(new Follow()));

        assertThrows(RuntimeException.class,
                () -> service.follow(1L,2L,false));
    }

    //TEST ZA UNFOLLOW
    @Test
    void shouldDeleteFollow(){
        Follow follow = new Follow();
        when(repository.findByFollowerIdAndFollowingId(1L,2L))
                .thenReturn(Optional.of(follow));

        service.unfollow(1L,2L);

        verify(repository,times(1)).delete(follow);
    }

    //TEST UNFOLLOW KAD RELACIJE NE POSTOJI
    @Test
    void shouldThrowExceptionWhenUnfollowNotExisting(){
        when(repository.findByFollowerIdAndFollowingId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.unfollow(1L,2L));
    }


}
