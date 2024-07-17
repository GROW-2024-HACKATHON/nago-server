package kr.seula.nagoserver.domain.report.repository;

import kr.seula.nagoserver.domain.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findAllByNameAndEmailAndPhone(String name, String email, String phone);
    void deleteAllByNameAndEmailAndPhone(String name, String email, String phone);

}
