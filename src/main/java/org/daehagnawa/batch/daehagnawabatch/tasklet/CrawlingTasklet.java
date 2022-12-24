package org.daehagnawa.batch.daehagnawabatch.tasklet;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.domain.department.Department;
import org.daehagnawa.batch.daehagnawabatch.domain.department.DepartmentRepository;
import org.daehagnawa.batch.daehagnawabatch.excel.ExcelData;
import org.daehagnawa.batch.daehagnawabatch.excel.ReadExcel;
import org.daehagnawa.batch.daehagnawabatch.service.UwayCrawlingService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
@RequiredArgsConstructor
public class CrawlingTasklet implements Tasklet {

    private final UwayCrawlingService uwayCrawlingService;
    private final DepartmentRepository departmentRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Queue<ExcelData> excelDataQueue = ReadExcel.readExcelData();

        while (!excelDataQueue.isEmpty()) {
            departmentSaveAll(
                    uwayCrawlingService.startCrawling(
                            excelDataQueue.poll()
                    )
            );
        }

        return RepeatStatus.FINISHED;
    }

    private void departmentSaveAll(List<Department> departments) {
        departmentRepository.saveAll(departments);
    }
}
