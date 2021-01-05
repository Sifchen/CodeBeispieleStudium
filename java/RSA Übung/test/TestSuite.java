import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Klasse für JUnit 4.x, mit der alle grundlegenden Tests durchgeführt werden.
 * 
 * @author kar / mhe / ...
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ExampleTests.class,
    RsaTests.class,
    FileIoTests.class
    // Weitere Testklassen hier ergänzen
})

public class TestSuite {

    /**
     * Methode zum einfachen Starten des Tests von der Kommandozeile.
     * 
     * @param args
     *            Kommandozeilenargumente.
     */
    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.main(TestSuite.class.getName());
    }
}
