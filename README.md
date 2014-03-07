POS
===

Java development environment, and to practice unit testing of object-oriented programs under the JUnit 4 framework.  The basic functionality of a POS system is helping customers make payments to merchants in exchange of goods or services. Typical POS systems may also offer other functionalities like product management, sales statistics analysis. 

#### Some Finding
* both TMVAT and TMNoTax return the original tax
* do not test the main function
* In the batch mode, there is no check for the membership information
* how to test the mehtod that can invoke System.exit(1), which can shut down the while JVM, including all the test cases.
http://stackoverflow.com/questions/309396/java-how-to-test-methods-that-call-system-exit?lq=1
  * http://www.stefan-birkner.de/system-rules/index.html, use rule to solve this problem
* mock System.in input, there is no check information about user information in the saleRecord function. Besides, we can invoke saleRecord() without using the register method
* username and password is public attr
* POS functional Test:
  * normal input test
  * has member ship / no membership
  * cash enough / cash not enough, there is not any check for invalid cash such as negative cash
* How to decide whether the saleRegister menthod is normally invoked.
  * read the log file to decide whether the result is correct
* Eventhough we can use sub-class to override some method to test, but for the POS situation, there is only one instance of POS, 
even we use subclass, the instance is still POS instance, so we need to solve the singleton problem
  
#### Some guidelines
* The purpose of test cases is to test if a method performs correctly. 
As such, each test case MUST define its expected outputs/behavior. 
The assert statements should reflect what are to be checked if the test is carried out manually.
* Your test cases do not need to cover those branches that unconditionally/always lead to the execution of System.exit.
* So in order to test some methods that require console inputs, you need to simulate user interactions by 
redirecting system standard input to your own input streams. 
For example, you can redirect System.in to some string