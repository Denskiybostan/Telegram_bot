package pro.sky.telegrambot.configuration;

import lombok.Data;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    public String botName;

    public String token;

}
