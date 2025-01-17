CREATE database pokemon;
USE pokemon;

CREATE TABLE trainers(
 id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(35) NOT NULL
);

CREATE TABLE pokemons(
 id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(35) NOT NULL,
    owner_id INT
);

INSERT INTO trainers (name)
VALUES
('Ash'),
('Misty'),
('Brock'),
('James'),
('Jessie');

INSERT INTO pokemons (name, owner_id)
VALUES
('Pikachu', '1'),
('Charizard', '1'),
('Bulbasaur', '1'),
('Starmie', '2'),
('Onix', '3'),
('Geodude', '3'),
('Meowth', '4'),
('Growlie', '4'),
('Wobbuffet', '5'),
('Lickitung', '5');

INSERT INTO pokemons (name)
VALUES
('Vulpix'),
('Zubat'),
('Magnemite'),
('Ninetales'),
('Weepinbell'),
('Stufful'),
('Magicarp'),
('Snorlax'),
('Rockruff'),
('Squirtle'),
('Lapras');