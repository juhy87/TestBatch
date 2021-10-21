package com.example.testbatch.batch.job;

import com.example.testbatch.batch.entity.ROImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
@Slf4j
public class ImageResizerConfig extends DefaultBatchConfigurer {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Value("${job.name}")
    private String jobName;

    @Value("${job.step.name}")
    private String stepName;

    @Value("${job.step.chunk-size}")
    private int chunkSize;

    @Value("${job.step.reader.name}")
    private String readerName;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job ImageResizerJob() throws Exception {
        Job imageResizerJob = jobBuilderFactory.get(jobName)
//                .incrementer(new UniqueRunIdIncrementer()) // 개발
                .start(imageResizerStep())
                .build();

        return imageResizerJob;
    }

    @Bean
    @JobScope
    public Step imageResizerStep() throws Exception {
//        log.info("[imageResizerStep]");
        return stepBuilderFactory.get(stepName)
//                .tasklet(new ImageTasklet())
                .<ROImage, ROImage>chunk(chunkSize)
//                .reader(imageResizerReader(null)) // 일배치
                .reader(imageResizerAllReader()) // 초기적재
                .writer(imageResizerWriter())
                .build();
    }

    //     초기적재
    @Bean
    @StepScope
    public JpaPagingItemReader<ROImage> imageResizerAllReader() throws Exception {
//        log.info("[imageResizerAllReader]");
        String queryString = "SELECT p FROM ROImage p " +
                "WHERE ORIGIN_IMG_URL IS NULL " +
                "AND THUMB_IMG_URL IS NULL " +
                "AND RSZ_STAT IS NULL " +
                "ORDER BY IMG_SN ASC";

        return getROImageJpaPagingItemReader(queryString);
    }

    // 일배치
    @Bean
    @StepScope
    public JpaPagingItemReader<ROImage> imageResizerReader() throws Exception {
        String queryString = "SELECT p FROM ROImage p " +
                "WHERE ORIGIN_IMG_URL IS NULL " +
                "AND THUMB_IMG_URL IS NULL " +
                "AND RESIZE_STATUS IS NULL " +
                "ORDER BY IMG_SN ASC";

        return getROImageJpaPagingItemReader(queryString);
    }

    private JpaPagingItemReader<ROImage> getROImageJpaPagingItemReader(String queryString) {
        JpaPagingItemReader<ROImage> reader = new JpaPagingItemReader<>();
        reader.setPageSize(chunkSize);
        reader.setQueryString(queryString);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName(readerName);

        return reader;
    }



    // writer
    @Bean
    @StepScope
    public JpaItemWriter<ROImage> imageResizerWriter() throws Exception {
//        log.info("[imageResizerWriter]");
        return new JpaItemWriterBuilder<ROImage>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Override
    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setDatabaseType("ORACLE");
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
