package com.example.telegram_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData implements Serializable {

	String name;
	String gender;
	String color;
	String movie;
	String song;
	int age;
	int number;

	@Override
	public String toString() {
		return "Имя = '" + name + '\'' +
				", Пол = '" + gender + '\'' +
				", Цвет = '" + color + '\'' +
				", Фильм = '" + movie + '\'' +
				", Песня = '" + song + '\'' +
				", Возвраст = " + age +
				", Номер = " + number;
	}
}
