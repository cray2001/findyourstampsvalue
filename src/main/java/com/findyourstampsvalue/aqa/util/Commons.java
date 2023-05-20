package com.findyourstampsvalue.aqa.util;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;

/**
 * Методы не использующие локаторы
 */
public class Commons {

    public static void getRandomElementFromCollection(ElementsCollection collection) {

        int index = (int) (Math.random() * collection.size());
        index = index < collection.size() ? index : index - 1;
        collection.get(index).shouldBe(Condition.exist).click();
    }
}
