package bot;

import mail.EmailSetting;
import mail.ExceptionEmail;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotSetting extends TelegramLongPollingBot {

    ExceptionEmail exceptionEmail = new ExceptionEmail();

    private static final EmailSetting sender = new EmailSetting("usertelegram98", "Telegram98");

    public List<String> list = new ArrayList<>();
    private String email;
    private String subject;
    public static String text;

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            switch (message.getText()) {
                case "/start":
                    sendMsg(message, "Сао\uD83D\uDD90, " + message.getChat().getUserName() + "\uD83D\uDE43\n" +
                            "Натискай  '/write',  та пиши листа\uD83E\uDD13\uD83D\uDCDD");
                    break;
                case "/write":
                    list.clear();
                    sendMsg(message, "Сао\uD83D\uDD90, введи такі дані, як:\n" +
                            "1️⃣ - Електронна адреса✉\n" +
                            "2️⃣ - Тема листа\uD83D\uDD16\n" +
                            "3️⃣ - Текст листа\uD83D\uDCDD\n" +
                            "Після цього натисни '/sendsms', та відправ листа!");

                    break;
                case "/sendsms":

                    email = list.get(0);
                    subject = list.get(1);
                    text = list.get(2);

                    //Перевірка почти на коректність
                    exceptionEmail.setAuditEmail(email);
                    exceptionEmail.checkEmail();
                    if (exceptionEmail.checkEmail()) {
                        sendMsg(message, "❗️Електронна адреса не містить в собі '@' або '.'❗️");
                        list.clear();
                        break;
                    }

                    sender.send(subject, "usertelegram98@gmail.com", email);
                    sendMsg(message, "\uD83D\uDCE8✈");
                    break;
                case "/overwritedata":
                    sendMsg(message, "Дані видалено!\uD83D\uDDDE\uD83D\uDDD1");
                    list.clear();
                    break;
                default:
                    list.add(message.getText());
            }
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/write"));
        keyboardFirstRow.add(new KeyboardButton("/sendsms"));
        keyboardFirstRow.add(new KeyboardButton("/overwritedata"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    @Override
    public String getBotUsername() {
        return "emailsend_bot";
    }

    @Override
    public String getBotToken() {
        return "1723026063:AAE9C7Mz5EV4wn8DUtaFQUuQRqLf18PZuyA";
    }

}
