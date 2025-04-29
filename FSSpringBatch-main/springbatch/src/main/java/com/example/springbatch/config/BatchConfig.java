package com.example.springbatch.config;

import com.example.springbatch.DTO.UserCsv;
import com.example.springbatch.model.User;
import com.example.springbatch.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Reader - reads data from CSV file
     */
    @Bean
    @StepScope
    public FlatFileItemReader<UserCsv> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        log.info("Reading CSV file from path: {}", filePath);
        return new FlatFileItemReaderBuilder<UserCsv>()
                .name("userCsvReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("userName12", "emailId12")
                .linesToSkip(1) // skip header
                .targetType(UserCsv.class)
                .build();
    }

    /**
     * Processor - converts UserCsv to User
     */
    @Bean
    public ItemProcessor<UserCsv, User> processor() {
        return userCsv -> {
            log.info("Processing user: {}", userCsv.getUserName());
            User user = new User();
            user.setUserName(userCsv.getUserName());
            user.setUserEmailId(userCsv.getEmailId());
            return user;
        };
    }

    /**
     * Writer - writes User to DB
     */
    @Bean
    public ItemWriter<User> writer() {
        return users -> {
            log.info("Writing {} users to DB", users.size());
            userRepository.saveAll(users);
        };
    }

    /**
     * Step - complete chunk-based step
     */
    @Bean
    public Step csvStep(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager,
                        ItemReader<UserCsv> reader,
                        ItemProcessor<UserCsv, User> processor,
                        ItemWriter<User> writer) {
        return new StepBuilder("csvStep", jobRepository)
                .<UserCsv, User>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * Job - CSV import job
     */
    @Bean
    public Job csvJob(JobRepository jobRepository, Step csvStep) {
        return new JobBuilder("csvImportJob", jobRepository)
                .start(csvStep)
                .build();
    }
}
