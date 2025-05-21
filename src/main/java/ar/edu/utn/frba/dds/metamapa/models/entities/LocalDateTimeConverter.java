package ar.edu.utn.frba.dds.metamapa.models.entities;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter extends AbstractBeanField<LocalDateTime, String> {
  @Override
  protected Object convert(String valor) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDateTime.parse(valor.trim(), formatter);
  }
}
