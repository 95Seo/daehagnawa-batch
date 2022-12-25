package org.daehagnawa.batch.daehagnawabatch.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, Long> {

    @Query("SELECT DISTINCT d " +
            "FROM university_department_info d " +
            "WHERE (d.universityName LIKE %:keyword% OR d.departmentName LIKE %:keyword%) ")
    List<DepartmentInfo> findAllDepartmentByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 임시로
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE university_department_info AUTO_INCREMENT = 1", nativeQuery = true)
    void initAutoIncrement();
}
