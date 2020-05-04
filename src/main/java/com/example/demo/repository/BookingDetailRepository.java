package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import com.example.demo.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    @Query("select bd from BookingDetail bd where bd.roomId =?1 and ((bd.fromDate >= ?2 and bd.fromDate <= ?3) or (bd.toDate >= ?2 and bd.toDate <=?3) or ((?2 between bd.fromDate and bd.toDate) and (?3 between bd.fromDate and bd.toDate)))")
    public List<BookingDetail> findByRoomIdAndDateRange(Long roomId, Date fromDate, Date toDate);
}
