DROP DATABASE IF EXISTS VWCC;

CREATE DATABASE VWCC;

USE VWCC;

CREATE TABLE User(
  UserID INT NOT NULL,
  Name VARCHAR(60) NOT NULL,
  Email VARCHAR(60) NOT NULL,
  Phone VARCHAR(12) NOT NULL,
  Username VARCHAR (50) NOT NULL,
  UPassword VARCHAR (12) NOT NULL,
  UserType VARCHAR(20) NOT NULL,

  PRIMARY KEY (UserID)
)ENGINE = innoDB;

CREATE TABLE Admin(
  AdminID INT NOT NULL,
  EMPID INT NULL,

  PRIMARY KEY (AdminID),
  CONSTRAINT EMPID_fk FOREIGN KEY (EMPID) REFERENCES User (UserID)

)ENGINE = innoDB;

CREATE TABLE Class (
  Class_Num INT NOT NULL AUTO_INCREMENT,
  ClassID VARCHAR (12) NOT NULL,
  CTEMPID INT NULL,
  Class_Name VARCHAR(60) NOT NULL,
  Description VARCHAR(300) NULL,


  PRIMARY KEY (Class_Num),
  UNIQUE KEY ClassID_UNIQUE (ClassID),
  KEY key_1 (Class_Num, ClassID)

)ENGINE = innoDB;

CREATE TABLE Teacher(
  TechID INT NOT NULL AUTO_INCREMENT,
  TEMPID INT NULL,
  TClassID VARCHAR(12) NULL,

  PRIMARY KEY (TechID),
  CONSTRAINT TEMPID_fk FOREIGN KEY (TEMPID) REFERENCES User (UserID),
  CONSTRAINT TClassID_fk FOREIGN KEY (TClassID) REFERENCES Class (ClassID)

)ENGINE = innoDB;

CREATE TABLE Student(
  StudID INT NOT NULL AUTO_INCREMENT,
  SEMPID INT NOT NULL,
  STEMPID INT NOT NULL,
  SClassID VARCHAR(12)  NULL,

  PRIMARY KEY (StudID),
  CONSTRAINT SEMPID_fk FOREIGN KEY (SEMPID) REFERENCES User (UserID),
  CONSTRAINT STEMPID_fk FOREIGN KEY (STEMPID) REFERENCES Teacher (TEMPID),
  CONSTRAINT SClassID_fk FOREIGN KEY (SClassID) REFERENCES Class (ClassID)

)ENGINE = innoDB;

CREATE TABLE Grade (
  GradeID INT NOT NULL AUTO_INCREMENT,
  GSEMPID INT NOT NULL,
  GClassID VARCHAR(12) NOT NULL,
  Grade_Name VARCHAR(50) NOT NULL,
  Score INT NOT NULL,
  Update_Reason VARCHAR(300) NULL,

  PRIMARY KEY (GradeID),

  CONSTRAINT GSEMPID_fk FOREIGN KEY (GSEMPID) REFERENCES Student (SEMPID),
  CONSTRAINT GClassID_fk FOREIGN KEY (GClassID) REFERENCES Class(ClassID)

)ENGINE = innoDB;

CREATE TABLE Login(
  LogID INT NOT NULL AUTO_INCREMENT,
  LEMPID INT NOT NULL,

  PRIMARY KEY (LogID),
  CONSTRAINT LEMPID_fk FOREIGN KEY (LEMPID) REFERENCES User (UserID)

)ENGINE = innoDB;

CREATE TABLE Record(
  RecoID INT NOT NULL AUTO_INCREMENT,
  SEMPID INT NOT NULL,
  Class_ID VARCHAR(10) NOT NULL,
  Student_Name VARCHAR(100) NOT NULL,
  Class_Name VARCHAR (60) NOT NULL,
  Unite INT NOT NULL,
  Grade VARCHAR(2) NOT NULL,
  Term VARCHAR(100) NOT NULL,
  Year VARCHAR(5) NOT NULL,

  PRIMARY KEY (RecoID),
  CONSTRAINT RClassID_fk FOREIGN KEY (SEMPID) REFERENCES User (UserID)

)ENGINE = innoDB;

