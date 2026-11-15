package com.brewcoders.ascend.backend.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/* Purpose: Store and validate the course name and date range.
 */
public class CourseCycle {
    private final String courseName;
    private final LocalDate startDate;
    private final LocalDate endDate;

    /* Purpose: Validate course name and date order before storing them.
     */
    public CourseCycle(String courseName, LocalDate startDate, LocalDate endDate) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter a course name.");
        }
        if (courseName.trim().length() > 30) {
            throw new IllegalArgumentException("Course name must be at most 30 characters.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Choose both course dates.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.courseName = courseName.trim();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCourseName() { return courseName; }

    public LocalDate getStartDate() { return startDate; }
    
    public LocalDate getEndDate() { return endDate; }

    public boolean isActive(LocalDate today) {
        requireDate(today);
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public boolean hasEnded(LocalDate today) {
        requireDate(today);
        return today.isAfter(endDate);
    }

    // Calendar days, including today when the course has started.
    public long getDaysRemaining(LocalDate today) {
        requireDate(today);
        if (hasEnded(today)) {
            return 0;
        }
        if (today.isBefore(startDate)) {
            return ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return ChronoUnit.DAYS.between(today, endDate) + 1;
    }

    /* Purpose: Reject a missing date before calling date methods.
     */
    private void requireDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("A date is required.");
        }
    }
}
