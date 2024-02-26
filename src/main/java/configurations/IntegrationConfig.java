package configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import model.User;
import service.DateService;

import java.io.File;

@Configuration
public class IntegrationConfig {
    @Autowired
    DateService dateService;


    /** Входной канал
     * @return
     */
    @Bean
    public MessageChannel inChannel() {
        return new DirectChannel();
    }

    /** Выходной канал
     * @return
     */
    @Bean
    public MessageChannel logChannel() {
        return new DirectChannel();
    }

    /** Транслятор сообщений из канала inChannel в logChanel
     * преобразовывает сообщение типа User в строку с текущей датой в формате ГГГГ-ММ-ДД ЧЧ:мм<br>
     * id, name, login,<br>
     * пример: 2024-02-18 22:45:52:	id=5, name='Ivan', login='ivan87'
     * @return
     */
    @Bean
    @Transformer(inputChannel = "inChannel", outputChannel = "logChannel")
    public GenericTransformer<User, String> userToLogStringTranslator() {
        return user ->{
            StringBuilder returnStr = new StringBuilder(dateService.get()).append(":\taddUser{");
            return returnStr
                    .append("id=").append(user.getId())
                    .append(", name='").append(user.getName()).append("'")
                    .append(", login='").append(user.getLogin()).append("'}")
                    .toString();
        };
    }


    /** Обработчик сообщения из канала logChannel.
     * Записывает строку из сообщения в файл, добавляя данные в конец файла.
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "logChannel")
    public FileWritingMessageHandler logWriteHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
                new File("./log"));
        handler.setExpectReply(false);//отключение ожидания ответного сообщения после обработки входного сообщения
        handler.setFileExistsMode(FileExistsMode.APPEND);//добавление текста в конец файла
        handler.setAppendNewLine(true);//добавление перехода на новую строку в конце записи
        return handler;
    }

}