INSERT INTO User (UserID, Name, Email, Phone, Username, UPassword, UserType)
    VALUE (67531, 'Dr. Wolff', 'dWolff@email.com', '673-23-8934', 'dWolff',
           '!2Diane',  'Teacher'),
  (96431, 'P. Jeff', 'djeff@email.com', '540-452-1110', 'dJeff', 'Jeffd134', 'Teacher'),
  (65011, 'Dr. Smith', 'dsmaith23@email.com', '980-876-0012', 'dSmith', 'Smith!7', 'Teacher'),
  (80234, 'P. Fosdal', 'fosdal54@email.com', '704-103-7734', 'fosdal23', 'Peter34!', 'Teacher'),
  (67344, 'Tarnue Jallah', 'tarnue34@email.com', '704-23-8934', 'jallah', 'Tarnuej', 'Student'),
  (76002, 'Ashely William', 'w78342@email.com', '897-201-3244', 'Awilliam', 'Washely^', 'Student'),
  (76098, 'Mohamed Keita', 'mk24238@email.com', '784-212-8000', 'mk24238', '12Keita', 'Student'),
  (72201, 'Melanie Crouch', 'crouch780@email.com', '540-111-8900', 'mcrouch89', '#crouchM', 'Teacher'),
  (65003, 'P. Jodi Clingenpeel', 'jodi623@email.com', '562-907-4623', 'jc78342', '&8jodi', 'Teacher'),
  (542310, 'Lacey Peterson', 'lperson@email.com', '562-523-9034', '1', '1', 'Admin');;

INSERT INTO Class (ClassID, CTEMPID, Class_Name, Description)
    VALUE ('ITP 120', 67531, 'Java Programming I', 'NA'),
  ('ACC 211', 96431 , 'Principles of accounting I', 'NA'),
  ('ART 101', 65011 , 'History and Appreciation of Art I', 'NA'),
  ('BIO 101', 80234 , 'General Biology I', 'NA'),
  ('MTH 120', 65003, 'Introduction To Mathematics', 'NA'),
  ('ENG 11', 80234, 'College Composition', 'NA'),
  ('ITP 100', 67531, 'Software Design', 'NA'),
  ('ITP 146', 72201, 'Client Side Scripting', 'NA'),
  ('ITP 246', 72201, 'Web Scripting Languages', 'NA'),
  ('ITP 220', 67531, 'Java Programming II', 'NA');

INSERT INTO Teacher (TEMPID, TClassID)
  VALUE (67531, 'ITP 120'),
  (67531, 'ITP 220'),
  (96431, 'ACC 211'),
  (65011, 'ART 101'),
  (80234, 'BIO 101'),
  (67531, 'ITP 100'),
  (65003, 'MTH 120'),
  (72201, 'ITP 246');

INSERT INTO Student (SEMPID, STEMPID, SClassID)
  VALUE (67344, 67531, 'ITP 220'),
  (76098, 67531, 'ITP 220'),
  (76002, 80234, 'ENG 11'),
  (67344, 72201, 'ITP 246'),
  (76098, 67531, 'ITP 100'),
  (76002, 65011, 'ART 101'),
  (76098, 67531, 'ITP 120');

INSERT INTO Grade (GSEMPID, GClassID, Grade_Name, Score, Update_Reason)
    VALUE (67344, 'ITP 220', 'Home Work 1', 8, 'NA'),
    (76098, 'ITP 220', 'Test 1', 93, 'NA'),
    (76098, 'ITP 220', 'Home Work 2', 9, 'NA'),
    (67344, 'ITP 220', 'Test 1', 98, 'NA'),
    (67344, 'ITP 220', 'Home Work 2', 10, 'NA'),
    (76002, 'ENG 11', 'Home Work 1', 8, 'NA'),
    (76098, 'ITP 120', 'Exam', 97, 'NA'),
    (76098, 'ITP 220', 'Home Work 1', 10, 'NA');

INSERT INTO Record(SEMPID, Class_ID, Student_Name, Class_Name, Unite, Grade, Term, Year)
    VALUE (76098, 'GOL 105', 'Mohamed', 'Physical Geology', 4, 'C', 'Fall', '2013'),
    (76098, 'GOL 106', 'Mohamed', 'Historical Geology', 4, 'B+', 'Spring', '2013'),
    (76098, 'PHY 201', 'Mohamed', 'General College Physics I', 4, 'C+', 'Fall', '2013'),
    (76098, 'MTH 151', 'Mohamed', 'Math for the Liberal Arts I', 3, 'A', 'Fall', '2012'),
    (76098, 'BIO 141', 'Mohamed', 'Human Anatomy & Physiology I', 3, 'B+', 'Summer', '2012'),
    (76098, 'CHM 111', 'Mohamed', 'General Chemistry I', 4, 'B', 'Fall', '2015'),
    (76098, 'MTH 161', 'Mohamed', 'PreCalculus I', 3, 'B', 'Fall', '2012'),
    (76098, 'BIO 200', 'Mohamed', 'Human Anatomy & Physiology 2', 4, 'C', 'Fall', '2015');

ALTER TABLE Admin AUTO_INCREMENT = 011;
ALTER TABLE Teacher AUTO_INCREMENT = 20;
ALTER TABLE Class AUTO_INCREMENT = 011;
ALTER TABLE Student AUTO_INCREMENT = 01;
ALTER TABLE Login AUTO_INCREMENT = 50;
ALTER TABLE Login AUTO_INCREMENT = 10;

