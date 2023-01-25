SensorSafe
=========

SensorSafe is a powerful yet light weight application that configures your sensors, sanitizes sensor data and saves it with a sanity flag. With SensorSafe, you can easily set up and configure your sensors, ensuring that they are working at optimal performance. 
The application also includes a data sanitization feature that removes any unwanted or inaccurate data, ensuring that your sensor data is always accurate and reliable.
The application also saves that data in a timeseries collection.

Getting Started
---------------

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Requirements

- Java 19.0.1
- Spring Boot 2.7.3
- Apache Maven 3.8.1
- MongoDB 6.0.0

### Build

To build the backend, run the following command in the root directory of the project:

    mvn clean install

### Run

To run the backend, run the following command in the root directory of the project:

    mvn spring-boot:run

### Test

To run the tests, run the following command in the root directory of the project:

    mvn test


Installation
---------------

### MongoDB

The backend uses MongoDB as a database. To install MongoDB on your system, follow the steps below:

1. Install MongoDB on your system. The installation instructions can be found [here](https://docs.mongodb.com/manual/installation/).
2. Start the MongoDB server. The instructions can be found [here](https://docs.mongodb.com/manual/tutorial/manage-mongodb-processes/).

### Backend

To run the backend locally, follow the steps below:

1. Clone the repository to your local machine.
2. Open the project in your IDE, preferably IntelliJ IDEA.
3. Run the project.    

Note: Make sure that MongoDB server is running before starting the application.

Feature
-------

*   Sensor Configuration
*   Data Sanitization
*   Sanity Flag
*   Notification to users

## Usage

To be decided.

Contact
-------

If you have any questions or need help getting started, please feel free to contact us. We're always here to help.
