Java Encryption Application
This Java application provides functionalities for various encryption operations including hashing, DES encryption and decryption, AES encryption and decryption, and RSA encryption and decryption. It offers a menu-driven interface for ease of use.

Features:
Hashing: SHA-256 hashing of input strings.
DES Encryption and Decryption: Encrypts and decrypts input strings using the Data Encryption Standard (DES) algorithm.
AES Encryption and Decryption: Encrypts and decrypts input strings using the Advanced Encryption Standard (AES) algorithm.
RSA Encryption and Decryption: Encrypts and decrypts input strings using the Rivest-Shamir-Adleman (RSA) algorithm.
Key Generation: Generates DES and RSA key pairs of specified lengths.
Usage:
Clone the repository to your local machine.
Ensure you have Java installed.
Compile the source files using the following command:
bash
Copy code
javac App.java
Run the compiled program using the following command:
bash
Copy code
java App
Follow the on-screen instructions to perform encryption and decryption operations.
Note:
Ensure proper input validation and error handling for robust usage.
It's recommended to use more secure encryption modes such as CBC or GCM instead of ECB for real-world applications.
This application serves as a demonstration of basic encryption techniques and may not be suitable for production use without further enhancements.
License:
This project is licensed under the MIT License - see the LICENSE file for details.
