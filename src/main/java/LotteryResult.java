import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class LotteryResult {

    // Constant for item position (7th lottery result)
    private static final int ITEM_POSITION = 7;

    // Constants for scrolling behavior
    private static final int SCROLL_ATTEMPTS = 5;
    private static final int SCROLL_PIXELS = 1000;
    private static final int SCROLL_WAIT_TIME_MS = 2000;

    // Path to the ChromeDriver executable
    // Make sure to add the location of the ChromeDriver before run this application
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\manoj\\Desktop\\chromedriver-win64\\chromedriver.exe";

    // URL to the lottery page
    private static final String LOTTERY_URL = "https://www.dlb.lk/";

    public static void main(String[] args) {

        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

        // Create an instance of ChromeDriver
        WebDriver driver = new ChromeDriver();

        // Open the website
        driver.get(LOTTERY_URL);

        // Wait for the page to load completely
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Simulate scrolling to load content
        scrollPage(driver);

        // Locate all lottery result containers
        List<WebElement> resultBoxes = driver.findElements(By.cssSelector(".col-md-12.no_padding.slideanim.slide"));

        // Check if there are enough results and extract the 7th one
        if (resultBoxes.size() >= ITEM_POSITION) {
            WebElement seventhResultBox = resultBoxes.get(ITEM_POSITION - 1); // Get the 7th lottery result

            // Extract the lottery name and draw date
            String lotteryNameAndDateText = seventhResultBox.findElement(By.cssSelector(".lottery_n_d")).getText();

            // Extract the numbers
            StringBuilder numbers = extractLotteryNumbers(seventhResultBox);

            // Extract the prize amount
            String prizeAmount = seventhResultBox.findElement(By.cssSelector(".next_jkpt")).getText();

            // Print the extracted information
            printLotteryResult(lotteryNameAndDateText, numbers, prizeAmount);
        } else {
            System.out.println("The requested lottery result is not available.");
        }

        // Close the browser
        driver.quit();
    }

    // Method to simulate scrolling on the page
    private static void scrollPage(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        for (int i = 0; i < SCROLL_ATTEMPTS; i++) {
            jsExecutor.executeScript("window.scrollBy(0, " + SCROLL_PIXELS + ");");
            try {
                Thread.sleep(SCROLL_WAIT_TIME_MS);  // Wait for content to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to extract lottery numbers from a result box
    private static StringBuilder extractLotteryNumbers(WebElement resultBox) {
        List<WebElement> numberElements = resultBox.findElements(By.cssSelector(".number_shanida.number_circle"));
        StringBuilder numbers = new StringBuilder();
        for (WebElement numberElement : numberElements) {
            numbers.append(numberElement.getText()).append(" ");
        }
        return numbers;
    }

    // Method to print the lottery result details
    private static void printLotteryResult(String lotteryNameAndDateText, StringBuilder numbers, String prizeAmount) {
        System.out.println("Jayoda Lottery Result: ");
        System.out.println(lotteryNameAndDateText);  // Lottery name and date
        System.out.println("Numbers: " + numbers.toString().trim());  // Lottery numbers
        System.out.println("Prize: " + prizeAmount);  // Prize amount
    }
}
