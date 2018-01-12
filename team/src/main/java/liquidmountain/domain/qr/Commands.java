package liquidmountain.domain.qr;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class Commands {
    // For now both commands are grouped and have the same timeout
    private static final String POOL_NAME = "Qr";
    private static final int TIMEOUT_IN_MS = 2000;

    public static class QrCommand extends HystrixCommand<Image> {
        private QrGenerator qr;

        public QrCommand(QrGenerator qrGenerator) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(POOL_NAME)).andCommandPropertiesDefaults
                    (HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(TIMEOUT_IN_MS)));
            this.qr = qrGenerator;
        }

        @Override
        public Image run() {
            return qr.gen();
        }
    }

    public static class LogoCommand extends HystrixCommand<LogoId> {
        private LogoIdGenerator logo;

        public LogoCommand(LogoIdGenerator logoIdGenerator) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(POOL_NAME)).andCommandPropertiesDefaults
                    (HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(TIMEOUT_IN_MS)));
            this.logo = logoIdGenerator;
        }

        @Override
        public LogoId run() {
            return logo.genId();
        }
    }
}
