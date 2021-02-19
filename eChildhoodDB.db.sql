BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "childhood" (
	"username"	TEXT,
	"password"	TEXT NOT NULL,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"birth"	DATE NOT NULL,
	"location"	TEXT NOT NULL,
	"address"	TEXT NOT NULL,
	"parent_name"	TEXT NOT NULL,
	"parent_email"	TEXT NOT NULL,
	"parent_number"	INTEGER NOT NULL,
	"avatar"	TEXT NOT NULL,
	"educator"	TEXT NOT NULL,
	PRIMARY KEY("username")
);
CREATE TABLE IF NOT EXISTS "educators" (
	"username"	TEXT,
	"password"	TEXT NOT NULL,
	"name"	TEXT NOT NULL,
	"surname"	TEXT NOT NULL,
	"birth"	DATE NOT NULL,
	"location"	TEXT NOT NULL,
	"address"	TEXT NOT NULL,
	"number"	INTEGER NOT NULL,
	"email"	TEXT NOT NULL,
	"avatar"	TEXT NOT NULL,
	"admin"	INTEGER NOT NULL CHECK(admin BETWEEN 0 AND 1),
	PRIMARY KEY("username")
);
INSERT INTO "educators" VALUES ('admin','admin','admin','admin', '1202943600000','admin','admin',999999999,'e.childhood00@gmail.com','https://cdn.iconscout.com/icon/premium/png-256-thumb/administrator-16-621387.png',1);
COMMIT;
