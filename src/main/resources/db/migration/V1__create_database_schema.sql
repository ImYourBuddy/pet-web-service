CREATE TABLE public.pet_owner
(
    id bigint NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20),
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.pet
(
    id bigint NOT NULL,
    species character varying(10) NOT NULL,
    name character varying(50),
    breed character varying(20),
    gender character varying(6),
    birthdate timestamp,
    owner bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.pet
    ADD CONSTRAINT owner_fk FOREIGN KEY (owner)
        REFERENCES public.pet_owner (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
        NOT VALID;

CREATE TABLE public.pet_expert
(
    id bigint NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    qualification character varying(50) NOT NULL,
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    online_help boolean NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.post
(
    id bigint NOT NULL,
    title character varying(20) NOT NULL,
    description character varying(40),
    text text NOT NULL,
    author bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.post
    ADD CONSTRAINT author_fk FOREIGN KEY (author)
        REFERENCES public.pet_expert (id)
        ON UPDATE NO ACTION
        ON DELETE RESTRICT
    NOT VALID;

CREATE TABLE public.moderator
(
    id integer NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name character varying(20) NOT NULL,
    login character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    admin_privileges boolean NOT NULL,
    PRIMARY KEY (id)
);
