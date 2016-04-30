import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;

@RunWith(JUnit5.class)
public class IndigoTest {
    @Test void enumerateReaction() {
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
    }

    private String simpleRxnFile() {
        //noinspection ConstantConditions
        return ClassLoader.getSystemClassLoader().getResource("simple-reaction/2-circles.rxn").getFile();
    }
    private String simpleMolFile() {
        //noinspection ConstantConditions
        return ClassLoader.getSystemClassLoader().getResource("simple-reaction/2-circles.mol").getFile();
    }
}
