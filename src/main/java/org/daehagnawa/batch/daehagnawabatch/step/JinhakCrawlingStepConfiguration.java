package org.daehagnawa.batch.daehagnawabatch.step;

import lombok.RequiredArgsConstructor;
import org.daehagnawa.batch.daehagnawabatch.chunk.JinhakCrawlingProcessor;
import org.daehagnawa.batch.daehagnawabatch.chunk.JpaItemListWriter;
import org.daehagnawa.batch.daehagnawabatch.domain.DepartmentInfo;
import org.daehagnawa.batch.daehagnawabatch.domain.UniversityDocument;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JinhakCrawlingStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final Partitioner jinhakCrawlingPartitioner;
    private final JinhakCrawlingProcessor jinhakCrawlingProcessor;
    private final JpaItemListWriter<DepartmentInfo> itemListWriter;
    private final TaskExecutor taskExecutor;

    int chunkSize = 1;

    @Bean
    @JobScope
    public Step jinhakCrawlingMasterStep() {
        return stepBuilderFactory.get("jinhakCrawlingMasterStep")
                .partitioner(jinhakCrawlingSlaveStep().getName(), jinhakCrawlingPartitioner)
                .step(jinhakCrawlingSlaveStep())
                .gridSize(5)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step jinhakCrawlingSlaveStep() {
        return stepBuilderFactory.get("jinhakCrawlingSlaveStep")
                .<UniversityDocument, List<DepartmentInfo>>chunk(chunkSize)
                .reader(jinhakItemReader(null, null))
                .processor(jinhakCrawlingProcessor)
                .writer(itemListWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UniversityDocument> jinhakItemReader(
            @Value("#{stepExecutionContext['start']}") Integer start,
            @Value("#{stepExecutionContext['end']}") Integer end
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
                        "where type = 'jinhak' " +
                        "order by id")
                .currentItemCount(start)
                .maxItemCount(end)
                .build();
    }
}
