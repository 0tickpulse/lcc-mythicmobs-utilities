package me.tick.lccmythicmobsutilities.components.placeholders;

import io.lumine.mythic.core.skills.placeholders.Placeholder;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;
import me.tick.lccmythicmobsutilities.modules.PlaceholderUtilities;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

public class MetaPlaceholders implements Placeholders {
    @Override
    public void register() {
        PlaceholderUtilities.registerPlaceholder(new String[]{"jexl"}, Placeholder.meta((meta, arg) -> {
            JexlEngine jexl = new JexlBuilder().create();
            JexlContext context = new MapContext();
            context.set("meta", meta);
            try {
                return jexl.createExpression(arg).evaluate(context).toString();
            } catch (Exception e) {
                LccMythicmobsUtilities.error("Error while evaluating JEXL expression: " + arg);
                LccMythicmobsUtilities.error(e);
                return "";
            }
        }), new ComponentEntryGenerator()
                .setName("jexl.<expression>")
                .setDescription("Parses a [JEXL](https://commons.apache.org/proper/commons-jexl/) expression and returns the result. Use \"meta\" to access the meta object.")
                .setAuthor("0TickPulse")
                .generate());
    }
}
