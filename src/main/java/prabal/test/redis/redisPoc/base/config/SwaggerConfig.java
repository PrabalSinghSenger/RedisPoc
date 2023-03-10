//package prabal.test.redis.redisPoc.base.config;
//
//import java.util.Collections;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.swagger.models.Contact;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//	/*
//	 * @Bean public Docket api() { return new
//	 * Docket(DocumentationType.SWAGGER_2).select()
//	 * .apis(RequestHandlerSelectors.basePackage(
//	 * "prabal.test.redis.redisPoc.cache.controller"))
//	 * .paths(PathSelectors.any()).build().apiInfo(apiInfo()); }
//	 */
//		  
//	/*
//	 * @Bean public Docket api() { return new
//	 * Docket(DocumentationType.SWAGGER_2).select()
//	 * .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//	 * .paths(PathSelectors.any()).build().apiInfo(apiInfo()); }
//	 * 
//	 */
//		  
//		  private ApiInfo apiInfo() { return new ApiInfo("Cache POC", "Cache",
//		  "api version 1.0.0", "Copyright © 2022 . All Rights Reserved.", "Adidas",
//		  "www.adidas.co.in", "support.opensource.redis.cache@adidas.co.in");
//		  
//		  
//		  
//		  }
//		 
//  
//	    @Bean
//	    public Docket api() {
//	        return new Docket(DocumentationType.OAS_30)
//	                .apiInfo(apiInfo())
//	                .select()
//	                .apis(RequestHandlerSelectors.any())
//	                .paths(PathSelectors.any())
//	                .build();
//	    }
//}
