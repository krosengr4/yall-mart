package com.pluralsight.yallmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YallmartApplication {

	public static void main(String[] args) {
		SpringApplication.run(YallmartApplication.class, args);

		System.out.println("http://localhost:63342/yallmart/FRONTEND/index.html?_ijt=q4sdurrvt8jme3hcfgu72843dp&_ij_reload=RELOAD_ON_SAVE");
	}

}
