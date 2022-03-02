package edu.ecnu.Woodpecker.constant;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
/**
 * @description:数据分布类型的枚举，包括unique、uniform normal zipf
 */
public enum DistributionType {
	UNIQUE("UNIQUE"), UNIFORM("UNIFORM"), NORMAL("NORMAL"), ZIPF("ZIPF");
	public String value = null;
	
	private DistributionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	 /**
     * Returns an enum constant of the specified value
     * 
     * @param value
     * @return
     * @throws NoSuchElementException
     */
    public DistributionType of(String value) throws NoSuchElementException
    {
        Stream<DistributionType> stream = Stream.of(DistributionType.class.getEnumConstants());
        return stream.filter(ele -> ele.getValue().equals(value)).findAny().get();
    }
}
