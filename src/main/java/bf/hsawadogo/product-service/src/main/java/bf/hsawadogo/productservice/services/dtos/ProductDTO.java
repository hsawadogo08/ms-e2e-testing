package bf.hsawadogo.productservice.services.dtos;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProductDTO {
    @Id
    private Long id;
    @NotNull @NotEmpty
    private String code;
    @NotNull
    private String libelle;
}
