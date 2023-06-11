docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=eazybank-user --env MYSQL_PASSWORD=dummypassword --env MYSQL_DATABASE=eazybank --name mysql --publish 3306:3306 mysql:8-oracle


create table users(
    --id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(45) NOT NULL,
	password VARCHAR(45) NOT NULL,
	enabled INT NOT NULL,
    PRIMARY KEY (username)
);

create table authorities (
    --id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(45) NOT NULL,
	authority VARCHAR(45) NOT NULL,
	constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);

INSERT IGNORE INTO users VALUES('happy', '123456', 1);
INSERT IGNORE INTO authorities VALUES('happy', 'write');
