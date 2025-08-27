package com.rca.mis.service;

import com.rca.mis.model.communication.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementService {

    Announcement createAnnouncement(Announcement announcement);
    
    Announcement updateAnnouncement(UUID id, Announcement announcement);
    
    Optional<Announcement> findById(UUID id);
    
    Page<Announcement> findAll(Pageable pageable);
    
    List<Announcement> findActiveAnnouncements();
    
    List<Announcement> findByTargetAudience(String targetAudience);
    
    List<Announcement> findByPriority(String priority);
    
    List<Announcement> findByAuthor(UUID authorId);
    
    List<Announcement> findByTitleContaining(String title);
    
    List<Announcement> findByContentContaining(String content);
    
    void deleteAnnouncement(UUID id);
    
    long countByTargetAudienceAndActive(String audience);
}
