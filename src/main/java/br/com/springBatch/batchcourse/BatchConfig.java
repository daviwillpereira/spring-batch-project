package br.com.springBatch.batchcourse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory; //build a JOB
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean // Faz com que o Spring exergue o JOB no contexto da aplicação o gerencie
	public Job printHelloJob() {
		return jobBuilderFactory
				.get("printHelloJob") //JOB's name
				.start(printHelloStep())
				.build();
	}

	private Step printHelloStep() {
		return stepBuilderFactory
				.get("printHelloStep")
				.tasklet(new Tasklet() {    //Tasklets são usados para comandos simples, que não precisam de muito processamento
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Hello, world!");
						return RepeatStatus.FINISHED;
					}
				})
				.build();
	}
}
