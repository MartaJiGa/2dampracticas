-- Inserciones en authors
INSERT INTO authors (author_name, active_author, nacionality, birth_date) VALUES
('Gabriel Garcia Marquez', FALSE, 'Colombiana', '1927-03-06'),
('J.K. Rowling', TRUE, 'Britanica', '1965-07-31');

-- Inserciones en books
INSERT INTO books (title, genre, publication_date, available, id_author) VALUES
('Cien meses de soledad', 'Realismo magico', '1967-05-30', TRUE, 1),
('Harry Potter y la piedra filosofal', 'Fantasia', '1997-06-26', TRUE, 2),
('Harry Potter y la camara secreta', 'Fantasia', '1998-07-02', FALSE, 2),
('Harry Potter y el prisionero de Azkaban', 'Fantasia', '1999-07-08', TRUE, 2);