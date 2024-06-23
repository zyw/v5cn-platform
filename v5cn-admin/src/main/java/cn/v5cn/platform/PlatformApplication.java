package cn.v5cn.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class PlatformApplication {
    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication application =new SpringApplication(PlatformApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  v5cn-platform启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
