
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class SeleniumI {

    /**
     * Login to teammate with prefixed username and password
     *
     * @param chromeDriver - The webPage controller
     */
    public void login(WebDriver chromeDriver) {
        chromeDriver.get("http://teammatesv4.appspot.com/");
        chromeDriver.findElement(By.id("btnInstructorLogin")).click();
        chromeDriver.findElement(By.id("identifierId")).sendKeys("dat261mbt@gmail.com");
        chromeDriver.findElement(By.id("identifierNext")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
        chromeDriver.findElement(By.name("password")).sendKeys("helloMBT2018");
        chromeDriver.findElement(By.id("passwordNext")).click();
    }

    /**
     * Got a specific tab
     *
     * @param chromeDriver - The webPage controller
     * @param tabCss       - a string representaing the CSS of tab in the page
     */
    public void goToTab(String tabCss, WebDriver chromeDriver) {
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(tabCss)));
        chromeDriver.findElement(By.cssSelector(tabCss)).click();
    }

    /**
     * Sort a column in a table from the webpage given the buttonID in the HTML code
     *
     * @param columnId     - the buttonID in the HTML code
     * @param chromeDriver - The webPage controller
     */
    public void sortColumn(String columnId, WebDriver chromeDriver) {
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(columnId)));
        WebElement element = chromeDriver.findElement(By.id(columnId));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(element);
        actions.click(element);
        actions.build().perform();
        //chromeDriver.findElement(By.id(columnId)).sendKeys(Keys.RETURN);
    }

    /**
     * Add a course given the ID and the NAME
     *
     * @param courseID     - A course ID can contain letters, numbers, fullstops, hyphens, underscores, and dollar signs.
     *                     It cannot be longer than 40 characters, cannot be empty and cannot contain spaces
     * @param courseName   - A course name must start with an alphanumeric character,and cannot contain any vertical bar (|) or percent sign (%)
     * @param chromeDriver - The webPage controller
     */
    public void addCourse(String courseID, String courseName, WebDriver chromeDriver) {
        //System.out.print("Second layer in adding phase \n");
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("courseid")));
        chromeDriver.findElement(By.name("courseid")).sendKeys(courseID);
        chromeDriver.findElement(By.name("coursename")).sendKeys(courseName);
        chromeDriver.findElement(By.id("btnAddCourse")).click();

    }

    /**
     * Add a course given the ID and the NAME then check if the course was added on the webpage
     *
     * @param id           - A course ID can contain letters, numbers, fullstops, hyphens, underscores, and dollar signs.
     *                     It cannot be longer than 40 characters, cannot be empty and cannot contain spaces
     * @param name         - A course name must start with an alphanumeric character,and cannot contain any vertical bar (|) or percent sign (%)
     * @param chromeDriver - The webPage controller
     */
    public boolean addCourseAndCheckIfAdded(String id, String name, WebDriver chromeDriver) {
        //System.out.print("First layer in adding phase \n");
        //We add the course
        addCourse(id, name, chromeDriver);
        // A sleep to allow page to refresh after adding course
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("th.align-center.no-print")));

        // We sort according to "Creation date", our added course will be on top of the list,
        // we extract name and ID and we compare to the added course
        String str = new String();
        sortColumn("button_sortcoursecreateddate", chromeDriver);
        sortColumn("button_sortcoursecreateddate", chromeDriver);
        List<WebElement> rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        // We extract the ID first
        WebElement dataElement = rows.get(0).findElement(By.xpath("td[position()=1]"));
        str = (dataElement.getText().toString());
        System.out.print("Theoretically added | name : " + name + " " + "ID : " + str + " " + (str.equals(id)) + "\n");
        if (str.equals(id)) {
            dataElement = rows.get(0).findElement(By.xpath("td[position()=2]"));
            str = (dataElement.getText().toString());
            System.out.print("True: ID in active list | name : " + name + " " + "ID : " + id + " " +(str.equals(name))+" "+ str + "\n");
            return str.equals(name);
        } else {
            System.out.print("False: ID not in active list | name : " + name + " " + "ID : " + id + " " + false + "\n");
            return false;
        }
    }

    /**
     * delete the top course in the list of active courses
     *
     * @param chromeDriver - The webPage controller
     */
    public void deleteCourse(WebDriver chromeDriver) {

        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("courseid")));
        // Scroll to see the table
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 350)");

        // Click on Delete button then confirm the "OK" in the POP-UP
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tableActiveCourses")));
        List<WebElement> rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        WebElement element = rows.get(0).findElement(By.xpath("td[8]/a[5]"));
        Actions actions = new Actions(chromeDriver);
        actions.moveToElement(element);
        actions.click(element);
        actions.build().perform();
        // Ok button
        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")));
        } catch (InterruptedException e) {
            actions.build().perform();
            e.printStackTrace();
        }
        chromeDriver.findElement(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")).click();
    }


    /**
     * delete all courses in the list of active courses
     *
     * @param chromeDriver - The webPage controller
     */
    public void deleteAllCourses(WebDriver chromeDriver) {

        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("courseid")));

        // Scroll to see the table
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 350)");

        // Click on Delete button then confirm the "OK" in the POP-UP for all courses iteratively
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tableActiveCourses")));
        List<WebElement> rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));

        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> tempRows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
            WebElement element = tempRows.get(0).findElement(By.xpath("td[8]/a[5]"));
            Actions actions = new Actions(chromeDriver);
            actions.moveToElement(element);
            actions.click(element);
            actions.build().perform();
            // Ok button
            try {
                Thread.sleep(1000);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")));
            } catch (InterruptedException e) {
                actions.build().perform();
                e.printStackTrace();
            }
            chromeDriver.findElement(By.xpath("/html/body/div[4]/div/div/div[3]/button[2]")).click();
        }
    }
}