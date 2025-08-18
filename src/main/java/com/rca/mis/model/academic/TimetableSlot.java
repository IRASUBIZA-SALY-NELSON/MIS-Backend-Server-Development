package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetable_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_id", "day_of_week", "start_time"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TimetableSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "room", length = 100)
    private String room;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "slot_type", length = 50)
    private String slotType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "color_code", length = 7)
    private String colorCode;

    @Column(name = "is_break")
    private Boolean isBreak = false;

    @Column(name = "break_type", length = 50)
    private String breakType;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "online_platform")
    private String onlinePlatform;

    @Column(name = "online_link")
    private String onlineLink;

    @OneToMany(mappedBy = "timetableSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimetableException> exceptions = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return subject.getName() + " - " + clazz.getName() + " (" + 
               dayOfWeek + " " + startTime + "-" + endTime + ")";
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isBreak() {
        return Boolean.TRUE.equals(isBreak);
    }

    public boolean isOnline() {
        return Boolean.TRUE.equals(isOnline);
    }

    public boolean hasRoom() {
        return room != null && !room.trim().isEmpty();
    }

    public boolean hasOnlineDetails() {
        return isOnline() && onlinePlatform != null && onlineLink != null;
    }

    public int getDurationMinutes() {
        if (startTime != null && endTime != null) {
            return (int) java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return 0;
    }

    public boolean isOverlapping(TimetableSlot other) {
        if (!dayOfWeek.equals(other.dayOfWeek)) return false;
        if (!clazz.equals(other.clazz)) return false;
        
        return !(endTime.isBefore(other.startTime) || startTime.isAfter(other.endTime));
    }

    public boolean isTeacherAvailable(TimetableSlot other) {
        if (!dayOfWeek.equals(other.dayOfWeek)) return true;
        if (!teacher.equals(other.teacher)) return true;
        
        return !(endTime.isBefore(other.startTime) || startTime.isAfter(other.endTime));
    }

    public boolean isRoomAvailable(TimetableSlot other) {
        if (!dayOfWeek.equals(other.dayOfWeek)) return true;
        if (!hasRoom() || !other.hasRoom()) return true;
        if (!room.equals(other.room)) return true;
        
        return !(endTime.isBefore(other.startTime) || startTime.isAfter(other.endTime));
    }
}
