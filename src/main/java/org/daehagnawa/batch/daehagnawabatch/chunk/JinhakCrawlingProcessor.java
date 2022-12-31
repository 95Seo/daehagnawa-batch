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

import java.io.IOException;
import java.util.*;

@Slf4j
@StepScope
@Component
public class JinhakCrawlingProcessor implements ItemProcessor<UniversityDocument, List<DepartmentInfo>> {

    @Override
    public List<DepartmentInfo> process(UniversityDocument item) throws Exception {

        log.info("크롤링을 시작합니다.");

        List<DepartmentInfo> docs = parseToDepartment(item);

        log.info("docs.size : {}", docs.size());

        log.info("크롤링이 끝났습니다.");

        return docs;
    }


    private List<DepartmentInfo> parseToDepartment(
            UniversityDocument item
    ) throws IOException {

        String universityName = item.getUniversityName();

        log.info("universityName : {}", universityName);

        Document document = item.getDocument();

        List<DepartmentInfo> departments = new ArrayList<>();

        // table 구하기
        Elements tables = getTables(document);
        Elements admissionTypes = getAdmissionTypes(tables);

        DepartmentInfoProxy proxy = item.toDepartmentProxy();

        for (int i = 0; i < admissionTypes.size(); i++) {

            proxy.setAdmissionType(admissionTypes.get(i).text());

            Element targetTable = tables.get(i).child(1);
            int tableSize = targetTable.select("tbody tr").size();

            String[] tableColumn = targetTable.child(0).text().split(" ");
            HashMap<String, Integer> columnIndex = getColumnIndex(tableColumn);

            log.info("proxy admission : {}", admissionTypes.get(i).text());

            for (int j = 1; j < tableSize - 1; j++) {
                Elements tds = targetTable.child(j).children();

                log.info("j : {}, tableSize : {}", j, tableSize);

                if (tds.size() <= 0) {
                    continue;
                }

                log.info("column size : {} ", columnIndex.size());

                for (String column : columnIndex.keySet()) {
                    int index = tds.size() - columnIndex.get(column) - 1 < 0 ? 0 : tds.size() - columnIndex.get(column) - 1;

                    log.info("tds : \n{}", tds);

                    if (column.equals("모집단위")) {

                        String departmentName = tds.get(index).text().trim();
                        log.info("모집단위 index : {}", index);
                        proxy.setDepartmentName(departmentName);
                    }

                    if (column.equals("모집인원")) {
                        String recruitmentCount = tds.get(index).text().trim();
                        log.info("모집인원 index : {}", index);
                        proxy.setRecruitmentCount(recruitmentCount);
                    }

                    if (column.equals("지원인원")) {
                        String applicantsCount = tds.get(index).text().trim();
                        log.info("지원인원 index : {}", index);
                        proxy.setApplicantsCount(applicantsCount);
                    }

                    if (column.equals("경쟁률")) {
                        String competitionRatio = tds.get(index).text().trim();
                        log.info("경쟁률 index : {}", index);
                        proxy.setCompetitionRatio(Float.parseFloat(competitionRatio.split(":")[0].trim()));
                    }
                }

                departments.add(proxy.toEntity());
            }
        }

        return departments;
    }

    private HashMap<String, Integer> getColumnIndex(String[] tableColumn) {
        HashMap<String, Integer> columnIndex = new HashMap<>();
        for (int i = 0; i < tableColumn.length; i++) {
            if (tableColumn[i].contains("모집단위")) {
                columnIndex.put("모집단위", tableColumn.length - i - 1);
                log.info("모집단위 index : {}", tableColumn.length - i - 1);
            }

            if (tableColumn[i].contains("모집인원")) {
                columnIndex.put("모집인원", tableColumn.length - i - 1);
                log.info("모집인원 index : {}", tableColumn.length - i - 1);
            }

            if (tableColumn[i].contains("지원인원")) {
                columnIndex.put("지원인원", tableColumn.length - i - 1);
                log.info("지원인원 index : {}", tableColumn.length - i - 1);
            }

            if (tableColumn[i].contains("경쟁률")) {
                columnIndex.put("경쟁률", tableColumn.length - i - 1);
                log.info("경쟁률 index : {}", tableColumn.length - i - 1);
            }
        }
        return columnIndex;
    }

    private String[] getTableColumn(Elements tables) {
        return tables
                .select("tbody tr")
                .first()
                .children()
                .text()
                .split(" ");
    }

    private Elements getAdmissionTypes(Elements tables) {
        return tables
                .prev()
                .select("strong");
    }

    private Elements getTables(Document document) {
        return document
                .select("table.tableRatio3");
    }
}
