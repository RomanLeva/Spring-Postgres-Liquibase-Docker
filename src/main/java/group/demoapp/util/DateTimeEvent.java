package group.demoapp.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DateTimeEvent {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "'Дата' yyyy-MM-dd' Время 'HH:mm:ss:SSS", locale = "ru_RU")
    private LocalDateTime dateTime;

    public DateTimeEvent(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
