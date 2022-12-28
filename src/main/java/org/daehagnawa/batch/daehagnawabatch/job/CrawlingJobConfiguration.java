package org.daehagnawa.batch.daehagnawabatch.job;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.chunk.CountItemProcessor;
import org.daehagnawa.batch.daehagnawabatch.chunk.JpaItemListWriter;
import org.daehagnawa.batch.daehagnawabatch.chunk.UwayCrawlingProcessor;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.domain.UniversityDocument;
import org.daehagnawa.batch.daehagnawabatch.listener.StopWatchJobListener;
import org.daehagnawa.batch.daehagnawabatch.partition.CrawlingPartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CrawlingJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final Partitioner CrawlingPartitioner;
    private final UwayCrawlingProcessor uwayCrawlingProcessor;
    private final CountItemProcessor countItemProcessor;

    private int chunkSize = 1;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("crawlingJob")
                .start(crawlingMasterStep())
                .incrementer(new RunIdIncrementer())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step crawlingMasterStep() {
        return stepBuilderFactory.get("crawlingMasterStep")
                .partitioner(crawlingSlaveStep().getName(), CrawlingPartitioner)
                .step(crawlingSlaveStep())
                .gridSize(5)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setThreadNamePrefix("uway-crawling-thread-");

        return taskExecutor;
    }

//    @Bean
//    public Partitioner partitioner() {
//        return new CrawlingPartitioner();
//    }

    @Bean
    public Step crawlingSlaveStep() {
        return stepBuilderFactory.get("crawlingSlaveStep")
                .<UniversityDocument, List<DepartmentInfo>>chunk(chunkSize)
                .reader(itemReader(null, null))
                .processor(uwayCrawlingProcessor)
                .writer(itemWriter())
                .build();
    }

//    @Bean
//    public Step crawlingSlaveStep() {
//        return stepBuilderFactory.get("crawlingSlaveStep")
//                .<UniversityDocument, List<DepartmentInfo>>chunk(chunkSize)
//                .reader(itemReader(null, null))
//                .processor(uwayCrawlingProcessor)
//                .writer(new ItemWriter<List<DepartmentInfo>>() {
//                    @Override
//                    public void write(List<? extends List<DepartmentInfo>> items) throws Exception {
//                        items.forEach(item -> System.out.println(item));
//                    }
//                })
//                .build();
//    }

//    @Bean
//    public Step crawlingSlaveStep() {
//        return stepBuilderFactory.get("crawlingSlaveStep")
//                .<UniversityDocument, Integer>chunk(chunkSize)
//                .reader(itemReader(null, null))
//                .processor(countItemProcessor)
//                .writer(new ItemWriter<Integer>() {
//                    @Override
//                    public void write(List<? extends Integer> items) throws Exception {
//                        items.forEach(item -> System.out.println(item));
//                    }
//                })
//                .build();
//    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UniversityDocument> itemReader(
            @Value("#{stepExecutionContext['start']}") Long start,
            @Value("#{stepExecutionContext['end']}") Long end
    ) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("start", start);
        parameter.put("end", end);

        return new JpaPagingItemReaderBuilder<UniversityDocument>()
                .name("CrawlingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select u " +
                        "from university_document u " +
                        "where university_id >= :start and university_id <= :end ")
                .parameterValues(parameter)
                .build();
    }

    @Bean
    @StepScope
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
