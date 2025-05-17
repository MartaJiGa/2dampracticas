CREATE TABLE authors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    active_author boolean,
    nacionality VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL 
);

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    genre VARCHAR(50),
	publication_date DATE,
    available BOOLEAN DEFAULT TRUE,
    id_author INT NOT NULL,
    FOREIGN KEY (id_author) REFERENCES authors(id) ON DELETE CASCADE
);
