package vn.ute.smartphoneshop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserDTO {
    private int userId;
    @JsonProperty("username")
    @NotNull(message = "Username can't blank")
    private String userName;
    private String firstName;
    private String lastName;
    @NotNull(message = "Email is required")
    private String email;
    private String address;
    private String phone;
    private String password;
    private String confirmPassword;
    private int roleId;
}
