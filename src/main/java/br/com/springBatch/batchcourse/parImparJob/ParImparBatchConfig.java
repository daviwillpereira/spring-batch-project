package br.com.springBatch.batchcourse.parImparJob;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class ParImparBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory; //build a JOB
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean // Faz com que o Spring exergue o JOB no contexto da aplicação o gerencie
	public Job printParImparJob() {
		return jobBuilderFactory
				.get("printParImparJob") //JOB's name
				.start(printParImparStep())
				.incrementer(new RunIdIncrementer()) // executa o JOB e incrementa um run ID a cada execuçao adicionando automaticamente um parâmetro, gerando uma nova instância do JOB. Usado quando o JOB não precisa ser reinicializado
				.build();
	}

	public Step printParImparStep() {
		return stepBuilderFactory
				.get("imprimeParImparStep")
				.<Integer, String>chunk(1) // <Tipo do dado que será lido, tipo que será escrito>chunck(tamanho da chunk -> commit interval)
				.reader(countUntilTenReader())
				.processor(parOuImparProcessor())
				.writer(printWriter())
				.build(); 
	}

	public ItemWriter<String> printWriter() {
		return itens -> itens.forEach(System.out::println);
	}

	public FunctionItemProcessor<Integer, String> parOuImparProcessor() {
		return new FunctionItemProcessor<Integer, String>
		(item -> item % 2 == 0 ? String.format("Item %s é Par", item) : String.format("Item %s é Ímpar", item));
	}

	public IteratorItemReader<Integer> countUntilTenReader() {
		List<Integer> numbersOneToTen = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		return new IteratorItemReader<Integer>(numbersOneToTen.iterator());
	}
}

