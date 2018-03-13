//From https://dzone.com/articles/creating-a-rest-api-with-java-and-spring

package org.safs.rest.sample;

import org.safs.rest.sample.model.Customer;
import org.safs.rest.sample.model.Invoice;
import org.safs.rest.sample.model.Item;
import org.safs.rest.sample.model.Product;
import org.safs.rest.sample.repository.CustomerRepository;
import org.safs.rest.sample.repository.InvoiceRepository;
import org.safs.rest.sample.repository.ItemRepository;
import org.safs.rest.sample.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;

@EnableEntityLinks
@EnableHypermediaSupport(type = HypermediaType.HAL)
@SpringBootApplication
@EntityScan("org.safs.rest.sample.model")
@EnableJpaRepositories("org.safs.rest.sample.repository")
public class RestSampleApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(RestSampleApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(RestSampleApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(RestSampleApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository customerRep,
			                      InvoiceRepository invoiceRep,
			                      ItemRepository itemRep,
			                      ProductRepository productRep){
		return (args) -> {
			if(customerRep.count()>0){
				log.debug("======================Customer Data=========================");
				for(Customer i: customerRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(productRep.count()>0){
				log.debug("======================Product Data=========================");
				for(Product i: productRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(invoiceRep.count()>0){
				log.debug("======================Invoice Data==========================");
				for(Invoice i: invoiceRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
			if(itemRep.count()>0){
				log.debug("======================Item Data=============================");
				for(Item i: itemRep.findAll()){
					log.debug(i.toString());
				}
				log.debug("============================================================");
			}
		};
	}

}