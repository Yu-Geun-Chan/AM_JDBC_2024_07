CREATE DATABASE JDBC;
USE JDBC;

CREATE TABLE article (
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         regDate DATETIME NOT NULL,
                         updateDate DATETIME NOT NULL,
                         title VARCHAR(100) NOT NULL,
                         `body` TEXT NOT NULL
);

SELECT * FROM article;

DROP TABLE article;

SELECT '제목1';

SELECT CONCAT ('제목', '1'); ##### 문자열 더하기

SELECT RAND();  ##### 0 ~ 1까지 랜덤 소수

SELECT SUBSTRING(RAND() * 1000 FROM 1 FOR 2); ##### SUBSTRING(1, 3) : INDEX 1번부터 3번까지 자르겠다.

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = CONCAT ('제목', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
`body` = CONCAT ('내용', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));

SELECT * FROM article;
