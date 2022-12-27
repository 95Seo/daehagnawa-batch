package org.daehagnawa.batch.daehagnawabatch.job;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.chunk.JpaItemListWriter;
import org.daehagnawa.batch.daehagnawabatch.chunk.UwayCrawlingProcessor;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.excel.ExcelData;
import org.daehagnawa.batch.daehagnawabatch.listener.StopWatchJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CrawlingJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("crawlingJob")
                .start(crawlingStep())
                .incrementer(new RunIdIncrementer())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step crawlingStep() {
        return stepBuilderFactory.get("crawlingStep")
                .<ExcelData, List<DepartmentInfo>>chunk(1)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<ExcelData> itemReader() {
        return new FlatFileItemReaderBuilder<ExcelData>()
                .name("readExcelData")
                .resource(new ClassPathResource("UwayType.csv"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(ExcelData.class)
                .delimited().delimiter(",")
                .names("universityName", "universityURL", "degree", "area")
                .build();
    }

    @Bean
    public ItemProcessor<ExcelData, List<DepartmentInfo>> itemProcessor() {
        return new UwayCrawlingProcessor();
    }

    @Bean
    public JpaItemListWriter<DepartmentInfo> itemWriter() {

        JpaItemWriter<DepartmentInfo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        jpaItemWriter.setUsePersist(true);

        JpaItemListWriter<DepartmentInfo> writer = new JpaItemListWriter<>(jpaItemWriter);
        writer.setEntityManagerFactory(entityManagerFactory);
        writer.setUsePersist(true);

        return writer;
    }

}
