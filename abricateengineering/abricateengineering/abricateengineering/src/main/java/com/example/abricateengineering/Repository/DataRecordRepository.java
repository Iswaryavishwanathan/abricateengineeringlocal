package com.example.abricateengineering.Repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.abricateengineering.entity.DataRecord;
public interface DataRecordRepository extends JpaRepository<DataRecord, LocalDateTime>{
    
    List<DataRecord> findAllByDateTimeAfter(LocalDateTime dateTime);

    List<DataRecord> findAllByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
     @Query("SELECT d FROM DataRecord d WHERE d.dateTime BETWEEN :start AND :end ORDER BY d.dateTime ASC")
List<DataRecord> findRecordsSorted(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT TOP 1 DateTime FROM dbo.LOG ORDER BY DateTime DESC", nativeQuery = true)
LocalDateTime findLatestRecord();

}



