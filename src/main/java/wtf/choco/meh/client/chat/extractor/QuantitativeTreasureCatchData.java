package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;

import org.apache.commons.lang3.math.NumberUtils;

public record QuantitativeTreasureCatchData(String name, int quantity) {

    static QuantitativeTreasureCatchData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String name = matcher.group("name");
        int quantity = NumberUtils.toInt(matcher.group("quantity"));

        return new QuantitativeTreasureCatchData(name, quantity);
    }

}
