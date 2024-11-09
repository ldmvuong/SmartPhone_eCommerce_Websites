package vn.ute.smartphoneshop.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProfileUpdateRequest {
    private int userId;

    @JsonProperty("username")
    @NotBlank(message = "Username can't be blank")
    private String userName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String address;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private int roleId;
}
