package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class TelegramBotConfiguration extends TelegramLongPollingBot {
    final BotConfig botConfig;
    public TelegramBotConfiguration (BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Value("${telegram.bot.token}")
    private String token(){
        return botConfig.getToken();
    }

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token());
        bot.execute(new DeleteMyCommands());
        return bot;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                default:
                    try {
                        sendMessage(chatId, "Sorry interpreter was not recognized");
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    private void startCommandReceived(long chatId, String name) throws TelegramApiException {
        String answer = "Hi, " + name + " nice to meet you";
        sendMessage(chatId, answer);
    }
    private void sendMessage(long chatId, String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }




    }
}
