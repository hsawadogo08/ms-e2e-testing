package bf.hsawadogo.customerservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CustomerDTO {
    private Long id;
    @NotNull @Size(min = 3, max = 50)
    private String firstName;
    @NotNull @Size(min = 3)
    private String lastName;
    @NotNull
    private String email;
}
