package org.daehagnawa.batch.daehagnawabatch;

import org.assertj.core.api.Assertions;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        DepartmentInfo departmentInfo1 = DepartmentInfo.builder()
                .id(1L)
                .universityName("안녕대학교")
                .admissionType("안녕타입")
                .departmentName("안녕과")
                .applicantsCount("asda")
                .build();

        DepartmentInfo departmentInfo2 = DepartmentInfo.builder()
                .id(null)
                .universityName("안녕대학교")
                .admissionType("안녕타입")
                .departmentName("안녕과")
                .applicantsCount("30.67")
                .build();

        Assertions.assertThat(departmentInfo1.equals(departmentInfo2)).isTrue();

    }
}
