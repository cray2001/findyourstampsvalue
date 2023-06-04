package com.findyourstampsvalue.aqa.util;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@Setter
public class HideMeItem implements Comparable<HideMeItem> {

    @SerializedName("lastseen")
    private int lastseen;

    @SerializedName("checks_up")
    private String checksUp;

    @SerializedName("anon")
    private String anon;

    @SerializedName("city")
    private String city;

    @SerializedName("ip")
    private String ip;

    @SerializedName("checks_down")
    private String checksDown;

    @SerializedName("ssl")
    private String ssl;

    @SerializedName("socks4")
    private String socks4;

    @SerializedName("socks5")
    private String socks5;

    @SerializedName("country_code")
    private String countryCode;

    @SerializedName("delay")
    private int delay;

    @SerializedName("port")
    private String port;

    @SerializedName("host")
    private String host;

    @SerializedName("country_name")
    private String countryName;

    @SerializedName("http")
    private String http;

    @SerializedName("cid")
    private String cid;

/*	Метод compareTo в Java сравнивает вызывающий объект с объектом, переданным в качестве параметра,
 	и возвращает в результате выполнения сравнения целое число:
	положительное, если вызывающий объект больше объекта, переданного в качестве параметра;
	отрицательное, если вызывающий объект меньше объекта, переданного в качестве параметра;
	нуль, если объекты равны.*/

    @Override
    public int compareTo(@NotNull HideMeItem obj) {

        int result = this.countryName.compareTo(obj.countryName);

        if (result == 0) {
            result = this.city.compareTo(obj.city);
        }

        if (result == 0) {
            result = this.delay - obj.delay;
        }

        //System.out.println("[" + this.countryName + ":" + this.city + ":" + this.delay + "] [" + obj.countryName + ":" + obj.city + ":" + obj.delay + "] " + result);

        return result;
    }

    @Override
    public String toString() {
        return String.join(":", countryName, city, String.valueOf(delay));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HideMeItem that = (HideMeItem) o;

        boolean result = this.countryName.equals(that.countryName) && this.city.equals(that.city);

        //System.out.println("[" + this.countryName + ":" + this.city + "] [" + that.countryName + ":" + that.city + "] " + result);

        return result;
    }
}