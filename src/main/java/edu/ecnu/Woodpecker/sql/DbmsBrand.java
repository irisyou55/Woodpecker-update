package edu.ecnu.Woodpecker.sql;

import java.util.stream.Stream;

import edu.ecnu.Woodpecker.constant.ConfigConstant;

public enum DbmsBrand
{
    MYSQL(ConfigConstant.MYSQL),
    TIDB(ConfigConstant.TIDB),
    POSTGRESQL(ConfigConstant.POSTGRESQL);

    private String brand = null;

    private DbmsBrand(String brand)
    {
        this.brand = brand;
    }

    public String getBrand()
    {
        return brand;
    }

    /**
     * Return brand
     * @param brand
     * @return
     */
    public static String getBrand(String brand)
    {
        return  brand;
    }

    public String toString()
    {
        return brand;
    }

    /**
     * Return a DbmsBrand object according specified value
     * @param value
     * @return
     */
    public static DbmsBrand of(String value)
    {
        String trimBrand = value.trim().toLowerCase();
        Stream<DbmsBrand> stream = Stream.of(DbmsBrand.class.getEnumConstants());
        return stream.filter(ele -> ele.getBrand().equals(trimBrand)).findAny().get();
    }
}
