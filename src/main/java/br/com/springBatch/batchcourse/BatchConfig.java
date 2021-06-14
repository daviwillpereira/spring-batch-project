package br.com.springBatch.batchcourse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
				.incrementer(new RunIdIncrementer()) // executa o JOB e incrementa um run ID a cada execuçao adicionando automaticamente um parâmetro, gerando uma nova instância do JOB. Usado quando o JOB não precisa ser reinicializado
				.build();
	}

	public Step printHelloStep() {
		return stepBuilderFactory
				.get("printHelloStep")
				.tasklet(printHelloTaskelt(null))
				.build();
	}

	@Bean
	@StepScope //Permite que no momento em que estiver sendo criado o STEP, ele vai buscar o valor do jobParameters 
	public Tasklet printHelloTaskelt(@Value("#{jobParameters['nome']}") String nome) { //O @Value permite obter dados dos parametros do JOB e tbm de propriedade do app.properties, associa o parametro no colchete com a variável do método
		return new Tasklet() {    //Tasklets são usados para comandos simples, que não precisam de muito processamento
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println(String.format("Olá, %s!", nome));
				return RepeatStatus.FINISHED;
			}
		};
	}
}
