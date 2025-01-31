CREATE DATABASE bookapi;
USE bookapi;

CREATE TABLE authors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    active_author boolean,
    nacionality VARCHAR(100) UNIQUE NOT NULL,
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


-- Inserta autores
INSERT INTO authors (author_name, active_author, nacionality, birth_date) VALUES
('Gabriel García Márquez', FALSE, 'Colombiana', '1927-03-06'),
('J.K. Rowling', TRUE, 'Británica', '1965-07-31');
SELECT * FROM authors;
-- Inserta libros con la relación a autores
INSERT INTO books (title, genre, publication_date, available, id_author) VALUES
('Cien años de soledad', 'Realismo mágico', '1967-05-30', TRUE, 1),
('Harry Potter y la piedra filosofal', 'Fantasía', '1997-06-26', TRUE, 2),
('Harry Potter y la cámara secreta', 'Fantasía', '1998-07-02', FALSE, 2);
SELECT * FROM books;

-- DROP TABLE IF EXISTS autores;
-- DROP TABLE IF EXISTS libros;
-- DROP DATABASE biblioteca;

