•	Database Schema Design: Designed and implemented database schema for banking entities using MySQL, ensuring data integrity and efficient retrieval.
•	User Authentication: Implemented robust user authentication and authorization using Spring-Security and JWT Authentications, including email and password-based login.
•	RESTful APIs: Built APIs for managing accounts, transactions, and balances, account-to-account transfers, transaction statement generation, ensuring seamless data exchange.
•	Data Validation: Enhanced reliability and user experience by integrating data to validation and error handling, including Spring Boot exception handling and custom Messages.
•	Security Measures: Enhanced security using JWT Authentication encryption and secure coding practices to protect sensitive data.

rest apis requests
//LOGIN//
post http://localhost:3036/user-api/statement

//createAccount//
post http://localhost:3036/api-user/user
//credit-amount//
post http://localhost:3036/user-api/credit

//debit amount//
http://localhost:3036/user-api/debit
//balance enquiry//
get http://localhost:3036/user-api/balanceEnquiry
//name enquiry//
get http://localhost:3036/user-api/nameEnquiry
//amount transaction//
post http://localhost:3036/user-api/transfer
//statement//
get http://localhost:3036/user-api/statement


