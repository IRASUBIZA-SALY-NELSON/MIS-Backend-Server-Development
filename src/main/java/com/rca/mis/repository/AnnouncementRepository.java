package com.rca.mis.repository;

import com.rca.mis.model.communication.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

    List<Announcement> findByStatus(Announcement.AnnouncementStatus status);
    
    List<Announcement> findByTargetAudience(String targetAudience);
    
    List<Announcement> findByPriority(String priority);
    
    List<Announcement> findByCreatedBy_Id(UUID createdById);
    
    // Removed problematic queries - using service-level filtering instead
    
    List<Announcement> findByTitleContainingIgnoreCase(String title);
    
    List<Announcement> findByContentContainingIgnoreCase(String content);
    
    Page<Announcement> findByStatusOrderByCreatedAtDesc(Announcement.AnnouncementStatus status, Pageable pageable);
    
    long countByTargetAudienceAndStatus(Announcement.TargetAudience audience, Announcement.AnnouncementStatus status);
}
