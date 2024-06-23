package cn.v5cn.platform.framework.web.config;

import cn.v5cn.platform.framework.core.utils.SpringUtils;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.task.VirtualThreadTaskExecutor;

/**
 * Undertow自动配置
 */
@AutoConfiguration
public class UndertowAutoConfiguration implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    /**
     * 设置 Undertow 的 websocket 缓冲池
     */
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        // 默认不直接分配内存 如果项目中使用了 websocket 建议直接分配
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 512));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
            // 使用虚拟线程
            if (SpringUtils.isVirtual()) {
                VirtualThreadTaskExecutor executor = new VirtualThreadTaskExecutor("undertow-");
                deploymentInfo.setExecutor(executor);
                deploymentInfo.setAsyncExecutor(executor);
            }
        });
    }
}
