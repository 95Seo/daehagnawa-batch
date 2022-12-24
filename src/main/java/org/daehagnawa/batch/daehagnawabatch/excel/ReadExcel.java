package org.daehagnawa.batch.daehagnawabatch.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 로컬 경로의 엑셀 파일을 MultipartFile로 읽어들인다.
 * 엑셀 파일을 파싱한다.
 * Queue 에 담아 리턴한다.
 */
@Slf4j
public class ReadExcel {
    private static final ClassLoader classLoader = ReadExcel.class.getClassLoader();

    public static Queue<ExcelData> readExcelData() throws IOException {
        File excel = getExcel();

        log.info("{}", excel.getName());

        Workbook workbook = new XSSFWorkbook(new FileInputStream(excel));

        Sheet worksheet = workbook.getSheetAt(0);

        Queue<ExcelData> queue = new LinkedList<>();
        for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            queue.add(
                    ExcelData.builder()
                            .universityName(row.getCell(0).getStringCellValue())
                            .universityURL(row.getCell(1).getStringCellValue())
                            .build()
            );

            log.info("universityName = {}", row.getCell(0).getStringCellValue());
            log.info("universityURL = {}", row.getCell(1).getStringCellValue());
        }

        return queue;
    }

    private static File getExcel() {
        URL resource = classLoader.getResource("UwayType01.xlsx");
        Objects.requireNonNull(resource);

        return new File(resource.getFile());
    }
}
