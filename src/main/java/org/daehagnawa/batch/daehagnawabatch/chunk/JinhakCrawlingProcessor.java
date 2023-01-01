package org.daehagnawa.batch.daehagnawabatch.chunk;

import lombok.extern.slf4j.Slf4j;
import org.daehagnawa.batch.daehagnawabatch.category.Category;
import org.daehagnawa.batch.daehagnawabatch.category.CategoryFactory;
import org.daehagnawa.batch.daehagnawabatch.category.type.CompetitionRatio;
import org.daehagnawa.batch.daehagnawabatch.category.type.RecruitmentCount;
import org.daehagnawa.batch.daehagnawabatch.category.type.SubDepartment;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfoProxy;
import org.daehagnawa.batch.daehagnawabatch.domain.UniversityDocument;
import org.daehagnawa.batch.daehagnawabatch.support.CategoryReg;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@StepScope
@Component
public class JinhakCrawlingProcessor implements ItemProcessor<UniversityDocument, List<DepartmentInfo>> {

    private List<DepartmentInfo> docs;

    private DepartmentInfoProxy departmentProxy;

    private List<Category> categoryInfos = new LinkedList<>();

    private Queue<Category> categoryRemoveQueue = new LinkedList<>();

    private Category deptCategory;

    private int recuitSaveCnt = 0, ratioSaveCnt = 0;

    @Override
    public List<DepartmentInfo> process(UniversityDocument item) throws Exception {
        log.info("크롤링을 시작합니다.");

        // init
        docs = new ArrayList<>();
        departmentProxy = item.toDepartmentProxy();
        departmentProxy.setEntranceExamYear(2023);

        log.info("{}", item.getUniversityName());

        Document document = item.getDocument();

        // 경쟁률을 보여주는 테이블(div.DivType)을 Element로 복수개인 Elements를 만들어서 가져온다.
        Elements tables = getTables(document);

        parseToDepartment(tables);

        log.info("docs.size : {}", docs.size());

        log.info("크롤링이 끝났습니다.");

        return docs;
    }


    private void parseToDepartment(
            Elements tables
    ) {
//        List<Department> departments = new ArrayList<>();

        // table 구하기
        Elements admissionTypes = getAdmissionTypes(tables);

        for (int i = 0; i < admissionTypes.size(); i++) {

            String admissionType = admissionTypes.get(i).text();

            departmentProxy.setAdmissionType(admissionType);

            Element targetTable = tables.get(i).child(1);

            // 테이블의 row를 나누고 rows 로 만들어 가져옴
            Elements rows = targetTable.select("tbody tr");
            Elements tableCategory = rows.get(0).select("th");

            if (!checkCategoryValid(tableCategory)) {
//                log.info("{}", admissionType + " 수집 X");
                continue;
            }

//            log.info("{}", admissionType + " 수집 O");


            rowParsing(rows);

            resourceDeallocate();
        }
    }

    // 넘겨 받은 표를 학과별로 한 줄씩 나눈다.
    private void rowParsing(Elements rows) {

        // rows의 각 row를 꺼내와서 하나 씩 반복
        for (int i = 1; i < rows.size()-1; i++) {
            Element row = rows.get(i);

            if (!row.getElementsByClass("total").isEmpty()) {
                downRowCount();
            } else {
                // 한줄의 row를 column 으로 나눔
                Elements columns = row.select("td");

                columnPolicyExecute(columns);

                if ((recuitSaveCnt == 0) && (ratioSaveCnt == 0)) {
                    docs.add(departmentProxy.toEntity());
                }
            }
        }
    }

    // DepartmentTemplate을 받아서 DepartmentTemplate을 다시 return하는 구조 수정해야 함
    public void columnPolicyExecute(Elements columns) {
        int currentPoint = 0;
        int staticCategoryCnt = categoryInfos.size();
        int dynamicCategoryCnt = getSubDeptCount(columns);
        int subDeptCnt;

        // if (columns.size() != 0) 이 부분도 수정 해 봅시다.
        if (columns.size() != 0) {

            int deptSeq = deptCategory.getCategorySeq();

            // 사이즈가 크면 dept 뒤에 subDept 이어 붙이기
            if ((subDeptCnt = dynamicCategoryCnt - staticCategoryCnt) > 0) {
//                log.info("===========================모집단위 나눠짐===========================");
                for (int i = 1; i <= subDeptCnt; i++) {
                    int subDeptSeq = deptSeq + i;
                    categoryInfos.add(subDeptSeq, CategoryFactory.create("서브모집단위", subDeptSeq));
                }
            } else {
//                log.info("===========================모집단위 줄어듬===========================");

                for (int i = (subDeptCnt*-1); i >= 1 ; i--) {
                    int subDeptSeq = deptSeq + i;
                    categoryInfos.remove(subDeptSeq);
                }
            }

            // recuitSaveCnt != 0 && ratioSaveCnt != 0 이면 true
            // toLinked가 true면 문자열 연결하세요.
            boolean toLinked = (recuitSaveCnt != 0) || (ratioSaveCnt != 0);

            for (Category info : categoryInfos) {
//                log.info("-------------------------");

                // rowCount 확인
                // rowCount가 0이면 column 값을 가져와서 CategoryInfo 객체에 셋팅
                if (info.rowCountIsZero()) {
                    Element column = columns.get(currentPoint);
                    info.setColumn(column);
                    currentPoint++;
                }

                info.downRowCount();

                if (info.isTarget()) {
                    info.setDepartmentProxyData(departmentProxy, toLinked);
//                    log.info("{} : {} ", info.getCategoryName(), info.getColumnData());
                }

                // instanceof 보다 메서드 호출이 더 빠르다는데...
                // instanceof -> 메서드 호출로 바꿔보자
                if (info instanceof RecruitmentCount) {
                    recuitSaveCnt = info.getRowCount();
                }

                if (info instanceof CompetitionRatio) {
                    if (!info.getColumnData().equals("-1")) {
                        ratioSaveCnt = info.getRowCount();
                    }
                }

                if (info instanceof SubDepartment) {
                    if (info.rowCountIsZero())
                        categoryRemoveQueue.add(info);
                }
            }

            // subDept 없애기
            for (Category removeTarget : categoryRemoveQueue) {
                categoryInfos.remove(removeTarget);
                // 할당 해제
                removeTarget = null;
            }
        } else {
            // 임시 방편
            downRowCount();
        }
    }

