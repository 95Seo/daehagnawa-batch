package org.daehagnawa.batch.daehagnawabatch.job;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.chunk.JpaItemListWriter;
import org.daehagnawa.batch.daehagnawabatch.chunk.UwayCrawlingProcessor;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.domain.UniversityDocument;
import org.daehagnawa.batch.daehagnawabatch.listener.StopWatchJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CrawlingJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step uwayCrawlingMasterStep;
    private final Step jinhakCrawlingMasterStep;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("crawlingJob")
                .start(uwayCrawlingMasterStep)
                .next(jinhakCrawlingMasterStep)
                .incrementer(new RunIdIncrementer())
                .listener(new StopWatchJobListener())
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

}
