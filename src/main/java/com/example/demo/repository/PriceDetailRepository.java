package com.example.demo.repository;

import java.util.Date;

import com.example.demo.model.PriceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PriceDetailRepository extends JpaRepository<PriceDetail, Long> {
    @Query("select pr from PriceDetail pr where pr.roomId =?1 and( (pr.fromDate >= ?2 and pr.fromDate <= ?3) or (pr.toDate >= ?2 and pr.toDate <=?3)) or ((?2 between pr.fromDate and pr.toDate) and (?3 between pr.fromDate and pr.toDate))")
    public PriceDetail findByRoomIdAndDateRange(Long roomId, Date fromDate, Date toDate);
}