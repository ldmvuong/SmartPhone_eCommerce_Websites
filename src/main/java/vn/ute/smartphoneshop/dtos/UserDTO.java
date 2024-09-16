package vn.ute.smartphoneshop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserDTO {
    @JsonProperty("userId")
    private int id;
    @JsonProperty("username")
    @NotNull(message = "Username can't blank")
    private String userName;
    private String firstName;
    private String lastName;
    @NotNull(message = "Email is required")
    private String email;
    private String address;
    private String phone;
    private int roleId;
}
