DROP TABLE IF EXISTS tbl_refresh_token;
DROP TABLE IF EXISTS tbl_user;

# user 정보를 위한 테이블
CREATE TABLE tbl_user (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       nickname VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       phone_number VARCHAR(20) NOT NULL ,
                       created_at DATETIME,
                       updated_at DATETIME
);

# refresh token 저장을 위한 테이블
CREATE TABLE tbl_refresh_token (
                        token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        refresh_token VARCHAR(512) NOT NULL UNIQUE,
                        issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        expired_at TIMESTAMP NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES tbl_user(user_id) ON DELETE CASCADE
);
