package org.instagram.notification;


import org.instagram.notification.dto.NotificationRequestDto;
import org.instagram.notification.dto.NotificationResponseDto;
import org.instagram.notification.model.Notification;
import org.instagram.notification.model.NotificationType;
import org.instagram.notification.repository.NotificationRepository;
import org.instagram.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setRecipientId(2L);
        notification.setSenderId(1L);
        notification.setType(NotificationType.FOLLOW_REQUEST);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createNotification_ShouldSaveNotification(){
        NotificationRequestDto request = new NotificationRequestDto();

        request.setRecipientId(2L);
        request.setSenderId(1L);
        request.setType(NotificationType.FOLLOW_REQUEST);

        notificationService.createNotification(request);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void getNotifications_ShouldReturnList(){
        when(notificationRepository.findByRecipientIdOrderByCreatedAtDesc(2L))
                .thenReturn(List.of(notification));

        List<NotificationResponseDto> result = notificationService.getNotifications(2L);

        assertEquals(1,result.size());
        assertEquals(2L, result.get(0).getRecipientId().longValue());
    }

    @Test
    void getUnreadNotifications_ShouldReturnOnlyUnread(){
        when(notificationRepository.findByRecipientIdAndIsReadFalse(2L))
                .thenReturn(List.of(notification));

        List<NotificationResponseDto> result = notificationService.getUnreadNotifications(2L);

        assertEquals(1,result.size());
        assertFalse(result.get(0).isRead());
    }

    @Test
    void markAsRead_ShouldSetReadTrue(){
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L);

        verify(notificationRepository, times(1)).save(argThat(n->n.getIsRead()));
    }

    @Test
    void markAsRead_ShouldThrowException_WhenNotFound(){
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(1L));
        verify(notificationRepository, never()).save(any(Notification.class));
    }

}
