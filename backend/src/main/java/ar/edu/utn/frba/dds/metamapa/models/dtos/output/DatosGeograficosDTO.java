package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatosGeograficosDTO {
  private List<String> categorias;
  private List<String> paises;
  private List<String> provincias;
  private List<String> localidades;
}
