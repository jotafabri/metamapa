package ar.edu.utn.frba.dds.metamapa.converters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.opencsv.bean.AbstractBeanField;

public class LocalDateTimeConverter extends AbstractBeanField<LocalDateTime, String> {
  @Override
  protected Object convert(String valor) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(valor.trim(), formatter).atStartOfDay();
  }
}
