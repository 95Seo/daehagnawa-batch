package org.daehagnawa.batch.daehagnawabatch.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryReg {
    DEPARTMENT_REG1("모집단위.*"),
    DEPARTMENT_REG2("^[모집학과\\s]*$"),
    DEPARTMENT_REG3(".*학부.*"),
    SUB_DEPARTMENT_REG1("^[서브모집단위\\s]*$"),
    SUB_DEPARTMENT_REG2("^[전공\\s]*$"),
    EVENT_REG("^[종목\\s]*$"),
    GENDER_REG("^[성별\\s]*$"),
    // 모집인원이 "40이내" 라고 되어 있으면 -> -40으로 변경
    RECURITMENT_COUNT_REG("^[모집인원\\s]*$"),
    APPLICANTS_COUNT_REG1("^[지원인원\\s]*$"),
    APPLICANTS_COUNT_REG2("^[지원자\\s]*$"),
    COMPETITION_RATIO_REG1("^[경쟁률\\s]*$"),
    COMPETITION_RATIO_REG2("^[지원현황\\s]*$"),
    COMPETITION_RATIO_REG3("^[지원율\\s]*$");

    private final String reg;
}
