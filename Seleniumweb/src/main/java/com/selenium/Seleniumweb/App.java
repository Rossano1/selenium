package com.selenium.Seleniumweb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Hello world!
 *
 */
public class App 
{
  
	public static void main( String[] args ) throws IOException
    {
      
    
       
    	 Thread ebayThread = new Thread(new Runnable() {
			@Override
			public void run() {
			     WebDriver driver = new ChromeDriver();
			     driver.get("https://www.ebay.com");
			     searchEbay(driver);
			    
			 }
		});

         Thread targetThread = new Thread(new Runnable() {
			@Override
			public void run() {
			     WebDriver drivertarget = new ChromeDriver();
			     drivertarget.get("https://www.target.com");
			     searchTarget(drivertarget);
			   
			     
			 }
		});

        
         ebayThread.start();
         targetThread.start();

       
         try {
             ebayThread.join();
             targetThread.join();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
       
         displayFileContents("target_product_details.txt");
     }

    
        public static void searchEbay(WebDriver driver) {
            
        	
            WebElement searchBox = driver.findElement(By.name("_nkw"));
            searchBox.sendKeys("iphone");


            WebElement searchButton = driver.findElement(By.id("gh-btn"));
            searchButton.click();

         
          

            List<WebElement> products = driver.findElements(By.xpath("//li[contains(@id,'item')]"));

    
            StringBuilder productDetails = new StringBuilder();

   
            for (WebElement product : products) {
                String description = product.findElement(By.xpath(".//h3")).getText();
                String price = product.findElement(By.className("s-item__price")).getText();
                productDetails.append(description).append(" - ").append(price).append("\n");
            }
            try (FileWriter writer = new FileWriter("ebay_product_details.txt")) {
                writer.write(productDetails.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
          
        }
        public static void searchTarget(WebDriver driver) {
        	WebElement searchBox = driver.findElement(By.id("search"));
        	searchBox.sendKeys("iphone");
        	WebElement button = driver.findElement(By.cssSelector("[data-test=\"@web/Search/SearchButton\"]"));
        	button.click();
        	
        	List<WebElement>pro = driver.findElements(By.xpath("//div[@title]"));
        	StringBuilder pd = new StringBuilder();
        	for(WebElement prod : pro) {
        		 WebElement descriptionElement = prod.findElement(By.xpath(".//a[@data-test='product-title']"));
        	        String description = descriptionElement.getAttribute("title");
        	    WebElement price = prod.findElement(By.xpath(".//span[@data-test='current-price']"));
        	    String currentprice = price.getText();
        	    pd.append(description).append(" - ").append(price).append("\n");
        }
        	  try (FileWriter writer = new FileWriter("target_product_details.txt")) {
        	        writer.write(pd.toString());
        	    } catch (IOException e) {
        	        e.printStackTrace();
        	    }
        }
        public static void displayFileContents(String filePath) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