    // 수정 포인트 제발 수정합시다.
    private boolean checkCategoryValid(Elements tableCategory) {
        // 우리가 수집을 목표로 하는 2가지 타입의 카테고리
        List<String> categoryList = tableCategory.eachText();

        if (
                (
                        categoryList.stream().anyMatch(s -> s.matches(CategoryReg.DEPARTMENT_REG1.getReg())) ||
                        categoryList.stream().anyMatch(s -> s.matches(CategoryReg.DEPARTMENT_REG2.getReg())) ||
                        categoryList.stream().anyMatch(s -> s.matches(CategoryReg.DEPARTMENT_REG3.getReg()))
                ) &&
                        categoryList.stream().anyMatch(s -> s.matches(CategoryReg.RECURITMENT_COUNT_REG.getReg())) &&
                        (
                            categoryList.stream().anyMatch(s -> s.matches(CategoryReg.APPLICANTS_COUNT_REG1.getReg())) ||
                            categoryList.stream().anyMatch(s -> s.matches(CategoryReg.APPLICANTS_COUNT_REG2.getReg()))
                        )
        ) {
            for (int i = 0; i < categoryList.size(); i++) {

                String category = categoryList.get(i);

                categoryInfos.add(CategoryFactory.create(category, i));

                // 모집인원 카테고리는 나뉘어지는 기준점이 된다.
                // 따로 빼서 바로 접근 할 수 있도록 하자.
                if (
                        category.matches(CategoryReg.DEPARTMENT_REG1.getReg()) ||
                        category.matches(CategoryReg.DEPARTMENT_REG2.getReg())
                )
                    deptCategory = categoryInfos.get(i);
            }

            // 모집단위, 모집인원, 지원인원은 존재하는데,
            // 경쟁률만 없는 경우 경쟁률을 -1로 설정 해서 DB에 저장
            // 회원들 한테 보여줄 때 경쟁률 -1 이면 - 로 보여줌
            if (
                    categoryList.stream().noneMatch(s -> s.matches(CategoryReg.COMPETITION_RATIO_REG1.getReg())) &&
                    categoryList.stream().noneMatch(s -> s.matches(CategoryReg.COMPETITION_RATIO_REG2.getReg())) &&
                    categoryList.stream().noneMatch(s -> s.matches(CategoryReg.COMPETITION_RATIO_REG3.getReg()))
            ) {
//                log.info("경쟁률 생성!!");
                Category ratio = CategoryFactory.createRatio(Integer.MAX_VALUE);
                categoryInfos.add(ratio);
            }

            return true;
        }

        return false;
    }

    // 임시 메서드 - 중복코드
    private void downRowCount() {
        for (Category info : categoryInfos) {
            if (!info.rowCountIsZero()) {
                info.downRowCount();

                if (info instanceof RecruitmentCount) {
                    recuitSaveCnt = info.getRowCount();
                }

                if (info instanceof CompetitionRatio) {
                    if (!info.getColumnData().equals("-1")) {
//                    log.info("ratioRowCount2 = {}", info.getRowCount());
                        ratioSaveCnt = info.getRowCount();
                    }
                }

                // instanceof -> 메서드 호출로 바꾸기
                if (info instanceof SubDepartment) {
                    if (info.rowCountIsZero())
                        categoryRemoveQueue.add(info);
                }
            }
        }
    }

    private int getSubDeptCount(Elements columns) {
        int count = 0;

        // html에서 가져온 columns.size + 현재 rowCount가 1이상인 categoryInfo의 갯수
        for (Category info : categoryInfos) {
            if (!info.rowCountIsZero())
                count += 1;
        }
        return columns.size() + count;
    }

    private Elements getAdmissionTypes(Elements tables) {
        return tables
                .prev()
                .select("h2");
    }

    private Elements getTables(Document document) {
        return document
                .select("table.tableRatio3");
    }

    private void resourceDeallocate() {
        categoryInfos.clear();
    }
}
