package wtf.choco.meh.client.chat.extractor;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;

import org.apache.commons.lang3.math.NumberUtils;

public record MythicalFishCatchData(String name, int weight) {

    static MythicalFishCatchData fromMatcher(Matcher matcher) {
        Preconditions.checkArgument(matcher.hasMatch(), "Expected matcher to already have matched");

        String name = matcher.group("name");
        int weight = NumberUtils.toInt(matcher.group("weight"));

        return new MythicalFishCatchData(name, weight);
    }

}
