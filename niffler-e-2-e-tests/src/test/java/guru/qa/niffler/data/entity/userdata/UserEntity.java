package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private String firstname;
  private String surname;
  private String fullname;
  private byte[] photo;
  private byte[] photoSmall;

  public static UserEntity fromJson(UserJson json) {
    UserEntity ue = new UserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setCurrency(json.currency());
    ue.setFirstname(json.firstname());
    ue.setSurname(json.surname());
    ue.setFullname(json.fullname());

    return ue;
  }


  @Override
  public String toString() {
    return "UserEntity{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", currency=" + currency +
            ", firstname='" + firstname + '\'' +
            ", surname='" + surname + '\'' +
            ", fullname='" + fullname + '\'' +
            ", photo=" + Arrays.toString(photo) +
            ", photoSmall=" + Arrays.toString(photoSmall) +
            '}';
  }

}