package peoplehere.peoplehere.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.locationtech.jts.geom.Point;
import peoplehere.peoplehere.util.CustomLocalTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import peoplehere.peoplehere.util.PointSerializer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // For handling Point type serialization
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Point.class, new PointSerializer());
        mapper.registerModule(simpleModule);

        // For handling Java Time (Date and Time API) serialization and deserialization
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalTime.class, new CustomLocalTimeDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, LOCAL_DATETIME_SERIALIZER);
        mapper.registerModule(javaTimeModule);

        return mapper;
    }
}
