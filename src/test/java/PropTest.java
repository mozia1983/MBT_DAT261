
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.quicktheories.WithQuickTheories;
import org.testng.annotations.Test;
import org.quicktheories.core.Gen;

import java.util.ArrayList;

import static org.quicktheories.generators.SourceDSL.*;
import static org.testng.AssertJUnit.assertEquals;

public class PropTest implements WithQuickTheories {

    private static ArrayList<String> addedNames = new ArrayList<>();
    private static ArrayList<String> addedIds = new ArrayList<>();

    //to check if the id format respect the specification
    public boolean regMatchId(String id) {
        boolean b = id.matches("([A-Z]|[a-z]|[0-9]|-|_|\\.|\\$)+");
        System.out.print("The generated id: " + id + " Matched: " + b + "\n");
        return b;
    }
    //to check if the name format respect the specification
    public boolean regMatchName(String name) {
        boolean b = name.matches("^([A-Z]|[a-z]|[0-9])([^\\|%]*)");
        System.out.print("The generated name: " + name + " Matched: " + b + "\n");
        return b;

    }

    // A test for adding a course
    @Test
    public void respectedCourseFormatIsAdded() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mozia\\Desktop\\Trial\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        SeleniumI seleniumI = new SeleniumI();
        seleniumI.login(driver);
        seleniumI.goToTab("a.nav.courses", driver);
        // created just to be able to call a method from Course clas
        Course course = new Course("hello", "hi");
        // the test is aborting almost of the times because the generators are almost of the time rejected because they don't match the regex
        // we must investigate how to generate Strings with less characters then LatinAlphabet.
        qt()
                .forAll(strings().basicLatinAlphabet().ofLength(4), strings().basicLatinAlphabet().ofLength(4))
                .assuming((id, name) -> regMatchId(id)
                        && regMatchName(name)
                        && course.addToCurrentAndAssess(id, name, addedNames, addedIds)) //we check that the course was not added before
                .check((id, name) -> seleniumI.addCourseAndCheckIfAdded(id, name, driver));

        seleniumI.deleteAllCourses(driver);
    }
}

