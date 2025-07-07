
# ODTFM - ODT File Manager

ODTFM - is a simple JavaFX-based application designed to manage ODT files, making it easier to organize and take notes during lessons.

## Features
- Displays folders on the left side of the interface
- Lists ODT files in the selected directory
- User-friendly JavaFX-based UI
- Allows basic file management (open, rename, delete)
- Create a new file with one click
- Tray icon
- Custom background image for the application
- Automatic backup of files

## Requirements
- Computer
- Java 23+
- JavaFX 23+
- Maven

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/Gruskas/ODTFM.git
   ```
2. Navigate to the project directory:
   ```sh
   cd ODTFM
   ```
3. Run the application:
   ```sh
   mvn clean package
   java -jar target/OdtManager-1.0-shaded.jar
   ```
   Alternatively, you can double-click the JAR file to run it.
