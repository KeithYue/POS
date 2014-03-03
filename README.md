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
