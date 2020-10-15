package com.weiqing.noctorrosmssend;

import com.weiqing.noctorrosmssend.core.InitTask;
import com.weiqing.noctorrosmssend.util.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author Rodney Cheung
 * @date 10/15/2020 9:49 AM
 */
@SpringBootApplication
public class NoctorroSmsSendApplication {

	public static void main(String[] args) {
		SpringApplication application=new SpringApplication(NoctorroSmsSendApplication.class);
		application.addListeners(new AppStartListener());
		application.run(args);
	}

}

class AppStartListener implements ApplicationListener<ApplicationReadyEvent> {
	/**
	 * spring boot 加载完成后的回调，这里初始化系统数据。
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		SpringContextUtils.setApplicationContext(event.getApplicationContext());
		SpringContextUtils.getBean(InitTask.class).startThread();
	}
}