package kr.seula.nagoserver.domain.report.repository;

import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {



}
