package Utils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import model.ChangeableDataUser;
import model.User;
import model.UserCredentials;

import java.util.Locale;

public class GeneratorRandomData {
    Faker faker = new Faker();
    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("ru"), new RandomService());
    String email = fakeValuesService.bothify("?????##@gmail.com");
    String password = faker.bothify("########");
    String name = faker.name().fullName();

    public GeneratorRandomData() {
    }

    public User randomUser() {
        return new User(email, password, name);
    }

    public UserCredentials randomCredentials() {
        return new UserCredentials(email, password);
    }

    public ChangeableDataUser randomDataUser() {
        return new ChangeableDataUser(email, name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
