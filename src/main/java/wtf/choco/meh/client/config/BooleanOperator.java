package wtf.choco.meh.client.config;

import it.unimi.dsi.fastutil.booleans.BooleanBinaryOperator;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

import org.jetbrains.annotations.NotNull;

public enum BooleanOperator implements BooleanBinaryOperator, Translatable {

    AND((a, b) -> a && b),
    OR((a, b) -> a || b);

    private final BooleanBinaryOperator operator;

    private BooleanOperator(BooleanBinaryOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean apply(boolean a, boolean b) {
        return operator.apply(a, b);
    }

    @NotNull
    @Override
    public String getKey() {
        return "meh.config.boolean_operator." + name().toLowerCase();
    }

}
