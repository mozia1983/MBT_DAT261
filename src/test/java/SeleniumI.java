
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
        // improvement to clear the box each time because when it fails to add a course the course attributes remain in the
        // input boxes on the web application
        WebElement id = chromeDriver.findElement(By.name("courseid"));
        id.clear();
        id.sendKeys(courseID);
        WebElement name = chromeDriver.findElement(By.name("coursename"));
        name.clear();
        name.sendKeys(courseName);

        chromeDriver.findElement(By.id("btnAddCourse")).click();
        // A sleep to allow page to refresh after adding course
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add a course given the ID and the NAME then check if the course was added on the webpage
     *
     * @param id           - A course ID can contain letters, numbers, fullstops, hyphens, underscores, and dollar signs.
     *                     It cannot be longer than 40 characters, cannot be empty and cannot contain spaces
     * @param name         - A course name must start with an alphanumeric character,and cannot contain any vertical bar (|) or percent sign (%)
     * @param chromeDriver - The webPage controller
     * @return boolean     - True if the course appear in the active course list, false in the other case.
     */
    public boolean addCourseAndCheckIfAdded(String id, String name, WebDriver chromeDriver) {
        //System.out.print("First layer in adding phase \n");
        //We add the course
        addCourse(id, name, chromeDriver);
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
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
    public void deleteOneCourse(WebDriver chromeDriver) {

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

    /**
     * Add a new session given the name and due date
     *
     * @param sessionName     - A sessionName A/An feedback session name must start with
     *                        an alphanumeric character, and cannot contain any vertical bar (|) or percent sign (%).
     *                        it cannot contain the following special html characters in brackets: (< > " / ' &).
     * @param sessionDate   - A  due date must be later then opening date.
     * @param chromeDriver - The webPage controller
     */
    public  void addOneSession(String sessionName,String sessionDate, WebDriver chromeDriver) {
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fsname")));

        WebElement name = chromeDriver.findElement(By.id("fsname"));
        name.clear();
        name.sendKeys(sessionName);

        WebElement date = chromeDriver.findElement(By.id("enddate"));
        date.clear();
        date.sendKeys(sessionDate);

        chromeDriver.findElement(By.id("fsname")).click();
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 250)"); // if the element is on bottom.
        chromeDriver.findElement(By.id("button_submit")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button_done_editing")));
        jse.executeScript("scroll(0, 4800)");
        chromeDriver.findElement(By.id("button_done_editing")).click();
    }

    /**
     * Add a new session given the name and due date and verify that it was added
     *
     * @param sessionName   - A sessionName A/An feedback session name must start with
     *                        an alphanumeric character, and cannot contain any vertical bar (|) or percent sign (%).
     *                       it cannot contain the following special html characters in brackets: (< > " / ' &).
     * @param closingDate   - A  due date must be later then opening date.
     * @param chromeDriver  - The webPage controller
     * @return boolean      - True if the course appear in the active course list, false in the other case.
     */
    public boolean addSessionAndCheckIfAdded(String sessionName, String closingDate, WebDriver chromeDriver) {
        //System.out.print("First layers in adding phase \n");
        //We add the course
        System.out.print("The Date as passed "+ closingDate);
        addOneSession(sessionName, closingDate, chromeDriver);
        // A sleep to allow page to refresh after adding course
        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("form_feedbacksession")));

        // We sort according to "Creation date", our added course will be on top of the list,
        // we extract name and ID and we compare to the added course
        String str = new String();
        sortColumn("button_sortname", chromeDriver);
        sortColumn("button_sortname", chromeDriver);
        List<WebElement> rows = chromeDriver.findElements(By.xpath("//table[@id='table-sessions']/tbody/tr"));
        // We extract the ID first
        WebElement dataElement = rows.get(0).findElement(By.xpath("td[position()=2]"));
        str = (dataElement.getText().toString());
        //System.out.print("Theoretically added | name : " + sessionName + " " + "ID : " + str + " " + (str.equals(id)) + "\n");
        if (str.equals(sessionName)) {
            //dataElement = rows.get(0).findElement(By.xpath("td[position()=2]"));
            //str = (dataElement.getText().toString());
            //System.out.print("True: ID in active list | name : " + name + " " + "ID : " + id + " " +(str.equals(name))+" "+ str + "\n");
            //return str.equals(name);
            System.out.print("True: Session in session list \n");
            return true;
        } else {
            System.out.print("False: Session not in session list | name : " + sessionName + " " + "found : " + str + " " + false + "\n");
            return false;
        }
    }

    /**
     * Get the count of courses
     * @param chromeDriver - The webPage controller
     * @return int         - The number of courses in the "active courses" list
     */
    public int getCoursesCount(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("th.align-center.no-print")));
        ArrayList<String> data = new ArrayList<>();
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        for (WebElement row : rows) {
            WebElement dataElement = row.findElement(By.xpath("td[position()=2]"));  // The courses names are in the second element
            data.add(dataElement.getText().toString());
        }
        if (data.get(0).isEmpty()){return 0;} // this test is mandatory because empty list does not mean empty html table
        else return data.size();
    }

    /**
     * check if the the number of courses still the same after surfing between tabs a random number of times
     *
     * @param i                 - The random number of surfing times
     * @param initialNbCourse   - The number of added courses initially
     * @param chromeDriver      - The webPage controller
     * @return boolean          - True if the number of courses appearing in the active course list is the same, false in the other case.
     */
    public boolean checkForCourseAfterSurfing(int i,int initialNbCourse, WebDriver chromeDriver) {
        int j = i % 5;
        switch (j) {
            case 0: goToTab("a.nav.home", chromeDriver);
                return true;
            case 1: goToTab("a.nav.courses", chromeDriver);
                    int count = getCoursesCount(chromeDriver);
                        if (count==initialNbCourse) return true;
                        else return false;
            case 2: goToTab("a.nav.evaluations", chromeDriver);
                return true;
            case 3: goToTab("a.nav.students", chromeDriver);
                return true;
            case 4: goToTab("a.nav.search", chromeDriver);
                return true;
        }
        System.out.print("Why i am here ?");
        return false;
    }

    /**
     * Get the count of courses
     * @param chromeDriver - The webPage controller
     * @return int         - The number of sessions in the "Sessions" list
     */
    public int getSessionsCount(WebDriver chromeDriver){
        WebDriverWait wait = new WebDriverWait(chromeDriver,30);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("table-sessions")));
        ArrayList<String> data = new ArrayList<>();
        // From the table rows you construct a list called rows where each element is a row.
        List<WebElement> rows =  chromeDriver.findElements(By.xpath("//table[@id='table-sessions']/tbody/tr"));
        for (WebElement row : rows) {
            WebElement dataElement = row.findElement(By.xpath("td[position()=2]")); // The sessions names are in the second element
            data.add(dataElement.getText().toString());
        }
        if (data.get(0).isEmpty()){return 0;} // this test is mandatory because empty list does not mean empty html table
        else return data.size();
    }

    /**
     * check if the the number of sessions still the same after surfing between tabs a random number of times
     *
     * @param i                 - The random number of surfing times
     * @param initialNbSessions   - The number of added sessions initially
     * @param chromeDriver      - The webPage controller
     * @return boolean          - True if the number of sessions appearing in the active course list is the same, false in the other case.
     */
    public boolean checkForSessionsAfterSurfing(int i,int initialNbSessions, WebDriver chromeDriver) {
        int j = i % 5;
        switch (j) {
            case 0: goToTab("a.nav.home", chromeDriver);
                return true;
            case 1: goToTab("a.nav.courses", chromeDriver);
                return true;
            case 2: goToTab("a.nav.evaluations", chromeDriver);
                int count = getSessionsCount(chromeDriver);
                    if (count==initialNbSessions) return true;
                    else return false;
            case 3: goToTab("a.nav.students", chromeDriver);
                return true;
            case 4: goToTab("a.nav.search", chromeDriver);
                return true;
        }
        System.out.print("Why i am here ?");
        return false;
    }

    /**
     * check if the the number of sessions still the same after surfing between tabs a random number of times
     *
     * @param i                 - The random number of alternative sort clicks
     * @param chromeDriver      - The webPage controller
     * @return boolean          - True if the sort of courses list remain consistent however the number of clicks, false if not.
     */
    public boolean courseIDSort(int i, WebDriver chromeDriver) {

        goToTab("a.nav.evaluations",chromeDriver);
        goToTab("a.nav.courses",chromeDriver);
        JavascriptExecutor jse = (JavascriptExecutor) chromeDriver;
        jse.executeScript("scroll(0, 350)");

        WebElement dataElement;
        List<WebElement> rows;
        List<String> idSortedAscending = new ArrayList<>();
        List<String> idSortedDescending = new ArrayList<>();

        // We get the list of courses sorted in ascending order ( by default)
        rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        for ( int j = 0; j < rows.size(); j++) {
            dataElement = rows.get(j).findElement(By.xpath("td[position()=1]"));
            idSortedAscending.add(dataElement.getText());
        }
        System.out.print("Sorted Ascending : \n");
        idSortedAscending.forEach(System.out::println);

        // We get the list of courses sorted in descending order ( we click one time)
        sortColumn("button_sortcourseid", chromeDriver);
        rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        for ( int j = 0; j < rows.size(); j++) {
            dataElement = rows.get(j).findElement(By.xpath("td[position()=1]"));
            idSortedDescending.add(dataElement.getText());
        }
        System.out.print("Sorted Descending : \n");
        idSortedDescending.forEach(System.out::println);

        // To return to the initial state we go to another tab and then we return
        goToTab("a.nav.evaluations",chromeDriver);
        goToTab("a.nav.courses",chromeDriver);
        jse.executeScript("scroll(0, 350)");

        // We click 'i' times
        for(int j = 0; j < i ; j++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sortColumn("button_sortcourseid", chromeDriver);
        }
        // we extract the sorted list after i clicks
        rows = chromeDriver.findElements(By.xpath("//table[@id='tableActiveCourses']/tbody/tr"));
        List<String> sortedList = new ArrayList<>();
            // We extract the ID first
            for (int j = 0; j < rows.size(); j++) {
                dataElement = rows.get(j).findElement(By.xpath("td[position()=1]"));
                sortedList.add(dataElement.getText());
            }

        System.out.print("Number of clicks : "+i+"\n");

        System.out.print("Sorted \n");
        sortedList.forEach(System.out::println);

        // if i is  even the list must return to the default case ( Ascending)
        // if i is  odd the list must return to the opposite of default case ( Descending)
        if(i%2==0)  return sortedList.equals(idSortedAscending);
        else        return sortedList.equals(idSortedDescending);
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// Part2 ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Login to teammate on local host with the username that you have added using the admin account
     * Attention : you must login manually first time to confirm it is you.
     *
     * @param email        - The email you have registered
     * @param chromeDriver - The webPage controller
     */
    public void local_Login(String email, WebDriver chromeDriver) {
        //chromeDriver.get("http://localhost:8080/page/instructorCourseJoin?key=0DD735D868C242CE96C0F07420B57B8F2798B1F80BB868A172D7849C8781D80B548E8B47F7637FD4411C5B67BFCA8AB7&instructorinstitution=momo");

        // I think this works if you launch the server again without closing the project ( attention u must logout, otherwise
        // it will take you to the account directly
        chromeDriver.get("http://localhost:8080/");
        chromeDriver.findElement(By.id("btnInstructorLogin")).click();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement emailInput = chromeDriver.findElement(By.id("email"));
        emailInput.clear();
        emailInput.sendKeys(email);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chromeDriver.findElement((By.id("btn-login"))).click();
        //button_confirm
    }
}

