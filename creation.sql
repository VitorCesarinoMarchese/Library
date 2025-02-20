DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS last_changes;
DROP TABLE IF EXISTS renting;
DROP VIEW IF EXISTS books_total;

CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price TEXT NOT NULL,
    total_quantity INTEGER NOT NULL,
    rent_quantity INTEGER NOT NULL,
    available_quantity INTEGER NOT NULL,
    last_changes_id INTEGER,
    creation_date TEXT DEFAULT CURRENT_TIME,
    FOREIGN KEY(last_changes_id) REFERENCES last_changes(id)
);

CREATE VIEW books_total AS SELECT id, rent_quantity, available_quantity, total_quantity FROM books;

CREATE TRIGGER update_total_after_insert
    AFTER INSERT ON books
BEGIN
    UPDATE books
    SET total_quantity = NEW.rent_quantity + NEW.available_quantity
    WHERE rowid = NEW.rowid;
END;
CREATE TRIGGER update_total_after_update
    AFTER UPDATE ON books
BEGIN
    UPDATE books
    SET total_quantity = NEW.rent_quantity + NEW.available_quantity
    WHERE rowid = NEW.rowid;
END;

CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    creation_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    adm INTEGER NOT NULL DEFAULT 0,
    strikes INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE last_changes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    type TEXT NOT NULL check(type in ( 'insert', 'update', 'delete' )),
    date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE renting (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    rent_date TEXT NOT NULL DEFAULT CURRENT_DATE,
    return_date TEXT,
    price TEXT NOT NULL,
    status TEXT NOT NULL CHECK(status IN ('active', 'returned', 'late')),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(book_id) REFERENCES books(id)
);