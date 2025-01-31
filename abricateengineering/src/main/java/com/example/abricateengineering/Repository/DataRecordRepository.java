package com.example.abricateengineering.Repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.abricateengineering.entity.DataRecord;
public interface DataRecordRepository extends JpaRepository<DataRecord, LocalDateTime>{
    
    List<DataRecord> findAllByDateTimeAfter(LocalDateTime dateTime);

    List<DataRecord> findAllByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<DataRecord> findAllByDateTimeBetweenAndFormula(LocalDateTime startDateTime, LocalDateTime endDateTime, String formula);
}



