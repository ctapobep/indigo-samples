import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.epam.indigo.IndigoRenderer;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import org.junit.gen5.api.Disabled;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;

import static io.qala.datagen.RandomValue.length;
import static io.qala.datagen.StringModifier.Impls.prefix;

@RunWith(JUnit5.class)
public class IndigoTest {
    @Test IndigoObject enumerateReaction() {
        Indigo i = new Indigo();
        IndigoObject reaction = i.loadQueryReactionFromFile(simpleRxnFile());

        //loading reactants
        IndigoObject monomers = i.createArray();
        monomers.arrayAdd(i.createArray());
        monomers.arrayAdd(i.createArray());

        for (IndigoObject indigoObject : i.iterateSDFile(simpleMolFile()))
            monomers.at(0).arrayAdd(indigoObject);
        for (IndigoObject indigoObject : i.iterateSDFile(simpleMolFile()))
            monomers.at(1).arrayAdd(indigoObject);

        // getting products and enumerating over them
        IndigoObject enumeration = i.reactionProductEnumerate(reaction, monomers);
        for(IndigoObject rxn : enumeration.iterateArray()) {
            System.out.println(rxn.rxnfile());
        }
        return enumeration;
    }

    @Test void rendingReactionsAndReactantsAndProducts() {
        IndigoObject enumeration = enumerateReaction();
        Indigo indigo = enumeration.getIndigo();

        IndigoRenderer renderer = new IndigoRenderer(indigo);
        indigo.setOption("render-label-mode", "all");
        indigo.setOption("render-output-format", "png");
        indigo.setOption("render-bond-length", 25);
        indigo.setOption("render-coloring", true);
        indigo.setOption("render-margins", 0, 0);

        for (IndigoObject rxn : enumeration.iterateArray()) {
            for (IndigoObject reactant : rxn.iterateReactants())
                renderer.renderToFile(reactant, randomPngFile("reactant"));
            for (IndigoObject product : rxn.iterateProducts())
                renderer.renderToFile(product, randomPngFile("product"));
            renderer.renderToFile(rxn, randomPngFile("reaction"));
        }
    }

    @Disabled("Crashes JVM") void renderingReactionsIntoCdxml() {
        Indigo i = new Indigo();
        IndigoObject rxn = i.loadQueryReactionFromFile(simpleRxnFile());
        IndigoRenderer renderer = new IndigoRenderer(i);
        i.setOption("render-label-mode", "all");
        i.setOption("render-output-format", "cdxml");
        i.setOption("render-bond-length", 25);
        i.setOption("render-coloring", true);
        i.setOption("render-margins", 0, 0);
        renderer.renderToFile(rxn, randomCdxmlFile("reaction"));
    }

    private String simpleRxnFile() {
        //noinspection ConstantConditions
        return ClassLoader.getSystemClassLoader().getResource("simple-reaction/2-circles.rxn").getFile();
    }
    private String simpleMolFile() {
        //noinspection ConstantConditions
        return ClassLoader.getSystemClassLoader().getResource("simple-reaction/2-circles.mol").getFile();
    }
    private String randomPngFile(String prefix) {
        return "target/" + length(prefix.length() + 20).with(prefix(prefix + "-")).alphanumeric() + ".png";
    }

    private String randomCdxmlFile(String prefix) {
        return "target/" + length(prefix.length() + 20).with(prefix(prefix + "-")).alphanumeric() + ".xml";
    }
}
