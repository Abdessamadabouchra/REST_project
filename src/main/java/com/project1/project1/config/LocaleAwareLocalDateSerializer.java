package com.project1.project1.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocaleAwareLocalDateSerializer extends StdSerializer<LocalDate> {
    private static final DateTimeFormatter FRENCH_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocaleAwareLocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();

        if (locale.getLanguage().equalsIgnoreCase("fr") || locale.getLanguage().equalsIgnoreCase("fr-FR")) {
            gen.writeString(value.format(FRENCH_FORMATTER));
        } else {
            gen.writeString(value.format(ISO_FORMATTER));
        }
    }
}