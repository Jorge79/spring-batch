package org.example.springbatch.batch.config;

import org.example.springbatch.batch.decider.InputFilesDecider;
import org.example.springbatch.batch.domain.SaleRecord;
import org.example.springbatch.batch.listener.SalesReportSftpJobListener;
import org.example.springbatch.batch.writer.SalesReportWriter;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SalesReportJobConfig {

    @Bean
    public Job salesReportJob(JobRepository jobRepository,
                              Step salesReportStep,
                              InputFilesDecider inputFilesDecider,
                              SalesReportSftpJobListener salesReportSftpJobListener) {
        return new JobBuilder("salesReportJob", jobRepository)
                .listener(salesReportSftpJobListener)
                .start(inputFilesDecider)
                .on(InputFilesDecider.NO_INPUT).end(InputFilesDecider.NO_INPUT)
                .from(inputFilesDecider)
                .on("*").to(salesReportStep)
                .end()
                .build();
    }

    @Bean
    public Step salesReportStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                MultiResourceItemReader<SaleRecord> saleReader,
                                ItemProcessor<SaleRecord, SaleRecord> saleProcessor,
                                SalesReportWriter writer) {
        return new StepBuilder("salesReportStep", jobRepository)
                .<SaleRecord, SaleRecord>chunk(100)
                .reader(saleReader)
                .processor(saleProcessor)
                .writer(writer)
                .transactionManager(transactionManager)
                .build();
    }
}