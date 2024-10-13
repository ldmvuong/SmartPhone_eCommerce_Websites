package vn.ute.smartphoneshop.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull(message = "Role name is required")
    public String roleName;
}
