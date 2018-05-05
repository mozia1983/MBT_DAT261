import org.quicktheories.WithQuickTheories;
import org.testng.annotations.Test;

import java.util.Date;

import static org.quicktheories.generators.Generate.*;

public class PropTest implements WithQuickTheories {

    @Test
    public void test() {
        qt()
                .forAll(range(1, 102), constant(7))
                .check((i, c) -> i + c >= 7);
    }



}
