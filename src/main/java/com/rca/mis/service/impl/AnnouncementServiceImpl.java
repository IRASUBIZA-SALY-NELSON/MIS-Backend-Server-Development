package com.rca.mis.service.impl;

import com.rca.mis.model.communication.Announcement;
import com.rca.mis.repository.AnnouncementRepository;
import com.rca.mis.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Override
    public Announcement createAnnouncement(Announcement announcement) {
        log.info("Creating new announcement: {}", announcement.getTitle());
        
        if (announcement.getCreatedAt() == null) {
            announcement.setCreatedAt(LocalDateTime.now());
        }
        
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement updateAnnouncement(UUID id, Announcement announcement) {
        log.info("Updating announcement with ID: {}", id);
        
        Announcement existingAnnouncement = announcementRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Announcement not found with ID: " + id));
        
        existingAnnouncement.setTitle(announcement.getTitle());
        existingAnnouncement.setContent(announcement.getContent());
        existingAnnouncement.setTargetAudience(announcement.getTargetAudience());
        existingAnnouncement.setPriority(announcement.getPriority());
        existingAnnouncement.setStatus(announcement.getStatus());
        existingAnnouncement.setStartDate(announcement.getStartDate());
        existingAnnouncement.setEndDate(announcement.getEndDate());
        
        return announcementRepository.save(existingAnnouncement);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Announcement> findById(UUID id) {
        return announcementRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Announcement> findAll(Pageable pageable) {
        return announcementRepository.findByStatusOrderByCreatedAtDesc(Announcement.AnnouncementStatus.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findActiveAnnouncements() {
        return announcementRepository.findByStatus(Announcement.AnnouncementStatus.PUBLISHED).stream()
                .filter(a -> a.getStartDate().isBefore(java.time.LocalDate.now().plusDays(1)) && 
                           (a.getEndDate() == null || a.getEndDate().isAfter(java.time.LocalDate.now().minusDays(1))))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findByTargetAudience(String targetAudience) {
        return announcementRepository.findByStatus(Announcement.AnnouncementStatus.PUBLISHED).stream()
                .filter(a -> a.getTargetAudience().toString().equalsIgnoreCase(targetAudience))
                .filter(a -> a.getStartDate().isBefore(java.time.LocalDate.now().plusDays(1)) && 
                           (a.getEndDate() == null || a.getEndDate().isAfter(java.time.LocalDate.now().minusDays(1))))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findByPriority(String priority) {
        return announcementRepository.findByPriority(priority);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findByAuthor(UUID authorId) {
        return announcementRepository.findByCreatedBy_Id(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findByTitleContaining(String title) {
        return announcementRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> findByContentContaining(String content) {
        return announcementRepository.findByContentContainingIgnoreCase(content);
    }

    @Override
    public void deleteAnnouncement(UUID id) {
        log.info("Deleting announcement with ID: {}", id);
        
        if (!announcementRepository.existsById(id)) {
            throw new IllegalArgumentException("Announcement not found with ID: " + id);
        }
        
        announcementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTargetAudienceAndActive(String audience) {
        return announcementRepository.countByTargetAudienceAndStatus(Announcement.TargetAudience.valueOf(audience.toUpperCase()), Announcement.AnnouncementStatus.PUBLISHED);
    }
}
