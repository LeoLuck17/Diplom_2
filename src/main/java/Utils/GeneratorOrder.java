package Utils;

import model.Order;

public class GeneratorOrder {
//    Faker faker = new Faker();
//    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("ru"), new RandomService());
//    String ingredientType = faker.bothify("########");
//    String name = faker.bothify("##########");
//    String price = fakeValuesService.bothify("???");

    String[] ingredientsIncorrect = {"61c05a71d1f82001bdaaa6d"};
    String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"};

    public Order getOrderWithIngredient() {
        return new Order(ingredients);
    }

    public Order getOrderWithoutIngredients() {
        return new Order();
    }

    public Order getOrderWithIncorrectIngredients() {
        return new Order(ingredientsIncorrect);
    }
}
